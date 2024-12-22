/*
    Since this component is only rendered in the ActiveUserProfile component
    This component will serve as the parent component for handling user profile management.
    This includes the child components for handling password change as well as name changing.
    We will set state for userFirstName and userLastName.
    We will also set state for the current password and updated password, but initialized to empty strings since user.password -> undefined
    We will set state for showChangeName, set to true by default and have a button to switch which form is rendered by clicking and inverting this state.
    ChangeName component will serve a form with two input fields, one for the firstName to be set to and one for the lastName to be set to.
    ChangePassword component will serve a form with two input fields, one for the current password and one for the new password
*/

import axios from "axios";
import { useState } from "react";
import { useUser } from "../UserContext";
import ChangeName from "./ChangeName";
import ChangePassword from "./ChangePassword";


const ProfileManagement = () => {
    const { user } = useUser();
    const { updateUser } = useUser();

    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [currPassword, setCurrPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [showChangeName, setShowChangeName] = useState(true);

    const passwordSubmitHandler = async (e) => {
        e.preventDefault();
        console.log(user.userId);

        const request = {
            userId: user.userId,
            currentPassword: currPassword,
            newPassword: newPassword
        };

        try {
            const response = await axios.patch("http://localhost:8080/update-password", request);
            console.log(response);
        } catch (error) {
            console.error("Error updating password: ", error);
        }
    }

    const nameSubmitHandler = async (e) => {
        e.preventDefault();
        console.log(user.userId);

        const request = {
            userId: user.userId,
            firstName: firstName,
            lastName: lastName
        }

        try{
            const response = await axios.patch("http://localhost:8080/update-name", request);
            console.log(response);
            updateUser(response.data);
        } catch (error) {
            console.error("Error updating name: ", error);
        }
    }



    return(
        <>
        <br/><br/>
        <h2 className="modal-title">Profile Management</h2>
        <br/>
        {
            showChangeName ? 
            <ChangeName firstName={firstName} lastName={lastName} nameSubmitHandler={nameSubmitHandler} setFirstName = {setFirstName} setLastName={setLastName} /> 
            :
            <ChangePassword currPassword={currPassword} 
                            newPassword = {newPassword} 
                            passwordSubmitHandler={passwordSubmitHandler} 
                            setCurrPassword={setCurrPassword} 
                            setNewPassword={setNewPassword} />
        }
        <br/>
        <button onClick = {() => setShowChangeName(!showChangeName)}>
            {showChangeName ? "Change your password" : "Change your name"}
        </button>
        </>
    );

}

export default ProfileManagement;