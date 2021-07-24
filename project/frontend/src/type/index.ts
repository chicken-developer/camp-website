export interface User {
    username: String
    firstName: String,
    lastName: String,
    password: String,
    email?: String,
    phoneNumber?: String,
    typeOfUser?: String,
    bookingHistory?: Array<String>
} 

export interface SingleUser {
    data: User,
    message: String,
    status: Number
}
export interface Camp {
    _id: String,
    address: String,
    allImgSrc: Array<String>,
    mainImgSrc: String,
    name: String,
    price: Number,
    rvMax: Number,
    sd_maxNumOfPeople: Number,
    sd_typeOfUse: Number,
    tenMax: Number,
    vd_maxNumOfVehicles: Number,
    vd_maxVehicleLengthForVehicle: Number
}

export interface ListCamp {
    data: Array<Camp>,
    message: String,
    status: Number
}

