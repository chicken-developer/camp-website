import React, {useState, useEffect, Component} from "react";
import { NavLink } from "react-router-dom";
import * as API from '../../service';
import {toastSuccess, toastFailure} from "../../utils/toast_mixin"
import "./Register.css"
interface IRegisterProps {

}
interface IRegisterState {
  firstName: String,
  lastName: String,
  username: String,
  password: String,
  phoneNumber: String,
  email: String
}
export class Register extends Component<IRegisterProps, IRegisterState> {

  constructor(props: any) {
    super(props);

    this.state = {
      firstName: "",
      lastName: "",
      username: "",
      password: "",
      phoneNumber: "",
      email: ""
    }
  }

  handleInputChange = (evt: any) => {
    const {name, value} = evt.target;

    this.setState({
      [name]: value
    }as Pick<IRegisterState, keyof IRegisterState>)
  }

  onSignUp = (evt: any) => {
    const {firstName, lastName, username, password, phoneNumber, email} = this.state;
    
    API.register({
      firstName,
      lastName,
      username,
      password,
      phoneNumber,
      email
    }).then(userResponse => {
      const userData = userResponse.data;
      if (userData.status) {
        toastSuccess("Sign Up Ok")
      } else {
        toastFailure("Sign Up Error")
      }
    })
    .catch(error => {
      console.log(error);
      toastFailure("Sign Up Error")
    })
  }


  render() {
    return (
      <div className = "page-register">
        <form>
          <h3>Sign Up</h3>
            <div className="form-group">
                <label>Username</label>
                <input
                    type="text"
                    className="form-control"
                    placeholder="Enter username"
                    onChange = {this.handleInputChange}
                    name = "username"
                />
            </div>
          <div className="form-group">
            <label>First name</label>
            <input 
              type="text" 
              className="form-control"
              placeholder="First name" 
              onChange = {this.handleInputChange} 
              name = "firstName"/>
          </div>
    
          <div className="form-group">
            <label>Last name</label>
            <input 
              type="text" 
              className="form-control" 
              placeholder="Last name"
              onChange = {this.handleInputChange} 
              name = "lastName" />
          </div>
    
          <div className="form-group">
            <label>Email address</label>
            <input
              type="email"
              className="form-control"
              placeholder="Enter email"
              onChange = {this.handleInputChange} 
              name = "email"
            />
          </div>
    
          <div className="form-group">
            <label>Phone number</label>
            <input
              type="phone"
              className="form-control"
              placeholder="Enter phone number"
              onChange = {this.handleInputChange} 
              name = "phoneNumber"
            />
          </div>
    
          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="Enter password"
              onChange = {this.handleInputChange} 
              name = "password"
            />
          </div>
    
          <button type="button" className="btn btn-primary btn-block"
            onClick = {this.onSignUp}
          >
            Sign Up
          </button>
    
          <p className="forgot-password text-right">
            Already registered <NavLink to="/sign-in">sign in?</NavLink>
          </p>
        </form>
      </div>

    );
  }
}


export default Register;
