import React, {useEffect, useState} from 'react';
import {Table} from 'reactstrap';
import * as Model from "../../type";
import * as API from "../../service"
import "./BookingHistoryPage.scss";
import {useSelector} from "react-redux"
import {RootState} from "../../store";

function BookingPage() {
    const [detailHistory, setDetailHistory] = useState([]);
    const [isLoading, setLoading] = useState(false);
    const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;


    useEffect(() => {
        fetchHistory(authUser.username);
        // fetchHistory("60fdf3a85756b8629ed0129a") //fake
    }, [])


    const fetchHistory = (username: String) => {
        API.getHistory(username)
        .then(response => {
          const listCampData = response.data;
          console.log(listCampData)
          if (listCampData) {
            setDetailHistory(listCampData.data);
          }
          setLoading(false);
        })
        .catch(error => {
          setLoading(false);
        })
    }


    return (
        <div className = "container" style = {{minHeight: '500px'}}>
            <h1 className = "text-center my-4"> Booking History</h1>
            <Table>
                <thead>
                <tr>
                    <th>Camp Book Id</th>
                    <th>Time Start</th>
                    <th>Time End</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {detailHistory.map((history: any) => 
                    <tr>
                        <th>{history.campBookedId}</th>
                        <td>{history.timeStart}</td>
                        <td>{history.timeEnd}</td>
                        <td>{history.totalPrice}$</td>
                    </tr>
                )}
                </tbody>
            </Table>
        </div>
          
    );
}

export default BookingPage;
