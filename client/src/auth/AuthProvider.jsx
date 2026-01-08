import { useState, useEffect } from "react";
import { AuthContext } from "./authContext";
import authService from "./authService";

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(authService.getToken());
  const [isAuthenticated, setIsAuthenticated] = useState(!!token);
  // Note: Since the login endpoint only returns a token,
  // we derive 'user' existence from the token.
  // In a real app, you might decode the JWT or fetch /me here.
  const [user, setUser] = useState(token ? { token } : null);

  useEffect(() => {
    const storedToken = authService.getToken();
    if (storedToken) {
      setToken(storedToken);
      setIsAuthenticated(true);
      setUser({ token: storedToken });
    }
  }, []);

  const login = async (email, password) => {
    try {
      const data = await authService.login(email, password);
      setToken(data.token);
      setIsAuthenticated(true);
      setUser({ token: data.token });
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
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
