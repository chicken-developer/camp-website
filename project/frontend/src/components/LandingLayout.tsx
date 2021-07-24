import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import Footer from "./Footer";
import "./LandingLayout.css";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  return (
    <div>
      <NavBar />
      <div className="auth-wrapper">
        {/* <div className="appbody">{children}</div> */}
        {children}
      </div>
      <Footer />
    </div>
  );
};

export default Layout;
