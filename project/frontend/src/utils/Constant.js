const HOST_URL = "http://103.153.65.194:54000/api_v01"
const ENDPOINT = {
    login: `${HOST_URL}/user/login`,
    register: `${HOST_URL}/user/register`,
}

const KEY = {
    USER: "user@data",
}

export default {
    API: ENDPOINT,
    HOST_URL,
    KEY
}