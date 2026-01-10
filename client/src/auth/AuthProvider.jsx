import { useState, useEffect } from "react";
import { AuthContext } from "./authContext";
import authService from "./authService";
import { httpClient } from "../api/httpClient";

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(authService.getToken());
  const [isAuthenticated, setIsAuthenticated] = useState(!!token);
  const [user, setUser] = useState(token ? { token } : null);
  const [loading, setLoading] = useState(true);

  const fetchUserInfo = async (authToken) => {
    try {
      const response = await httpClient.get("/user/me");
      const userData = response.data || response;
      setUser({ token: authToken || token, ...userData });
    } catch (error) {
      console.error("Failed to fetch user info:", error);
      // If fetch fails, still set user with token
      setUser({ token: authToken || token });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const storedToken = authService.getToken();
    if (storedToken) {
      setToken(storedToken);
      setIsAuthenticated(true);
      fetchUserInfo(storedToken);
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email, password) => {
    try {
      const data = await authService.login(email, password);
      setToken(data.token);
      setIsAuthenticated(true);
      // If user data is in response, use it, otherwise fetch
      if (data.user) {
        setUser({ 
          token: data.token,
          ...data.user
        });
      } else {
        await fetchUserInfo(data.token);
      }
      return data;
    } catch (error) {
      throw error;
    }
  };

  const register = async (userData) => {
    return await authService.register(userData);
  };

  const logout = () => {
    authService.logout();
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
  };

  const value = {
    token,
    user,
    isAuthenticated,
    loading,
    login,
    register,
    logout,
    refreshUser: fetchUserInfo,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
