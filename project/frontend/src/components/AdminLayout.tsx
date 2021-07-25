import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import Footer from "./Footer";
import "./LandingLayout.css";
import {useSelector} from "react-redux";
import * as Model from "../type"
import {RootState} from "../store";
import { Redirect, useLocation } from "react-router-dom";
import SideBar from "../components/SideBar";
import AdminHeader from "../components/AdminHeader";

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
            <SideBar />  
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
                <div className="footer-left">
                Copyright &copy; 2018 <div className="bullet"></div> Design By <a href="https://nauval.in/">Muhamad Nauval Azhar</a>
                </div>
                <div className="footer-right">
                2.3.0
                </div>
            </footer>
        </div>
    </div>
  );
};

export default Layout;
