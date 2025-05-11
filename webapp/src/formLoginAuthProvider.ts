import { AuthProvider, UserIdentity } from "react-admin";

const LOGIN_URL = import.meta.env.VITE_LOGIN_URL as string;
const LOGOUT_URL = import.meta.env.VITE_LOGOUT_URL as string;

const USERNAME_KEY = 'amplicode-auth';

const authProvider: AuthProvider = {
    login: async function (params: any): Promise<any> {
        // sending request to authenticate user
        const request: Request = new Request(LOGIN_URL, {
            method: "POST",
            body: `username=${params.username}&password=${params.password}`,
            headers: new Headers({
                "Content-Type": "application/x-www-form-urlencoded",
            }),
            credentials: "include",
        });

        const response = await fetch(request);

        if (response.status === 200) {
            // storing username to indicate login
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
    getPermissions: function (): Promise<any> {
        // configure your custom permissions obtaining
        throw new Error("Function not implemented.");
    },

    getIdentity: async function (): Promise<UserIdentity> {
        // configure your custom user identity obtaining
        throw new Error("Function not implemented.");
    }
};

export default authProvider; 