import { useState, useEffect } from "react";
import axios from "axios";
import PostComponent from "./PostComponent";
import PostForm from "./PostForm";
import { useUser } from "../UserContext";

const UserFeed = () => {
    const { user } = useUser();
    const [posts, setPosts] = useState([]);

    const fetchPosts = async () => {
        if (user && user.userId) {
            try {
                const response = await axios.get(`http://localhost:8080/feed/${user.userId}`);
                setPosts(response.data);
            } catch (error) {
                console.error("Error fetching posts: ", error);
            }
        }
    };

    // Fetch posts on initial render or when the user changes
    useEffect(() => {
        fetchPosts();
    }, [user]);

    // Callback to handle a new post
    const handlePostCreated = (newPost) => {
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    if (!posts.length) {
        return (
            <div>
                <PostForm onPostCreated={handlePostCreated} />
                <p>No posts to display</p>
            </div>
        );
    }

    return (
        <div className="user-feed">
            <PostForm onPostCreated={handlePostCreated} />
            {posts.map((post) => (
                <PostComponent key={post.postId} post={post} />
            ))}
        </div>
    );
};

export default UserFeed;