// import { userService } from "../../service";
import {
  LOGIN_ERROR,
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGOUT,
  UPDATE_PROFILE_SUCCESS,
} from "./constants";
import * as Type from '../../type'
import * as action from '../../reducers/auth/reducer'

export const loginSuccess = (data: Type.User) => {
  return {
    type: LOGIN_SUCCESS,
    data,
  };
};

export const updateSuccess = (data: Type.User) => {
  return {
    type: UPDATE_PROFILE_SUCCESS,
    data,
  };
};

export const loginHasError = (err: any) => {
  return {
    type: LOGIN_ERROR,
    err,
  };
};

export const loginIsLoading = () => {
  return {
    type: LOGIN_REQUESTING,
  };
};

export const logoutAction = () => {
  return {
    type: LOGOUT
  }
}

