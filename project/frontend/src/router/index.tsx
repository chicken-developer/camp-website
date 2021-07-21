import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Login from "../pages/Login/Login";
import Register from "../pages/RegisterPage/Register";
import HomePage from "../pages/HomePage/HomePage";
import Layout from "../components/Layout";
import PrivateRoute from "./PrivateRoute";
import { useSelector } from "react-redux";

function AppRouter() {
  const auth = useSelector((state: any) => state.auth);
  return (
    <Router>
      {/* <Layout> */}
        {/* <Route exact path="/" component={Login} />
        <Route path="/sign-in" component={Login} />
        <Route path="/sign-up" component={Register} /> */}
      {/* </Layout> */}

      <Route path="/home" component={HomePage} />

      {/* <PrivateRoute
        authentication={auth.isLogin}
        path="/camp"
        component={HomePage}
      />

      <PrivateRoute
        authentication={auth.isLogin}
        path="/booking"
        component={HomePage}
      />
      <PrivateRoute
        authentication={auth.isLogin}
        path="/bookadmining"
        component={HomePage}
      /> */}
    </Router>
  );
}

export default AppRouter;
