import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import Footer from "./Footer";
import "./LandingLayout.css";
import { useSelector } from "react-redux";
import * as Model from "../type"
import { RootState } from "../store";
import { Redirect } from "react-router-dom";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

  return (
    <div>
      <NavBar />
      <div className="auth-wrapper">
        {/* <div className="appbody">{children}</div> */}
        {/* {authUser.username 
          ? children
          : <Redirect to = "/sign-in" />
        } */}
        {children}
      </div>
      <Footer />
    </div>
  );
};

export default Layout;
