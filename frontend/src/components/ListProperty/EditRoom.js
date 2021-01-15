import React, { Component } from 'react';
import Header from '../Header/Header';
import { Formik, Form, Field, ErrorMessage, FastField } from "formik";
import * as Yup from "yup";
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert';

const DetailsSchema = Yup.object().shape({
    roomSpace: Yup.number()
        .required("Room space is required"),
    weekdayPrice: Yup.number()
        .required("Mention the weekday price"),
    weekendPrice: Yup.number()
        .required("Mention the weekend price")
});

class EditRoom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomSpace: "",
            weekdayPrice: "",
            weekendPrice: "",
            bath: null,
            shower: null,
            always: null,
            monday: null,
            tuesday: null,
            wednesday: null,
            thursday: null,
            friday: null,
            saturday: null,
            sunday: null
        }
        this.handleBath = this.handleBath.bind(this)
        this.handleShower = this.handleShower.bind(this)
        this.handleMonday = this.handleMonday.bind(this)
        this.handleTuesday = this.handleTuesday.bind(this)
        this.handleWed = this.handleWed.bind(this)
        this.handleThu = this.handleThu.bind(this)
        this.handleFri = this.handleFri.bind(this)
        this.handleSat = this.handleSat.bind(this)
        this.handelSun = this.handelSun.bind(this)
        this.updateRoom = this.updateRoom.bind(this)
    }

    async handleBath() {
        await this.setState({ bath: !this.state.bath });
        console.log("this.state", this.state.bath)
    }

    async handleShower() {
        await this.setState({ shower: !this.state.shower });
        console.log("this.state", this.state.shower)
    }

    async handleMonday() {
        await this.setState({ monday: !this.state.monday });
        console.log("this.state", this.state.monday)
    }

    async handleTuesday() {
        await this.setState({ tuesday: !this.state.tuesday });
        console.log("this.state", this.state.tuesday)
    }

    async handleWed() {
        await this.setState({ wednesday: !this.state.wednesday });
        console.log("this.state", this.state.wednesday)
    }

    async handleThu() {
        await this.setState({ thursday: !this.state.thursday });
        console.log("this.state", this.state.thursday)
    }

    async handleFri() {
        await this.setState({ friday: !this.state.friday });
        console.log("this.state", this.state.friday)
    }

    async handleSat() {
        await this.setState({ saturday: !this.state.saturday });
        console.log("this.state", this.state.saturday)
    }

    async handelSun() {
        await this.setState({ sunday: !this.state.sunday });
        console.log("this.state.sun", this.state.sunday)
    }

    componentWillMount() {
        console.log("link state", this.props.location.state.room)
        this.setState({
            roomSpace: this.props.location.state.room.roomSpace,
            weekdayPrice: this.props.location.state.room.weekdayPrice,
            weekendPrice: this.props.location.state.room.weekendPrice,
            bath: JSON.parse(this.props.location.state.room.bath),
            shower: JSON.parse(this.props.location.state.room.shower),
            monday: JSON.parse(this.props.location.state.room.monday),
            tuesday: JSON.parse(this.props.location.state.room.tuesday),
            wednesday: JSON.parse(this.props.location.state.room.wednesday),
            thursday: JSON.parse(this.props.location.state.room.thursday),
            friday: JSON.parse(this.props.location.state.room.friday),
            saturday: JSON.parse(this.props.location.state.room.saturday),
            sunday: JSON.parse(this.props.location.state.room.sunday)
        })
        console.log("updated state", this.state)
    }

    updateRoom(details) {
        let data = {
            propertyId: this.props.location.state.room.propertyId,
            roomId: this.props.location.state.room.roomId,
            roomSpace: details.roomSpace,
            bath: JSON.stringify(this.state.bath),
            shower: JSON.stringify(this.state.shower),
            weekdayPrice: details.weekdayPrice,
            weekendPrice: details.weekendPrice,
            always: "false",
            monday: JSON.stringify(this.state.monday),
            tuesday: JSON.stringify(this.state.tuesday),
            wednesday: JSON.stringify(this.state.wednesday),
            thursday: JSON.stringify(this.state.thursday),
            friday: JSON.stringify(this.state.friday),
            saturday: JSON.stringify(this.state.saturday),
            sunday: JSON.stringify(this.state.sunday),
            ownerEmail: localStorage.getItem("userEmail")
        }
        console.log("data,", data)
        axios.post(rootURL + '/updateRoom', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Result: ', response.data);

                    this.setState({
                        success: true,

                    });
                    console.log("this.state", this.state)
                    swal("Room Updated", "Bookings related to this room might get affected", "warning")
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
        let redirectVar = null
        if (!localStorage.getItem('accountType') === 2) {
            redirectVar = <Redirect to='/' />
        }
        if (this.state.success) {
            redirectVar = <Redirect to={'/property/' + this.props.location.state.room.propertyId + '/rooms'} />
        }
        console.log("this.state in render", this.state)
        return (
            <div className="container">
                {redirectVar}
                <Header />
                <div className="row">

                    <div className="col-2">

                    </div>
                    <div className="col-8">
                        <div className="menu-bar-hor border col-8">

                            <div className="location-form-headlinetext">
                                <h4>Room Details</h4>
                            </div>
                            <hr />
                            <div className="details-form-description pad-bot-10">
                                <p>Fill in the following details of your Room</p>
                            </div>
                            <br />
                            <Formik
                                initialValues={{
                                    roomSpace: this.state.roomSpace,
                                    weekdayPrice: this.state.weekdayPrice,
                                    weekendPrice: this.state.weekendPrice,

                                }}
                                validationSchema={DetailsSchema}
                                onSubmit={(values, actions) => {
                                    this.updateRoom(values)
                                    actions.setSubmitting(false);
                                }}
                            >
                                {({ touched, errors, isSubmitting }) => (
                                    <Form>



                                        <div className="form-group text-left">
                                            <label className="font-weight-bold">Amenities </label>
                                            <div>
                                                <input type="checkbox" name='bath' onChange={this.handleBath} defaultChecked={this.state.bath} />Bath
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleShower} defaultChecked={this.state.shower} />Shower
                                            </div>
                                        </div>

                                        <div className="form-group text-left">
                                            <label className="font-weight-bold">Availability </label>
                                            <div>
                                                <input type="checkbox" onChange={this.handleMonday} defaultChecked={this.state.monday} />Monday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleTuesday} defaultChecked={this.state.tuesday} />Tuesday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleWed} defaultChecked={this.state.wednesday} />Wednesday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleThu} defaultChecked={this.state.thursday} />Thursday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleFri} defaultChecked={this.state.friday} />Friday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handleSat} defaultChecked={this.state.saturday} />Saturday
                                            </div>
                                            <div>
                                                <input type="checkbox" onChange={this.handelSun} defaultChecked={this.state.sunday} />Sunday
                                            </div>
                                        </div>

                                        <div className="form-group text-left">
                                            <label htmlFor="weekendPrice">Weekend Price</label>
                                            <Field
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
                                        <button
                                            type="submit"

                                            className="btn btn-lg btn-primary text-white float-right font-weight-bold"
                                            disabled={isSubmitting}
                                        >
                                            {isSubmitting ? "Please wait..." : "Update Room"}
                                        </button>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                </div>
            </div >
        )
    }
}

export default EditRoom;