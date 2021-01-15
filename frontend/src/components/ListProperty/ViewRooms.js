import React, { Component } from 'react';
import Header from '../Header/Header';
import { Formik, Form, Field, ErrorMessage, FastField } from "formik";
import * as Yup from "yup";
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import { Link } from "react-router-dom";

const DetailsSchema = Yup.object().shape({
    roomSpace: Yup.number()
        .required("Room space is required"),
    weekdayPrice: Yup.number()
        .required("Mention the weekday price"),
    weekendPrice: Yup.number()
        .required("Mention the weekend price")
});

class ViewRooms extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomDetails: []
        }

    }

    componentDidMount() {
        let data = {
            ownerEmail: localStorage.getItem('userEmail'),
            propertyId: this.props.match.params.id
        }

        console.log('Data: ', data);
        axios.post(rootURL + '/getOwnerRooms', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Result: ', response.data);

                    this.setState({
                        roomDetails: response.data,

                    });
                    console.log("this.state", JSON.parse(this.state.roomDetails[0].monday))

                }
                else {
                    console.log("no data received from get owner property")
                }
            }).catch((err) => {
                if (err) {
                    this.setState({
                        errorRedirect: true
                    })
                }
            });
    }


    render() {
        let roomDetails = null
        roomDetails = this.state.roomDetails.map((room, index) => {
            return (
                <div className=" container property-display-content border" >
                    <div className="row">
                        <div className="property-display-img-content col-6">
                            <div id="myCarousel" className="carousel slide" data-ride="carousel">
                                <div className="carousel-inner">
                                    <div className={"carousel-item active"} >
                                        <img className=" carousel-img property-display-img" src="https://images.unsplash.com/photo-1550581190-9c1c48d21d6c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60" alt="property-image" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="center-content">
                            <Formik
                                initialValues={{
                                    roomSpace: room.roomSpace,
                                    weekdayPrice: room.weekdayPrice,
                                    weekendPrice: room.weekendPrice,

                                }}
                                validationSchema={DetailsSchema}
                                onSubmit={(values, actions) => {
                                    this.updateRoom(values, room.roomId)
                                    actions.setSubmitting(false);
                                }}
                            >
                                {({ touched, errors, isSubmitting }) => (
                                    <Form>

                                        <div className="form-group text-left">
                                            <label htmlFor="roomSpace">Room Space</label>
                                            <Field
                                                disabled
                                                type="number"
                                                name="roomSpace"
                                                className={`form-control ${
                                                    touched.roomSpace && errors.roomSpace ? "is-invalid" : ""
                                                    }`}
                                            />
                                            <ErrorMessage
                                                component="div"
                                                name="roomSpace"
                                                align="text-left"
                                                className="invalid-feedback"
                                            />
                                        </div>

                                        <div className="form-group text-left">
                                            <label className="font-weight-bold">Amenities </label>
                                            <div>
                                                <input type="checkbox" name='bath' onChange={this.handleBath} disabled defaultChecked={JSON.parse(room.bath)} />Bath
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleShower} disabled defaultChecked={JSON.parse(room.shower)} />Shower
                                            </div>
                                        </div>

                                        <div className="form-group text-left">
                                            <label className="font-weight-bold">Availability </label>
                                            <div>
                                                <input type="checkbox" onChange={this.handleMonday} disabled defaultChecked={JSON.parse(room.monday)} />Monday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleTuesday} disabled defaultChecked={JSON.parse(room.tuesday)} />Tuesday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleWed} disabled defaultChecked={JSON.parse(room.wednesday)} />Wednesday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleThu} disabled defaultChecked={JSON.parse(room.thursday)} />Thursday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleFri} disabled defaultChecked={JSON.parse(room.friday)} />Friday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleSat} disabled defaultChecked={JSON.parse(room.saturday)} />Saturday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handelSun} disabled defaultChecked={JSON.parse(room.sunday)} />Sunday
                                            </div>
                                        </div>

                                        <div className="form-group text-left">
                                            <label htmlFor="weekendPrice">Weekend Price</label>
                                            <Field
                                                disabled
                                                type="number"
                                                name="weekendPrice"
                                                className={`form-control ${
                                                    touched.weekendPrice && errors.weekendPrice ? "is-invalid" : ""
                                                    }`}
                                            />
                                            <ErrorMessage
                                                component="div"
                                                name="weekendPrice"
                                                align="text-left"
                                                className="invalid-feedback"
                                            />
                                        </div>

                                        <div className="form-group text-left">
                                            <label htmlFor="weekdayPrice">Weekday Price</label>
                                            <Field
                                                disabled
                                                type="number"
                                                name="weekdayPrice"
                                                className={`form-control ${
                                                    touched.weekdayPrice && errors.weekdayPrice ? "is-invalid" : ""
                                                    }`}
                                            />
                                            <ErrorMessage
                                                component="div"
                                                name="weekdayPrice"
                                                align="text-left"
                                                className="invalid-feedback"
                                            />
                                        </div>

                                        <br />
                                        <Link
                                            to={
                                                {
                                                    pathname: `/editroom/${room.roomId}`,
                                                    state: { room: room }
                                                }
                                            }

                                            className="btn btn-lg btn-primary text-white float-right font-weight-bold"

                                        >
                                            Edit
                                        </Link>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                </div >
            )
        })

        return (
            <div>
                <Header />
                {roomDetails}
            </div >
        )
    }
}

export default ViewRooms