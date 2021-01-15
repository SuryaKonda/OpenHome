import React, { Component } from "react";
import { Formik, Form, Field, ErrorMessage, FastField } from "formik";
// import twitterlogo from "../../images/twitterlogo.png";
// import default_avatar from "../../images/default_avatar.png";
import * as Yup from "yup";
import { Link } from "react-router-dom";
import axios from 'axios';
import swal from 'sweetalert';
import rootURL from "../Config/settings";
import { Redirect } from 'react-router';
import Header from '../Header/Header';

const zipRegEx = /^[0-9]{5}(?:-[0-9]{4})?$/
const phoneRegExp = /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/

const DetailsSchema = Yup.object().shape({
    propertyName: Yup.string()
        .required("Property name is required"),
    // streetAddress: Yup.string()
    //     .required("Street Address is required"),
    propertyPhone: Yup.string()
        .matches(phoneRegExp, 'Phone number is not valid')
        .required("Phone number is required"),
    // state: Yup.string()
    //     .required("State is required"),
    // city: Yup.string()
    //     .required("City is required"),
    // zipCode: Yup.string()
    //     .matches(zipRegEx, "Zip code is not valid")
    //     .required("ZIP code is required")
});

const PropertySchema = Yup.object().shape({
    description: Yup.string()
        .required("Description is required"),
    propertyType: Yup.string()
        .required("Property Type is required"),
    sharingType: Yup.string()
        .required("Sharing Type is required"),
    totalSpace: Yup.number()
        .required("Total Space is required"),
    parking: Yup.string()
        .required("Parking availability is required"),
    isWifi: Yup.string()
        .required("WiFi availability is required")
})

class EditProperty extends Component {
    constructor() {
        super()
        this.state = {
            locationActive: true,
            detailsActive: false,


            propertyName: "",
            propertyPhone: "",
            streetAddress: "",
            city: "",
            state: "",
            zipCode: "",

            description: "",
            propertyType: "",
            sharingType: "",
            totalSpace: "",
            parking: "",
            freeParking: "",
            parkingFee: "",
            isWifi: "",
            propertyImage: "",

            locationError: false,
            detailsError: false,
            propertyInsertComplete: false,
            errorRedirect: false
        }
        this.handleDetailsClick = this.handleDetailsClick.bind(this);
        this.handleLocationClick = this.handleLocationClick.bind(this);
        this.submitPropertyDetails = this.submitPropertyDetails.bind(this);
    }

    handleDetailsClick = (details) => {
        console.log("details in handle details click", details)

        this.setState({
            propertyName: details.propertyName,
            propertyPhone: details.propertyPhone,
            streetAddress: details.streetAddress,
            city: details.city,
            state: details.state,
            zipCode: details.zipCode,
            validator: true,
            locationActive: false,
            detailsActive: true
        })
    }

    handleLocationClick = () => {
        this.setState({
            locationActive: true,
            detailsActive: false
        })
    }

    componentWillMount() {
        console.log("data in edit property", this.props.location.state.property.propertyName)
        if (this.props.location.state) {
            this.setState({
                propertyName: this.props.location.state.property.propertyName,
                propertyPhone: this.props.location.state.property.propertyPhone,
                streetAddress: this.props.location.state.property.address,
                city: this.props.location.state.property.city,
                state: this.props.location.state.property.state,
                zipCode: this.props.location.state.property.zip,

                description: this.props.location.state.property.description,
                propertyType: this.props.location.state.property.propertyType,
                sharingType: this.props.location.state.property.sharingType,
                totalSpace: this.props.location.state.property.totalSpace,
                parking: this.props.location.state.property.parkingAvailability,
                freeParking: this.props.location.state.property.freeParking,
                parkingFee: this.props.location.state.property.parkingFee,
                isWifi: this.props.location.state.property.wifi,
                propertyImage: this.props.location.state.property.propertyImage,
            })

            console.log("updated state in edit property", this.state)
        }
        else {
            this.setState({
                errorRedirect: true
            })
        }


    }

