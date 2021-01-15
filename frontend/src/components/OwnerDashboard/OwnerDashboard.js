import React, { Component } from 'react';
import Header from '../Header/Header';
import cookie from 'react-cookies';
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import { Link } from "react-router-dom";


class OwnerDashboard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentTripDetails: [],
            pastTripDetails: [],
            futureTripDetails: [],
            errorRedirect: false,
            selectedView: ""
        }
        this.selectCurrect = this.selectCurrect.bind(this);
        this.selectPast = this.selectPast.bind(this);
        this.selectFuture = this.selectFuture.bind(this);

    }

    selectCurrect() {
        this.setState({
            selectedView: 1
        })
    }

    selectFuture() {
        this.setState({
            selectedView: 2
        })
    }

    selectPast() {
        this.setState({
            selectedView: 3
        })
    }

    componentWillMount() {
        // axios.defaults.withCredentials = true;
        let data = {
            propertyId: this.props.match.params.id
        }
        axios.post(rootURL + '/getCurrentOwnerBookingDetails', data)
            .then(response => {
                if (response.status === 200) {
                    console.log("Response : ", response.data);
                    this.setState({
                        currentTripDetails: response.data
                    });
                }
            }).catch((err) => {
                if (err) {
                    this.setState({
                        errorRedirect: true
                    })
                }
            });
        axios.post(rootURL + '/getPastOwnerBookingDetails', data)
            .then(response => {
                if (response.status === 200) {
                    console.log("Response : ", response.data);
                    this.setState({
                        pastTripDetails: response.data
                    });
                }
            }).catch((err) => {
                if (err) {
                    this.setState({
                        errorRedirect: true
                    })
                }
            });
        axios.post(rootURL + '/getFutureOwnerBookingDetails', data)
            .then(response => {
                if (response.status === 200) {
                    console.log("Response : ", response.data);
                    this.setState({
                        futureTripDetails: response.data
                    });
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

        let redrirectVar = null;
        if (!localStorage.getItem("accountType") === "2") {
            redrirectVar = <Redirect to="/" />
        }
        if (this.state.errorRedirect === true) {
            redrirectVar = <Redirect to="/error" />
        }
        let tripDetails
        if (this.state.selectedView === '') {
            tripDetails = this.state.currentTripDetails.map(function (trip, index) {

                return (
                    <div className="container trip-details-container" key={index}>
                        <div className="trip-details-content border">
                            <div className="trip-main-details blue-text">
                                <h2><strong>Traveler Name: {trip.customerName}</strong></h2>
                                <div>Start Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingStartDate}</div>
                                <div>End Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingEndDate}</div>
                                <div>Property Name : &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyName}</div>
                                <div>Address: &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyAddress}</div>
                                <div>Payment ID: &nbsp;&nbsp;&nbsp;&nbsp;{trip.paymentId}</div>
                                <div>Refund Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.refundAmount}</div>
                                <div>Penalty Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.penaltyAmount}</div>
                                <div>Owner Email &nbsp;&nbsp;&nbsp;&nbsp;{trip.ownerEmail}</div>
                            </div>

                            <div className="pricing-content">
                                <h3><strong>Total Cost: ${trip.totalPrice}</strong></h3>
                            </div>
                            <div className="center-content">
                                <Link to={
                                    {
                                        pathname: `/cancelbooking/${trip.bookingId}`,
                                        state: trip
                                    }
                                }
                                    className="btn btn-lg btn-danger book-btn" >Cancel Booking</Link>
                            </div>
                        </div>
                    </div>
                )
            })
        }
        if (this.state.selectedView === 1) {
            tripDetails = this.state.currentTripDetails.map(function (trip, index) {

                return (
                    <div className="container trip-details-container" key={index}>
                        <div className="trip-details-content border">
                            <div className="trip-main-details blue-text">
                                <h2><strong>Traveler Name: {trip.customerName}</strong></h2>
                                <div>Start Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingStartDate}</div>
                                <div>End Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingEndDate}</div>
                                <div>Property Name : &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyName}</div>
                                <div>Address: &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyAddress}</div>
                                <div>Payment ID: &nbsp;&nbsp;&nbsp;&nbsp;{trip.paymentId}</div>
                                <div>Refund Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.refundAmount}</div>
                                <div>Penalty Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.penaltyAmount}</div>
                                <div>Owner Email &nbsp;&nbsp;&nbsp;&nbsp;{trip.ownerEmail}</div>
                            </div>

                            <div className="pricing-content">
                                <h3><strong>Total Cost: ${trip.totalPrice}</strong></h3>
                            </div>
                            <div className="center-content">
                                <Link to={
                                    {
                                        pathname: `/cancelbooking/${trip.bookingId}`,
                                        state: trip
                                    }
                                }
                                    className="btn btn-lg btn-danger book-btn" >Cancel Booking</Link>
                            </div>
                        </div>
                    </div>
                )
            })
        }
        if (this.state.selectedView === 3) {
            tripDetails = this.state.pastTripDetails.map(function (trip, index) {

                return (
                    <div className="container trip-details-container" key={index}>
                        <div className="trip-details-content border">
                            <div className="trip-main-details blue-text">
                                <h2><strong>Traveler Name: {trip.customerName}</strong></h2>
                                <div>Start Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingStartDate}</div>
                                <div>End Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingEndDate}</div>
                                <div>Property Name : &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyName}</div>
                                <div>Address: &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyAddress}</div>
                                <div>Payment ID: &nbsp;&nbsp;&nbsp;&nbsp;{trip.paymentId}</div>
                                <div>Refund Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.refundAmount}</div>
                                <div>Penalty Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.penaltyAmount}</div>
                                <div>Owner Email &nbsp;&nbsp;&nbsp;&nbsp;{trip.ownerEmail}</div>
                            </div>

                            <div className="pricing-content">
                                <h3><strong>Total Cost: ${trip.totalPrice}</strong></h3>
                            </div>
                        </div>
                    </div>
                )
            })
        }
        if (this.state.selectedView === 2) {
            tripDetails = this.state.futureTripDetails.map(function (trip, index) {

                return (
                    <div className="container trip-details-container" key={index}>
                        <div className="trip-details-content border">
                            <div className="trip-main-details blue-text">
                                <h2><strong>Traveler Name: {trip.customerName}</strong></h2>
                                <div>Start Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingStartDate}</div>
                                <div>End Date: &nbsp;&nbsp;&nbsp;&nbsp;{trip.bookingEndDate}</div>
                                <div>Property Name : &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyName}</div>
                                <div>Address: &nbsp;&nbsp;&nbsp;&nbsp;{trip.propertyAddress}</div>
                                <div>Payment ID: &nbsp;&nbsp;&nbsp;&nbsp;{trip.paymentId}</div>
                                <div>Refund Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.refundAmount}</div>
                                <div>Penalty Amount &nbsp;&nbsp;&nbsp;&nbsp;{trip.penaltyAmount}</div>
                                <div>Owner Email &nbsp;&nbsp;&nbsp;&nbsp;{trip.ownerEmail}</div>
                            </div>

                            <div className="pricing-content">
                                <h3><strong>Total Cost: ${trip.totalPrice}</strong></h3>
                            </div>
                            <div className="center-content">
                                <Link to={
                                    {
                                        pathname: `/cancelbooking/${trip.bookingId}`,
                                        state: trip
                                    }
                                }
                                    className="btn btn-lg btn-danger book-btn" >Cancel Booking</Link>
                            </div>
                        </div>
                    </div>
                )
            })

        }
        return (
            <div className="container">
                {redrirectVar}
                <Header />
                <div className='row'>
                    <div className='col-10 text-center' >
                        <h4 className='font-weight-bold'>Owner Dashboard</h4>
                        <h6 className='text-secondary'>@{localStorage.getItem("userName")}</h6>
                    </div>
                </div>
                <div className="row">
                    <div className="col-4">
                        <button className="btn btn-outline-primary font-weight-bold text-secondary" onClick={this.selectCurrect}>Current Bookings</button>
                    </div>
                    <div className="col-4">
                        <button className="btn btn-outline-primary font-weight-bold text-secondary" onClick={this.selectFuture}>Future Bookings</button>
                    </div>
                    <div className="col-4">
                        <button className="btn btn-outline-primary font-weight-bold text-secondary" onClick={this.selectPast}>Past Bookings</button>
                    </div>
                </div>
                <div className="row">
                    {tripDetails}
                </div>
            </div>
        )
    }
}

export default OwnerDashboard;