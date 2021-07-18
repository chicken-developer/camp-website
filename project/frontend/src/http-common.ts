import axios from "axios";

export default axios.create({
    baseURL: "http://192.168.220.129:54000/api_v01/",
    headers: {
        "Content-type": "application/json"
    }
});
