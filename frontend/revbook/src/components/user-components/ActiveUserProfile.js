/*
    the active user should be able to update their profile information on this page
    the active user should be able to change their password on this page
    the active user should be able to view their followers on this page
    the active user should be able to view their followings on this page
    the active user should be able to edit and delete their own posts on this page
*/

import ProfileManagement from "./ProfileManagement";
import UserPostsComponent from "./UserPostsComponent";
import ConnectionDisplay from "./ConnectionDisplay";
import { useState } from "react";
import Modal from 'react-modal';

const ActiveUserProfile = ({displayUser, posts, followers, following}) => {

    const [visible, setVisible] = useState(false);

    return(
        <div className="profile-container">
            <h3>{displayUser.firstName} {displayUser.lastName}</h3>
            <ConnectionDisplay followers={followers} following={following} />
            <button onClick={() => setVisible(true)}>Profile Management</button>
            <Modal isOpen={visible}>
                <div>
                    <button onClick={() => setVisible(false)}>Close</button>
                    <ProfileManagement />
                </div>   
            </Modal>
            <br/>
            <br/>
            <hr/>
            <br/>
            <UserPostsComponent posts={posts}/>
        </div>
    )

}

export default ActiveUserProfile;