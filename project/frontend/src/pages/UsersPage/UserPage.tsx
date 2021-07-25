import React, {useState, useEffect} from "react";
import MaterialTable from 'material-table'
import * as API from "../../service"
import "./UserPage.scss";

const UsersPage = ({}) => {
    const [isLoading, setLoading] = useState(false);
    const [listUser, setListUser] = useState([] as any);

    useEffect(() => {
        setLoading(true);
        fetchUsers();
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

    return (
        <div style={{ maxWidth: '100%' }}>
            <MaterialTable
                columns={[
                    { title: 'User Name', field: 'username' },
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
                            console.log(newUser)
                            // setListUser(newUser)

                        })
                    },
                    onRowDelete: (oldData) => {
                        return API.deleteUser(oldData.username)
                    }
                }}
            />
      </div>
    )
}

export default UsersPage;