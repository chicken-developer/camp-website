import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import Footer from "./Footer";
import "./LandingLayout.css";
import {useSelector} from "react-redux";
import * as Model from "../type"
import {RootState} from "../store";
import { Redirect, useLocation } from "react-router-dom";
import AdminHeader from "../components/AdminHeader";
import Sidebar from "./SideBar";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

  return (
    <div id="app"
    style = {{
      width: '75vw'
    }}>
      
        <div className="main-wrapper">
            <div className="navbar-bg"></div>
            <AdminHeader/>
            <Sidebar />  
            <div className="main-content">
                <section className="section">
                <div className="section-header">
                  <h1>Admin Page</h1>
                </div>

                <div className="section-body">
                    { authUser.username && authUser.typeOfUser == "root" 
                        ? children
                        : <Redirect to = "/sign-in" />
                    }
                </div>
                </section>
            </div>

            <footer className="main-footer">

            </footer>
        </div>
    </div>
  );
};

export default Layout;
