import { createContext, useContext } from "react";

// 1. Create the Context Object ONCE
export const AuthContext = createContext(null);

// 2. Export a helper hook for easy access
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    // Safety check to prevent the "null" crash in the future
    return { user: null, isAuthenticated: false, loading: true, login: () => {}, logout: () => {} };
  }
  return context;
};