import React from "react";
import { Link } from "react-router-dom";
import {useSelector} from "react-redux";
import * as Model from "../type";
import {RootState} from "../store";

interface Props {}

const NavBar = (props: Props) => {
  const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

  return (
    <nav className="navbar navbar-expand-md navbar-light fixed-top">
      <div className="container">
        <Link className="navbar-brand" to={"/sign-in"}>
          Booking Camp Super
        </Link>
        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
          <ul className="navbar-nav ml-auto">
            { !authUser.username ?
              <>
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
              </>
            :
              <>
                <li className="nav-item">
                    Wellcome <strong>{authUser.username}</strong>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to={"/sign-in"}>
                    Logout
                  </Link>
                </li>
              </>
            }
            
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
