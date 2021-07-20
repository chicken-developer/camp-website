import {
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGIN_ERROR,
  LOGOUT,
} from "./constants";

const initialState = {
  requesting: false,
  successful: false,
  messages: {},
  errors: false,
  loading: false,
  isLogin: false,
};

const authReducer = function loginReducer(
  state = { ...initialState },
  action: any
) {
  switch (action.type) {
    case LOGIN_REQUESTING:
      return {
        requesting: true,
        successful: false,
        loading: true,
        messages: "",
        errors: false,
        isLogin: false,
      };
    case LOGIN_SUCCESS:
      return {
        ...action,
        isLogin: true,
        requesting: false,
        loading: false,
        errors: false,
        successful: true,
      };
    case LOGOUT: {
      return {};
    }
    case LOGIN_ERROR:
      return {
        messages: "Please check your password or account",
        loading: false,
        requesting: false,
        successful: false,
        errors: true,
      };

    default:
      return state;
  }
};

export default authReducer;
