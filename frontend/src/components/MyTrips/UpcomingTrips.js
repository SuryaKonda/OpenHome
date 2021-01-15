import React, { Component } from 'react';
import Header from '../Header/Header';
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert'

class UpcomingTrips extends Component {

    constructor() {
        super();
        this.state = {
            trips: [],
            myTrips: [],
            errorRedirect: false
        }
    }

    componentWillMount() {
        const data = {
            customerId: localStorage.getItem("userId")
        }
        //404 no bookings for past/future/current
        //400 no bookings at all
        axios.post(rootURL + '/getFutureUserBookingDetails', data)
            .then(response => {
                if (response.status === 200) {
                    console.log(response.data);
                    this.setState({
                        myTrips: response.data
                    })
                }
            }).catch((err) => {
                if (err) {
                    console.log(err);

                    if (err.response.status === 404) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No current bookings found.');
                        swal("No current bookings found.")
                    }
                    else if (err.response.status === 400) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No bookings found.');
                        // swal("No bookings found.")
                    }
                }
            });
    }

    checkIn = (bookingId, roomId, totalPrice) => {
        const data = {
            bookingId: bookingId,
            roomId: roomId,
            customerId: localStorage.getItem("userId")
        }
        console.log(data);

        axios.post(rootURL + '/guestCheckIn', data)
            .then(response => {
                let data = ""
                if (response.status === 200) {
                    console.log(response.data);
                    swal("Success", "Successfully, Checked In. Total price of $" + totalPrice + " is deducted", "success")
                    const data1 = {
                        customerId: localStorage.getItem("userId")
                    }
                    this.setState({
                        redirect: true
                    })
                    //404 no bookings for past/future/current
                    //400 no bookings at all
                    axios.post(rootURL + '/getCurrentUserBookingDetails', data1)
                        .then(response => {
                            if (response.status === 200) {
                                console.log(response.data);
                                this.setState({
                                    myTrips: response.data
                                })
                            }
                        }).catch((err) => {
                            if (err) {
                                console.log(err);

                                if (err.response.status === 404) {
                                    this.setState({
                                        isCardAvailable: false
                                    })
                                    console.log("Error message", 'No current bookings found.');
                                    swal("No current bookings found.")
                                }
                                else if (err.response.status === 400) {
                                    this.setState({
                                        isCardAvailable: false
                                    })
                                    console.log("Error message", 'No bookings found.');
                                    // swal("No bookings found.")
                                }
                            }
                        });
                }

            }).catch((err) => {
                if (err) {
                    console.log(err);
                    let data = ""
                    if (err.response.status === 400) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 401) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 402) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 403) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 404) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 405) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                    else if (err.response.status === 406) {
                        console.log("err.response.data", err.response.data);
                        data = "" + err.response.data;
                        swal("Warning", data, "warning")
                    }
                }
            });
    }

    CancelBooking = (bookingId) => {
        const data = {
            bookingId: bookingId,
            customerId: localStorage.getItem("userId")
        }
        axios.post(rootURL + '/cancelBookingGuest', data)
            .then(response => {
                let penalty = ""
                if (response.status === 200) {
                    console.log(response.data);
                    swal("Success", "Successfully cancelled booking", "seccess")
                }
                else if (response.status === 201) {
                    console.log(response.data);
                    penalty = "" + response.data
                    swal("Success", penalty, "success")
                }
                const data1 = {
                    customerId: localStorage.getItem("userId")
                }
                //404 no bookings for past/future/current
                //400 no bookings at all
                axios.post(rootURL + '/getFutureUserBookingDetails', data1)
                    .then(response => {
                        if (response.status === 200) {
                            console.log(response.data);
                            this.setState({
                                myTrips: response.data
                            })
                        }
                    }).catch((err) => {
                        if (err) {
                            console.log(err);

                            if (err.response.status === 404) {

                                console.log("Error message", 'No current bookings found.');
                                swal("No current bookings found.")
                            }
                            else if (err.response.status === 400) {

                                console.log("Error message", 'No bookings found.');
                                // swal("No bookings found.")
                            }
                        }
                    });
            }).catch((err) => {
                if (err) {
                    console.log(err);

                    if (err.response.status === 404) {

                        console.log("Error message", 'Could Not cancel your booking');
                        swal("Failed", "Could Not cancel your booking", "error")
                    }
                    else {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No bookings found.');
                        swal("Unknown error")
                    }
                }
            });
    }


    render() {

        let redrirectVar = null;
        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }
        if (this.state.errorRedirect === true) {
            redrirectVar = <Redirect to="/error" />
        }
        if (this.state.redirect === true) {
            redrirectVar = <Redirect to="/my-trips" />
        }
        let tripDetails = "No Upcoming Bookings For You"
        console.log(this.state.myTrips.length > 0);
        if (this.state.myTrips) {
            tripDetails = this.state.myTrips.map((trip, index) => {
                console.log(trip)
                let checkStatus
                if (trip.bookingStatus === "new") {
                    checkStatus = <button onClick={() => this.checkIn(trip.bookingId, trip.roomId, trip.totalPrice)} className="btn btn-warning" id="button">Check In</button>
                }
                else if (trip.bookingStatus === "checkin") {
                    checkStatus = <button onClick={() => this.checkOut(trip.bookingId)} className="btn btn-warning" id="button">Check out</button>
                }
                console.log(tripDetails);

                return (
                    <div className="container trip-details-container" key={index}>
                        <div className="trip-details-content border">
                            <div className="trip-main-details ">
                                <h2><strong>Booking id: {trip.bookingId}</strong></h2>
                                <div>Check - in Date and Time : {trip.bookingStartDate}</div>
                                <div>Check out Date and Time : {trip.bookingEndDate} </div>
                            </div>
                            <div className="pricing-content">
                                <h3><strong>Total Cost: ${trip.totalPrice}</strong></h3>
                            </div>
                            <div>
                                {checkStatus}
                                <button onClick={() => this.CancelBooking(trip.bookingId)} className="btn btn-danger" id="button">Cancel Booking</button>
                            </div>
                        </div>
                    </div>
                )
            })
        }


        return (
            <div>
                {redrirectVar}
                <Header />
                <div className="my-trips-container">
                    <div className="center-content trip-banner">
                        <h1>My Bookings</h1>
                    </div>
                    <ul className="nav nav-tabs">
                        <li className="col-4 nav-item text-center">
                            <a className="nav-link font-weight-bold text-danger " href="/my-trips/previous"><h4>Past Bookings</h4></a>
                        </li>
                        <li className="col-4 nav-item text-center">
                            <a className="nav-link font-weight-bold text-primary" href="/my-trips" ><h4>Current Bookings</h4></a>
                        </li>
                        <li className="col-4 nav-item text-center">
                            <a className="nav-link font-weight-bold text-success" href="/my-trips/upcoming" ><h4>Upcoming Bookings</h4></a>
                        </li>
                    </ul>
                    <div className="text-primary">
                    </div>
                </div>
                <div className="container">
                    {tripDetails}
                </div>
            </div>
        )
    }
}

export default UpcomingTrips;