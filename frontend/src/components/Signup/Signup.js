import React, { Component } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
// import twitterlogo from "../../images/twitterlogo.png";
import default_avatar from "../../Static/Images/default_avatar.png";
import * as Yup from "yup";
import { Link } from "react-router-dom";
import axios from 'axios';
import swal from 'sweetalert';
import rootUrl from "../Config/settings";
import { Redirect } from 'react-router';
import GoogleLogin from "react-google-login";
import FacebookLogin from "react-facebook-login";
import { Input, Button, message } from "antd";

const phoneRegExp = /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/

const SignUpSchema = Yup.object().shape({
    userName: Yup.string()
        .required("User name is required"),
    email: Yup.string()
        .email("Invalid email address format")
        .required("Email is required"),
    password: Yup.string()
        .min(6, "Password must be 6 characters at minimum")
        .required("Password is required"),
    userPhone: Yup.string()
        .matches(phoneRegExp, 'Phone number is not valid')
        .required("Phone number is required")
});

class SignUp extends Component {
    constructor() {
        super()
        this.state = {
            profileImage: "",
            profileImagePreview: "",
            authFlag: false
        }
        this.submitSignup = this.submitSignup.bind(this)
    }


    submitSignup = (details) => {
        console.log("Inside submit login", details);
        const data = {
            userName: details.userName,
            userPassword: details.password,
            userEmail: details.email,
            userPhone: details.userPhone,
            profileImage: details.profileImage
        }
        //set the with credentials to true
        // axios.defaults.withCredentials = true;
        //make a post request with the user data
        axios.post(rootUrl + '/signUp', data)
            .then(response => {
                console.log("inside success")
                console.log("Status Code : ", response.status);
                if (response.status === 200) {
                    this.setState({
                        authFlag: true
                    })
                    // alert("Signup successfull! You can now login in to your account!")
                    swal("Successful", "You can now login to your account!", "success");
                }
                console.log(this.state.authFlag)
            })
            .catch(error => {
                console.log("In error");
                this.setState({
                    authFlag: false
                });
                swal("OOps...", "Something went wrong. Please try again!", "error");
                console.log(error);
            })
    }

