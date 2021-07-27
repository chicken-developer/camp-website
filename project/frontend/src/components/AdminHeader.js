import React from "react"
import { useDispatch, useSelector } from "react-redux"
import { useHistory, useLocation } from "react-router-dom"
import Constant from "../utils/Constant"
import {RootState} from "../store";
import {User} from "../type"
import { toastSuccess } from "../utils/toast_mixin"
import * as action from '../reducers/auth/actions'

export default ({ })  => {
  
  const authUser = useSelector((state) => state.auth.userData)

  let history = useHistory()
  const dispatch = useDispatch();

  let onLogout = () => {
    dispatch(action.logoutAction)
    localStorage.removeItem(Constant.KEY.USER)
    toastSuccess("Logout Successfull");
    // history.push('/sign-in')
  }

  return (
    <nav class="navbar navbar-expand-lg main-navbar">
      <form class="form-inline mr-auto">
        <ul class="navbar-nav mr-3">
          <li><a href="#" data-toggle="sidebar" class="nav-link nav-link-lg"><i class="fas fa-bars"></i></a></li>
          <li><a href="#" data-toggle="search" class="nav-link nav-link-lg d-sm-none"><i class="fas fa-search"></i></a></li>
        </ul>
      </form>
      <ul class="navbar-nav navbar-right">

        <li class="dropdown"><a href="#" data-toggle="dropdown" class="nav-link dropdown-toggle nav-link-lg nav-link-user">
          <img alt="image" src="../assets/img/avatar/avatar-1.png" class="rounded-circle mr-1" />
          <div class="d-sm-none d-lg-inline-block">Hi, {authUser.username}</div></a>
          <div class="dropdown-menu dropdown-menu-right">
            <a href="#" class="dropdown-item has-icon text-danger" onClick={onLogout}>
              <i class="fas fa-sign-out-alt"></i> Logout
            </a>
          </div>
        </li>
      </ul>
    </nav>
  )
}