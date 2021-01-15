import React, { Component } from 'react'
import Header from '../Header/Header'
import axios from 'axios';
import rootURL from '../Config/settings';
import swal from 'sweetalert'


export class Summary extends Component {
    constructor() {
        super()
        this.state = {
            tripSummary: null,
            reportValues: null,
            propertyReport: null
        }
    }

    componentDidMount = () => {
        const data = {
            customerId: localStorage.getItem("userId")
        }

        axios.post(rootURL + '/getCustomerReport', data)
            .then(response => {
                if (response.status === 200) {
                    console.log('summary', response.data);
                    this.setState({
                        propertyReport: response.data,
                        reportValues: Object.values(response.data)
                    });
                }
            }).catch((err) => {
                if (err) {
                    if (err.response.status === 404) {
                        this.setState({
                            isCardAvailable: false
                        })
                        console.log("Error message", 'No trips in your last twelve months');
                        swal("No trips in your last twelwe months")
                    }
                }
            });
    }

    render() {
        let customerSummary = null
        let total = 0
        if (this.state.reportValues) {
            customerSummary = Object.keys(this.state.propertyReport).map((report, index) => {
                return (
                    <div>
                        {report}: {this.state.reportValues[index]}
                    </div>
                )
            })

            this.state.reportValues.map((report, index) => {
                total = total + report
            })
        }
        console.log(total);

        return (
            <div>
                <Header />
                <div className='container'>
                    <div className="center-content font-weight-bold">
                        Report for {localStorage.getItem("userName")}
                    </div>
                    <hr />
                    <div className="center-content">
                        {customerSummary}
                    </div>
                    <hr />
                    <div className="center-content">
                        Money you spent in the last 1 year  = ${total}
                    </div>


                </div>
            </div>
        )
    }
}

export default Summary