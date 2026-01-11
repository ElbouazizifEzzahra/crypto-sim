import { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "./authContext";

const Register = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { register } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await register(formData);
      navigate("/login");
    } catch (err) {
      const msg = err.message || "Registration failed. Try again.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#050505] font-sans text-gray-200 relative overflow-hidden">
      {/* 🌟 Creative Background Glows */}
      <div className="absolute top-[-10%] left-[-10%] w-96 h-96 bg-emerald-600/20 rounded-full blur-[120px] pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-96 h-96 bg-green-900/20 rounded-full blur-[120px] pointer-events-none" />

      {/* 🔮 Glass Card */}
      <div className="relative z-10 bg-[#121212]/80 backdrop-blur-xl p-8 md:p-10 rounded-2xl shadow-2xl w-full max-w-md border border-white/5 ring-1 ring-emerald-500/20">
        {/* Logo */}
        <div className="flex justify-center mb-6">
          <img
            src="/assets/logo.png"
            alt="Crypto Sim Logo"
            className="h-16 w-auto object-contain"
          />
        </div>

        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 to-green-500">
            Create Account
          </h2>
          <p className="text-sm text-gray-400 mt-2">
            Initialize your crypto portfolio
          </p>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-500 p-3 rounded-lg text-sm text-center mb-6 animate-pulse">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <div className="grid grid-cols-2 gap-4">
            <div className="flex flex-col gap-1.5">
              <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                First Name
              </label>
              <input
                name="firstName"
                required
                className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all duration-300 hover:border-[#444]"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="AHMED YASSINE"
              />
            </div>
            <div className="flex flex-col gap-1.5">
              <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                Last Name
              </label>
              <input
                name="lastName"
                required
                className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all duration-300 hover:border-[#444]"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="HANCHOUCH"
              />
            </div>
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
              Email Address
            </label>
            <input
              name="email"
              type="email"
              required
              className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all duration-300 hover:border-[#444]"
              value={formData.email}
              onChange={handleChange}
              placeholder="wallet@example.com"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
              Password
            </label>
            <input
              name="password"
              type="password"
              required
              className="w-full p-3 rounded-lg border border-[#333] bg-[#1a1a1a] text-white placeholder-gray-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all duration-300 hover:border-[#444]"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            className="w-full mt-4 p-4 rounded-lg bg-gradient-to-r from-emerald-600 to-green-600 text-white font-bold tracking-wide hover:from-emerald-500 hover:to-green-500 active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed transition-all duration-200 shadow-lg shadow-emerald-900/40 border border-emerald-500/20"
            disabled={loading}
          >
            {loading ? (
              <span className="flex items-center justify-center gap-2">
                <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                Mining ID...
              </span>
            ) : (
              "JOIN NOW"
            )}
          </button>
        </form>

        <p className="mt-8 text-center text-sm text-gray-500">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-emerald-400 hover:text-emerald-300 font-semibold transition-colors underline decoration-transparent hover:decoration-emerald-400 underline-offset-4"
          >
            Sign in here
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Register;
