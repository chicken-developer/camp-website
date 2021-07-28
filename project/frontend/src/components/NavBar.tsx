import React, { MouseEventHandler } from "react";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import * as Model from "../type";
import { RootState } from "../store";
import Constant from "../utils/Constant";
import { toastSuccess } from "../utils/toast_mixin";
import * as action from '../reducers/auth/actions'
interface Props { }

// 
// let AccountDropdown = styled

// 
const NavBar = (props: Props) => {

  let history = useHistory()
  let dispatch = useDispatch()
  const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

  let onLogout: MouseEventHandler<HTMLAnchorElement> = () => {
    dispatch(action.logoutAction())
    localStorage.removeItem(Constant.KEY.USER)
    toastSuccess("Logout Successfull");
    history.push('/home')
  }

  return (
    <nav className="navbar navbar-expand-md navbar-light fixed-top">
      <div className="container">
        <Link className="navbar-brand" to={"/home"}>
          Camp For Life
        </Link>
        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
          <ul className="navbar-nav ml-auto">
            {!authUser.username
              ? (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to={"/sign-in"}>
                      Login
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/sign-up">
                      Sign up
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/home">
                      Home
                    </Link>
                  </li>
                </>
              )
              : (
                <>
                  <li className="dropdown">

                    <a href="." data-toggle="dropdown" className="nav-link dropdown-toggle nav-link-lg nav-link-user">
                      <img alt="image" src="../assets/img/avatar/avatar-1.png" 
                      className="rounded-circle mr-1" />
                      <div className="d-sm-none d-lg-inline-block">Hi, {authUser.username}</div>
                    </a>

                    <div className="dropdown-menu dropdown-menu-right">
                      <Link className="dropdown-item has-icon text-dark d-flex align-items-center" to={'/profile'}>
                        <i className="fas fa-user"></i>Profile
                      </Link>
                      <a href="." className="dropdown-item has-icon text-dark d-flex align-items-center">
                        <i className="fas fa-bookmark"></i> <span>My Booking</span>
                      </a>
                      <hr />

                      <a href="." className="dropdown-item has-icon text-danger d-flex align-items-center" onClick={onLogout}>
                        <i className="fas fa-sign-out-alt"></i> Logout
                      </a>
                    </div>
                  </li>
                </>
              )
            }

          </ul>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
