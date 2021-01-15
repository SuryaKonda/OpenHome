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

        let redrirectVar = null;
        if (!localStorage.getItem("accountType") === "2") {
            redrirectVar = <Redirect to="/" />
        }

        return (
            <div className="container">
                <Header />
                <hr />
                {redrirectVar}
                <div className="row ">
                    <div className="col-6 center-content">
                        <h1><p className="strong " >Welcome </p></h1>
                        <h1><p className="strong" >to</p></h1>
                        <h1><p className="strong font-weight-bold" >OpenHome!</p></h1>
                    </div>
                    <div className="col-6 center-content">
                        <div className="row center-content">
                            <div>
                                <a href="/owner-property-display" className="btn btn-success btn-lg">Manage Your Property</a>
                            </div>
                        </div>
                        <br /> <br /> <br />
                        <div className="row center-content">
                            <div>
                                <a href="/add-property" className="btn btn-success btn-lg">List Your Property</a>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <div className="row">
                    <div className='col-6 text-danger'>
                        <h2 className="text-danger">Current Time:<br /> {this.state.time}</h2>
                    </div>
                    <div className="col-6 content-center">
                        <h2 className="text-danger">Advance Time:</h2>
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
            </div >
        )
    }
}


export default Home;