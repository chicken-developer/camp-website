import Constant from "./Constant";

export function getLink(serverLocalURL: String) {
    const regex = /[ \w-]+\.[a-z]+/;

    const filepath =  serverLocalURL.match(regex)?.pop() as string;

    return Constant.HOST_URL + "/images/" + filepath;
}