    render() {
        let redirectVar = null;
        if (this.state.authFlag === true) {
            redirectVar = <Redirect to="/" />
        }

        const responseGoogle = response => {
            const returnObj = response.profileObj;
            console.log(returnObj);

            let values = {

                userName: returnObj.name,
                userPhone: "9234786547",
                userEmail: returnObj.email,
                userPassword: "oauth"
            };
            console.log("before sending google login ", values);

            axios.post(rootUrl + '/signUp', values)
                .then((response) => {
                    if (response.status === 200) {
                        this.setState({
                            authFlag: true
                        })
                        swal("Successful", "You can now login to your account!", "success");
                    }
                    // else {
                    //     this.setState({
                    //         isNewUserCreated: false
                    //     })
                    // }
                })
                .catch(error => {
                    console.log("In error");
                    this.setState({
                        authFlag: false
                    });
                    swal("OOps...", "Something went wrong. Please try again!", "error");
                    console.log(error);
                })
        };

        const responseFacebook = response => {
            const returnObj = response.profileObj;
            console.log(returnObj);

            let values = {

                userName: returnObj.name,
                userPhone: "9234786547",
                userEmail: returnObj.email,
                userPassword: "oauth"
            };
            console.log("before sending facebook login ", values);

            axios.post(rootUrl + '/signUp', values)
                .then((response) => {
                    if (response.status === 200) {
                        this.setState({
                            authFlag: true
                        })
                        swal("Successful", "You can now login to your account!", "success");
                    }
                    // else {
                    //     this.setState({
                    //         isNewUserCreated: false
                    //     })
                    // }
                })
                .catch(error => {
                    console.log("In error");
                    this.setState({
                        authFlag: false
                    });
                    swal("OOps...", "Something went wrong. Please try again!", "error");
                    console.log(error);
                })
        };
        // console.log("profile image preview", this.state.profileImagePreview)

        return (
            <div>
                {redirectVar}
                <div className="container-fluid" id="signup">
                    <div className="row align-items-center h-100 ">
                        <div className="col-md-4 mx-auto">
                            <div className="card shadow p-3 mb-5 rounded">
                                <div className="card-body">
                                    {/* <img id="twitterlogo" alt="" src={twitterlogo} /> &nbsp; */}
                                    <h4 className="text-black text-left font-weight-bold">Create your account!</h4>
                                    <br />
                                    <Formik
                                        initialValues={{
                                            userName: "",
                                            password: "",
                                            email: "",
                                            userPhone: ""
                                        }}
                                        validationSchema={SignUpSchema}
                                        onSubmit={(values, actions) => {
                                            this.submitSignup(values)
                                            actions.setSubmitting(false);
                                        }}
                                    >
                                        {({ touched, errors, isSubmitting }) => (
                                            <Form>
                                                <div className="form-group text-left">
                                                    <label htmlFor="userName">User Name</label>
                                                    <Field
                                                        type="text"
                                                        name="userName"
                                                        className={`form-control ${
                                                            touched.userName && errors.userName ? "is-invalid" : ""
                                                            }`}
                                                    />
                                                    <ErrorMessage
                                                        component="div"
                                                        name="userName"
                                                        align="text-left"
                                                        className="invalid-feedback"
                                                    />
                                                </div>

                                                <div className="form-group text-left">
                                                    <label htmlFor="email">Email</label>
                                                    <Field
                                                        type="email"
                                                        name="email"
                                                        className={`form-control ${
                                                            touched.email && errors.email ? "is-invalid" : ""
                                                            }`}
                                                    />
                                                    <ErrorMessage
                                                        component="div"
                                                        name="email"
                                                        align="text-left"
                                                        className="invalid-feedback"
                                                    />
                                                </div>

                                                <div className="form-group text-left">
                                                    <label htmlFor="password">Password (6 character minimum)</label>
                                                    <Field
                                                        type="password"
                                                        name="password"
                                                        className={`form-control ${
                                                            touched.password && errors.password ? "is-invalid" : ""
                                                            }`}
                                                    />
                                                    <ErrorMessage
                                                        component="div"
                                                        name="password"
                                                        align="text-left"
                                                        className="invalid-feedback"
                                                    />
                                                </div>

                                                <div className="form-group text-left">
                                                    <label htmlFor="userPhone">Phone number</label>
                                                    <Field
                                                        type="text"
                                                        name="userPhone"
                                                        //   autofocus="true"
                                                        className={`form-control ${
                                                            touched.userPhone && errors.userPhone ? "is-invalid" : ""
                                                            }`}
                                                    />
                                                    <ErrorMessage
                                                        component="div"
                                                        name="userPhone"
                                                        align="text-left"
                                                        className="invalid-feedback"
                                                    />
                                                </div>

                                                <div className="form-group text-left">
                                                    <label htmlFor="profileImage">Provide profile image URL (optional)</label>
                                                    <Field
                                                        type="text"
                                                        name="profileImage"
                                                        className={`form-control ${
                                                            touched.profileImage && errors.profileImage ? "is-invalid" : ""
                                                            }`}
                                                    />
                                                    <ErrorMessage
                                                        component="div"
                                                        name="profileImage"
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
                                                    {isSubmitting ? "Please wait..." : "Sign Up"}
                                                </button>
                                            </Form>
                                        )}
                                    </Formik>

                                    <br />
                                    <div style={{ textAlign: "center" }}>SignUp
                                        <GoogleLogin
                                            clientId="171832382206-i0cjiqmpiugde4nl2bd75i74k7bsh2ia.apps.googleusercontent.com"
                                            onSuccess={responseGoogle}
                                            onFailure={responseGoogle}
                                            cookiePolicy={"single_host_origin"}
                                        />
                                    </div>
                                    <br></br>
                                    <div style={{ textAlign: "center" }}>SignUp
                                        <FacebookLogin
                                            appId="2562769363999319"
                                            autoLoad={false}
                                            fields="name,email,picture"
                                            callback={responseFacebook}
                                        />
                                    </div>
                                    <br></br>
                                    Already have an account?&nbsp;<Link to="/login">Sign in!</Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}


export default SignUp;


