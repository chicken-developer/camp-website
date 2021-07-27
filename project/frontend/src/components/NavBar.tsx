import React, { MouseEventHandler } from "react";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import * as Model from "../type";
import { RootState } from "../store";
import Constant from "../utils/Constant";
import { toastSuccess } from "../utils/toast_mixin";
import * as action from '../reducers/auth/actions'
interface Props { }

const NavBar = (props: Props) => {

  let history = useHistory()

  const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

  let onLogout: MouseEventHandler<HTMLAnchorElement> = () => {
    // useDispatch(action.logoutAction())
    localStorage.removeItem(Constant.KEY.USER)
    toastSuccess("Logout Successfull");
    history.push('/sign-in')
  }

  return (
    <nav className="navbar navbar-expand-md navbar-light fixed-top">
      <div className="container">
        <Link className="navbar-brand" to={"/sign-in"}>
          Booking Camp Super
        </Link>
        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
          <ul className="navbar-nav ml-auto">
            {!authUser.username ?
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
                  {/* <Link className="nav-link" to={"/sign-in"}>
                    Logout
                  </Link> */}
                  <a className="menu_link" onClick={onLogout}> Logout</a>
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
