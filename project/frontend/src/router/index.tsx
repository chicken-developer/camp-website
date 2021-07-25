import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Login from "../pages/Login/Login";
import Register from "../pages/RegisterPage/Register";
import HomePage from "../pages/HomePage/HomePage";
import CampPage from "../pages/CampPage/CampPage";
import Layout from "../components/Layout";
import LandingLayout from "../components/LandingLayout";

import { PrivateRoute } from "./PrivateRoute";

function AppRouter() {
  return (
    <Router>
      <Switch>
          <Route exact path="/" component={ (props: any) => (
              <Layout>
                <Login {...props} />
              </Layout>
          )} />
          <Route exact path="/sign-in" component={ (props: any) => (
              <Layout>
                <Login {...props} />
              </Layout>
          )} />

          <Route exact path="/sign-up" component={ (props: any) => (
              <Layout>
                <Register {...props} />
              </Layout>
          )} />

        {/* <PrivateRoute path="/home" component={HomePage} /> */}
        <LandingLayout>
           <React.Suspense fallback = {() => <div/>}>
              <Route exact path="/home" component={HomePage} />
              <Route path="/camp/:campId" component={CampPage} />
              <Route exact path="/camp" component={CampPage} />
           </React.Suspense>
        </LandingLayout>
        

        {/* <PrivateRoute path="/camp" component={HomePage} />

        <PrivateRoute path="/booking" component={HomePage} />
        <PrivateRoute path="/bookadmining" component={HomePage} /> */}
      </Switch>
    </Router>
  );
}

export default AppRouter;
