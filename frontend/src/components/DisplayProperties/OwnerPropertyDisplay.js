import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import cookie from 'react-cookies';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import Header from '../Header/Header';
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert';

class PropertyDisplay extends Component {

    constructor() {
        super();
        this.state = {
            propertyDetails: [],
        }

    }

    componentDidMount() {
        let data = {
            ownerId: localStorage.getItem('userId')
        }
        let imageURLs = []
        console.log('Data: ', data);
        axios.post(rootURL + '/getOwnerProperty', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Result: ', response.data);

                    this.setState({
                        propertyDetails: response.data,

                    });
                    console.log("this.state", this.state)
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
        let redrirectVar = null;
        if (!localStorage.getItem("accountType") === "2") {
            redrirectVar = <Redirect to='/' />
        }
        if (this.state.errorRedirect === true) {
            redrirectVar = <Redirect to="/error" />
        }


        let propertyDetails = null
        console.log("this", this.state.propertyDetails)
        if (typeof this.state.propertyDetails === "object") {
            propertyDetails = this.state.propertyDetails.map((property, index) => {
                return (
                    <div className=" container property-display-content border">
                        <div className="row">
                            <div className="property-display-img-content col-6">
                                <div id="myCarousel" className="carousel slide" data-ride="carousel">
                                    <div className="carousel-inner">
                                        <div className={"carousel-item active"} >
                                            <img className=" carousel-img property-display-img" src={property.propertyImage} alt="property-image" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="property-display-pricing-content col-5 border">
                                <div>
                                    <div className="center-content">
                                        <Link
                                            to={"/property/" + property.propertyId + "/bookings"}
                                            className="btn btn-lg btn-dark book-btn" >Manage Bookings</Link>
                                    </div>
                                    <hr />
                                    <div className="center-content">
                                        <Link to={"/property/" + property.propertyId + '/rooms'} className="btn btn-lg btn-warning book-btn" >View Rooms</Link>
                                    </div>
                                    <hr />
                                    <div className="center-content">
                                        <Link to={"/property/" + property.propertyId + '/addroom'} className="btn btn-lg btn-success book-btn" >Add Rooms</Link>
                                    </div>
                                    <hr />
                                    <div className="center-content">
                                        <Link
                                            to={
                                                {
                                                    pathname: `/${property.propertyId}/editproperty`,
                                                    state: { property: property }
                                                }
                                            }
                                            className="btn btn-lg btn-primary book-btn" >Edit Property</Link>
                                    </div>
                                    <hr />
                                    <div className="center-content">
                                        <Link
                                            to={
                                                {
                                                    pathname: `/${property.propertyId}/report`,
                                                    state: { property: property }
                                                }
                                            }
                                            className="btn btn-lg btn-secondary book-btn" >View Report</Link>
                                    </div>
                                    <hr />
                                    <div className="center-content">
                                        <Link to={{
                                            pathname: "/property/" + property.propertyId + '/delete',
                                            state: {
                                                property: property
                                            }
                                        }} className="btn btn-lg btn-danger book-btn" >Delete Property</Link>
                                    </div>
                                    <hr />

                                    <div className="center-content">
                                        <label htmlFor="ownername">Property Owner: </label>
                                        <span id="ownername"><strong> {property.ownerName}</strong></span>
                                    </div>
                                </div>
                                <div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="property-display-details-content col-6">
                                <div className="details-content-headline-text"><h4><strong>{property.propertyName}</strong></h4></div>
                                <div className="details-content-headline-text"><h4>{property.Description}</h4></div>
                                <div>
                                    <p>{property.address}, {property.city} {property.state}</p>
                                </div>
                                <div className="details-table">
                                    <table className="table table-hover">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Details</th>
                                                <th scope="col">Information</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <th scope="row">1</th>
                                                <td>Property type</td>
                                                <td>{property.propertyType}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">2</th>
                                                <td>Total Space</td>
                                                <td>{property.totalSpace}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">3</th>
                                                <td>Sharing Type</td>
                                                <td>{property.sharingType}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">4</th>
                                                <td>Parking Availabilty</td>
                                                <td>{property.parkingAvailability}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">5</th>
                                                <td>Free Parking</td>
                                                <td>{property.freeParking}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">5</th>
                                                <td>Parking Fee</td>
                                                <td>{property.parkingFee}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">5</th>
                                                <td>WiFi Availabilty</td>
                                                <td>{property.wifi}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                )
            })
        }
        else {
            swal("Oops", "No properties added!")
            propertyDetails = <Redirect to="/"></Redirect>
        }



        return (
            <div>
                <Header />
                {redrirectVar}
                {propertyDetails}
            </div>
        )
    }
}

export default PropertyDisplay;