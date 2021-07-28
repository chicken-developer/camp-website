import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import "./Layout.css";
import { useLocation } from "react-router-dom";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  let location = useLocation()
  let pathName = location?.pathname
  return (
    <div className="App">
      {(pathName.indexOf('/sign-in') >= 0 && pathName.indexOf('/sign-up') >= 0) && <NavBar />}
      <div className="auth-wrapper">
        <div className="auth-inner">{children}</div>
      </div>
    </div>
  );
};

export default Layout;
