import { useState } from 'react';
import axios from 'axios';
import { useUser } from "../UserContext";

const PostForm = ({ onPostCreated }) => {

    const { user } = useUser();
    const [postText, setPostText] = useState("");

    const handleInputChange = (e) => {
        setPostText(e.target.value);
    };

    const submitHandler = async (e) => {
        e.preventDefault();
        if (!postText.trim()) return;

        const request = { posterId: user.userId, postText };

        try {
            const response = await axios.post("http://localhost:8080/posts", request, {
                headers: { "Content-Type": "application/json" },
            });

            // lift the state
            onPostCreated(response.data);
            // clear the form after submission
            setPostText("");
        } catch (error) {
            console.error("Error creating post:", error);
        }
    };

    return (
        <div className="post-form">
            <form onSubmit={submitHandler}>
                <label htmlFor="post-field">
                    <h3>Have something on your mind? Share it!</h3>
                </label>
                <textarea
                    id="post-field"
                    name="post-field"
                    placeholder="Write your post..."
                    value={postText}
                    onChange={handleInputChange}
                    required
                ></textarea>
                <button type="submit">Post</button>
            </form>
        </div>
    );
};

export default PostForm;