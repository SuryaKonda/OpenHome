import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import axios from 'axios';
import cookie from 'react-cookies';
import { Redirect } from 'react-router';
import Header from '../Header/Header';
import rootURL from '../Config/settings';


class AddProperty extends Component {

    constructor() {
        super();

        this.state = {
            locationActive: true,
            detailsActive: false,
            // photosActive: false,
            // pricingActive: false,
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

            // photoThumbnail: [],
            // availabilityStartDate: moment(),
            // availabilityEndDate: moment(),

            locationError: false,
            detailsError: false,
            // photosError: false,
            // pricingError: false,
            propertyInsertComplete: false,
            errorRedirect: false
        }



        //bind
        // this.handleAvailabilityStartDateChange = this.handleAvailabilityStartDateChange.bind(this);
        // this.handleAvailabilityEndDateChange = this.handleAvailabilityEndDateChange.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.submitPropertyDetails = this.submitPropertyDetails.bind(this);
    }

    handleLocationClick = () => {

        this.setState({
            locationActive: true,
            detailsActive: false
            // photosActive: false,
            // pricingActive: false
        });
    }

    handleDetailsClick = () => {
        console.log("this.state", this.state)

        const validator = this.state.propertyName === "" || this.state.propertyPhone === "" || this.state.streetAddress === "" || this.state.city === "" || this.state.state === "" || this.state.zipCode === "";

        console.log(validator);
        if (validator) {
            this.setState({
                locationError: true
            })
        }
        else {
            this.setState({
                locationActive: false,
                detailsActive: true,
                // photosActive: false,
                // pricingActive: false,
                // locationError: false

            });
        }
    }

    // handlePhotosClick = () => {

    //     const validator = this.state.headline === "" || this.state.description === "" || this.state.propertyType === "" || this.state.bedrooms === "" || this.state.accomodates === "" || this.state.bathrooms === "";

    //     if (validator) {
    //         this.setState({
    //             detailsError: true
    //         })
    //     }
    //     else {
    //         this.setState({
    //             locationActive: false,
    //             detailsActive: false,
    //             photosActive: true,
    //             pricingActive: false,
    //             detailsError: false
    //         });
    //     }


    // }

    // handlePricingClick = () => {
    //     const validator = this.state.photos.length == 0;

    //     if (validator) {
    //         this.setState({
    //             photosError: true
    //         })

    //     }
    //     else {

    //         this.setState({
    //             locationActive: false,
    //             detailsActive: false,
    //             photosActive: false,
    //             pricingActive: true,
    //             photosError: false

    //         });
    //     }

    // }

    // handleAvailabilityStartDateChange(date) {
    //     this.setState({
    //         availabilityStartDate: date
    //     })
    // }

    // handleAvailabilityEndDateChange(date) {
    //     this.setState({
    //         availabilityEndDate: date
    //     })
    // }

    handleInputChange(event) {
        const target = event.target;
        const name = target.name;
        const value = target.value;

        if (name === "photos") {
            console.log('Files : ', target.files);

            var photos = target.files;
            console.log('photos:', photos);
            let data = new FormData();
            for (var i = 0; i < photos.length; i++) {
                data.append('photos', photos[i]);
            }

            axios.defaults.withCredentials = true;
            axios.post('http://localhost:3001/upload-file', data)
                .then(response => {
                    var imagePreviewArr = [];
                    var photoArr = "";

                    if (response.status === 200) {
                        for (var i = 0; i < photos.length; i++) {
                            photoArr = photoArr.length == 0 ? photos[i].name : photoArr + ',' + photos[i].name;
                            axios.defaults.withCredentials = true;
                            axios.post('http://localhost:3001/download-file/' + photos[i].name)
                                .then(response => {
                                    //console.log("Imgae Res : ", response);
                                    let imagePreview = 'data:image/jpg;base64, ' + response.data;
                                    imagePreviewArr.push(imagePreview);


                                    this.setState({
                                        photoThumbnail: imagePreviewArr,
                                        photos: photoArr
                                    });

                                })
                                .catch((err) => {
                                    if (err) {
                                        this.setState({
                                            errorRedirect: true
                                        })
                                    }
                                });
                        }


                        console.log('Photos: ', this.state.photos);
                    }

                }).catch((err) => {
                    if (err) {
                        this.setState({
                            errorRedirect: true
                        })
                    }
                });
        }
        else {
            this.setState({
                [name]: value
            });
        }

    }

