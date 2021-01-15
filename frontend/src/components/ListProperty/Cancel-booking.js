import React, { Component } from 'react';
import Header from '../Header/Header';
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert';

class CancelBooking extends Component {
    constructor(props) {
        super(props)
        this.state = {
            booking: {}
        }
    }

    componentWillMount() {
        console.log(this.props.location.state)
        this.setState({
            booking: this.props.location.state
        })
    }

    cancelBooking() {
        let data = {
            propertyId: this.state.booking.propertyId,
            bookingId: this.state.booking.bookingId,
            roomId: this.state.booking.roomId,
            ownerEmail: localStorage.getItem("userEmail")
        }
        console.log("data in cancel booking", data)
        axios.post(rootURL + '/cancelBookingHost', data)
            .then(response => {
                if (response.status === 200) {
                    console.log("Response : ", response.data);
                    swal("Success", response.data, "warning")
                    this.setState({
                        success: true
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
        if (this.state.success === true) {
            redrirectVar = <Redirect to={"/property/" + this.state.booking.propertyId + "/bookings"} />
        }
        return (
            <div className="container">
                {redrirectVar}
                <Header />
                <div className="container trip-details-container">
                    <div className="trip-details-content border">
                        <div className="trip-main-details blue-text">
                            <h2><strong>Traveler Name: {this.state.booking.customerName}</strong></h2>
                            <div>Start Date: &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.bookingStartDate}</div>
                            <div>End Date: &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.bookingEndDate}</div>
                            <div>Property Name : &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.propertyName}</div>
                            <div>Address: &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.propertyAddress}</div>
                            <div>Payment ID: &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.paymentId}</div>
                            <div>Refund Amount &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.refundAmount}</div>
                            <div>Penalty Amount &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.penaltyAmount}</div>
                            <div>Owner Email &nbsp;&nbsp;&nbsp;&nbsp;{this.state.booking.ownerEmail}</div>
                        </div>

                        <div className="pricing-content">
                            <h3><strong>Total Cost: ${this.state.booking.totalPrice}</strong></h3>
                        </div>
                        <div className="center-content">
                            Do you really want to cancel?
                            <hr />
                            <button
                                className="btn btn-lg btn-danger book-btn" onClick={() => { this.cancelBooking() }}>Yes</button> &nbsp;
                            <button
                                className="btn btn-lg btn-outline-primary book-btn" >No</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default CancelBooking;
