import { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "./authContext";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login(email, password);
      navigate("/");
    } catch (err) {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#050505] font-sans text-gray-200 relative overflow-hidden">
      {/* 🌟 Creative Background Glows - Blue Theme */}
      <div className="absolute top-[-10%] left-[-10%] w-96 h-96 bg-blue-600/20 rounded-full blur-[120px] pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-96 h-96 bg-indigo-900/20 rounded-full blur-[120px] pointer-events-none" />

      {/* 🔮 Glass Card */}
      <div className="relative z-10 bg-[#121212]/80 backdrop-blur-xl p-8 md:p-10 rounded-2xl shadow-2xl w-full max-w-md border border-white/5 ring-1 ring-blue-500/20">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-indigo-500">
            Welcome Back
          </h2>
          <p className="text-sm text-gray-400 mt-2">
            Sign in to access your portfolio
          </p>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-500 p-3 rounded-lg text-sm text-center mb-6 animate-pulse">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold uppercase tracking-wider text-blue-500/80 pl-1">
              Email Address
            </label>
            <input
              type="email"
              required
              className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all duration-300 hover:border-[#444]"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="wallet@example.com"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold uppercase tracking-wider text-blue-500/80 pl-1">
              Password
            </label>
            <input
              type="password"
              required
              className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all duration-300 hover:border-[#444]"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            className="w-full mt-4 p-4 rounded-lg bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold tracking-wide hover:from-blue-500 hover:to-indigo-500 active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed transition-all duration-200 shadow-lg shadow-blue-900/40 border border-blue-500/20"
            disabled={loading}
          >
            {loading ? (
              <span className="flex items-center justify-center gap-2">
                <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                Signing In...
              </span>
            ) : (
              "SIGN IN"
            )}
          </button>
        </form>

        <p className="mt-8 text-center text-sm text-gray-500">
          Don't have an account?{" "}
          <Link
            to="/register"
            className="text-blue-400 hover:text-blue-300 font-semibold transition-colors underline decoration-transparent hover:decoration-blue-400 underline-offset-4"
          >
            Register here
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
