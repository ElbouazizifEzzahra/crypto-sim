import React, { useState } from "react"; // Removed useContext import
import { useNavigate } from "react-router-dom";
// FIX: Import the HOOK from authContext, not the Provider
import { useAuth } from "./authContext"; 

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  
  // FIX: Use the hook
  const { login, authError } = useAuth(); 
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      await login(username, password);
      // App.jsx will handle redirect
    } catch (err) {
      console.error("Login failed", err);
    } finally {
      setIsLoading(false);
    }
  };

  const inputStyle = "w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500 transition-all";

  return (
    <div className="min-h-screen flex items-center justify-center bg-black p-4">
      <div className="bg-gray-900 p-8 rounded-lg border border-gray-800 w-full max-w-md shadow-2xl">
        <h2 className="text-2xl font-bold text-white mb-6 text-center text-blue-400">
          Sign In
        </h2>

        {authError && (
          <div className="bg-rose-500/20 border border-rose-500 text-rose-500 p-3 rounded mb-4 text-sm text-center animate-pulse">
            {authError}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Username" 
            required
            className={inputStyle}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            disabled={isLoading}
          />
          <input
            type="password"
            placeholder="Password"
            required
            className={inputStyle}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={isLoading}
          />
          <button
            type="submit"
            disabled={isLoading}
            className={`w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded transition flex justify-center items-center ${isLoading ? "opacity-70 cursor-not-allowed" : ""}`}
          >
            {isLoading ? "Authenticating..." : "Sign In"}
          </button>
        </form>

        <button onClick={() => navigate("/register")} className="w-full mt-6 text-center text-gray-400 text-sm hover:text-white" disabled={isLoading}>
          Need an account? <span className="text-blue-400">Register</span>
        </button>
      </div>
    </div>
  );
};

export default Login;