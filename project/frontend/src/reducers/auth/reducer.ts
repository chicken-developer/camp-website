import {
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGIN_ERROR,
  LOGOUT,
  UPDATE_PROFILE_SUCCESS,
} from "./constants";

const initialState = {
  userData: {}
};

const authReducer = function loginReducer(
  state = initialState,
  action: any
) {
  const nextData = action.data
  let newState = { ...state }

  switch (action.type) {
    case LOGIN_SUCCESS:
      newState.userData = nextData;
      break;
    case UPDATE_PROFILE_SUCCESS:
      newState.userData = nextData;
      break;
    case LOGOUT:
      newState.userData = {}
      break;

    default:
      newState = state;
      break;
  }

  return newState;
};

export default authReducer;
