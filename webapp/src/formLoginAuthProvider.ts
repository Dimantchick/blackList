import {AuthProvider, UserIdentity} from "react-admin";

const LOGIN_URL = import.meta.env.VITE_LOGIN_URL as string;
const USER_URL = '/rest/users/info';
const LOGOUT_URL = import.meta.env.VITE_LOGOUT_URL as string;

const USERNAME_KEY = 'amplicode-auth';

const authProvider: AuthProvider = {
    login: async function (params: any): Promise<any> {
        const formData = new URLSearchParams();
        formData.append('username', params.username);
        formData.append('password', params.password);

        const request = new Request(LOGIN_URL, {
            method: "POST",
            body: formData,
            headers: new Headers({
                "Content-Type": "application/x-www-form-urlencoded",
            }),
            credentials: "include",
        });

        const response = await fetch(request);
        if (response.status === 200) {
            localStorage.setItem(USERNAME_KEY, params.username);
        } else {
            throw new Error("Incorrect login or password");
        }
    },
    logout: async function (): Promise<string | false | void> {
        await fetch(LOGOUT_URL);
        localStorage.removeItem(USERNAME_KEY);
    },
    checkAuth: async function (): Promise<void> {
        // checking if user logged in
        if (!localStorage.getItem(USERNAME_KEY)) {
            return Promise.reject();
        }
    },
    checkError: async function (error: any): Promise<void> {
        // redirecting to login page if request is unauthorized
        if (error.status === 401) {
            localStorage.removeItem(USERNAME_KEY);
            return Promise.reject();
        }
    },
    getPermissions: (): Promise<string[]> => {
        // Для системы без RBAC возвращаем массив с "общим" разрешением
        return Promise.resolve(['all']);
    },

    getIdentity: async (): Promise<UserIdentity> => {
        try {
            const request = new Request(USER_URL, {
                method: "GET",
                credentials: "include",
            });
            const response = await fetch(request);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const userData = await response.json();
            return {
                id: userData.id.toString(),
                fullName: userData.username,
            };
        } catch (error) {
            console.error('Failed to fetch user identity:', error);
            return {
                id: 'anonymous',
                fullName: 'Anonymous',
            };
        }
    }
};

export default authProvider; 
