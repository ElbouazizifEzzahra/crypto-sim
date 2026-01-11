import { useState, useContext, useEffect } from "react";
import { X } from "lucide-react";
import { AuthContext } from "../auth/authContext";
import { httpClient } from "../api/httpClient";

const SettingsModal = ({ isOpen, onClose }) => {
  const { user, refreshUser } = useContext(AuthContext);
  const [activeTab, setActiveTab] = useState("profile");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // Profile form
  const [profileData, setProfileData] = useState({
    firstName: "",
    lastName: "",
    email: "",
  });

  // Password form
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  useEffect(() => {
    if (isOpen && user) {
      setProfileData({
        firstName: user.firstName || "",
        lastName: user.lastName || "",
        email: user.email || "",
      });
    }
  }, [isOpen, user]);

  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setLoading(true);

    try {
      await httpClient.put("/user/me", profileData);
      setSuccess("Profile updated successfully!");
      await refreshUser();
      setTimeout(() => {
        setSuccess("");
      }, 3000);
    } catch (err) {
      setError(err.data?.message || err.message || "Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    // Validation
    if (!passwordData.currentPassword) {
      setError("Current password is required");
      return;
    }

    if (!passwordData.newPassword) {
      setError("New password is required");
      return;
    }

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setError("New passwords do not match. Please make sure both fields are identical.");
      return;
    }

    if (passwordData.newPassword.length < 8) {
      setError("New password must be at least 8 characters long");
      return;
    }

    if (passwordData.currentPassword === passwordData.newPassword) {
      setError("New password must be different from your current password");
      return;
    }

    setLoading(true);

    try {
      await httpClient.post("/user/change-password", {
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
      });
      setSuccess("Password changed successfully!");
      setPasswordData({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      });
      setTimeout(() => {
        setSuccess("");
      }, 3000);
    } catch (err) {
      // Extract detailed error message
      let errorMessage = "Failed to change password";
      
      if (err.data?.message) {
        errorMessage = err.data.message;
      } else if (err.message) {
        errorMessage = err.message;
      }
      
      // Map common error messages to user-friendly ones
      if (errorMessage.toLowerCase().includes("incorrect") || 
          errorMessage.toLowerCase().includes("wrong") ||
          errorMessage.toLowerCase().includes("mot de passe actuel")) {
        errorMessage = "Current password is incorrect. Please check and try again.";
      } else if (errorMessage.toLowerCase().includes("8")) {
        errorMessage = "Password must be at least 8 characters long.";
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-[#13151b] rounded-2xl border border-gray-800 shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-[#13151b] border-b border-gray-800 p-6 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 to-green-500">
            Settings
          </h2>
          <button
            onClick={onClose}
            className="p-2 text-gray-500 hover:text-white hover:bg-gray-800 rounded-lg transition-all"
          >
            <X size={20} />
          </button>
        </div>

        {/* Tabs */}
        <div className="flex border-b border-gray-800">
          <button
            onClick={() => {
              setActiveTab("profile");
              setError("");
              setSuccess("");
            }}
            className={`flex-1 px-6 py-4 text-sm font-semibold transition-all ${
              activeTab === "profile"
                ? "text-emerald-400 border-b-2 border-emerald-400"
                : "text-gray-500 hover:text-gray-300"
            }`}
          >
            Profile
          </button>
          <button
            onClick={() => {
              setActiveTab("password");
              setError("");
              setSuccess("");
            }}
            className={`flex-1 px-6 py-4 text-sm font-semibold transition-all ${
              activeTab === "password"
                ? "text-emerald-400 border-b-2 border-emerald-400"
                : "text-gray-500 hover:text-gray-300"
            }`}
          >
            Password
          </button>
        </div>

        {/* Content */}
        <div className="p-6">
          {error && (
            <div className="bg-red-500/10 border border-red-500/20 text-red-500 p-3 rounded-lg text-sm mb-4">
              {error}
            </div>
          )}

          {success && (
            <div className="bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 p-3 rounded-lg text-sm mb-4">
              {success}
            </div>
          )}

          {activeTab === "profile" && (
            <form onSubmit={handleProfileUpdate} className="space-y-5">
              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                    First Name
                  </label>
                  <input
                    type="text"
                    required
                    className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                    value={profileData.firstName}
                    onChange={(e) =>
                      setProfileData({ ...profileData, firstName: e.target.value })
                    }
                  />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                    Last Name
                  </label>
                  <input
                    type="text"
                    required
                    className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                    value={profileData.lastName}
                    onChange={(e) =>
                      setProfileData({ ...profileData, lastName: e.target.value })
                    }
                  />
                </div>
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                  Email Address
                </label>
                <input
                  type="email"
                  required
                  className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                  value={profileData.email}
                  onChange={(e) =>
                    setProfileData({ ...profileData, email: e.target.value })
                  }
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full mt-6 p-4 rounded-lg bg-gradient-to-r from-emerald-600 to-green-600 text-white font-bold tracking-wide hover:from-emerald-500 hover:to-green-500 active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed transition-all duration-200 shadow-lg shadow-emerald-900/40"
              >
                {loading ? "Updating..." : "UPDATE PROFILE"}
              </button>
            </form>
          )}

          {activeTab === "password" && (
            <form onSubmit={handlePasswordChange} className="space-y-5">
              <div className="flex flex-col gap-1.5">
                <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                  Current Password
                </label>
                <input
                  type="password"
                  required
                  className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                  value={passwordData.currentPassword}
                  onChange={(e) =>
                    setPasswordData({
                      ...passwordData,
                      currentPassword: e.target.value,
                    })
                  }
                />
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                  New Password
                </label>
                <input
                  type="password"
                  required
                  className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                  value={passwordData.newPassword}
                  onChange={(e) =>
                    setPasswordData({
                      ...passwordData,
                      newPassword: e.target.value,
                    })
                  }
                />
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-[11px] font-bold uppercase tracking-wider text-emerald-500/80 pl-1">
                  Confirm New Password
                </label>
                <input
                  type="password"
                  required
                  className="w-full p-3 rounded-lg border border-gray-700 bg-[#1a1a1a] text-white focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                  value={passwordData.confirmPassword}
                  onChange={(e) =>
                    setPasswordData({
                      ...passwordData,
                      confirmPassword: e.target.value,
                    })
                  }
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full mt-6 p-4 rounded-lg bg-gradient-to-r from-emerald-600 to-green-600 text-white font-bold tracking-wide hover:from-emerald-500 hover:to-green-500 active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed transition-all duration-200 shadow-lg shadow-emerald-900/40"
              >
                {loading ? "Changing..." : "CHANGE PASSWORD"}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default SettingsModal;
