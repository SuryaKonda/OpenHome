import React, { Component } from 'react';
import axios from 'axios';
import cookie from 'react-cookies';
import { Redirect } from 'react-router';
import Header from '../Header/Header';
import rootURL from '../Config/settings';
import swal from 'sweetalert';
import GoogleLogin from "react-google-login";
import FacebookLogin from "react-facebook-login";
import { Form, Input, Button, message } from "antd";

class Login extends Component {


    constructor() {
        super();
        this.state = {
            Email: "",
            Password: "",
            formValidationFailure: false,
            isValidationFailure: true,
            errorRedirect: false,
            userData: null
        }

        //Bind events
        this.emailChangeHandler = this.emailChangeHandler.bind(this);
        this.passwordChangeHandler = this.passwordChangeHandler.bind(this);
        this.submitLogin = this.submitLogin.bind(this);
    }

    emailChangeHandler = (e) => {
        this.setState({
            Email: e.target.value
        })
    }

    passwordChangeHandler = (e) => {
        this.setState({
            Password: e.target.value
        })
    }

    submitLogin = (e) => {
        e.preventDefault();
        var data = {
            userEmail: this.state.Email,
            userPassword: this.state.Password
        }

        if (this.state.Email == "" || this.state.Password == "") {
            this.setState({
                formValidationFailure: true
            });
            console.log('Form Error!');
        }
        else {
            this.setState({
                isValidationFailure: true,
                formValidationFailure: false,
                redirect: true
            })
            // axios.defaults.withCredentials = true;
            axios.post(rootURL + '/login', data)
                .then((response) => {
                    console.log('response', response);

                    if (response.status === 200) {
                        console.log("response in login", response.data)
                        localStorage.setItem("accountType", response.data[0].accountType)
                        localStorage.setItem('userName', response.data[0].userName)
                        localStorage.setItem('userEmail', response.data[0].userEmail)
                        localStorage.setItem('userId', response.data[0].userId)
                        localStorage.setItem('userPhone', response.data[0].userPhone)
                        this.setState({
                            isValidationFailure: true,
                            formValidationFailure: false,
                            userData: response.data
                        })

                    }
                })
                .catch((err) => {
                    if (err) {
                        console.log(err.response);

                        if (err.response.status === 404) {
                            this.setState({
                                isValidationFailure: false
                            })
                            console.log("Error message", 'Invalid credentials');
                            swal("Invalid credentials")
                        }
                        else if (err.response.status === 405) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Please verify your email to login")
                            console.log("Error message", 'Please verify your email to login');
                        }
                        else if (err.response.status === 400) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Email id doesnt match or doesn't exist")
                            console.log("Error message", "Email id doesnt match or doesn't exist");
                        }
                        else {
                            this.setState({
                                errorRedirect: true
                            })
                        }
                    }
                });
        }
    }

    render() {
        let redrirectVar = null;
        if (localStorage.getItem('accountType') === '2') {
            redrirectVar = <Redirect to='/ownerhome' />
        }
        if (localStorage.getItem('accountType') === '1') {
            redrirectVar = <Redirect to='/home' />
        }
        if (this.state.errorRedirect) {
            redrirectVar = <Redirect to="/error" />
        }
        let errorPanel = null;
        // if (!this.state.isValidationFailure) {
        //     errorPanel = <div>
        //         <div className="alert alert-danger" role="alert">
        //             <strong>Validation Error!</strong> Username and Password doesn't match!
        //         </div>
        //     </div>
        // }
        let formErrorPanel = null;
        console.log('FormvalidationFailur', this.state.formValidationFailure);
        if (this.state.formValidationFailure) {
            formErrorPanel =
                <div>
                    <div className="alert alert-danger" role="alert">
                        <strong>Validation Error!</strong> Username and Password are required!
                </div>
                </div>
        }

        const responseGoogle = response => {
            const returnObj = response.profileObj;
            console.log(returnObj);
            let values = {

                userEmail: returnObj.email,
                userPassword: "oauth"

                // username: returnObj.email,
                // password: "oauth"
            };
            console.log("before sending google login ", values);

            axios.post(rootURL + '/login', values)
                .then((response) => {
                    console.log('response', response);

                    if (response.status === 200) {
                        console.log("response in login", response.data)
                        localStorage.setItem("accountType", response.data[0].accountType)
                        localStorage.setItem('userName', response.data[0].userName)
                        localStorage.setItem('userEmail', response.data[0].userEmail)
                        localStorage.setItem('userId', response.data[0].userId)
                        localStorage.setItem('userPhone', response.data[0].userPhone)
                        this.setState({
                            isValidationFailure: true,
                            formValidationFailure: false,
                            userData: response.data
                        })

                    }
                })
                .catch((err) => {
                    if (err) {
                        console.log(err.response);

                        if (err.response.status === 404) {
                            this.setState({
                                isValidationFailure: false
                            })
                            console.log("Error messagw", 'Invalid credentials');
                            swal("Invalid credentials")
                        }
                        else if (err.response.status === 405) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Please verify your email to login")
                            console.log("Error messagw", 'Please verify your email to login');
                        }
                        else if (err.response.status === 400) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Email id doesnt match or doesn't exist")
                            console.log("Error messagw", "Email id doesnt match or doesn't exist");
                        }
                        else {
                            this.setState({
                                errorRedirect: true
                            })
                        }
                    }
                });
        };

        const responseFacebook = response => {
            const returnObj = response.profileObj;
            console.log(returnObj);
            let values = {

                userEmail: returnObj.email,
                userPassword: "oauth"

                // username: returnObj.email,
                // password: "oauth"
            };

            console.log("before sending google login ", values);

            axios.post(rootURL + '/login', values)
                .then((response) => {
                    console.log('response', response);

                    if (response.status === 200) {
                        console.log("response in login", response.data)
                        localStorage.setItem("accountType", response.data[0].accountType)
                        localStorage.setItem('userName', response.data[0].userName)
                        localStorage.setItem('userEmail', response.data[0].userEmail)
                        localStorage.setItem('userId', response.data[0].userId)
                        localStorage.setItem('userPhone', response.data[0].userPhone)
                        this.setState({
                            isValidationFailure: true,
                            formValidationFailure: false,
                            userData: response.data
                        })

                    }
                })
                .catch((err) => {
                    if (err) {
                        console.log(err.response);

                        if (err.response.status === 404) {
                            this.setState({
                                isValidationFailure: false
                            })
                            console.log("Error messagw", 'Invalid credentials');
                            swal("Invalid credentials")
                        }
                        else if (err.response.status === 405) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Please verify your email to login")
                            console.log("Error messagw", 'Please verify your email to login');
                        }
                        else if (err.response.status === 400) {
                            this.setState({
                                isValidationFailure: false
                            })
                            swal("Email id doesnt match or doesn't exist")
                            console.log("Error messagw", "Email id doesnt match or doesn't exist");
                        }
                        else {
                            this.setState({
                                errorRedirect: true
                            })
                        }
                    }
                });
        };

        return (
            <div>
                <Header />
                <div className="container fill-graywhite">
                    {redrirectVar}
                    <div className="container content">
                        <div className="login-container">
                            <div>
                                <p>Log in to OpenHome</p>
                                <p>Need an account? <a href="/sign-up">Sign Up</a></p>
                            </div>
                            <div className="login-form-container col-lg-4 col-md-4 col-sm-12 offset-lg-4 offset-md-4 border">
                                <div className="login-form-heading input-group pad-top-10 input-group-lg">
                                    Account login
                            </div>
                                <hr />
                                {errorPanel}
                                {formErrorPanel}
                                <div className="form-group login-form-control">
                                    <input type="text" name="email" id="email" className="form-control form-control-lg" placeholder="Email Address" onChange={this.emailChangeHandler} required />
                                </div>
                                <div className="form-group login-form-control">
                                    <input type="password" name="password" id="password" className="form-control form-control-lg" placeholder="Password" onChange={this.passwordChangeHandler} required />
                                </div>
                                <div className="form-group login-form-control">
                                    <a href="" className="">Forgot Password?</a>
                                </div>
                                <div className="form-group login-form-control">
                                    <button className="btn btn-login col-lg-12 col-md-12 col-sm-12" onClick={this.submitLogin} >Login </button>
                                </div>
                                <hr />
                                <div className="form-group login-form-control">
                                    <br></br>
                                    <div style={{ textAlign: "center" }}>
                                        <GoogleLogin
                                            clientId="171832382206-i0cjiqmpiugde4nl2bd75i74k7bsh2ia.apps.googleusercontent.com"
                                            onSuccess={responseGoogle}
                                            onFailure={responseGoogle}
                                            cookiePolicy={"single_host_origin"}
                                        />
                                    </div>
                                    <br></br>
                                    <div style={{ textAlign: "center" }}>
                                        <FacebookLogin
                                            appId="2505133353101658"
                                            autoLoad={false}
                                            fields="name,email,picture"
                                            callback={responseFacebook}
                                        />
                                    </div>
                                    <br></br>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Login;