    submitPropertyDetails = (e) => {
        console.log("details in submit property details", e)
        this.setState({
            description: e.description,
            propertyType: e.propertyType,
            sharingType: e.sharingType,
            totalSpace: e.totalSpace,
            freeParking: e.freeParking,
            parkingFee: e.parkingFee,
            parking: e.parking,
            isWifi: e.isWifi,
            propertyImage: e.propertyImage
        })
        const data = {
            ownerId: localStorage.getItem("userId"),
            ownerName: localStorage.getItem("userName"),
            ownerEmail: localStorage.getItem('userEmail'),
            propertyId: this.props.location.state.property.propertyId,
            propertyName: this.state.propertyName,
            propertyPhone: this.state.propertyPhone,
            address: this.state.streetAddress,
            state: this.state.state,
            city: this.state.city,
            zip: this.state.zipCode,
            description: this.state.description,
            parkingAvailability: this.state.parking,
            freeParking: this.state.freeParking,
            parkingFee: this.state.parkingFee,
            wifi: this.state.isWifi,
            totalSpace: this.state.totalSpace,
            sharingType: this.state.sharingType,
            propertyType: this.state.propertyType,
            propertyImage: this.state.propertyImage
        }

        console.log("data in edit property final", data)

        axios.post(rootURL + '/updateProperty', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('Success!');
                    this.setState({
                        propertyInsertComplete: true,
                        pricingError: false
                    })
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
        let redirectVar = null;
        if (!localStorage.getItem("accountType") === "2") {
            redirectVar = <Redirect to="/" />
        }
        if (this.state.propertyInsertComplete === true) {
            redirectVar = <Redirect to="/" />
        }
        if (this.state.errorRedirect === true) {
            redirectVar = <Redirect to='/error' />
        }
        console.log("state in edit property", this.state)
        return (
            <div>
                <Header />
                {redirectVar}
                <div className="add-property-content">
                    <div className="container">
                        <hr />
                        <div className="row">
                            <div className="menu-bar-ver col-3">
                                <ul>
                                    <li>Welcome</li>
                                    <li> <a href="#" onClick={this.handleLocationClick}>Property</a></li>
                                    <li><a href="#" onClick={this.handleDetailsClick}>Details</a></li>
                                </ul>
                            </div>
                            <div className="menu-bar-hor border col-8">
                                <div className="add-property-form">
                                    <div className={this.state.locationActive ? "location-form show-form" : "location-form"}>
                                        <div className="location-form-headlinetext">
                                            <h4>Property Details</h4>
                                        </div>
                                        <hr />
                                        <div className="details-form-description pad-bot-10">
                                            <p>Edit the following details!</p>
                                        </div>
                                        <br />
                                        <Formik
                                            initialValues={{
                                                propertyName: this.state.propertyName,
                                                propertyPhone: this.state.propertyPhone,
                                                // streetAddress: this.state.streetAddress,
                                                // city: this.state.city,
                                                // state: this.state.state,
                                                // zipCode: this.state.zipCode
                                            }}
                                            validationSchema={DetailsSchema}
                                            onSubmit={(values, actions) => {
                                                this.handleDetailsClick(values)
                                                actions.setSubmitting(false);
                                            }}
                                        >
                                            {({ touched, errors, isSubmitting }) => (
                                                <Form>
                                                    <div className="form-group text-left">
                                                        <label htmlFor="propertyName">Property Name</label>
                                                        <Field
                                                            type="text"
                                                            name="propertyName"
                                                            className={`form-control ${
                                                                touched.propertyName && errors.propertyName ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="propertyName"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="propertyPhone">Property Phone</label>
                                                        <Field
                                                            type="text"
                                                            name="propertyPhone"
                                                            className={`form-control ${
                                                                touched.propertyPhone && errors.propertyPhone ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="propertyPhone"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <br />
                                                    <button
                                                        type="submit"

                                                        className="btn btn-lg btn-primary text-white float-right font-weight-bold"
                                                        disabled={isSubmitting}
                                                    >
                                                        {isSubmitting ? "Please wait..." : "Next"}
                                                    </button>
                                                </Form>
                                            )}
                                        </Formik>
                                    </div>

                                    <div className={this.state.detailsActive ? "details-form show-form" : "details-form"}>
                                        <div className="details-form-headlinetext">
                                            <h4>Describe your property</h4>
                                        </div>
                                        <hr />
                                        <div className="details-form-description pad-bot-10">
                                            <p>Share the details of your property</p>
                                        </div>

                                        <Formik
                                            initialValues={{
                                                description: this.state.description,
                                                propertyType: this.state.propertyType,
                                                sharingType: this.state.sharingType,
                                                totalSpace: this.state.totalSpace,
                                                freeParking: this.state.freeParking,
                                                parkingFee: this.state.parkingFee,
                                                parking: this.state.parking,
                                                isWifi: this.state.isWifi,
                                                propertyImage: this.state.propertyImage
                                            }}
                                            validationSchema={PropertySchema}
                                            onSubmit={(values, actions) => {
                                                this.submitPropertyDetails(values)
                                                actions.setSubmitting(false);
                                            }}
                                        >
                                            {({ touched, errors, isSubmitting }) => (
                                                <Form>
                                                    <div className="form-group text-left">
                                                        <label htmlFor="description">Description</label>
                                                        <Field
                                                            type="text"
                                                            name="description"
                                                            className={`form-control ${
                                                                touched.description && errors.description ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="description"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="propertyType">Property Type</label> &nbsp;
                                                             <Field
                                                            name="propertyType"
                                                            component="select"
                                                            placeholder="Property Type"
                                                            className={`form-control ${
                                                                touched.propertyType && errors.propertyType ? "is-invalid" : ""
                                                                }`}
                                                        >
                                                            <option value="" label="Select your Property Type" />
                                                            <option value="house">House</option>
                                                            <option value="town house">Town House</option>
                                                            <option value="condo">Condo/Apt</option>

                                                        </Field>
                                                        <ErrorMessage
                                                            component="div"
                                                            name="propertyType"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="sharingType">Sharing Type</label> &nbsp;
                                                             <Field
                                                            name="sharingType"
                                                            component="select"
                                                            placeholder="Sharing Type"
                                                            className={`form-control ${
                                                                touched.sharingType && errors.sharingType ? "is-invalid" : ""
                                                                }`}
                                                        >
                                                            <option value="" label="Select a Sharing Type for you property" />
                                                            <option value="entire place">Entire Place</option>
                                                            <option value="private room">Private Room</option>

                                                        </Field>
                                                        <ErrorMessage
                                                            component="div"
                                                            name="sharingType"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>



                                                    <div className="form-group text-left">
                                                        <label htmlFor="parking">Is parking available on your property?</label> &nbsp;
                                                             <Field
                                                            name="parking"
                                                            component="select"
                                                            placeholder="Parking"
                                                            className={`form-control ${
                                                                touched.parking && errors.parking ? "is-invalid" : ""
                                                                }`}
                                                        >
                                                            <option value="" label="Select an option" />
                                                            <option value="true">Yes</option>
                                                            <option value="false">No</option>

                                                        </Field>
                                                        <ErrorMessage
                                                            component="div"
                                                            name="parking"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="freeParking">If parking available, is it free?</label> &nbsp;
                                                             <Field
                                                            name="freeParking"
                                                            component="select"
                                                            placeholder="freeParking"
                                                            className={`form-control ${
                                                                touched.freeParking && errors.freeParking ? "is-invalid" : ""
                                                                }`}
                                                        >
                                                            <option value="" label="Select an option" />
                                                            <option value="true">Yes</option>
                                                            <option value="false">No</option>

                                                        </Field>
                                                        <ErrorMessage
                                                            component="div"
                                                            name="freeParking"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="parkingFee">How much is the charge for parking? (if applicable)</label>
                                                        <Field
                                                            type="number"
                                                            name="parkingFee"
                                                            className={`form-control ${
                                                                touched.parkingFee && errors.parkingFee ? "is-invalid" : ""
                                                                }`}
                                                        />
                                                        <ErrorMessage
                                                            component="div"
                                                            name="parkingFee"
                                                            align="text-left"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <div className="form-group text-left">
                                                        <label htmlFor="isWifi">Is WiFi available?</label> &nbsp;
                                                             <Field
                                                            name="isWifi"
                                                            component="select"
                                                            placeholder="isWifi"
                                                            className={`form-control ${
                                                                touched.isWifi && errors.isWifi ? "is-invalid" : ""
                                                                }`}
                                                        >
                                                            <option value="" label="Select an option" />
                                                            <option value="true">Yes</option>
                                                            <option value="false">No</option>

                                                        </Field>
                                                        <ErrorMessage
                                                            component="div"
                                                            name="isWifi"
                                                            className="invalid-feedback"
                                                        />
                                                    </div>

                                                    <br />
                                                    <button
                                                        type="submit"

                                                        className="btn btn-lg btn-primary text-white font-weight-bold"
                                                        disabled={isSubmitting}
                                                    >
                                                        {isSubmitting ? "Please wait..." : "Update Property"}
                                                    </button>
                                                </Form>
                                            )}
                                        </Formik>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}


export default EditProperty;


