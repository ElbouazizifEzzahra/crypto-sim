// src/api/httpClient.js

// Use Vite proxy in Dev, relative path in Prod
const BASE_URL = "/api";

const getHeaders = () => {
  const token = localStorage.getItem("jwt_token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

const request = async (endpoint, method = "GET", body = null) => {
  const config = {
    method,
    headers: getHeaders(),
    ...(body && { body: JSON.stringify(body) }),
  };

  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, config);

    if (response.status === 401) {
      localStorage.removeItem("jwt_token");
      if (!window.location.pathname.includes("/login")) {
        window.location.href = "/login";
      }
      return Promise.reject({ message: "Session expired" });
    }

    const text = await response.text();
    const data = text ? JSON.parse(text) : {};

    if (!response.ok) {
      throw data || { message: `HTTP Error ${response.status}` };
    }

    return { data };
  } catch (error) {
    console.error("API Error:", error);
    throw error;
  }
};

// Define the client object
const client = {
  get: (url) => request(url, "GET"),
  post: (url, body) => request(url, "POST", body),
  put: (url, body) => request(url, "PUT", body),
  delete: (url) => request(url, "DELETE"),
};

// EXPORT BOTH WAYS to satisfy all files:
export const httpClient = client; // Named export (Fixes PortfolioContext error)
export default client; // Default export (Fixes authService)
