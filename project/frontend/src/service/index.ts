import axios from "axios";
import CONSTANTS from '../utils/Constant'
import * as Model from '../type'
import * as Utils from "../utils/Utils";

const axiosInstance = axios.create({
  baseURL: CONSTANTS.HOST_URL
});


export function getHistory(username: String) {
  return axiosInstance.get("/history/" + username);

}
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
  return axiosInstance.put("/user/update/" + userName, data)
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
  return axiosInstance.post("/camp/null", data)
}
export function booking(username, data) {
  return axiosInstance.post("/booking/" + username, data)
}

export function fullHistory() {
  return axiosInstance.get("/userfull")
}

export function uploadFile(data) {
  const formData = Utils.getFormData(data);
  return axiosInstance({
    method: 'post',
    url: '/upload',
    data: formData,
    headers: { "Content-Type": "multipart/form-data" },
  })
}
  
