// This file handles all HTTP requests
const BASE_URL = 'http://localhost/api'; // Going through Nginx Gateway

const getHeaders = () => {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        // If we have a token, attach it!
        ...(token && { 'Authorization': `Bearer ${token}` }) 
    };
};

// Generic Request Handler
const request = async (endpoint, method = 'GET', body = null) => {
    const config = {
        method,
        headers: getHeaders(),
        ...(body && { body: JSON.stringify(body) })
    };

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, config);
        
        // Handle 401 (Token Expired) -> Force Logout
        if (response.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
            return null;
        }

        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Something went wrong');
        
        return data;
    } catch (error) {
        console.error("API Error:", error);
        throw error;
    }
};

export const httpClient = {
    get: (url) => request(url, 'GET'),
    post: (url, body) => request(url, 'POST', body),
    put: (url, body) => request(url, 'PUT', body),
    delete: (url) => request(url, 'DELETE'),
};