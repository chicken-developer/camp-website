import axios from "axios";

export const userService = {
  login,
  logout,
  register,
};
const apiUrl = "http://103.153.65.194:54000/api_v01";

function login(username: string, password: string) {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  };

  return axios
    .post(`${apiUrl}/user/login`, requestOptions)
    .then((user) => {
      localStorage.setItem("user", JSON.stringify(user));
      return user;
    })
    .catch((err) => {
      return err;
    });
}

function logout() {
  localStorage.removeItem("user");
}

function register(user: any) {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(user),
  };

  return axios
    .post(`${apiUrl}/user/register`, requestOptions)
    .then((user) => {
      localStorage.setItem("user", JSON.stringify(user));
      return user;
    })
    .catch((err) => {
      return err;
    });
}
