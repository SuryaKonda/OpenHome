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
        axios.post(rootURL + '/getPastUserBookingDetails', data)
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

    render() {

        let redrirectVar = null;
        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }
        if (this.state.errorRedirect === true) {
            redrirectVar = <Redirect to="/error" />
        }
        let tripDetails = "No Past Bookings For You"
        console.log(this.state.myTrips);
        if (this.state.myTrips.length > 0) {
            tripDetails = this.state.myTrips.map((trip, index) => {

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
                        </div>
                    </div>
                )
            })
        }

        console.log(tripDetails);

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
                <div className = "container">
                    {tripDetails}
                </div>
            </div>
        )
    }
}

export default UpcomingTrips;