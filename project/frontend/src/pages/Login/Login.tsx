import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { NavLink, useHistory } from "react-router-dom";
import * as API from '../../service/'
import Swal from 'sweetalert2'
import * as actions from "../../reducers/auth/actions"
import Constant from "../../utils/Constant";
import {toastSuccess, toastFailure} from "../../utils/toast_mixin"
import "./Login.css"

function Login() {
  const dispatch = useDispatch();
  const history = useHistory();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = (e: any) => {
    e.preventDefault();
    API.login({
      username: email,
      password
    }).then(userResponse => {
      const userData = userResponse.data;
      if (userData.status) {
        const authUser = userData.data;
        console.log(authUser)
        dispatch(actions.loginSuccess(authUser))
        localStorage.setItem(Constant.KEY.USER, JSON.stringify(authUser))
        toastSuccess("Login Successfull");
        history.push("/home")
      } else {
        toastFailure("Login Fail")
        console.log("else!!!")
      }
    })
    .catch(error => {
      toastFailure("Login Fail")
      console.log("else!!!")
      console.log(error)
    })
  };

  const onChangeEmail = (e: any) => {
    setEmail(e.target.value);
  };
  const onChangePass = (e: any) => {
    setPassword(e.target.value);
  };
  return (
    <form>
      <h3>Sign In</h3>
      <div className="form-group">
        <label>Username</label>
        <input
          type="email"
          className="form-control"
          placeholder="Enter username"
          name="email"
          onChange={onChangeEmail}
        />
      </div>

      <div className="form-group">
        <label>Password</label>
        <input
          type="password"
          className="form-control"
          placeholder="Enter password"
          name="password"
          onChange={onChangePass}
        />
      </div>

      <div className="form-group">
        <div className="custom-control custom-checkbox d-flex align-items-center">
          <input
            type="checkbox"
            className="custom-control-input"
            id="customCheck1"
          />
          <label className="custom-control-label" htmlFor="customCheck1">
            Remember me
          </label>
        </div>
      </div>

      <button
        type="submit"
        className="btn btn-primary btn-block"
        onClick={handleLogin}
      >
        Login
      </button>
      <p className="forgot-password text-right">
        Forgot <NavLink to="">password?</NavLink>
      </p>
    </form>
  );
}
export default Login;
