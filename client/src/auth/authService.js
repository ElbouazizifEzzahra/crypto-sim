import { httpClient } from "../api/httpClient";

const login = async (email, password) => {
  try {
    // Note: ensure this matches your backend endpoint
    const response = await httpClient.post("/auth/login", { email, password });
    if (response.data.token) {
      localStorage.setItem("jwt_token", response.data.token);
    }
    return response.data;
  } catch (error) {
    throw error;
  }
};

const register = async (userData) => {
  try {
    const response = await httpClient.post("/user/register", userData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

const logout = () => {
  localStorage.removeItem("jwt_token");
};

const getToken = () => {
  return localStorage.getItem("jwt_token");
};

// Define the service object
const authService = {
  login,
  register,
  logout,
  getToken, // <--- This was likely missing or undefined before
};

// EXPORT BOTH WAYS (Fixes the crash)
export const AuthService = authService;
export default authService;
