import { useState } from 'react';
import axios from 'axios';

const PostForm = () => {

    const user = JSON.parse(sessionStorage.getItem("user"));
    const [postText, setPostText] = useState("");

    const handleInputChange = (e) => {
        setPostText(e.target.value);
    };

    const submitHandler = async (e) => {
        e.preventDefault();

        const request = {
            posterId: user.userId,
            postText: postText,
        };

        try {
            const response = await axios.post("http://localhost:8080/posts", request, {
                headers: {
                    "Content-Type": "application/json"
                },
            });

            console.log("Post created successfully:", response.data);
            // clear form after creating post
            setPostText("");
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return(
        <>
        <form onSubmit={submitHandler}>
            <label htmlFor="post-field">Have something on your mind? Share it!</label><br/>
            <input 
                type="textarea" 
                name="post-field" 
                placeholder="Write your post.." 
                value={postText}
                onChange={handleInputChange}
                required
            /><br/>
            <button type="submit">Post</button>
        </form>
        </>
    );
};

export default PostForm;