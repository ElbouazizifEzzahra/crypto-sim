import { httpClient } from '../api/httpClient';

export const authService = {
    // Keeps "Username" logic (Backend requirement)
    login: async (username, password) => {
        try {
            const response = await httpClient.post('/auth/login', { username, password });
            
            // Backend only returns token, so we return it plus the username we just sent
            return { 
                token: response.token, 
                user: { username: username } // We construct the user manually
            };
        } catch (error) {
            // Standardize error message for the UI
            throw new Error(error.response?.data?.message || "Invalid credentials or Server Error");
        }
    },

    register: async (username, password, email) => {
        // Keeps "/user/register" endpoint
        return await httpClient.post('/user/register', { username, password, email });
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
};