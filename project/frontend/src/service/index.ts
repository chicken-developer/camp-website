import axios from "axios";
import CONSTANTS from '../utils/Constant'
import * as Model from '../type'

const axiosInstance = axios.create({
  baseURL: CONSTANTS.HOST_URL
});

export function login(data: Object) {
  return axiosInstance.post<Model.SingleUser>("/user/login", data)
}

export function register(data: Object) {
  return axiosInstance.post<Model.SingleUser>("/user/register", data)
}

export function getListCamp() {
  return axiosInstance.get<Model.ListCamp>("/home");
}

export function getDetailCamp(campId: String) {
  return axiosInstance.get<Model.CampResponse>("/camp/" + campId);
}
