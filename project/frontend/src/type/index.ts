export interface User {
    _id: String,
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

export interface ListUser {
    data: Array<User>,
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

export interface HomeCamp {
    _id: String,
    allowableEquipmentList: {
        _id: String,
        items: {
            Tent: String,
            RV: String,
            Trailer: String
        }
    },
    allowableVehicleAndDrivewayDetails: {
        _id: String,
        drivewayEntry: String,
        drivewayLength: Number,
        drivewaySurface: String,
        isEquipmentMandatory: String,
        maxNumOfVehicles: Number,
        maxVehicleLength: Number,
        siteLength: Number
    },
    allImgSrc: Array<String>,
    campLocationAddress: String,
    campName: String,
    nearAddress: String,
    partAddress: String,
    price: Number,
    siteAvailability: {
        _id: String,
        date: String,
        state: String
    },
    siteDetails: {
        _id: String,
        campFireAllowed: String,
        capacityRating: String,
        checkInTime: String,
        checkOutTime: String,
        maxNumOfPeople: Number,
        minNumOfPeople: Number,
        petAllowed: String,
        shade: String,
        siteAccessible: String,
        siteReserveType: String,
        siteType: String,
        typeOfUse: String
    },
    campImgSrc: Array<String>
}

export interface CampResponse {
    message: String,
    status: Number,
    data: HomeCamp
}