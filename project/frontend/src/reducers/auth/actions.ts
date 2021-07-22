// import { userService } from "../../service";
import {
  LOGIN_ERROR,
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGOUT,
} from "./constants";
import * as Type from '../../type'

export const loginSuccess = (data: Type.User) => {
  return {
    type: LOGIN_SUCCESS,
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

