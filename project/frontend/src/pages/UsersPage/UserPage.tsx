import React, {useState, useEffect} from "react";
import MaterialTable from 'material-table'
import * as API from "../../service"
import "./UserPage.scss";
import {Table} from "reactstrap"

const UsersPage = ({}) => {
    const [isLoading, setLoading] = useState(false);
    const [listUser, setListUser] = useState([] as any);
    const [detailHistory, setDetailHistory] = useState([]);
    const [historys, setHistorys] = useState([] as any);


    useEffect(() => {
        setLoading(true);
        fetchUsers();
        getFullHistory();
      }, [])

    const fetchUsers = () => {
        API.getAllUser()
        .then(response => {
          const listCampData = response.data;
          console.log(listCampData)
          if (listCampData) {
            setListUser(listCampData.data);
          }
          setLoading(false);
        })
        .catch(error => {
          setLoading(false);
        })
    }

    const getFullHistory = () => {
      API.fullHistory()
      .then(response => {
        const listCampData = response.data;
        if (listCampData) {
          setHistorys(listCampData.data);
        }
        setLoading(false);
      })
      .catch(error => {
        setLoading(false);
      })
  }

    return (
        <div style={{ maxWidth: '100%' }}>
            <MaterialTable
                columns={[
                    { title: 'Id', field: '_id', disableClick: true },
                    { title: 'User Name', field: 'username', disableClick: true },
                    { title: 'First Name', field: 'firstName' },
                    { title: 'Last Name', field: 'lastName' },
                    { title: 'Email', field: 'email' },
                    { title: 'Phone Num', field: 'phoneNumber' }
                ]}
                data={listUser}
                title="Users Managment"
                editable = {{
                    onRowUpdate: (newData, oldData) => {
                        return API.editUser(oldData?.username, newData)
                        .then(newUser => {
                            fetchUsers()

                        })
                    },
                    onRowDelete: (oldData) => {
                        return API.deleteUser(oldData._id)
                          .then(newUser => {
                            fetchUsers()

                        })
                    }
                }}
                
                detailPanel ={[
                  {
                    render: rowData => {
                      console.log("two data", rowData);
                      const userHistory = historys.find(user => user.username == rowData.username);

                      const listHistory = userHistory ? userHistory.bookingHistory : [];
                     
                      console.log("hallo")
                      return (
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
                            {listHistory.map((history: any) => 
                              <tr>
                                  <th>{history.campBookedId}</th>
                                  <td>{history.timeStart}</td>
                                  <td>{history.timeEnd}</td>
                                  <td>{history.totalPrice}</td>
                              </tr>
                          )}
                          </tbody>
                      </Table>)
                    },
                    
                  }
                ]}
            

            />
      </div>
    )
}

export default UsersPage;