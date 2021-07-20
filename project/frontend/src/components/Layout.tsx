import React, { ReactNode } from "react";
import { Link } from "react-router-dom";

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  return (
    <div className="App">
      <nav className="navbar navbar-expand-lg navbar-light fixed-top">
        <div className="container">
          <Link className="navbar-brand" to={"/sign-in"}>
            Booking Camp Super
          </Link>
          <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
            <ul className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link className="nav-link" to={"/sign-in"}>
                  Login
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/sign-up"}>
                  Sign up
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/home"}>
                  Home
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <div className="auth-wrapper">
        <div className="auth-inner">{children}</div>
      </div>
    </div>
  );
};

export default Layout;
