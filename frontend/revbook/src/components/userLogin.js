import React, { useState } from 'react';
import axios from 'axios';

const UserLogin = () => {

    const [formData, setFormData] = useState({
        email: "",
        password: ""
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const submitHandler = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post("http://localhost:8080/login", formData, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log(response.data);
        } catch(error){
            console.error("Some error ", error);
        }
    };

    return(
        <>
        <h1>Login Form</h1>
        <form onSubmit={submitHandler}>
            <label htmlFor="email">Email </label>
            <input 
                type="email" 
                name="email"
                value={formData.email}
                onChange={handleInputChange}
            /><br/>
            <label htmlFor="password">Password </label>
            <input 
                type="password" 
                name="password"
                value={formData.password}
                onChange={handleInputChange}
            /><br/>
            <br/>
            <button type="submit">Login</button>
        </form>
        </>
    );

}

export default UserLogin;