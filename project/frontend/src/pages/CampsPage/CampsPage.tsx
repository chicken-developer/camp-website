import React, {useState, useEffect} from "react";
import MaterialTable from 'material-table'
import * as API from "../../service"
import "./CampsPage.scss";

const CampsPage = ({}) => {
    const [isLoading, setLoading] = useState(false);
    const [listCamp, setListCamp] = useState([] as any);

    useEffect(() => {
        setLoading(true);
        fetchUsers();
      }, [])

    const fetchUsers = () => {
        API.getListCamp()
        .then(response => {
          const listCampData = response.data;
          console.log(listCampData)
          if (listCampData) {
            setListCamp(listCampData.data);
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
                    {title: 'Id', field: '_id', hidden: true},
                    { title: 'Address', field: 'address' },
                    { title: 'Name', field: 'name' },
                    { title: 'Price', field: 'price' },
                    { title: 'RMax', field: 'rvMax' },
                    { title: 'Max People', field: 'sd_maxNumOfPeople' },
                    { title: 'Type', field: 'sd_typeOfUse' },
                    { title: 'Ten Max', field: 'tenMax' },
                    { title: 'Number Vehicles', field: 'vd_maxNumOfVehicles' },
                    { title: 'Max Number Vehicles', field: 'vd_maxVehicleLengthForVehicle' }
                ]}
                data={listCamp}
                title="Camp Managment"
                editable = {{
                    onRowUpdate: (newData, oldData) => {
                        return API.editCamp(oldData?._id, newData)
                        .then(newCamp => {
                            console.log(newCamp)
                            // setListUser(newUser)
                            fetchUsers()
                        })
                    },
                    onRowDelete: (oldData) => {
                        return API.deleteUser(oldData._id)
                        .then(newCamp => {
                            console.log(newCamp)
                            // setListUser(newUser)
                            fetchUsers()
                        })
                    },
                    onRowAdd: (newRow) => {
                        return API.createCamp(newRow)
                        .then(newCamp => {
                            console.log(newCamp)
                            // setListUser(newUser)
                            fetchUsers()
                        })
                    }
                }}
            />
      </div>
    )
}

export default CampsPage;