import React, { Component } from 'react'
import Header from '../Header/Header';
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert';

class DeleteProperty extends Component {
    constructor(props) {
        super(props)
        this.state = {
            property: this.props.location.state.property
        }
    }

    deleteProperty() {
        let data = {
            propertyId: this.state.property.propertyId,
            ownerEmail: localStorage.getItem("userEmail")
        }
        console.log("data in delete property", data)
        axios.post(rootURL + '/deleteProperty', data)
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
            redrirectVar = <Redirect to="/owner-property-display" />
        }
        let property = this.state.property
        return (
            <div className="container">
                {redrirectVar}
                <Header />
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

                <div className="center-content">
                    Do you really want to delete this property?
            <hr />
                    <button
                        className="btn btn-lg btn-danger book-btn" onClick={() => { this.deleteProperty() }}>Yes</button> &nbsp;
            <button
                        className="btn btn-lg btn-outline-primary book-btn" >No</button>
                </div>
            </div>
        )
    }
}


export default DeleteProperty;