import React, { ReactNode } from "react";
import NavBar from "./NavBar";
import "./Layout.css";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  return (
    <div className="App">
      <NavBar />
      <div className="auth-wrapper">
        <div className="auth-inner">{children}</div>
      </div>
    </div>
  );
};

export default Layout;