    submitPropertyDetails = (e) => {
        console.log("this.state", this.state)


        var validator = this.state.description === "" || this.state.propertyType === "" || this.state.sharingType === "";


        if (validator) {
            this.setState({
                pricingError: true
            })
            console.log("validator", validator)
        }
        else {

            e.preventDefault();

            // const locationDetails = {
            //     country: this.state.country,
            //     streetAddress: this.state.streetAddress,
            //     unitNumber: this.state.unitNumber,
            //     city: this.state.city,
            //     state: this.state.state,
            //     zipCode: this.state.zipCode
            // };

            // const details = {
            //     headline: this.state.headline,
            //     description: this.state.description,
            //     propertyType: this.state.propertyType,
            //     bedrooms: this.state.bedrooms,
            //     accomodates: this.state.accomodates,
            //     bathrooms: this.state.bathrooms
            // };
            // const photos = {
            //     photos: this.state.photos
            // };
            // const pricingDetails = {
            //     availabilityStartDate: this.state.availabilityStartDate,
            //     availabilityEndDate: this.state.availabilityEndDate,
            //     currency: this.state.currency,
            //     baserate: this.state.baserate,
            //     minStay: this.state.minStay
            // };

            const data = {
                ownerId: localStorage.getItem("userId"),
                ownerName: localStorage.getItem("userName"),
                propertyName: this.state.propertyName,
                propertyPhone: this.state.propertyPhone,
                address: this.state.streetAddress,
                state: this.state.state,
                city: this.state.city,
                zip: this.state.zipCode,
                description: this.state.description,
                isParkingAvailability: this.state.parking,
                freeParking: this.state.freeParking,
                parkingFee: this.state.parkingFee,
                isWifi: this.state.isWifi,
                totalSpace: this.state.totalSpace,
                sharingType: this.state.sharingType,
                propertyType: this.state.propertyType
            }

            console.log("data in add new property", data)

            // axios.defaults.withCredentials = true;
            axios.post(rootURL + '/addNewProperty', data)
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
    }


    render() {

        let redrirectVar = null;
        if (!localStorage.getItem("accountType") === "2") {
            redrirectVar = <Redirect to="/" />
        }
        if (this.state.errorRedirect === true) {
            redrirectVar = <Redirect to="/error" />
        }

        if (this.state.propertyInsertComplete) {
            redrirectVar = <Redirect to="/" />
        }

        let locationErrorPane = null;

        if (this.state.locationError) {
            locationErrorPane = <div>
                <div className="alert alert-danger" role="alert">
                    <strong>Error!</strong> All fields are required!
                </div>
            </div>
        }

        let detailsErrorPane = null;

        if (this.state.detailsError) {
            detailsErrorPane = <div>
                <div className="alert alert-danger" role="alert">
                    <strong>Error!</strong> All fields are required!
                </div>
            </div>
        }

        let photosErrorPane = null;

        if (this.state.photosError) {
            photosErrorPane = <div>
                <div className="alert alert-danger" role="alert">
                    <strong>Error!</strong> All fields are required!
                </div>
            </div>
        }

        let pricingErrorPane = null;

        if (this.state.pricingError) {
            pricingErrorPane = <div>
                <div className="alert alert-danger" role="alert">
                    <strong>Error!</strong> All fields are required!
                </div>
            </div>
        }

        // let photoThumbnails = this.state.photoThumbnail.map(function (thumbnail, index) {
        //     return (
        //         <img src={thumbnail} className="img-thumbnail" alt="thumbnail" width="304" height="236" key={index}></img>
        //     )
        // })
        // console.log('PhotoThumbnail inside return: ', this.state.photoThumbnail);
        return (
            <div>
                <Header />
                <div className="add-property-content">
                    {redrirectVar}
                    <div className="container">
                        <hr />
                        <div className="row">
                            <div className="menu-bar-ver col-3">
                                <ul>
                                    <li>Welcome</li>
                                    <li> <a href="#" onClick={this.handleLocationClick}>Property</a></li>
                                    <li><a href="#" onClick={this.handleDetailsClick}>Details</a></li>
                                    {/* <li><a href="#" onClick={this.handlePhotosClick}>Photos</a></li> */}
                                    {/* <li><a href="#" onClick={this.handlePricingClick}>Pricing</a></li> */}
                                </ul>
                            </div>
                            <div className="menu-bar-hor border col-8">
                                <div className="add-property-form">
                                    <div className={this.state.locationActive ? "location-form show-form" : "location-form"}>
                                        <div className="location-form-headlinetext">
                                            <h4>Property Details</h4>
                                        </div>
                                        <hr />
                                        <div>
                                            {locationErrorPane}
                                        </div>
                                        <div className="details-form-description pad-bot-10">
                                            <p>Fill in the following details of your property</p>
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="propertyName" id="propertyName" className="form-control form-control-lg" placeholder="Property Name" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="propertyPhone" id="propertyPhone" className="form-control form-control-lg" placeholder="Property Phone" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="streetAddress" id="streetAddress" className="form-control form-control-lg" placeholder="Street Address" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="city" id="city" className="form-control form-control-lg" placeholder="City" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="state" id="state" className="form-control form-control-lg" placeholder="State" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <input type="text" name="zipCode" id="zipCode" className="form-control form-control-lg" placeholder="Zip Code" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group location-form-btn flt-right">
                                            <button className="btn btn-primary btn-lg" onClick={this.handleDetailsClick}>Next</button>
                                        </div>
                                    </div>

                                    <div className={this.state.detailsActive ? "details-form show-form" : "details-form"}>
                                        <div className="details-form-headlinetext">
                                            <h4>Describe your property</h4>
                                        </div>
                                        <hr />
                                        <div>
                                            {detailsErrorPane}
                                        </div>
                                        <div className="details-form-description pad-bot-10">
                                            <p>Share the details of your property</p>
                                        </div>

                                        <div className="form-group">
                                            <textarea type="text" name="description" id="description" className="form-control form-control-lg" placeholder="Description" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="propertyType">Property Type </label>
                                            <select name="propertyType" id="propertyType" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="select">Select an option</option>
                                                <option value="house">House</option>
                                                <option value="townHouse">Town House</option>
                                                <option value="apartment">Condo</option>
                                            </select>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="sharingType">Sharing Type </label>
                                            <select name="sharingType" id="sharingType" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="select">Select an option</option>
                                                <option value="entirePlace">Entire Place</option>
                                                <option value="privateRoom">Private Room</option>
                                            </select>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="totalSpace">Total Space</label>
                                            <input type="number" step="0.01" name="totalSpace" id="totalSpace" className="form-control form-control-lg" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="parking">Is parking available on your property? </label>
                                            <select name="parking" id="parking" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="select">Select an option</option>
                                                <option value={1}>Yes</option>
                                                <option value={0}>No</option>
                                            </select>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="freeParking">If parking available, is it free? </label>
                                            <select name="freeParking" id="freeParking" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="select">Select an option</option>
                                                <option value={1}>Yes</option>
                                                <option value={0}>No</option>
                                            </select>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="parkingFee">How much is the charge for the parking? (if applicable)</label>
                                            <input type="number" step="0.01" name="parkingFee" id="parkingFee" className="form-control form-control-lg" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="isWifi">Is WiFi available> </label>
                                            <select name="isWifi" id="isWifi" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="select">Select an option</option>
                                                <option value={1}>Yes</option>
                                                <option value={0}>No</option>
                                            </select>
                                        </div>

                                        <div className="form-group flt-right">
                                            <button className="btn btn-primary btn-lg" onClick={this.submitPropertyDetails}>Submit</button>
                                        </div>

                                        {/* <div className="form-group details-form-btn flt-right">
                                            <button className="btn btn-primary btn-lg" onClick={this.handlePhotosClick}>Next</button>
                                        </div> */}

                                    </div>

                                    {/* <div className={this.state.photosActive ? "photos-form show-form" : "photos-form"}>
                                        <div>
                                            {photosErrorPane}
                                        </div>
                                        <div className="photos-form-headlinetext">
                                            <h4>Add up to 5 photos of your property</h4>
                                        </div>
                                        <hr />
                                        <div className="photos-form-description pad-bot-10">
                                            <p>Showcase your property’s best features (no pets or people, please). Requirements: JPEG, at least 1920 x 1080 pixels, less than 20MB file size, 2 photos minimum.</p>
                                        </div>
                                        <div className="container photo-upload-btn-container">
                                            <div className="center-content">
                                                <button className="btn btn-lg photo-upload-btn">
                                                    <input type="file" name="photos" className="btn btn-lg photo-upload-btn" onChange={this.handleInputChange} multiple className="btn btn-lg photo-upload-btn" />
                                                </button>

                                                <button className="btn btn-lg photo-upload-btn">SELECT PHOTOS TO UPLOAD</button>
                                            </div>
                                        </div>
                                        <div className="pad-top-10 pad-bot-10">
                                            {photoThumbnails}
                                        </div>
                                        <div className="form-group flt-right">
                                            <button className="btn photos-form-btn btn-primary btn-lg" onClick={this.handlePricingClick}>Next</button>
                                        </div>
                                    </div> */}

                                    {/* <div className={this.state.pricingActive ? "pricing-form show-form" : "pricing-form"}>
                                        <div className="pricing-form-headlinetext">
                                            <h4>How much do you want to charge?</h4>
                                        </div>
                                        <hr />
                                        {pricingErrorPane}
                                        <div className="pricing-form-description pad-bot-10">
                                            <p>We recommend starting with a low price to get a few bookings and earn some initial guest reviews. You can update your rates at any time.</p>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="availabilityStartDate">Start date</label>
                                            <DatePicker name="availabilityStartDate" id="availabilityStartDate" className="form-control form-control-lg" dateFormat="MM/DD/YY" selected={this.state.availabilityStartDate} onChange={this.handleAvailabilityStartDateChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="availabilityEndDate">End date</label>
                                            <DatePicker name="availabilityEndDate" id="availabilityEndDate" className="form-control form-control-lg" dateFormat="MM/DD/YY" selected={this.state.availabilityEndDate} onChange={this.handleAvailabilityEndDateChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="currency">Currency </label>
                                            <select name="currency" id="currency" className="form-control form-control-lg" onChange={this.handleInputChange}>
                                                <option value="AUD">Australian Dollar (AUD)</option>
                                                <option value="EUR">Euros (EUR)</option>
                                                <option value="GBP">Great British Pound (GBP)</option>
                                                <option value="$">US Dollar (USD)</option>
                                                <option value="CAD">Canadian Dollar (CAD)</option>
                                                <option value="NZD">New Zealand Dollar (NZD)</option>
                                                <option value="BRL">Brazil Real (BRL)</option>
                                            </select>
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="baserate">Nightly Base Rate</label>
                                            <input type="text" name="baserate" id="baserate" className="form-control form-control-lg" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group">
                                            <label htmlFor="minStay">Minimum Stay (nights)</label>
                                            <input type="text" name="minStay" id="minStay" className="form-control form-control-lg" onChange={this.handleInputChange} />
                                        </div>
                                        <div className="form-group flt-right">
                                            <button className="btn btn-primary btn-lg" onClick={this.submitPropertyDetails}>Submit</button>
                                        </div>
                                    </div> */}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="container center-content pad-top-20-pc">
                        <div>
                            Use of this Web site constitutes acceptance of the HomeAway.com Terms and conditions and Privacy policy.
                    </div>
                        <div>
                            ©2018 HomeAway. All rights reserved
                    </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AddProperty;