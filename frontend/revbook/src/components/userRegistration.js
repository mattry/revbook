import React, { useState } from 'react';
import axios from 'axios';

const UserRegistration = () => {

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        
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
            const response = await axios.post("http://localhost:8080/register", formData, {
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
        <h1>Registration Form</h1>
        <form onSubmit={submitHandler}>
            <label for="firstName">First Name </label>
            <input 
                type="text" 
                name="firstName" 
                value={formData.firstName}
                onChange={handleInputChange}
            /><br/>
            <label for="lastName">Last Name </label>
            <input 
                type="text" 
                name="lastName"
                value={formData.lastName}
                onChange={handleInputChange}
            /><br/>
            <label for="email">Email </label>
            <input 
                type="email" 
                name="email"
                value={formData.email}
                onChange={handleInputChange}
            /><br/>
            <label for="password">Password </label>
            <input 
                type="password" 
                name="password"
                value={formData.password}
                onChange={handleInputChange}
            /><br/>
            <br/>
            <button type="submit">Register</button>
        </form>
        </>
    );
};

export default UserRegistration;