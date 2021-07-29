import Constant from "./Constant";

export function getLink(serverLocalURL: String) {
    const regex = /[ \w-]+\.[a-z]+/;

    const filepath =  serverLocalURL.match(regex)?.pop() as string;

    return Constant.HOST_URL + "/images/" + filepath;
}

export function getFormData(object) {
    const formData = new FormData();
    Object.keys(object).forEach(key => formData.append(key, object[key]));
    return formData;
}