import React, { Component } from 'react';
import cookie from 'react-cookies';
import axios from 'axios';


class Header extends Component {

    constructor() {
        super();

        //bind
        this.handleLogout = this.handleLogout.bind(this);
    }

    handleLogout = () => {
        localStorage.clear()
    }

    render() {

        let loggedInUserContent = null;
        let ownerContent = null;
        let travelerContent = null;
        let ownerListPropertyTab = null;

        if (localStorage.getItem('accountType') === '2') {
            ownerContent = <div>
                <a className="dropdown-item blue-text" href="/ownerreport">Owner Report</a></div >
        }

        if (localStorage.getItem('accountType') === '1') {
            travelerContent = <div>
                <a className="dropdown-item blue-text" href="/my-trips">My Trips</a>
                <a className="dropdown-item blue-text" href="/userreport">My Report</a>
            </div>
        }

        let username = localStorage.getItem('userName')
        if (localStorage.getItem("accountType")) {
            loggedInUserContent = <span className="header-bar-tabs">
                <span>

                    <a className="btn dropdown-toggle userName-dropdown" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {username}
                    </a>

                    <div className="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        {/* <a className="dropdown-item blue-text" href="/owner-profile">Profile</a> */}
                        {travelerContent}
                        {ownerContent}
                        <a className="dropdown-item blue-text" href="/login" onClick={this.handleLogout}>Logout</a>
                    </div>
                </span>
                {ownerListPropertyTab}
            </span>
        }

        return (
            <div className="row" >
                <div className="col-6 float-left">
                    <h2><a href="/" className='font-weight-bold' id="airbnb"> OpenHome </a></h2>
                </div>

                <div className="col-6 float-right">
                    {loggedInUserContent}
                </div>
            </div>
        )
    }
}

export default Header;