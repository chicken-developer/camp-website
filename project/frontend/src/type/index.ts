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

