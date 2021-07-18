import http from "../../http-common";
import ICampData from "./types/Camp";

const getAll = () => {
    return http.get("/camp");
};

const get = (id: any) => {
    return http.get(`/camp/?campId=${id}`);
};

const create = (data: ICampData) => {
    return http.post("/camp", data);
};

const update = (id: any, data: ICampData) => {
    return http.put(`/camp`, data);
};

const remove = (id: any)=> {
    // @ts-ignore
    return http.delete(`/camp`, ICampData);
};

const removeAll = () => {
    return http.delete(`/camp`);
};

const CampService = {
    getAll,
    get,
    create,
    update,
    remove,
    removeAll,
};
export default CampService;