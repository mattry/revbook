import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../UserContext'

axios.defaults.withCredentials = true;

const UserRegistration = () => {

    const navigate = useNavigate();

    const { updateUser } = useUser();

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

            // set user information into context
            updateUser(response.data);

            // we should be able to navigate to / and see the user's landing page
            navigate("/");

        } catch(error){
            console.error("Some error: ", error);
        }
    };

    return(
        <>
            <form onSubmit={submitHandler}>
                <label htmlFor="firstName">First Name </label>
                <input 
                    placeholder="John"
                    type="text" 
                    name="firstName" 
                    value={formData.firstName}
                    onChange={handleInputChange}
                /><br/>
                <label htmlFor="lastName">Last Name </label>
                <input 
                    placeholder="Doe"
                    type="text" 
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleInputChange}
                /><br/>
                <label htmlFor="email">Email </label>
                <input 
                    placeholder="jdoe@mail.com"
                    type="email" 
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                /><br/>
                <label htmlFor="password">Password </label>
                <input 
                    placeholder="************"
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