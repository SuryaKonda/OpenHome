import React, { Component } from 'react'
import Header from '../Header/Header'
import { Redirect } from 'react-router';
import axios from 'axios';
import rootURL from '../Config/settings';
import { Link } from "react-router-dom";

class PropertyReport extends Component {
    constructor(props) {
        super(props)
        this.state = {
            propertyReport: [],
            reportValues: []
        }
    }

    componentWillMount() {
        let data = {
            propertyId: this.props.match.params.id,
            ownerEmail: localStorage.getItem("userEmail")
        }
        axios.post(rootURL + '/getOwnerReport', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Result: ', response.data);

                    this.setState({
                        propertyReport: response.data,
                        reportValues: Object.values(response.data)
                    });

                    console.log(this.state);
                }
                else {
                    console.log("no data received from get owner property report")
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


        let propertyDetails = null
        let totalAmount = 0
        propertyDetails = Object.keys(this.state.propertyReport).map((report, index) => {
            return (
                <div>
                    {report}: ${this.state.reportValues[index]}
                </div>
            )
        })
        this.state.reportValues.map((report, index) => {
            totalAmount = totalAmount + report
        })

        return (
            <div className="container ">
                <Header />
                <div className="center-content font-weight-bold">
                    Property Report for {this.props.location.state.property.propertyName}
                </div>
                <hr />
                <div className="center-content">
                    {propertyDetails}
                </div>
                <hr />
                <div className="center-content">
                    Total Amount: ${totalAmount}
                </div>


            </div >
        )
    }
}

export default PropertyReport