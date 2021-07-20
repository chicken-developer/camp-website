import axios from "axios";

export default axios.create({
    baseURL: "http://103.153.65.194:54000/api_v01/", //change to vps ip for remote backend
    headers: {
        "Content-type": "application/json"
    }
});
