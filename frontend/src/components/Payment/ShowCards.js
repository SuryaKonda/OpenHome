import React, { Component } from 'react'
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert'
import './Payment.css'
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import Header from '../Header/Header'
import { Redirect } from 'react-router';
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

const CardRegEx = /^[0-9]{16}?$/
const CVVRegeEx = /^[0-9]{3}?$/

const CardSchema = Yup.object().shape({
    cardDetails: Yup.string()
        .matches(CardRegEx, 'Card number is not valid')
        .required("Card number is required"),
    cvv: Yup.string()
        .matches(CVVRegeEx, 'CVV is not valid')
        .required("CVV is required"),
    paymentMethod: Yup.string()
        .required("Payment type is required")
});


let redrirectVar = null
export class ShowCards extends Component {
    constructor() {
        super()
        this.state = {
            cards: null,
            showModal: false,
            cardDetails: "",
            cvv: "",
            paymentMethod: "",
            confirm: false
        }
        this.addNewCard = this.addNewCard.bind(this)
    }

    componentDidMount = () => {
        const data = {
            userId: localStorage.getItem("userId")
        }
        axios.post(rootURL + '/getPayment', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Booking Successful!', response.data);
                    this.setState({
                        cards: response.data
                    })
                }
            }).catch((err) => {
                if (err) {
                    if (err.response.status === 404) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No previous cards found. Please add new card');
                        swal("No previous cards found. Please add new card")
                    }
                }
            });
    }

    showAddCardModal = () => {
        console.log("add new card");
        this.setState({
            showModal: !this.state.showModal
        })
    }
    handleInputChange = (e) => {
        // e.preventDefault()
        var target = e.target;
        var name = target.name;
        var value = target.value;

        this.setState({
            [name]: value
        });
        console.log(this.state);

    }
    addNewCard = (e) => {
        // e.preventDefault()
        console.log("add newcard");
        const data = {
            paymentMethod: e.paymentMethod,
            cardDetails: e.cardDetails,
            cvv: e.cvv,
            userId: localStorage.getItem("userId")
        }
        console.log(data);

        axios.post(rootURL + '/addPayment', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('added card successfully!');

                    const data1 = {
                        userId: localStorage.getItem("userId")
                    }
                    axios.post(rootURL + '/getPayment', data1)
                        .then(response => {
                            if (response.status === 200) {
                                console.log('Booking Successful!', response.data);
                                this.setState({
                                    cards: response.data,
                                    showModal: !this.state.showModal
                                })
                            }
                            swal("Success", "New card added succesfully", "success")
                        }).catch((err) => {
                            if (err) {
                                if (err.response.status === 404) {
                                    this.setState({
                                        isCardAvailable: false
                                    })
                                    console.log("Error message", 'No previous cards found. Please add new card');
                                    // swal("No previous cards found. Please add new card")
                                }
                            }
                        });
                }
            }).catch((err) => {
                if (err) {
                    if (err.response.status === 404) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No previous cards found. Please add new card');
                        swal("No previous cards found. Please add new card")
                    }
                    else if (err.response.status === 400) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'Card details exists, please add new card');
                        swal("Card details exists, please add new card")
                    }

                }
            });
    }
    confirmBooking = (paymentId) => {
        const data = {
            customerId: localStorage.getItem("userId"),
            propertyId: localStorage.getItem('propertyId'),
            roomId: localStorage.getItem("roomId"),
            customerName: localStorage.getItem("userName"),
            paymentId: paymentId,
            bookingStartDate: localStorage.getItem("startDate"),
            bookingEndDate: localStorage.getItem("endDate"),
            userEmail: localStorage.getItem('userEmail')
        }
        console.log(data);

        axios.post(rootURL + '/bookRoom', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Booking Successful!', response.data);
                    const amount = "" + response.data
                    this.setState({
                        redirect: true
                    })
                    swal({
                        title: "Success!",
                        text: "Booking Confirmed! Amount of $" + amount + " will be deducted on the day of check-in",
                        icon: "success",
                        // buttons: true,
                        // dangerMode: true,
                    })
                        .then((willDelete) => {
                            if (willDelete) {
                                localStorage.removeItem("roomId")
                                localStorage.removeItem("propertyId")
                                this.setState({
                                    confirm: true
                                })

                            } else {
                                swal("Your imaginary file is safe!");
                            }
                        });
                    // swal("Success", "Booking Confirmed! Money will be deducted on the day of check-in", "success")
                }
            }).catch((err) => {
                if (err) {
                    if (err.response.status === 400) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'Could Not book a room please try again');
                        swal("COuld Not book a room please try again")
                    }
                }
            });
    }
    render() {

        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }
        if (this.state.redirect === true) {
            redrirectVar = <Redirect to="/my-trips/upcoming" />
        }
        if (!localStorage.getItem('roomId')) {
            redrirectVar = <Redirect to="/" />
        }
        const closeBtn = <button className="close" onClick={() => this.showAddCardModal()}>&times;</button>;

        let modal = <div>
            <Modal isOpen={this.state.showModal} toggle={() => this.showAddCardModal()} className='modal-popup' scrollable>
                <ModalHeader toggle={() => this.showAddCardModal()} close={closeBtn}></ModalHeader>
                <ModalBody className="modal-body ">
                    <div className="form-group" >
                        <Formik
                            initialValues={{
                                cardDetails: "",
                                cvv: "",
                                paymentMethod: ""
                            }}
                            validationSchema={CardSchema}
                            onSubmit={(values, actions) => {
                                this.addNewCard(values)
                                actions.setSubmitting(false);
                            }}
                        >
                            {({ touched, errors, isSubmitting }) => (
                                <Form>

                                    <div className="form-group text-left">
                                        <label htmlFor="cardDetails">Card No.</label>
                                        <Field
                                            type="text"
                                            name="cardDetails"
                                            //   autofocus="true"
                                            className={`form-control ${
                                                touched.cardDetails && errors.cardDetails ? "is-invalid" : ""
                                                }`}
                                        />
                                        <ErrorMessage
                                            component="div"
                                            name="cardDetails"
                                            align="text-left"
                                            className="invalid-feedback"
                                        />
                                    </div>

                                    <div className="form-group text-left">
                                        <label htmlFor="cvv">CVV</label>
                                        <Field
                                            type="cvv"
                                            name="cvv"
                                            className={`form-control ${
                                                touched.cvv && errors.cvv ? "is-invalid" : ""
                                                }`}
                                        />
                                        <ErrorMessage
                                            component="div"
                                            name="cvv"
                                            align="text-left"
                                            className="invalid-feedback"
                                        />
                                    </div>

                                    <div className="form-group text-left">
                                        <label htmlFor="paymentMethod">Payment Type</label> &nbsp;
                                                    <Field
                                            name="paymentMethod"
                                            component="select"
                                            className={`form-control ${
                                                touched.paymentMethod && errors.paymentMethod ? "is-invalid" : ""
                                                }`}
                                        >
                                            <option value="" label="Select a payment type" />
                                            <option value="DEBIT">DEBIT</option>
                                            <option value="CREDIT">CREDIT</option>
                                        </Field>
                                        <ErrorMessage
                                            component="div"
                                            name="paymentMethod"
                                            className="invalid-feedback"
                                        />
                                    </div>

                                    <br />
                                    <button
                                        type="submit"

                                        className="btn btn-block btn-primary text-white font-weight-bold"
                                        disabled={isSubmitting}
                                    >
                                        {isSubmitting ? "Please wait..." : "Add Payment"}
                                    </button>
                                </Form>
                            )}
                        </Formik>
                    </div>
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={() => this.showAddCardModal()}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </div>

        let cards;
        if (this.state.cards) {
            cards = this.state.cards.map((card, index) => {
                return (
                    <div className="card">
                        <div className="card-body">
                            Card Number : {card.cardDetails}<br />

                            card Type : {card.paymentMethod}<br />

                            Card CVV : {card.cvv}
                        </div>
                        <div>
                            <button onClick={() => this.confirmBooking(card.paymentId)} id="use-card" className="btn btn-success">use this card</button>
                        </div>
                    </div>
                )
            })
        }
        if (this.state.confirm) {
            redrirectVar = <Redirect to="/home" />
        }
        return (
            <div className="container">
                {redrirectVar}
                <Header />
                <div className="newcard">
                    <button onClick={() => this.showAddCardModal()} className="btn btn-primary">Add New Card</button>
                </div>
                <div>
                    {cards}
                </div>
                {modal}
            </div>
        )
    }
}

export default ShowCards
