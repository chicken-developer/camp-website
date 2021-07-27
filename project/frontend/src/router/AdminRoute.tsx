import React, { Component, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Redirect, Route, useLocation } from 'react-router-dom';
import AdminLayout from "../components/AdminLayout";
import Constant from '../utils/Constant';
const DashboardLayout = ({ children, ...rest }) => {
    return (
        <AdminLayout>
            {children}
        </AdminLayout>

    )
}

const DashboardLayoutRoute = ({ component: Component, ...rest }) => {
    const location = useLocation();
    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);

    console.log('AUTH', data?.typeOfUser)
    return (
        data?.typeOfUser === 'root' ? (
            <Route {...rest} render={matchProps => (
                <DashboardLayout>
                    <Component {...matchProps} />
                </DashboardLayout>
            )} />
        )
            :
            <Redirect to={{ pathname: "/home", state: { from: location } }} />
    )
};

export default DashboardLayoutRoute;