// keycloak.js
import Keycloak from "keycloak-js";

// Initialize Keycloak instance only once
const kc = new Keycloak({
  url: "http://localhost:8182",
  realm: "yt-dev",
  clientId: "auth-client",
  pkceMethod: "S256",
});

export default kc;
