import axios from "axios";
import kc from "../config/keycloak";

export const AXIOS = axios.create({
  baseURL: "http://localhost:8080/api/v1",
});

export const getAllProducts = async () => {
  // console.log(kc.token);
  const prods = await AXIOS.get("/products/get-all", {
    headers: {
      Authorization: `Bearer ${kc.token}`,
    },
  });
  return prods.data;
};
