import { useNavigate } from "react-router-dom";
import { useState } from 'react';
import UserLogin from "./userLogin";
import UserRegistration from "./userRegistration";
import PostForm from "./postForm";
import UserFeed from "./userFeed";

// if a user is not logged in, they will be served the login form or registration form, with the ability to toggle between them 
// if a user is logged in, the landing page will show the homepage, which will contain posts from connected users and the ability to make a new post
const LandingPage = () => {

    // show the login form by default
    const [showLogin, setShowLogin] = useState(true);

    const navigate = useNavigate();
    // attempt to get user data from sessionStorage
    const user = JSON.parse(sessionStorage.getItem("user"));

    if (!user) {
        return (
            <>
            <h2>Welcome to Revbook!</h2>
            {showLogin ? <UserLogin /> : <UserRegistration />}
            <button onClick = {() => setShowLogin(!showLogin)}>
                {showLogin ? "New user? Register" : "Already have an account? Sign in"}
            </button>
            </>
        );
    }

    return(
        <>
        <h2>Welcome, {user.firstName} </h2>
        <PostForm />
        <UserFeed />
        </>
    );
};

export default LandingPage;