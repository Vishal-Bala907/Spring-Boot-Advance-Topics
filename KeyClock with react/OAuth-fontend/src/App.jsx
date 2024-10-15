import { useContext, useEffect } from "react";
import "./App.css";
import { useAuth } from "./config/AuthContext";
import toast, { Toaster } from "react-hot-toast";
import { getAllProducts } from "./services/api.services";

function App() {
  const { isAuthenticated } = useAuth();
  const { kc } = useAuth();
  // console.log(isAuthenticated);
  useEffect(() => {
    if (isAuthenticated) {
      loadProds();
    }
  }, [isAuthenticated]);
  const loadProds = async () => {
    alert("fetching");
    const data = await getAllProducts();
    console.log(data);
  };

  return (
    <>
      {isAuthenticated ? (
        <div>
          <h1>Welcome {kc.tokenParsed.name}, you are logged in!</h1>
          <button
            onClick={() => {
              kc.logout();
              toast.success("Logged out !!!");
            }}
          >
            Logout
          </button>
        </div>
      ) : (
        <div>
          <h1>Login required</h1>
          <button
            onClick={() => {
              kc.login();
            }}
          >
            Login
          </button>
        </div>
      )}
      <Toaster />
    </>
  );
}

export default App;
