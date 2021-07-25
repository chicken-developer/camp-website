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


export function getAllUser() {
  return axiosInstance.get<Model.ListUser>("/user/all");
}

export function editUser(userName: String, data: any) {
  return axiosInstance.put("/user/" + userName, data)
}

export function deleteUser(userName: String) {
  return axiosInstance.delete("/user/" + userName)
}

export function editCamp(campId: String, data: any) {
  return axiosInstance.put("/camp/" + campId, data)
}

export function deleteCamp(campId: String) {
  return axiosInstance.delete("/camp/" + campId)
}

export function createCamp(data) {
  return axiosInstance.post("/camp/", data)
}
