import React, { useState, useEffect } from "react";
// 1. IMPORT the context from the file above (Do not create a new one!)
import { AuthContext } from "./authContext"; 
import { authService } from "./authService";

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [authError, setAuthError] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    const token = localStorage.getItem("token");

    if (storedUser && token) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        localStorage.removeItem("user");
      }
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    setAuthError(null);
    try {
      const data = await authService.login(username, password);
      
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data.user));
      
      setUser(data.user);
      return true;
    } catch (err) {
      setAuthError(err.message);
      throw err;
    }
  };

  const register = async (username, password, email) => {
    return await authService.register(username, password, email);
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const isAuthenticated = !!user;

  // 2. Provide the imported Context
  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated,
        login,
        register,
        logout,
        loading,
        authError,
      }}
    >
      {!loading && children}
    </AuthContext.Provider>
  );
};