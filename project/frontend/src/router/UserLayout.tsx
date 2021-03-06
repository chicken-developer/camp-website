import React from 'react';
import PropTypes from 'prop-types';
import Constant from '../utils/Constant';
import { Redirect, Route, useLocation } from 'react-router-dom';
import AdminLayout from "../components/AdminLayout";
import { useSelector } from 'react-redux';
import LandingLayout from "../components/LandingLayout";
import { RootState } from '../store';
import * as Model from "../type"
const UserLayout = ({ children, ...rest }) => {
    return (
        <LandingLayout>
            {children}
        </LandingLayout>

    )
}

const UserRoute = ({ component: Component, ...rest }) => {
    const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);
    return (
        (authUser.username) ? (
            <Route {...rest} render={matchProps => (
                <UserLayout>
                    <Component {...matchProps} />
                </UserLayout>
            )} />
        )
            : <Redirect to={{ pathname: "/sign-in" }} />
        // :
        // (data?.typeOfUser === 'root'
        //     : <Redirect to={{ pathname: "/sign-in" }} />
        // )
    )
};

export default UserRoute;
