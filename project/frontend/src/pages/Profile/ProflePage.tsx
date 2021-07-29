import React, { useEffect, useState } from 'react'
import { useForm, SubmitHandler } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import * as Model from '../../type/index'
import * as API from '../../service'
import * as actions from "../../reducers/auth/actions"
import Constant from "../../utils/Constant";
import { toastFailure, toastSuccess } from '../../utils/toast_mixin';
import { useHistory } from 'react-router';

// import * as yup from "yup"
type Inputs = {
    firstname: string,
    lastname: string,
    email: string,
    phoneNumber: string,
    password: string,
    passwordConfirm: string,
};



// const schema = yup.object().shape({
//     firstName: yup.string().required(),
//     age: yup.number().positive().integer().required(),
// });
export const ProflePage = () => {
    const [checkMatch, setcheckMatch] = useState("")
    const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;
    let history = useHistory()
    const { register, setValue, handleSubmit, formState: { errors } } = useForm<Inputs>({
        // resolver: yupResolver(schema)
    });

    const dispatch = useDispatch()
    let updateProfile = (data) => {
        if (data.password === data.passwordConfirm) {
            let userObj = {
                firstName: data.firstname,
                lastName: data.lastname,
                email: data.email,
                phoneNumber: data.phoneNumber,
                password: data.password,
                passwordConfirm: data.passwordConfirm,
                bookingHistoryId: authUser.bookingHistory === undefined ? [""] : authUser.bookingHistory,
                username: authUser.username,
                _id: authUser._id,
                typeOfUser: "normal"
            }

            console.log('userObj:', userObj)
            API.editUser(authUser?.username, userObj).then(userResponse => {
                const userData = userResponse.data;
                console.log(userResponse)
                if (userData.status) {
                    const authUser = userData.data;
                    dispatch(actions.loginSuccess(authUser))
                    localStorage.setItem(Constant.KEY.USER, JSON.stringify(authUser))
                    toastSuccess("Update Successfull");
                    history.push('/home')

                } else {
                    toastFailure("Update Fail")
                }
            })
                .catch(error => {
                    toastFailure("Update Fail")
                    console.log(error)
                })
        } else {
            setcheckMatch("Password not Match!")
        }
    };

    setValue("firstname", authUser?.firstName as string)
    setValue("lastname", authUser?.lastName as string)
    setValue("email", authUser?.email as string)
    setValue("phoneNumber", authUser?.phoneNumber as string)
    setValue("password", authUser?.password as string)
    setValue("passwordConfirm", authUser?.password as string)
    return (
        <div className="page-register container py-5">
            <form onSubmit={handleSubmit(updateProfile)}>
                <h3>Profile</h3>
                <div className="form-group">
                    <label>Username</label>
                    <input
                        readOnly={true}
                        type="text"
                        className="form-control"
                        placeholder="Enter username"
                    // value={authUser?.username as string}
                    // onChange={this.handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>First name</label>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="First name"
                        // onChange={this.handleInputChange}
                        {...register('firstname')}

                    />
                </div>

                <div className="form-group">
                    <label>Last name</label>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Last name"
                        // onChange={this.handleInputChange}
                        {...register('lastname')}

                    />
                </div>

                <div className="form-group">
                    <label>Email address</label>
                    <input
                        type="email"
                        className="form-control"
                        placeholder="Enter email"
                        // onChange={this.handleInputChange}
                        {...register('email')}

                    />
                </div>

                <div className="form-group">
                    <label>Phone number</label>
                    <input
                        type="phone"
                        className="form-control"
                        placeholder="Enter phone number"
                        // onChange={this.handleInputChange}
                        {...register('phoneNumber')}

                    />
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        className="form-control"
                        placeholder="Enter password"
                        // onChange={this.handleInputChange}
                        {...register('password')}

                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        className="form-control"
                        placeholder="Enter password"
                        // onChange={this.handleInputChange}
                        {...register('passwordConfirm')}

                    />
                </div>
                <div className="form-group">
                    <label className="text-danger">{checkMatch}</label>
                </div>
                <button type="submit" className="btn btn-primary btn-block">
                    Update
                </button>
            </form>
        </div>
    )
}
