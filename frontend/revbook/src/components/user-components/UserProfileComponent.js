/*
    This component will be the parent component for user profile pages
    There are two different profile pages to be conditionally rendered
    The condition is whether or not the id in the path parameter is the same id of the logged in user
    If it is the users page the ActiveUserProfile will be rendered
    Else the ViewUserProfile will be rendered
    These components have some overlap, namely the user's posts and the user's following/followers
    The state of which will all be managed here in the parent
*/

import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ActiveUserProfile from "./ActiveUserProfile";
import ViewUserProfile from "./ViewUserProfile";

const UserProfileComponent = () => {

    const user = JSON.parse(sessionStorage.getItem("user"));
    const { id } = useParams();

    // flag to determine if the requested profile is the logged in user
    const [isCurrentUser, setIsCurrentUser] = useState(false);

    // state for user information, used for displaying information in the child components
    const [responseUser, setResponseUser] = useState(null);

    // states used for child component rendering
    const [posts, setPosts] = useState([]);
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);

    // we'll get the user information from the backend that should be rendered
    const getUserInformation = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/users/${id}`);
            setResponseUser(response.data);
            if (response.data.userId === user.userId) {
                setIsCurrentUser(true);
            } else {
                setIsCurrentUser(false);
            }
        } catch (error) {
            console.error('Error getting user: ', error);
        }
    }
    
    // get the param user's posts
    const getPosts = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/posts/${id}`);
            setPosts(response.data);
        } catch (error) {
            console.error("Error getting posts: ", error);
        }
    }

    // get the param user's followers
    const getFollowers = async() => {
        try {
            const response = await axios.get(`http://localhost:8080/users/${id}/followers`);
            setFollowers(response.data);
        } catch (error) {
            console.error("Error getting followers: ", error);
        }
    }

    // get the param user's following
    const getFollowing = async() => {
        try {
            const response = await axios.get(`http://localhost:8080/users/${id}/following`);
            setFollowing(response.data);
        } catch (error) {
            console.error("Error getting followers: ", error);
        }
    }



    useEffect(() => {
        getUserInformation();
        getPosts();
        getFollowers();
        getFollowing();
    }, [id]);
    
    // While we wait, let's render this so there is no error on screen
    if (!responseUser) {
        return <div>Loading...</div>; // Show a loading state while data is fetched
    }

    if(isCurrentUser){
        return (
            <ActiveUserProfile 
                displayUser={responseUser}
                posts={posts}
                followers={followers}
                following={following}
            />
        );
    } else {
        return (
            <ViewUserProfile 
                displayUser={responseUser}
                posts={posts}
                followers={followers}
                following={following}
            />
        )
    }

}

export default UserProfileComponent;