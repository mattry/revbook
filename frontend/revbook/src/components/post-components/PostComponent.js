import axios from "axios";
import Comment from "./Comment";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useUser } from "../UserContext";
import ReactionsDisplay from "./ReactionsDisplay";

const PostComponent = ({ post }) => {
    const [commentsVisible, setCommentsVisible] = useState(false);
    const [comments, setComments] = useState([]);
    const [newCommentText, setNewCommentText] = useState("");
    const [loaded, setLoaded] = useState(false);
    const { user } = useUser();

    const fetchComments = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/posts/${post.postId}/comments`);
            setComments(response.data);
            setLoaded(true);
        } catch (error) {
            console.error("Error fetching comments:", error);
        }
    };

    useEffect(() => {
        if (!loaded) fetchComments();
    }, [loaded]);

    const handleCommentSubmit = async (e) => {
        e.preventDefault();
        if (!newCommentText.trim()) return;

        const request = {
            commentText: newCommentText,
            posterId: user.userId,
        };

        try {
            const response = await axios.post(`http://localhost:8080/posts/${post.postId}/comments`, request);
            setComments((prevComments) => [...prevComments, response.data]);
            setNewCommentText("");
        } catch (error) {
            console.error("Error adding comment:", error);
        }
    };

    return (
        <div className="post-container">
            <h3>
            <Link to={`/user/${post.posterId}`} className="user-link">
                    {post.firstName} {post.lastName}
            </Link>
            </h3>
            <p>{post.postText}</p>
            <small>Posted: {new Date(post.timePosted).toLocaleString()}</small>

            <ReactionsDisplay entityId={post.postId} entityType="post" />

            <span onClick = {() => setCommentsVisible(!commentsVisible)}>
                {commentsVisible ? "Hide comments" : "Click to view comments"}
            </span>

            {commentsVisible && (
                <div className="post-comments">
                    {comments.map((comment) => (
                        <Comment key={comment.commentId} comment={comment} />
                    ))}
                </div>
            )}

            <form className="comment-form" onSubmit={handleCommentSubmit}>
                <input
                    type="text"
                    placeholder="Add a comment..."
                    value={newCommentText}
                    onChange={(e) => setNewCommentText(e.target.value)}
                    required
                />
                <button type="submit">Comment</button>
            </form>
            
        </div>
    );
};

export default PostComponent;