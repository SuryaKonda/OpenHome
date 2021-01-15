import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Header from './Header/Header';
// import DisplayProperties from './DisplayProperties/OwnerDisplayProperties';
import MyTrips from './MyTrips/MyTrips';
import Error from './Error/Error';
import moment from 'moment';
import Login from './Login/Login';
import Signup from './Signup/Signup';
import OwnerHome from './Home/OwnerHome';
import AddProperty from './ListProperty/Add-property';
import OwnerPropertyDisplay from './DisplayProperties/OwnerPropertyDisplay';
import OwnerDashboard from './OwnerDashboard/OwnerDashboard';
import OwnerProfile from './Profile/OwnerProfile';
import { Link } from 'react-router-dom';
import rootURL from './Config/settings';
import { Redirect } from 'react-router';
import axios from 'axios';
import swal from 'sweetalert'
import DisplayProperties from './DisplayProperties/DisplayProperties';
import PropertyDisplay from './DisplayProperties/PropertyDisplay';
import Home from './Home/Home';
import AddRoom from './ListProperty/AddRoom';
import ViewRooms from './ListProperty/ViewRooms';
import EditRoom from './ListProperty/EditRoom';
import EditProperty from './ListProperty/EditProperty';
import CancelBooking from './ListProperty/Cancel-booking';
import ShowCards from './Payment/ShowCards'
import PastTrips from './MyTrips/PastTrips'
import UpcomingTrips from './MyTrips/UpcomingTrips'
import OwnerReport from './Report/OwnerReport';
import PropertyReport from './Report/PropertyReport';
import Summary from './Report/UserReport';
import DeleteProperty from './DisplayProperties/DeleteProperty'

class Main extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isSearch: false,
            startDate: moment(),
            endDate: moment(),
            city: "san jose",
            zip: "",
            sharingType: "",
            propertyType: "",
            description: "",
            minPrice: "",
            maxPrice: "",
            wifi: "",
            totalDays: 1,

            Properties: []
        }
        this.handlesearchClick = this.handlesearchClick.bind(this);
        // this.handleStartDateChange = this.handleStartDateChange.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        // this.handleEndDateChange = this.handleEndDateChange.bind(this);
    }

    handlesearchClick = () => {
        const SDate = this.state.startDate.year() + '-' + (this.state.startDate.month() + 1) + '-' + this.state.startDate.date();
        const EDate = this.state.endDate.year() + '-' + (this.state.endDate.month() + 1) + '-' + this.state.endDate.date();

        if (SDate != EDate) {
            var data = {
                city: this.state.city,
                zip: this.state.zip,
                startDate: this.state.startDate._d,
                endDate: this.state.endDate._d,
                sharingType: this.state.sharingType,
                propertyType: this.state.propertyType,
                description: this.state.description,
                minPrice: this.state.minPrice,
                maxPrice: this.state.maxPrice,
                wifi: this.state.wifi,
                totalDays: this.state.totalDays
            };
            console.log('data', SDate)
            console.log('data', EDate)
            axios.post(rootURL + '/searchProperty', data)
                .then(response => {
                    console.log(response.data);

                    const totalDays = this.props.totalDays
                    if (response.data) {
                        localStorage.setItem("displayProperties", JSON.stringify(response.data))
                        localStorage.setItem("startDate", SDate)
                        localStorage.setItem("endDate", EDate)
                        this.setState({
                            Properties: response.data,
                            totalDays: totalDays,
                            isSearch: true
                        });
                    }
                    else {
                        swal("No properties found for the given criteria")
                    }
                }).catch((err) => {
                    if (err) {
                        this.setState({
                            errorRedirect: true
                        })
                    }
                })
        }
        else {
            swal("Use different dates for Check-in and Check-out")
        }
    }

    handleStartDateChange = (date) => {
        this.setState({
            startDate: date
        })
        console.log(date);

    }

    handleEndDateChange = (date) => {

        this.setState({
            endDate: date
        })

    }

    handleInputChange = (event) => {
        var target = event.target;
        var name = target.name;
        var value = target.value;

        this.setState({
            [name]: value
        });
    }



    render() {
        return (
            <div>
                {/** Render Different Components based on ROute*/}

                <Route exact path="/" component={Login} />
                <Route path="/sign-up" component={Signup} />
                <Route path="/login" component={Login} />
                <Route path='/ownerhome' component={OwnerHome} />
                <Route path="/add-property" component={AddProperty} />
                <Route path="/property/:id/addroom" component={AddRoom} />
                <Route path="/property/:id/rooms" component={ViewRooms} />
                <Route path="/:id/editproperty" component={EditProperty} />
                <Route path="/editroom/:id" component={EditRoom} />
                {/* <Route path="/owner-dashboard" component={OwnerDashboard} /> */}
                <Route path="/owner-profile" component={OwnerProfile} />
                <Route path="/owner-property-display" component={OwnerPropertyDisplay} />
                <Route path="/property/:id/bookings" component={OwnerDashboard} />
                <Route path="/cancelbooking/:id" component={CancelBooking} />
                <Route path="/ownerreport" component={OwnerReport} />
                <Route path="/:id/report" component={PropertyReport} />
                <Route path="/userreport" component={Summary} />

                {/* 
                <Route exact render={() => {
                    return (
                        <Home
                            handleInputChange={this.handleInputChange} searchText={this.state.searchText}
                            handleEndDateChange={this.handleEndDateChange.bind(this)} endDate={this.state.endDate}
                            handleStartDateChange={this.handleStartDateChange.bind(this)} startDate={this.state.startDate}
                            isSearch={this.state.isSearch} searchClick={this.handlesearchClick}
                            city={this.state.city}
                            zipcode={this.state.zipcode}
                        />
                    );
                }}
                    path="/userhome" /> */}

                <Route render={() => {
                    return (
                        <Home
                            handleInputChange={this.handleInputChange}
                            handleEndDateChange={this.handleEndDateChange.bind(this)} endDate={this.state.endDate}
                            handleStartDateChange={this.handleStartDateChange.bind(this)} startDate={this.state.startDate}
                            isSearch={this.state.isSearch} searchClick={this.handlesearchClick}
                            city={this.state.city}
                            zipcode={this.state.zipcode}
                        />
                    );
                }} path="/home" />

                <Route render={() => {
                    return (
                        <DisplayProperties properties={this.state.Properties} />
                    );
                }}
                    path="/display-properties" />


                <Route path="/property-display" component={PropertyDisplay} />
                <Route exact path="/my-trips" component={MyTrips} />
                <Route exact path="/my-trips/previous" component={PastTrips} />
                <Route exact path="/my-trips/upcoming" component={UpcomingTrips} />
                <Route path='/showcards' component={ShowCards} />
                <Route path='/property/:id/delete' component={DeleteProperty} />
                <Route path='/error' component={Error} />

            </div>
        )
    }

}

export default Main