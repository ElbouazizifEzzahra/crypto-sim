import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Use this instead of onSwitch
import { useAuth } from "./authContext";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login, authError } = useAuth();
  const navigate = useNavigate(); // Initialize navigate

  const handleSubmit = async (e) => {
    e.preventDefault();
    await login({ email, password });
    // Note: Redirection to /dashboard happens automatically in App.jsx via isAuthenticated
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-black p-4">
      <div className="bg-gray-900 p-8 rounded-lg border border-gray-800 w-full max-w-md shadow-2xl">
        <h2 className="text-2xl font-bold text-white mb-6 text-center text-blue-400">
          Sign In
        </h2>

        {authError && (
          <div className="bg-red-500/20 border border-red-500 text-red-500 p-3 rounded mb-4 text-sm">
            {authError}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="email"
            placeholder="Email"
            required
            className="w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500"
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            required
            className="w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500"
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            type="submit"
            className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded transition"
          >
            Sign In
          </button>
        </form>

        {/* Use navigate() here */}
        <button
          onClick={() => navigate("/register")}
          className="w-full mt-6 text-center text-gray-400 text-sm hover:text-white"
        >
          Need an account? <span className="text-blue-400">Register</span>
        </button>
      </div>
    </div>
  );
};

export default Login;
