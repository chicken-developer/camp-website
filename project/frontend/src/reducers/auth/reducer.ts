import {
  LOGIN_REQUESTING,
  LOGIN_SUCCESS,
  LOGIN_ERROR,
  LOGOUT,
} from "./constants";

const initialState = {
  userData: {}
};

const authReducer = function loginReducer(
  state = initialState,
  action: any
) {
  const nextData = action.data
  let newState = {...state}

  switch (action.type) {
    case LOGIN_SUCCESS:
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
