import { userService } from "../../service";
import {
  LOGIN_ERROR,
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGOUT,
} from "./constants";

const success = (data: any) => {
  return {
    type: LOGIN_SUCCESS,
    data,
  };
};

const loginHasError = (err: any) => {
  return {
    type: LOGIN_ERROR,
    err,
  };
};

const loginIsLoading = () => {
  return {
    type: LOGIN_REQUESTING,
  };
};

const login = (email: string, password: string) => {
  return (dispatch: any) => {
    dispatch(loginIsLoading());
    userService
      .login(email, password)
      .then((data: any) => dispatch(success(data)))
      .catch((err) => dispatch(loginHasError(err)));
  };
};

function logout() {
  userService.logout();
  return { type: LOGOUT };
}

export const userActions = {
  login,
  logout,
};
