import { createContext, useState, useContext } from "react";

const UserContext = createContext();

export const UserProvider = ({children}) => {

    const [user, setUser] = useState(null);

    const updateUser = (newUser) => {
        setUser(newUser);  
    }

    const clearUser = () => {
        setUser(null);
    }

    return (
        <UserContext.Provider value ={{ user, updateUser, clearUser }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => {
    return useContext(UserContext);
};