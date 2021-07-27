import React from "react";
import { BrowserRouter as Router, Route, Switch, useLocation } from "react-router-dom";
import Login from "../pages/Login/Login";
import Register from "../pages/RegisterPage/Register";
import HomePage from "../pages/HomePage/HomePage";
import CampPage from "../pages/CampPage/CampPage";
import Layout from "../components/Layout";
import LandingLayout from "../components/LandingLayout";
import AdminLayout from "../components/AdminLayout";
import AdminPage from "../pages/AdminPage/AdminPage";
import UsersPage from "../pages/UsersPage/UserPage";
import CampsPage from "../pages/CampsPage/CampsPage";
import Constant from "../utils/Constant";
import DashboardLayoutRoute from "./AdminRoute";
import UserRoute from "./UserLayout";


function AppRouter() {
  let location = useLocation();
  let authData = localStorage.getItem(Constant.KEY.USER) as any
  let authRoot = authData?.typeOfUser
  return (
    <Router>
      <Switch>
        <Route exact path="/" component={(props: any) => (
          <Layout>
            <Login {...props} />
          </Layout>
        )} />
        <Route exact path="/sign-in" component={(props: any) => (
          <Layout>
            <Login {...props} />
          </Layout>
        )} />

        <Route exact path="/sign-up" component={(props: any) => (
          <Layout>
            <Register {...props} />
          </Layout>
        )} />

        <DashboardLayoutRoute exact path="/admin" component={AdminPage} />
        <DashboardLayoutRoute path="/admin/users" component={UsersPage} />
        <DashboardLayoutRoute path="/admin/camps" component={CampsPage} />

        <UserRoute exact path="/home" component={HomePage} />
        <UserRoute path="/camp/:campId" component={CampPage} />
        <UserRoute path="/camp" component={CampPage} />

      </Switch>
    </Router>


  );
}

export default AppRouter;
