import React, { Component, useEffect, useState } from "react";
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
import { RouteComponentProps } from "react-router-dom";
import { DateRangePicker, SingleDatePicker, DayPickerRangeController } from 'react-dates';
import "./CampPage.scss"
import moment from "moment";
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';
import { START_DATE, END_DATE } from 'react-dates/constants';
import Swal from 'sweetalert2'
import { ButtonProps } from "@material-ui/core";
import {connect} from "react-redux";
import { Slide } from 'react-slideshow-image';
import 'react-slideshow-image/dist/styles.css';
import {getLink} from "../../utils/Utils"

interface MatchParams {
    campId: string
}
interface Props extends RouteComponentProps<MatchParams> {
    props: Props,
    authUser: any
}
interface State {
    camp: Model.HomeCamp,
    isLoading: Boolean,
    startDate?: moment.Moment,
    endDate?: moment.Moment,
    focusedInput?: String,
    totalDate: number
}

export class CampPage extends Component<Props, State> {
    constructor(props: any) {
        super(props)
        this.state = {
            camp: {} as Model.HomeCamp,
            isLoading: false,
            startDate: moment(new Date()),
            endDate: moment(new Date()),
            focusedInput: 'startDate',
            totalDate: 0
        }
    }

    componentDidMount() {
        this.fetchCamp()
    }

    fetchCamp = () => {
        const campId = this.props.match.params.campId;
        this.setState({ isLoading: true })
        API.getDetailCamp(campId)
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
            <strong>R</strong>
        </>
    }

    onBooking = () => {

        let totalDate = (this.state.startDate !== null && this.state.endDate !== null) ? this.state.endDate?.diff(this.state.startDate, "days") as number : 0 as number
        // let camObj = this.state.camp
        let camObj = {
            _id: "null",
            campBookedId: this.state.camp.campLocationAddress,
            // allImgSrc: this.state.camp.allImgSrc,
            // mainImgSrc: this.state.camp.main,
            usernameBooked: this.state.camp.campName,
            totalPrice: totalDate * Number(this.state.camp.price),
            timeStart: this.state.startDate ? this.state.startDate.format("YYYY-MM-DD") : "2021-07-27", 
            timeEnd:  this.state.endDate ? this.state.endDate.format("YYYY-MM-DD") : "2021-07-28", 
            // rvMax: this.state.camp.ma,
            // sd_maxNumOfPeople: this.state.camp.sd,
            // sd_typeOfUse: this.state.camp.ty,
            // tenMax: this.state.camp.ten,
            // vd_maxNumOfVehicles: Number,
            // vd_maxVehicleLengthForVehicle: Number
        }
        API.booking(this.props.authUser.username, camObj).then(response => {
            console.log(response);
            this.renderPopup()
        }).catch(error => {
            this.renderPopup()
        })
    }

