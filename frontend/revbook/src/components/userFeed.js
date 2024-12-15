import { useState, useEffect } from 'react';
import axios from 'axios';
import PostComponent from './postComponent';

const UserFeed = () => {
    const [posts, setPosts] = useState([]);
    const user = JSON.parse(sessionStorage.getItem("user"));

    const getPosts = async () => {
        if (user && user.userId) {
            try {
                const response = await axios.get(`http://localhost:8080/feed/${user.userId}`);
                setPosts(response.data);
            } catch (error) {
                console.error("Error getting posts: ", error);
            }
        } 
    }
    
    useEffect(() => {
        getPosts();
    }, []);

    if (posts.length > 0) {
        return (
            <>
            {posts.map(post => <PostComponent key={post.postId} post={post} />)}
            </>
        )
    }

    return (
        <>
        <p>No posts to display</p>
        </>
    );

}

export default UserFeed;