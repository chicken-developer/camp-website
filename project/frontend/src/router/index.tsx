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


function AppRouter() {
  let location = useLocation();

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
        {location.pathname.indexOf("/admin") >= 0 ?
          <AdminLayout>
            <React.Suspense fallback = {() => <div/>}>
                <Route exact path="/admin" component={AdminPage} />
                <Route exact path="/admin/users" component={UsersPage} />
                <Route exact path="/admin/camps" component={CampsPage} />
            </React.Suspense>
          </AdminLayout>
          : 
          <LandingLayout>
            <React.Suspense fallback = {() => <div/>}>
                <Route exact path="/home" component={HomePage} />
                <Route path="/camp/:campId" component={CampPage} />
                <Route exact path="/camp" component={CampPage} />
            </React.Suspense>
          </LandingLayout>
        }
        
      </Switch>
    </Router>
  );
}

export default AppRouter;
