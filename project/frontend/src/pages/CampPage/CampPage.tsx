import React, {Component, useEffect, useState} from "react";
import { NavLink } from "react-router-dom";
import { Row, Col, Button, Modal } from 'reactstrap';
import Loading from "../../components/Loading";
import * as API from "../../service";
import * as Model from "../../type";
import 'react-multi-carousel/lib/styles.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faPaw } from '@fortawesome/free-solid-svg-icons'
import CampInfo from "../../components/CampInfo"
import { withRouter } from "react-router";
import {RouteComponentProps} from "react-router-dom";
import { DateRangePicker, SingleDatePicker, DayPickerRangeController } from 'react-dates';
import "./CampPage.scss"
import moment from "moment";
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';   
import { START_DATE, END_DATE } from 'react-dates/constants';

interface Props extends RouteComponentProps {
    props: Props
}
interface State {
    camp: Model.HomeCamp,
    isLoading: Boolean,
    startDate?: moment.Moment,
    endDate?: moment.Moment,
    focusedInput?: String
}

export class CampPage extends Component<Props, State> {
    constructor(props: any) {
        super(props)
        this.state = {
            camp: {} as Model.HomeCamp,
            isLoading: false,
            startDate: moment(new Date()),
            endDate: moment(new Date()),
            focusedInput: 'startDate'
        }
    }

    componentDidMount() {
        this.fetchCamp()
    }

    fetchCamp = () => {
        this.setState({isLoading: true})
        API.getDetailCamp("123123")
        .then(response => {
            const campData = response.data;
            console.log(campData)
            if (campData.status) {
                this.setState({
                    camp: campData.data,
                    isLoading: false
                })
            }
        })
        .catch(error => {
            this.setState({
                isLoading: false
            })
        })
    }

    renderDateText = (date: moment.Moment) => {
        return <>
            <div>{date.date()}</div>
            <strong>A</strong>
        </>
    }


