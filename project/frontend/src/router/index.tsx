import React from "react";
import { BrowserRouter as Router, Redirect, Route, Switch, useLocation } from "react-router-dom";
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
import AuthRouter from "./AuthRouter";
import GuesRouter from "./GuestRouter";
import { ProflePage } from "../pages/Profile/ProflePage";


function AppRouter() {
  const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);

  return (
    <Router>
      <Switch>
        {/* <Route exact path="/" component={(props: any) => (
          <Layout>
            <Login {...props} />
          </Layout>
        )} /> */}

        <Route exact path="/">
          {
            data?.typeOfUser === 'root'
              ? <Redirect to={{ pathname: "/admin" }} />
              : <Redirect to={{ pathname: "/home" }} />
          }
        </Route>

        <AuthRouter exact path="/sign-in" component={Login} />
        <AuthRouter exact path="/sign-up" component={Register} />

        <DashboardLayoutRoute exact path="/admin" component={AdminPage} />
        <DashboardLayoutRoute path="/admin/users" component={UsersPage} />
        <DashboardLayoutRoute path="/admin/camps" component={CampsPage} />

        <GuesRouter exact path="/home" component={HomePage} />
        <GuesRouter path="/camp/:campId" component={CampPage} />
        <GuesRouter path="/camp" component={CampPage} />

        <UserRoute exect path="/profile" component={ProflePage} />

      </Switch>
    </Router>


  );
}

export default AppRouter;
