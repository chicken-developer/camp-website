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

    const mapObjectDetailFromHome = (isUpdate: Boolean, homeData: Model.Camp, detailData?: Model.HomeCamp) => {
        let templete: Model.HomeCamp = {
            _id: "null",
            allowableEquipmentList: {
                _id: "null",
                items: {
                    Tent: "YES",
                    RV: "20",
                    Trailer: "15"
                }
            },
            allowableVehicleAndDrivewayDetails: {
                _id: "null",
                drivewayEntry: "N/A",
                drivewayLength: 25.0,
                drivewaySurface: "Paved",
                isEquipmentMandatory: "Yes",
                maxNumOfVehicles: 1.0,
                maxVehicleLength: 28.0,
                siteLength: 35.0
            },
            allImgSrc: [],
            campLocationAddress: "Milpitas",
            campName: "Site: A001, Loop: B",
            nearAddress: " Milpitas, California",
            partAddress: " Canon National Forest",
            price: 10,
            siteAvailability: {
                _id: "null",
                date: "2021-07-24",
                state: "R"
            },
            siteDetails: {
                _id: "null",
                campFireAllowed: "Yes",
                capacityRating: "Group",
                checkInTime: "3:00 PM",
                checkOutTime: "12:00 AM",
                maxNumOfPeople: 16.0,
                minNumOfPeople: 2.0,
                petAllowed: "No",
                shade: "Yes",
                siteAccessible: "No",
                siteReserveType: "Site-Specific",
                siteType: "Standard Nonelectric",
                typeOfUse: "Over night"
            },
            campImgSrc: []
        };

        if (isUpdate) {
            templete = {...detailData};
            templete._id = "null";
            templete.allowableEquipmentList._id = "null";
            templete.allowableVehicleAndDrivewayDetails._id = "null";
            templete.siteAvailability._id = "null";
            templete.siteDetails._id = "null";
        }

        // update new
        templete.allowableEquipmentList.items.Tent = isUpdate ? templete.allowableEquipmentList.items.Tent.toString() : "YES"
        templete.campLocationAddress = homeData.address;
        templete.campName = homeData.name;
        templete.price = isUpdate ? homeData.price : Number.parseFloat(`${homeData.price}`);
        templete.allowableEquipmentList.items.RV = homeData.rvMax.toString();
        templete.siteDetails.maxNumOfPeople = Number.parseFloat(`${homeData.sd_maxNumOfPeople}`);
        templete.siteDetails.typeOfUse = homeData.sd_typeOfUse;
        templete.allowableEquipmentList.items.Tent = isUpdate ? homeData.tenMax.toString() : "YES"; 
        templete.allowableVehicleAndDrivewayDetails.maxVehicleLength = Number.parseFloat(`${homeData.vd_maxVehicleLengthForVehicle}`)
        templete.allowableVehicleAndDrivewayDetails.maxNumOfVehicles =  Number.parseFloat(`${homeData.vd_maxNumOfVehicles}`);

        return templete;
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
                    onRowUpdate: async (newData, oldData) => {
                        const resultCampDetail = await API.getDetailCamp(oldData?._id);
                        const fullCamp = resultCampDetail.data.data as Model.HomeCamp;
                        const mergeCamp = mapObjectDetailFromHome(true, newData as Model.Camp, fullCamp);
                        console.log(JSON.stringify(mergeCamp))
                        return API.editCamp(oldData?._id, mergeCamp)
                        .then(newCamp => {
                            console.log(newCamp)
                            // setListUser(newUser)
                            fetchUsers()
                        })
                    },
                    onRowDelete: (oldData) => {
                        return API.deleteCamp(oldData.address)
                        .then(newCamp => {
                            console.log(newCamp)
                            
                            // setListUser(newUser)
                            fetchUsers()
                        })
                    },
                    onRowAdd: async (newRow) => {
                         const mergeCamp = mapObjectDetailFromHome(false, newRow as Model.Camp);
                         // mergeCamp._id = "null";
                        console.log(JSON.stringify(mergeCamp))
                        return API.createCamp(mergeCamp)
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