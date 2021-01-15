import React, { Component } from 'react';
import axios from 'axios';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import { Redirect } from 'react-router';
import Header from '../Header/Header';
import rootURL from '../Config/settings';
import swal from 'sweetalert'


let redrirectVar = null;

class DisplayProperties extends Component {

    constructor(props) {
        super(props);

        this.state = {
            red: null,
            arrivalDate: moment(),
            departureDate: moment(),
            Properties: [],
            displayProperty: false,
            isRooms: false,
            Photos: [],
            errorRedirect: false
        }
    }

    componentDidMount = () => {
        console.log(JSON.parse(localStorage.getItem("displayProperties")))
        this.setState({
            Properties: JSON.parse(localStorage.getItem("displayProperties"))
        })
    }

    goToProperty = (property) => {
        console.log('in submit', property.propertyId);
        const data = {
            propertyId: property.propertyId,
            bookingStartDate: localStorage.getItem("startDate"),
            bookingEndDate: localStorage.getItem("endDate")
        }
        console.log("data", data);

        axios.post(rootURL + '/getAvailableRooms', data)
            .then(response => {
                console.log(response.data);
                if (response.status == 200) {
                    if (response.data) {
                        localStorage.setItem("displayrooms", JSON.stringify(response.data))
                        localStorage.setItem("propImage", property.propertyImage)
                        this.setState({
                            isRooms: true
                        });
                    }
                }
                else {
                    swal("No properties found for the given criteria")
                }
            }).catch((err) => {
                if (err) {
                    if (err.response.status === 404) {
                        this.setState({
                            isValidationFailure: false
                        })
                        console.log("Error messagw", 'No rooms available');
                        swal("No rooms available")
                    }
                    else {
                        swal("Unknown error")
                    }
                }
            })
        // swal("Booking Succesfull!", "Confirmation mail has been sent", "success")
    }

    render() {

        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }

        if (this.state.isRooms === true) {
            redrirectVar = <Redirect to="/property-display" />
        }

        let propertyList = this.state.Properties.map((property, index) => {
            // console.log(property)
            return (
                <div className="container display-properties-container" key={index}>
                    <div className="property-content row border" id='property-display'>
                        <div className="property-content-image col-3">
                            <img className="property-image" src={property.propertyImage} alt="property-image" />
                        </div>
                        <div className="property-content-desc col-9 hidden-xs">
                            <div>
                                <h2><strong>{property.propertyName}</strong></h2>
                                <h4>Owner: <strong>{property.ownerName}</strong></h4>
                                <div>{property.address}, {property.city}, {property.state}</div>
                                <div>Property Type : {property.propertyType}</div>
                                <div>Sharing Type : {property.sharingType}</div>
                            </div>
                            <div className="center-content">
                                <button className="btn btn-lg btn-primary book-btn" onClick={() => this.goToProperty(property)}>Get available rooms</button>
                            </div>
                            <div className="pricing-content">
                                {/* <h5><strong>${property.minPrice}</strong> per night</h5> */}
                            </div>
                        </div>
                    </div>
                </div>
            )
        })
        return (
            <div>
                <Header />

                <div className="cotainer">
                    {redrirectVar}
                    {this.state.red}

                    <div className="property-listing-content">
                        {propertyList}
                    </div>
                    <div className="container center-content pad-top-20-pc">
                        {/* <div>
                            Use of this Web site constitutes acceptance of the HomeAway.com Terms and conditions and Privacy policy.
                    </div>
                        <div>
                            ©2018 HomeAway. All rights reserved
                        </div> */}
                    </div>
                </div>
            </div>
        )
    }
}

export default DisplayProperties;