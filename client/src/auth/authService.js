import { httpClient } from "../api/httpClient";

// This is the Mock Login your partner wrote (Keep this)
export const loginRequest = async (credentials) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        token: "mock-jwt-token",
        user: {
          id: 1,
          email: credentials.email,
          name: "Demo User",
        },
      });
    }, 800);
  });
};

// UPDATE THIS: Changed from a real fetch to a Mock Promise
export const registerRequest = async (data) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log("Mocking registration for:", data);
      resolve({
        message: "Registration successful",
        user: { id: 2, email: data.email, name: data.name },
      });
    }, 800);
  });
};
