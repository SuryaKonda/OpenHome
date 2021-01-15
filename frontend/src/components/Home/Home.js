import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import cookie from 'react-cookies';
import { Redirect } from 'react-router';
import Header from '../Header/Header';
import DisplayProperties from '../DisplayProperties/DisplayProperties';
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import axios from 'axios';
import rootURL from '../Config/settings';

const CardRegEx = /^[0-9]/
const CVVRegeEx = /^[0-9]/

const CardSchema = Yup.object().shape({
    hours: Yup.string()
        .matches(CardRegEx, 'Hours is not valid'),

    minutes: Yup.string()
        .matches(CVVRegeEx, 'Minutes is not valid')
});

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            time: ""
        }
        this.advance = this.advance.bind(this)
    }

    componentWillMount() {
        axios.get(rootURL + "/time")
            .then((response) => {
                console.log(typeof response.data)
                this.setState({
                    time: response.data
                })
            }
            )
    }

    advance(details) {
        let hours = 0;
        let mins = 0;
        if (details.minutes) {
            mins = details.minutes
        }
        if (details.hours) {
            hours = details.hours
        }
        console.log(rootURL + '/addoffset/' + hours + "/" + mins)
        axios.post(rootURL + '/addoffset/' + hours + "/" + mins)
            .then((response) => {
                console.log('response', response);

                if (response.status === 200) {
                    window.location.reload()
                }
            })
            .catch((err) => {
                if (err) {
                    console.log(err.response);


                    this.setState({
                        errorRedirect: true
                    })
                }

            });
    }



    render() {
        var CurrentDate = new Date();
        let redrirectVar = null;
        if (!localStorage.getItem('userId')) {
            redrirectVar = <Redirect to="/login" />
        }


        if (this.props.isSearch) {
            redrirectVar = <Redirect to="/display-properties" />
        }

        return (
            <div className="home-container">
                <Header />
                {redrirectVar}
                <div className="content">
                    <div className="home-page-content">

                        <div className="Hero-Image">

                            <div className="jumbotron-content">
                                <h1 className="text-danger">
                                    Current Time:{this.state.time}
                                </h1>
                                <div className="row content-center">
                                    <div className="col-3">
                                        <h1 className="text-danger">Advance Time:</h1>
                                    </div>
                                    <div className="col-1">
                                        <Formik
                                            initialValues={{
                                                hours: "",
                                                minutes: "",

                                            }}
                                            validationSchema={CardSchema}
                                            onSubmit={(values, actions) => {
                                                this.advance(values)
                                                actions.setSubmitting(false);
                                            }}
                                        >
                                            {({ touched, errors, isSubmitting }) => (
                                                <Form>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="hours">Hours</label>
                                                        <Field
                                                            type="text"
                                                            name="hours"
                                                            //   autofocus="true"
                                                            className={`form-control ${
                                                                touched.hours && errors.hours ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="hours"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="minutes">Minutes</label>
                                                        <Field
                                                            type="minutes"
                                                            name="minutes"
                                                            className={`form-control ${
                                                                touched.minutes && errors.minutes ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="minutes"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <br />
                                                    <button
                                                        type="submit"

                                                        className="btn btn-block btn-primary text-white font-weight-bold"
                                                        disabled={isSubmitting}
                                                    >
                                                        {isSubmitting ? "Please wait..." : "Submit"}
                                                    </button>
                                                </Form>
                                            )}
                                        </Formik>
                                    </div>
                                </div>
                                <h1>
                                    <div className="headline-text">Book beach houses, cabins,</div>
                                    <div className="headline-text">condos and more!</div>
                                </h1>
                                <div className="form-group  search-tab">
                                    <div className='row'>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            CITY:
                                    </span>
                                        <span className="col-lg-4 col-md-12 col-sm-12 col-xs-12 pad-bot-10">
                                            <input type="textbox" className="form-control form-control-lg" name="city" placeholder='San Jose' onChange={this.props.handleInputChange}></input>
                                        </span>

                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            ZipCode:
                                    </span>
                                        <span className="col-lg-2 col-md-12 col-sm-12 col-xs-12 pad-bot-10">
                                            <input type="number" className="form-control form-control-lg" name="zip" placeholder='95112' onChange={this.props.handleInputChange}></input>
                                        </span>
                                    </div>
                                    <div className='row'>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Start Date:
                                    </span>
                                        <span className="col-lg-2 col-md-3 col-sm-4 col-xs-4 pad-bot-10">
                                            <DatePicker className="form-control form-control-lg" name='startDate' dateFormat="YYYY-MM-DD"
                                                minDate={new Date()}
                                                maxDate={moment().add(365, 'days')}
                                                selected={this.props.startDate}
                                                onChange={this.props.handleStartDateChange} />
                                        </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                        </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            End Date:
                                    </span>

                                        <span className="col-lg-2 col-md-3 col-sm-4 col-xs-4 pad-bot-10">
                                            <DatePicker className="form-control form-control-lg" name='endDate' dateFormat="YYYY-MM-DD" selected={this.props.endDate}
                                                minDate={moment(this.props.startDate).add(1, 'days')}
                                                maxDate={moment(this.props.startDate).add(14, 'days')}
                                                onChange={this.props.handleEndDateChange} />
                                        </span>
                                    </div>
                                    <div className='row'>

                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Sharing Type:
                                        </span>
                                        <span className="col-lg-2 col-md-3 col-sm-4 col-xs-4 pad-bot-10">
                                            <select onChange={this.props.handleInputChange} name="sharingType" className="form-control form-control-lg">
                                                <option value="entire place">Entire place</option>
                                                <option value="private room">Private room</option>
                                            </select>
                                        </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">

                                        </span>

                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Property Type:
                                    </span>
                                        <span className="col-lg-2 col-md-3 col-sm-4 col-xs-4 pad-bot-10">
                                            <select onChange={this.props.handleInputChange} name="propertyType" className="form-control form-control-lg">
                                                <option value="house">House</option>
                                                <option value="town house">Town House</option>
                                                <option value="condo">Condo/ Apt</option>
                                            </select>
                                        </span>
                                    </div>
                                    <div className='row'>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Description:
                                    </span>
                                        <span className="col-lg-8 col-md-8 col-sm-8 col-xs-8 pad-bot-10">
                                            <input onChange={this.props.handleInputChange} type="textbox" className="form-control form-control-lg" name="description" placeholder="description" onChange={this.props.handleInputChange}></input>
                                        </span>
                                    </div>
                                    <div className='row'>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Min Price:
                                    </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            <input type="number" className="form-control form-control-lg" name="minPrice" placeholder="$00.00" onChange={this.props.handleInputChange}></input>
                                        </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            Max Price:
                                    </span>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">
                                            <input type="number" className="form-control form-control-lg" name="maxPrice" placeholder="$1000.00" onChange={this.props.handleInputChange}></input>
                                        </span>
                                        <span className="col-lg-2 col-md-3 col-sm-4 col-xs-4 pad-bot-10">
                                            Wi-Fi Availability :<br /> <input type="radio" className="form-control-md" name="wifi" onChange={this.props.handleInputChange} value="true" ></input>Yes
                                        <input type="radio" className="form-control-md" name="wifi" onChange={this.props.handleInputChange} value="false"></input>No
                                    </span>
                                    </div>
                                    <div className='row'>
                                        <span className="col-lg-2 col-md-2 col-sm-2 col-xs-2 pad-bot-10">

                                        </span>
                                        <span className="col-lg-6 col-md-6 col-sm-6 col-xs-6 pad-bot-10">
                                            <button className="btn btn-primary btn-lg" style={{ width: "100%" }} onClick={this.props.searchClick}>Search</button>
                                        </span>
                                    </div>
                                </div>
                            </div>



                            <div className="home-page-list-content hidden-xs">
                                <ul className="home-page-list">
                                    <li className="value-props-item">
                                        <strong className="value-props-title">Your whole vacation starts here</strong>
                                        <span className="value-props-content">Choose a rental from world's best location</span>
                                    </li>
                                    <li className="value-props-item">
                                        <strong className="value-props-title">Book and stay with confidence</strong>
                                        <span className="value-props-content">Secure payments, peace of mind</span>
                                    </li>
                                </ul>
                            </div>
                            <div className="clear">
                            </div>
                        </div>


                        <div className="recent-activity">
                            <div className="jumbotron container recent-activity-content">

                                <div className="container mt-3">
                                    <div id="myCarousel" className="carousel slide" data-ride="carousel">


                                        <ul className="carousel-indicators">
                                            <li data-target="#myCarousel" data-slide-to="0" className="active"></li>
                                            <li data-target="#myCarousel" data-slide-to="1"></li>
                                            <li data-target="#myCarousel" data-slide-to="2"></li>
                                        </ul>


                                        <div className="carousel-inner">
                                            <div className="carousel-item active">
                                                <img className="carousel-img" src="https://odis.homeaway.com/odis/listing/4276d9d1-5735-4c1f-9a9a-22948b7d9bbd.c10.jpg" alt="home-1" />
                                            </div>
                                            <div className="carousel-item">
                                                <img className="carousel-img" src="https://odis.homeaway.com/odis/listing/638f7548-db23-4241-a224-8fc7da156d1e.c10.jpg" alt="home-2" />
                                            </div>
                                            <div className="carousel-item">
                                                <img className="carousel-img" src="https://odis.homeaway.com/odis/listing/668d6c3b-a08c-4a1d-b299-f3d6db1af3af.c10.jpg" alt="home-3" />
                                            </div>
                                        </div>

                                        <a className="carousel-control-prev" href="#myCarousel" data-slide="prev">
                                            <span className="carousel-control-prev-icon"></span>
                                        </a>
                                        <a className="carousel-control-next" href="#myCarousel" data-slide="next">
                                            <span className="carousel-control-next-icon"></span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}


export default Home;