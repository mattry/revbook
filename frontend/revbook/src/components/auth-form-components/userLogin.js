import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../UserContext'

const UserLogin = () => {

    const navigate = useNavigate(); 

    const { updateUser } = useUser();

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
                <label htmlFor="email">Email </label>
                <input 
                    placeholder="jdoe@email.com"
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
                <button type="submit">Login</button>
            </form>
        </>
    );

}

export default UserLogin;