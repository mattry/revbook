/*
    This component will be used to follow and unfollow other users
    It will be rendered when viewing other user's profiles
    A different component will exist for viewing a user's connections
*/

import axios from "axios";
import { useEffect, useState } from "react";
import { useUser } from "../UserContext";

axios.defaults.withCredentials = true;

const ConnectionManagement = ({displayUser, getFollowers, getFollowing}) => {

    const { user } = useUser();
    const [connection, setConnection] = useState(null);
    const [loading, setLoading] = useState(true);

    const request = {
        followerId: user.userId,
        followeeId: displayUser.userId
    };

    const getConnectionStatus = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/users/${user.userId}/isFollowing/${displayUser.userId}`);
            setConnection(response.data);
        } catch (error) {
            console.error("Error getting connection status: ", error)
        } finally {
            setLoading(false);
        }
    };

    useEffect(()=>{
        getConnectionStatus();
    },[]);

    const submitHandler = async () => {
        // if users are not connected, follow button is shown and clicking it will follow the user
        if(connection === false){
            try {
                await axios.post(`http://localhost:8080/users/${displayUser.userId}/follow`, request);
                setConnection(true);
                getFollowers();
                getFollowing();
            } catch (error) {
                console.error("Error following user: ", error);
            }
        } else {
            try {
                await axios.delete(`http://localhost:8080/users/${displayUser.userId}/unfollow`, { data: request });
                setConnection(false)
                getFollowers();
                getFollowing();
            } catch (error) {
                console.error("Error unfollowing user: ", error);
            }
        }
    };


    if (loading) {
        return <p>Loading...</p>;
      }

    return (
        <>
          <button onClick={()=>{submitHandler()}}>
            {connection ? "Unfollow" : "Follow"}
          </button>
        </>
      );
    
    

}

export default ConnectionManagement;