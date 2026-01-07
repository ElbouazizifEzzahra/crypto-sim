import React, { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
// 1. Correct Import: Use Context instead of direct Service
import { AuthContext } from "./authContext"; 

const Register = () => {
    // State for form inputs
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    
    // 2. Extract register function from the AuthProvider
    const { register } = useContext(AuthContext); 
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        
        try {
            // 3. Call the register function from context
            await register(formData.username, formData.password, formData.email);
            
            // Redirect to login after successful registration
            navigate("/login");
        } catch (err) {
            console.error(err);
            setError("Registration failed. Username might be taken.");
        }
    };

    return (
        <div className="flex flex-col items-center justify-center h-screen bg-gray-900 text-white">
            <div className="w-full max-w-md p-8 space-y-6 bg-gray-800 rounded-lg shadow-lg">
                <h2 className="text-2xl font-bold text-center text-green-400">Create Account</h2>
                
                {error && (
                    <div className="p-3 text-sm text-red-200 bg-red-900 rounded border border-red-700">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-400">Username</label>
                        <input
                            type="text"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            className="w-full px-4 py-2 mt-1 bg-gray-700 border border-gray-600 rounded focus:ring-green-500 focus:border-green-500 text-white"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-400">Email</label>
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            className="w-full px-4 py-2 mt-1 bg-gray-700 border border-gray-600 rounded focus:ring-green-500 focus:border-green-500 text-white"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-400">Password</label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            className="w-full px-4 py-2 mt-1 bg-gray-700 border border-gray-600 rounded focus:ring-green-500 focus:border-green-500 text-white"
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full px-4 py-2 font-bold text-gray-900 bg-green-500 rounded hover:bg-green-400 transition-colors"
                    >
                        Register
                    </button>
                </form>

                <p className="text-sm text-center text-gray-400">
                    Already have an account?{" "}
                    <Link to="/login" className="text-green-400 hover:underline">
                        Login here
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default Register;