import { combineReducers } from "redux";
import authReducer from "./auth/reducer";

export default combineReducers({
  // error: errorReducer,
  auth: authReducer,
});
