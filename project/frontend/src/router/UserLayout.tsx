import React from 'react';
import PropTypes from 'prop-types';
import Constant from '../utils/Constant';
import { Redirect, Route, useLocation } from 'react-router-dom';
import AdminLayout from "../components/AdminLayout";
import { useSelector } from 'react-redux';
import LandingLayout from "../components/LandingLayout";

const UserLayout = ({ children, ...rest }) => {
    return (
        <LandingLayout>
            {children}
        </LandingLayout>

    )
}

const UserRoute = ({ component: Component, ...rest }) => {

    const location = useLocation();
    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);
    return (
        data?.typeOfUser !== 'root' ? (
            <Route {...rest} render={matchProps => (
                <UserLayout>
                    <Component {...matchProps} />
                </UserLayout>
            )} />
        )
            :
            <Redirect to={{ pathname: "/admin", state: { from: location } }} />
    )
};

export default UserRoute;