    render() {
        const {camp, isLoading} = this.state;
        console.log(camp)
        return (
            <>
            {camp._id && <div className = "container">
                <Row>
                    <Col md={12}>
                        <Button 
                            outline 
                            onClick = {() => {
                                this.props.history.push("/home");
                            }}
                            className="badge badge-light border-0 p-3 rounded-pill list-inline-item text_muted">
                               <FontAwesomeIcon icon={faArrowLeft} className = "mr-4"/>
                                Back to Campground
                        </Button>
                    </Col>
                </Row>
                <Row className = "mt-4">
                    <Col md={6}>
                        <div>
                            <span className="badge badge-light text-muted">CAMPING</span>
                        </div>
                        <h1>{camp.campName}</h1>
                        <div>
                            <span>{camp.partAddress} |</span>
                            <span> {camp.nearAddress} |</span>
                            <span> {camp.nearAddress} </span>
                        </div>

                        <div>
                           <ul className = "camp-props-list" data-columns="2">
                               <li>
                                   <CampInfo icon = {faPaw} text={`Pets ${camp.siteDetails.petAllowed == "YES" ? "Allowed": "Noallowed"}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Campfire ${camp.siteDetails.campFireAllowed == "YES" ? "Allowed": "Noallowed"}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`RV(${camp.allowableEquipmentList.items.RV})`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Tent(${camp.allowableEquipmentList.items.Trailer})`}/>
                               </li>
                           </ul>

                        </div>
                    </Col>
                    <Col md={5}>
                    <img
                        className="w-100"
                        src="https://cdn.recreation.gov/public/2019/02/21/19/34/65350_e7c97c84-39f2-425b-9714-4566d2fbaf8f_700.jpg"
                        alt=""
                    />
                    </Col>
                </Row>
                <Row className = "mt-5">
                    <Col md={3}>
                        <ul className="category-list">
                            <li className= "active">
                               <div>
                                    <a href = "#availability">Site Availability</a>
                               </div>
                            </li>
                            <li>
                                <div>
                                    <a href = "#site-detail">Site Details</a>
                                </div>
                                
                            </li>
                            <li>
                                <div>
                                    <a href = "#allowable-equipment">Allowable Equipment</a>
                                </div>
                                
                            </li>
                            <li>
                                <div>
                                    <a href = "#vehicle-driveway">Allowable Vehicle / Driveway Details</a>
                                </div>

                            </li>
                        </ul>
                    </Col>
                    <Col md={8} className = "contain-category">
                        <section id="availability">
                            <h3>Site Availability</h3>
                            <span>1 night weekday minimum</span>

                            <div className = "my-4">
                                <DayPickerRangeController
                                    className = "my-4"
                                    startDate={this.state.startDate} // momentPropTypes.momentObj or null,
                                    endDate={this.state.endDate} // momentPropTypes.momentObj or null,
                                    onDatesChange={({ startDate, endDate }) => this.setState({ startDate, endDate })} // PropTypes.func.isRequired,
                                    focusedInput={this.state.focusedInput || START_DATE}
                                    onFocusChange={(focusedInput: String) => this.setState({ focusedInput })}
                                    initialVisibleMonth={() => moment().add(2, "M")} // PropTypes.func or null,
                                    numberOfMonths = {2}
                                    keepOpenOnDateSelect = {false}
                                    onPrevMonthClick = {() => {
                                        this.setState({
                                            focusedInput: START_DATE
                                        })
                                    }}
                                    onNextMonthClick = {() => {
                                        this.setState({
                                            focusedInput: END_DATE
                                        })
                                    }}
                                    autoFocusEndDate = {true}
                                    renderDayContents = {this.renderDateText}
                                    onOutsideClick = {() => {
                                    this.setState({
                                        focusedInput: END_DATE
                                    })
                                    }}
                                />
                            </div>
                            
                        </section>

                        <section id="site-detail">
                            <h3>Site Details</h3>
                            <ul className = "camp-props-list" data-columns="2">
                               <li>
                                   <CampInfo icon = {faPaw} text={`Site Type: ${camp.siteDetails.siteType}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Site Accessible: ${camp.siteDetails.siteAccessible}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Check-In: ${camp.siteDetails.checkInTime}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Check-Out: ${camp.siteDetails.checkOutTime}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Max Num of People: ${camp.siteDetails.maxNumOfPeople}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Min Num of People: ${camp.siteDetails.minNumOfPeople}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Type of Use: ${camp.siteDetails.typeOfUse}`}/>
                               </li>

                               <li>
                                   <CampInfo icon = {faPaw} text={`Campfire Allowed: ${camp.siteDetails.campFireAllowed}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Capacity Rating: ${camp.siteDetails.capacityRating}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Pets Allowed: ${camp.siteDetails.petAllowed}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Site Reserve Type: ${camp.siteDetails.siteType}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Shade: ${camp.siteDetails.siteType}`}/>
                               </li>
                           </ul>
                        </section>

                        <section id = "allowable-equipment">
                            <h3>Allowable Equipment</h3>
                            <ul className = "camp-props-list" data-columns="2">
                               <li>
                                   <CampInfo icon = {faPaw} text={`Tent ${camp.allowableEquipmentList.items.Tent}`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`RV (${camp.allowableEquipmentList.items.RV})`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={`Trailer (${camp.allowableEquipmentList.items.Trailer})`}/>
                               </li>
                               <li>
                                   <CampInfo icon = {faPaw} text={''}/>
                               </li>
                           </ul>
                        </section>

                        <section id = "vehicle-driveway">
                            <h3>Allowable Vehicle / Driveway Details</h3>

                            <ul>
                                <li>
                                    <span><strong>Driveway Entry: </strong>{camp.allowableVehicleAndDrivewayDetails.drivewayEntry}</span>
                                </li>
                                <li>
                                    <span><strong>Driveway Length: </strong>{camp.allowableVehicleAndDrivewayDetails.drivewayLength}</span>
                                </li>
                                <li>
                                    <span><strong>Driveway Surface: </strong>{camp.allowableVehicleAndDrivewayDetails.drivewaySurface}</span>
                                </li>
                                <li>
                                    <span><strong>Is Equipment Mandatory: </strong>{camp.allowableVehicleAndDrivewayDetails.isEquipmentMandatory}</span>
                                </li>
                                <li>
                                    <span><strong>Max Num of Vehicles: </strong>{camp.allowableVehicleAndDrivewayDetails.maxNumOfVehicles}</span>
                                </li>
                                <li>
                                    <span><strong>Max Vehicle Length: </strong>{camp.allowableVehicleAndDrivewayDetails.maxVehicleLength}</span>
                                </li>
                                <li>
                                    <span><strong>Site Length: </strong>{camp.allowableVehicleAndDrivewayDetails.siteLength}</span>
                                </li>
                            </ul>
                        </section>
                    </Col>
                    

                </Row>
                <Row>
                    <Col md = {12}>
                        <div className="fix-price">
                            <div className="content-price">
                                <div className="d-flex justify-content-between">
                                    <div>
                                        <h3>PINECREST</h3>
                                        <p>Site A001, Loop A</p>
                                    </div>

                                    <div className = "d-flex">
                                        <div className = "mr-5">
                                            <h2>
                                                ${camp.price}
                                            </h2>
                                            <p className = "text-muted">+ Fees & Taxes</p>
                                        </div>
                                        <div>
                                            <Button outline color="primary" style = {{
                                                padding: '10px',
                                                fontWeight: 500
                                                
                                            }}>
                                                Enter Dates
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Col>
                </Row>
                
            </div>}

            </>
        )
    }
}

export default withRouter(CampPage);
