import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Login from "../pages/Login/Login";
import Register from "../pages/RegisterPage/Register";
import HomePage from "../pages/HomePage/HomePage";
import Layout from "../components/Layout";
import { PrivateRoute } from "./PrivateRoute";

function AppRouter() {
  return (
    <Router>
      <Switch>
        <Layout>
          <Route exact path="/" component={Login} />
          <Route path="/sign-in" component={Login} />
          <Route path="/sign-up" component={Register} />
        </Layout>

        <PrivateRoute path="/home" component={HomePage} />

        {/* <PrivateRoute path="/camp" component={HomePage} />

        <PrivateRoute path="/booking" component={HomePage} />
        <PrivateRoute path="/bookadmining" component={HomePage} /> */}
      </Switch>
    </Router>
  );
}

export default AppRouter;
