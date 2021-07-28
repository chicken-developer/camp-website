import React from "react"
import "./CampInfo.scss";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPaw, IconDefinition } from '@fortawesome/free-solid-svg-icons'

interface Props {
    icon: IconDefinition,
    text: String
}

const CampInfo = ({ icon, text }: Props) => {
    return (
        <div className="card camp-info">
            <div className="card-info__content">
                <div className="camp-info__icon">
                    <FontAwesomeIcon icon={icon} className="mr-4" />
                </div>

                <div className="camp-info__title">
                    {text}
                </div>
            </div>
        </div>
    )
}

export default CampInfo;