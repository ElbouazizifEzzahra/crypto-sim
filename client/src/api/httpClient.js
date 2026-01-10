// src/api/httpClient.js

// Use Vite proxy in Dev, gateway path in Prod
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

    let data = {};
    try {
      const text = await response.text();
      data = text ? JSON.parse(text) : {};
    } catch (parseError) {
      console.warn("Failed to parse response as JSON:", parseError);
      data = { message: `HTTP ${response.status}: ${response.statusText}` };
    }

    if (!response.ok) {
      // Preserve the response data and status
      const error = new Error(data.message || `HTTP Error ${response.status}`);
      error.data = data;
      error.status = response.status;
      throw error;
    }

    return { data };
  } catch (error) {
    // If it's already our custom error, rethrow it
    if (error.data || error.status) {
      throw error;
    }
    // Otherwise, wrap it
    console.error("API Error:", error);
    throw { message: error.message || "Network error occurred", originalError: error };
  }
};

const client = {
  get: (url) => request(url, "GET"),
  post: (url, body) => request(url, "POST", body),
  put: (url, body) => request(url, "PUT", body),
  delete: (url) => request(url, "DELETE"),
};

export const httpClient = client;
export default client;
