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
    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);
    return (
        data?.typeOfUser === 'root'
            ? (
                <Route {...rest} render={matchProps => (
                    <DashboardLayout>
                        <Component {...matchProps} />
                    </DashboardLayout>
                )} />
            )
            :
            (
                data?.typeOfUser === null
                    ? <Redirect exact to={{ pathname: "/sign-in" }} />
                    : <Redirect exact to={{ pathname: "/home" }} />
            )
    )
};

export default DashboardLayoutRoute;