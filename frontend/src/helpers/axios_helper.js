import axios from 'axios';


export const getAuthToken = () => {
    return window.localStorage.getItem('auth_token');
};

export const setAuthHeader = (token) => {
    window.localStorage.setItem('auth_token', token);
};

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export const request = async (method, url, data) => {
    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = { 'Authorization': `Bearer ${getAuthToken()}` };
    }

    try {
        const response = await axios({
            method: method,
            url: url,
            headers: headers,
            data: data
        });

        return response;
    } catch (error) {
        if (error.response && error.response.status === 401) {
            // Обработка ошибки 401 (Unauthorized)
            // Сброс токена
            setAuthHeader(null);
            window.location.href = '/';
        } else {
            throw error; // Прокидываем другие ошибки дальше
        }
    }
};