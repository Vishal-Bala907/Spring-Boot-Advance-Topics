import kc from "./keycloak"; // Correct path to the Keycloak instance
import { toast, Toaster } from "react-hot-toast";
import { createContext, useContext, useEffect, useState } from "react";

// Create Auth Context
const AuthCTX = createContext();

// Create Auth Provider
export default function AuthContext({ children }) {
  const [isAuthenticated, setAuthenticated] = useState(false);
  const [kcObject, setKcObject] = useState(null);

  useEffect(() => {
    // Initialize Keycloak only once
    kc.init({
      onLoad: "login-required",
      pkceMethod: "S256",
    })
      .then((authenticated) => {
        console.log("User authenticated:", authenticated);
        setAuthenticated(authenticated);
        setKcObject(kc); // Set the Keycloak object here
        console.log(kc);
      })
      .catch((err) => {
        toast.error("Authentication failed", {
          position: "top-center",
        });
        console.error("Keycloak error:", err);
      });
  }, []); // Ensure this effect runs only once on mount

  return (
    <AuthCTX.Provider
      value={{
        isAuthenticated,
        kc: kcObject, // Provide Keycloak object in context
      }}
    >
      {children}
      <Toaster />
    </AuthCTX.Provider>
  );
}

// Hook to use the Auth Context
export const useAuth = () => useContext(AuthCTX);
