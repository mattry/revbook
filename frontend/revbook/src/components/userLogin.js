import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UserLogin = () => {

    const navigate = useNavigate(); 

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

            // set user information in session storage
            sessionStorage.setItem("user", JSON.stringify(response.data));

            // since we are logging in from inside the landing, refresh to show logged-in content
            window.location.reload();

        } catch(error){
            console.error("Some error: ", error);
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