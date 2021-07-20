import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { NavLink } from "react-router-dom";
import { userActions } from "../../reducers/auth/actions";

function Login() {
  const dispatch = useDispatch();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = (e: any) => {
    e.preventDefault();
    dispatch(userActions.login(email, password));
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
        <label>Email address</label>
        <input
          type="email"
          className="form-control"
          placeholder="Enter email"
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
        <div className="custom-control custom-checkbox">
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
