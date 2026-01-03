import React, { useState } from "react";
import { registerRequest } from "./authService";

const Register = ({ onSwitch }) => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [status, setStatus] = useState({ type: "", msg: "" });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await registerRequest(formData);
      setStatus({ type: "success", msg: "Account created! Please log in." });
      setTimeout(onSwitch, 2000);
    } catch (err) {
      setStatus({ type: "error", msg: err.message });
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-black p-4">
      <div className="bg-gray-900 p-8 rounded-lg border border-gray-800 w-full max-w-md shadow-2xl">
        <h2 className="text-2xl font-bold text-white mb-6 text-center text-blue-400">
          Create Account
        </h2>

        {status.msg && (
          <div
            className={`p-3 rounded mb-4 text-sm border ${
              status.type === "success"
                ? "bg-green-500/20 border-green-500 text-green-500"
                : "bg-red-500/20 border-red-500 text-red-500"
            }`}
          >
            {status.msg}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Full Name"
            required
            className="w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500"
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <input
            type="email"
            placeholder="Email"
            required
            className="w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500"
            onChange={(e) =>
              setFormData({ ...formData, email: e.target.value })
            }
          />
          <input
            type="password"
            placeholder="Password"
            required
            className="w-full p-3 bg-black border border-gray-700 rounded text-white outline-none focus:border-blue-500"
            onChange={(e) =>
              setFormData({ ...formData, password: e.target.value })
            }
          />
          <button
            type="submit"
            className="w-full py-3 bg-green-600 hover:bg-green-700 text-white font-bold rounded transition"
          >
            Register
          </button>
        </form>

        <button
          onClick={onSwitch}
          className="w-full mt-6 text-center text-gray-400 text-sm hover:text-white"
        >
          Already have an account?{" "}
          <span className="text-blue-400">Sign In</span>
        </button>
      </div>
    </div>
  );
};

export default Register; // <--- THIS LINE IS THE FIX
