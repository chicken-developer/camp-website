import React, { ReactNode } from "react";
import {NavLink} from 'react-router-dom'
import "./CampCard.scss";
import * as Model from "../../type"

interface Props {
    camp: Model.Camp
}

const CampCard = ({camp}: Props) => {
  return (
    <div className="card camp-card px-0">
        <div className="card-horizontal camp-card-contain">
            <img
                className="card-img-top item-img"
                src="https://cdn.recreation.gov/public/2019/02/21/19/34/65350_e7c97c84-39f2-425b-9714-4566d2fbaf8f_700.jpg"
                alt=""
            />
            <div className = "d-flex flex-column justify-content-between w-100">
                <div className="card-body pl-4 pr-0">
                    <h4 className="card-title ">{camp.name}</h4>
                    <ul className="rec-flex-card-meta rec-v-fill-space mb-half camp-info">
                        <li>{camp.address}</li>
                        <li>{camp.sd_typeOfUse}</li>
                        <li className="max-num-ppl">
                            <strong>{camp.sd_maxNumOfPeople}</strong> Max Occupants
                        </li>
                        <li className="max-num-vehicles">
                            <strong>{camp.vd_maxNumOfVehicles}</strong> Max Vehicles (max {camp.vd_maxVehicleLengthForVehicle} ft./vehicle)
                        </li>
                    </ul>
                    <div className="rec-panel-wrap">
                    <div className="icon-wrap">
                        <div>
                        <svg
                            data-component="Icon"
                            className="sarsa-icon rec-icon-rv-trailer"
                            viewBox="0 0 24 24"
                            role="presentation"
                            focusable="false"
                            height="24"
                            width="24"
                        >
                            <g>
                            <path d="M18.3 8.9h2.4L20 6.2c-.1-.3-.3-.5-.6-.5H4.1c-.9 0-1.6.7-1.6 1.6V16h2.4c0 1.3 1.1 2.4 2.4 2.4 1.3 0 2.4-1.1 2.4-2.4h4.7c0 1.3 1.1 2.4 2.4 2.4 1.3 0 2.4-1.1 2.4-2.4h2.4v-3.9l-3.3-3.2zm-11 8.2c-.7 0-1.2-.5-1.2-1.2s.5-1.2 1.2-1.2 1.2.5 1.2 1.2-.6 1.2-1.2 1.2zM12 12H4.1V8.9H12V12zm4.7 5.1c-.7 0-1.2-.5-1.2-1.2s.5-1.2 1.2-1.2 1.2.5 1.2 1.2-.5 1.2-1.2 1.2zM15.5 12V8.9h1.6l3.1 3.1h-4.7z"></path>
                            </g>
                        </svg>
                        &nbsp;<span>RV (max {camp.vd_maxVehicleLengthForVehicle} ft.)</span>
                        </div>
                        <div>
                        <svg
                            data-component="Icon"
                            className="sarsa-icon rec-icon-tent"
                            viewBox="0 0 24 24"
                            role="presentation"
                            focusable="false"
                            height="24"
                            width="24"
                        >
                            <g>
                            <path d="M6.17 6.045L1 18h3.795l1.547-7.756L7.876 18h3.91L6.718 6.041M17.726 6l-9.348.036L13.453 18H23"></path>
                            </g>
                        </svg>
                        &nbsp;<span>Tent (max {camp.tenMax} ft.)</span>
                        </div>
                    </div>
                </div>
            </div>

            <div className="rec-flex-card-content-bottom-wrap">
                <div className="rec-flex-card-price-button-wrap">
                    <div className="rec-flex-card-price-wrap">
                        <div className="rec-flex-card-price">${camp.price}</div>
                        <div className="rec-flex-card-duration">/ per night</div>
                    </div>
                    <NavLink
                        data-component="Button"
                        className="sarsa-button sarsa-button-tertiary sarsa-button-xs"
                        id="enter-dates-button-65218"
                        to="/camping/campsites/65218"
                        rel="noopener noreferrer"
                        aria-label="View details for Site: A004, Loop: A"
                    >
                        <span className="sarsa-button-inner-wrapper">
                        <span className="sarsa-button-content">View Details</span>
                        </span>
                    </NavLink>
                </div>
            </div>
        </div>
            
    </div>
</div>
  );
};

export default CampCard;