    onChangeDate = (start, end) => {
        this.setState({
            startDate: start,
            endDate: start,
            // totalDate: endDate.diff(startDate, "days")
        })

        if (start !== null && end !== null) this.setState({ totalDate: start.diff(start, "days") })
    }
    renderPopup() {
        Swal.fire({
            title: 'Are you sure?',
            text: 'Do you want to book this place??',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire(
                    'Success!',
                    'Your booking is in processing',
                    'success'
                )
                // For more information about handling dismissals please visit
                // https://sweetalert2.github.io/#handling-dismissals
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                Swal.fire(
                    'Cancelled',
                    'Cancel',
                    'error'
                )
            }
        })
    }

    renderPrice() {
        let totalDate = (this.state.startDate !== null && this.state.endDate !== null) ? this.state.endDate?.diff(this.state.startDate, "days") : 0
        console.log(totalDate);

        return (
            <div className="mr-5">
                <h2>
                    ${totalDate}
                </h2>
                <p className="text-muted">+ Fees & Taxes</p>
            </div>
        )
    }

    render() {
        let totalDate = (this.state.startDate !== null && this.state.endDate !== null) ? this.state.endDate?.diff(this.state.startDate, "days") : 0
        const { camp, isLoading } = this.state;
        console.log(camp)
        return (
            <>
                {camp._id && <div className="container">
                    <Row>
                        <Col md={12}>
                            <Button
                                outline
                                onClick={() => {
                                    this.props.history.push("/home");
                                }}
                                className="badge badge-light border-0 p-3 rounded-pill list-inline-item text_muted">
                                <FontAwesomeIcon icon={faArrowLeft} className="mr-4" />
                                Back to Campground
                            </Button>
                        </Col>
                    </Row>
                    <Row className="mt-4">
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
                                <ul className="camp-props-list" data-columns="2">
                                    <li>
                                        <CampInfo icon={faPaw} text={`Pets ${camp.siteDetails.petAllowed == "YES" ? "Allowed" : "Noallowed"}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Campfire ${camp.siteDetails.campFireAllowed == "YES" ? "Allowed" : "Noallowed"}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`RV(${camp.allowableEquipmentList.items.RV})`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Tent(${camp.allowableEquipmentList.items.Trailer})`} />
                                    </li>
                                </ul>

                            </div>
                        </Col>
                        <Col md={5}>
                            <Slide>
                                {camp.campImgSrc.map(source => {
                                    const   link = getLink(source)

                                    return (
                                        <div className="each-slide">
                                            <div style={{
                                                'backgroundImage': `url(${link})`,
                                                width: '100%',
                                                height: '400px'
                                            }} >
                                            </div>
                                        </div>
                                    )
                                })}
                            </Slide>
                        
                        </Col>
                    </Row>
                    <Row className="mt-5">
                        <Col md={3}>
                            <ul className="category-list">
                                <li className="active">
                                    <div>
                                        <a href="#availability">Site Availability</a>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <a href="#site-detail">Site Details</a>
                                    </div>

                                </li>
                                <li>
                                    <div>
                                        <a href="#allowable-equipment">Allowable Equipment</a>
                                    </div>

                                </li>
                                <li>
                                    <div>
                                        <a href="#vehicle-driveway">Allowable Vehicle / Driveway Details</a>
                                    </div>

                                </li>
                            </ul>
                        </Col>
                        <Col md={8} className="contain-category">
                            <section id="availability">
                                <h3>Site Availability</h3>
                                <span>1 night weekday minimum</span>

                                <div className="my-4">
                                    <DayPickerRangeController
                                        className="my-4"
                                        startDate={this.state.startDate} // momentPropTypes.momentObj or null,
                                        endDate={this.state.endDate} // momentPropTypes.momentObj or null,
                                        onDatesChange={({ startDate, endDate }) => {
                                            this.setState({
                                                startDate: startDate,
                                                endDate: endDate,
                                                // totalDate: endDate.diff(startDate, "days")
                                            })
                                        }} // PropTypes.func.isRequired,
                                        focusedInput={this.state.focusedInput || START_DATE}
                                        onFocusChange={(focusedInput: String) => this.setState({ focusedInput })}
                                        initialVisibleMonth={() => moment().add(2, "M")} // PropTypes.func or null,
                                        numberOfMonths={2}
                                        keepOpenOnDateSelect={false}
                                        onPrevMonthClick={() => {
                                            this.setState({
                                                focusedInput: START_DATE
                                            })
                                        }}
                                        onNextMonthClick={() => {
                                            this.setState({
                                                focusedInput: END_DATE
                                            })
                                        }}
                                        autoFocusEndDate={true}
                                        renderDayContents={this.renderDateText}
                                        onOutsideClick={() => {
                                            this.setState({
                                                focusedInput: END_DATE
                                            })
                                        }}
                                    />
                                </div>

                            </section>

                            <section id="site-detail">
                                <h3>Site Details</h3>
                                <ul className="camp-props-list" data-columns="2">
                                    <li>
                                        <CampInfo icon={faPaw} text={`Site Type: ${camp.siteDetails.siteType}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Site Accessible: ${camp.siteDetails.siteAccessible}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Check-In: ${camp.siteDetails.checkInTime}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Check-Out: ${camp.siteDetails.checkOutTime}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Max Num of People: ${camp.siteDetails.maxNumOfPeople}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Min Num of People: ${camp.siteDetails.minNumOfPeople}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Type of Use: ${camp.siteDetails.typeOfUse}`} />
                                    </li>

                                    <li>
                                        <CampInfo icon={faPaw} text={`Campfire Allowed: ${camp.siteDetails.campFireAllowed}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Capacity Rating: ${camp.siteDetails.capacityRating}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Pets Allowed: ${camp.siteDetails.petAllowed}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Site Reserve Type: ${camp.siteDetails.siteType}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Shade: ${camp.siteDetails.siteType}`} />
                                    </li>
                                </ul>
                            </section>

                            <section id="allowable-equipment">
                                <h3>Allowable Equipment</h3>
                                <ul className="camp-props-list" data-columns="2">
                                    <li>
                                        <CampInfo icon={faPaw} text={`Tent ${camp.allowableEquipmentList.items.Tent}`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`RV (${camp.allowableEquipmentList.items.RV})`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={`Trailer (${camp.allowableEquipmentList.items.Trailer})`} />
                                    </li>
                                    <li>
                                        <CampInfo icon={faPaw} text={''} />
                                    </li>
                                </ul>
                            </section>

                            <section id="vehicle-driveway">
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
                        <Col md={12}>
                            <div className="fix-price">
                                <div className="content-price">
                                    <div className="d-flex justify-content-between">
                                        <div>
                                            <h3>PINECREST</h3>
                                            <p>Site A001, Loop A</p>
                                        </div>

                                        <div className="d-flex">
                                            <div className="mr-5">
                                                <h2>
                                                    ${(Number(totalDate) !== 0 ? Number(totalDate) + 1 : 0) * Number.parseFloat(this.state.camp.price.toString())}
                                                </h2>
                                                <p className="text-muted">+ Fees & Taxes</p>
                                            </div>
                                            <div>
                                                <Button outline color="primary" style={{
                                                    padding: '10px',
                                                    fontWeight: 500

                                                }}

                                                    onClick={this.onBooking}
                                                >
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
const mapProps = state => ({
    authUser:  state.auth.userData
})
export default connect(mapProps)(withRouter(CampPage));
