import React, {useState, useEffect} from "react";
import MaterialTable from 'material-table'
import * as API from "../../service"
import {Table, Button, Input, CustomInput} from "reactstrap";
import "./CampsPage.scss";
import {getLink, getFormData} from "../../utils/Utils"
import * as Model from "../../type"

const CampsPage = ({}) => {
    const [isLoading, setLoading] = useState(false);
    const [listCamp, setListCamp] = useState([] as any);
    const [files, setFiles] = useState([] as any);
    let currentFile = React.useRef(null);

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

    const onPressAddNew = (rowId) => {
        let newListCamp = Array.from<Model.Camp>(listCamp);
        let campIndex = newListCamp.findIndex((camp) => {
            return (
                 camp._id == rowId
            )
        });
        if (campIndex >= 0) {
            newListCamp[campIndex].allImgSrc.push('')
            setListCamp(newListCamp);
        }   
        
        
    }

    const onPressUpdate = async (rowId) => {
        const resultCampDetail = await API.getDetailCamp(rowId);
        let campDetail = resultCampDetail.data.data;
        let newListCamp = Array.from<Model.Camp>(listCamp);
        let campIndex = newListCamp.findIndex((camp) => {
            return  camp._id == rowId
               
        });
        campDetail.campImgSrc = newListCamp[campIndex].allImgSrc;
        campDetail._id = "null";
        const resultUpdate = await API.editCamp(rowId, campDetail);
        console.log(resultUpdate);

        fetchUsers();
    }

    const uploadFileHandle = (rowId, event, index) => {
        API.uploadFile({
            image: event.target.files[0]
        }).then(upLoadSuccess => {
            const link = upLoadSuccess.data.data;
            console.log(link)
            let newListCamp = Array.from<Model.Camp>(listCamp);
            let campIndex = newListCamp.findIndex((camp) => {
                return  camp._id == rowId
                   
            });
            newListCamp[campIndex].allImgSrc[index] = link.toLowerCase();
            setListCamp(newListCamp);

        }).catch(error => {
            console.log(error)
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
                    { title: 'Max Number Vehicles', field: 'vd_maxVehicleLengthForVehicle' },
                    { title: 'Images', field: "allImgSrc", hidden: true}
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
                detailPanel = {rowData => {
                    return (
                        <div>
                            <Table>
                                <thead>
                                    <tr>
                                    <th>Upload Image</th>
                                    <th>Link</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    { rowData.allImgSrc.map((image, index) => {
                                        return (
                                            <tr>
                                                <th>
                                                    <CustomInput type="file" 
                                                    id="exampleCustomFileBrowser" 
                                                    name="customFile" 
                                                    onChange = {(event) => {
                                                        uploadFileHandle(rowData._id, event, index)
                                                    }}
                                                    ref = {refer => {
                                                        currentFile.current = refer;
                                                        return currentFile;
                                                    }}
                                                    accept="image/*"
                                                    />
                                                </th>

                                                <td>
                                                    <Input 
                                                        placeholder="upload" 
                                                        bsSize="sm" 
                                                        value = {image ? getLink(image) : ""} 
                                                       
                                                        disabled = {true}/>
                                                </td>
                                            </tr>
                                        )
                                    })

                                    }
                                   
                                </tbody>
                            </Table>

                            <div className = "container d-flex my-3">
                                <Button color = "primary mr-3" 
                                    onClick = {() => {
                                        onPressAddNew(rowData._id)
                                    }}
                                >Add new</Button>
                                <Button 
                                    onClick = {() => {
                                        onPressUpdate(rowData._id)
                                    }}
                                    color = "primary"
                                >Update</Button>
                            </div>
                        </div>
                    )
                }}
            />
      </div>
    )
}

export default CampsPage;