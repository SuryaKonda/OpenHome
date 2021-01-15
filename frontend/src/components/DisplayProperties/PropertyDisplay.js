import React, { Component } from 'react';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import { Redirect } from 'react-router';
import Header from '../Header/Header';




class PropertyDisplay extends Component {

    constructor() {
        super();

        this.state = {
            arrivalDate: moment(),
            departureDate: moment(),
            propertyRooms: {},
            photos: [],
            totalCost: 0,
            errorRedirect: false,
            totalDays: 1
        }
    }

    componentWillMount() {
        this.setState({
            propertyRooms: JSON.parse(localStorage.getItem("displayrooms"))
        })
    }

    submitBooking = (roomId, propertyId) => {
        console.log('in submit');
        localStorage.setItem("roomId", roomId)
        localStorage.setItem("propertyId", propertyId)
        this.setState({
            isCardAvailable: true
        })

    }

    render() {
        let redrirectVar = null;
        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }

        if (this.state.isCardAvailable === true) {
            redrirectVar = <Redirect to="/showcards" />
        }

        var totalCost = 0;

        if (this.state.propertyDetails) {
            const startDate = moment(this.state.arrivalDate);
            const timeEnd = moment(this.state.departureDate);
            const diff = timeEnd.diff(startDate);
            const diffDuration = moment.duration(diff);
            totalCost = (diffDuration._data.days + 1) * this.state.propertyDetails.minPrice;

        }
        let propertyList = this.state.propertyRooms.map((property, index) => {
            console.log(property)
            return (
                <div className="container display-properties-container" key={index}>

                    <div className="property-content row border" id='property-display'>
                        <div className="property-content-image col-3">
                            <img className="property-image" src={localStorage.getItem('propImage')} alt="property-image" />
                        </div>
                        <div className="property-content-desc col-9 hidden-xs">
                            <div>

                                <div>Room Space : {property.roomSpace} Sq.ft</div>
                                <div>WeekDay Price : ${property.weekdayPrice}</div>
                                <div>WeekEnd Price : ${property.weekendPrice}</div>

                            </div>
                            <div className="center-content">
                                <button className="btn btn-lg btn-primary book-btn" onClick={() => this.submitBooking(property.roomId, property.propertyId)}>Book now</button>
                            </div>
                            <div className="pricing-content">
                                {/* <button className = 'btn btn-success' onClick = {this.submitBooking}>Book Now</button> */}
                            </div>
                        </div>

                    </div>
                </div>
            )
        })


        return (
            <div>
                <Header />
                {redrirectVar}
                <div className="property-display-details-content ">
                    <div className="property-description-content">
                        {propertyList}
                    </div>
                </div>
            </div>
        )
    }
}

export default PropertyDisplay;