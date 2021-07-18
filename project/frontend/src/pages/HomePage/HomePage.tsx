import React, { useState, useEffect, ChangeEvent } from "react";
import CampService from "../network/camp-api";
import { Link } from "react-router-dom";
import ICampData from "../network/types/Camp";

const CampList: React.FC = () => {
    const [camp, setCamp] = useState<Array<ICampData>>([]);
    const [currentCamp, setCurrentCamp] = useState<ICampData | null>(null);
    const [currentIndex, setCurrentIndex] = useState<number>(-1);

    useEffect(() => {
        retrieveCamps();
    }, []);


    const retrieveCamps = () => {
        CampService.getAll()
            .then((response: { data: React.SetStateAction<ICampData[]>; }) => {
                setCamp(response.data);
                console.log(response.data);
            })
            .catch((e) => {
                console.log(e);
            });
    };

    const refreshList = () => {
        retrieveCamps();
        setCurrentCamp(null);
        setCurrentIndex(-1);
    };

    const setActiveCamp = (tutorial: ICampData, index: number) => {
        setCurrentCamp(tutorial);
        setCurrentIndex(index);
    };

    const removeAllCamps = () => {
        CampService.removeAll()
            .then((response: { data: any; }) => {
                console.log(response.data);
                refreshList();
            })
            .catch((e: any) => {
                console.log(e);
            });
    };

    return (
        <div className="list row">
            <div className="col-md-6">
                <h4>Camps List</h4>

                <ul className="list-group">
                    {camp &&
                    camp.map((camp, index) => (
                        <li
                            className={
                                "list-group-item " + (index === currentIndex ? "active" : "")
                            }
                            onClick={() => setActiveCamp(camp, index)}
                            key={index}
                        >
                            {camp.campId}
                        </li>
                    ))}
                </ul>

                <button
                    className="m-3 btn btn-sm btn-danger"
                    onClick={removeAllCamps}
                >
                    Remove All
                </button>
            </div>
            <div className="col-md-6">
                {currentCamp ? (
                    <div>
                        <h4>Camp</h4>
                        <div>
                            <label>
                                <strong>Title:</strong>
                            </label>{" "}
                            {currentCamp.campId}
                        </div>
                        <div>
                            <label>
                                <strong>Description:</strong>
                            </label>{" "}
                            {currentCamp.price}
                        </div>


                        <Link
                            to={"/camp/?campId=" + currentCamp.campId}
                            className="badge badge-warning"
                        >
                            Edit
                        </Link>
                    </div>
                ) : (
                    <div>
                        <br />
                        <p>Click on camp</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CampList;
