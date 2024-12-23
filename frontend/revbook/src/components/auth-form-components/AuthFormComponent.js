/*
    Parent Component for Login and Registration components
    States and functions will be defined here, the child components will only be concerned with rendering their respective forms
    The state of a user being logged in or not will be lifted up from these child components, once a user is logged in, they will be redirected to their home page
*/

import { useState } from "react";
import UserLogin from "./userLogin";
import UserRegistration from "./userRegistration";

const AuthFormComponent = () => {

    // show the login form by default
    const [showLogin, setShowLogin] = useState(true);



    return(
        <div className="auth-form">
            <h2>Welcome to RevBook!</h2><br/>
            <br/>
            {showLogin ? <UserLogin /> : <UserRegistration />}<br/>
            <button id="secondary-button" onClick = {() => setShowLogin(!showLogin)}>
                {showLogin ? "New user? Register" : "Existing user? Sign in"}
            </button>
        </div>
    )


}

export default AuthFormComponent;