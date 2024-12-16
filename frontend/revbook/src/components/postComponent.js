import axios from "axios";
import Comment from "./comment";
import { useState, useEffect } from "react";

const PostComponent = ({ post }) => {
    const [commentText, setCommentText] = useState('');
    const [comments, setComments] = useState([]);
    const [reactions, setReactions] = useState([]);
    const user = JSON.parse(sessionStorage.getItem('user'));
    
    const commentOnPost = async () => {
        if (commentText.trim()) {
            try {
                const response = await axios.post(`http://localhost:8080/posts/${post.postId}/comments`, {
                    commentText: commentText,
                    posterId: user.userId
                });
                setCommentText("");
            } catch (error) {
                console.error("Error adding comment:", error);
            }
        }
    };

    const reactToPost = async (reactionType) => {
        try {
            const response = await axios.post(`http://localhost:8080/posts/${post.postId}/reactions`, {
                reactionType: reactionType,
                reacterId: user.userId
            });
            getReactions();
        } catch (error) {
            console.error("Error reacting to post:", error);
        }
    };

    const getComments = async () => {
        if (user && user.userId) {
            try {
                const response = await axios.get(`http://localhost:8080/posts/${post.postId}/comments`);
                setComments(response.data);
            } catch (error) {
                console.error("Error getting comments: ", error);
            }
        }
    };

    const getReactions = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/posts/${post.postId}/reactions`);
            setReactions(response.data);
        } catch (error) {
            console.error("Error getting reactions: ", error);
        }
    };

    useEffect(() => {
        getComments();
        getReactions();
    }, []);

    return (
        <div className="post-container">
            <h3>{post.firstName} {post.lastName } </h3>
            <p> {post.postText}</p>
            <small>Posted at: {new Date(post.timePosted).toLocaleString()}</small>
            <div className="reaction-container">
                {reactions.map(reaction => (
                    <span key={reaction.reactionId}>{reaction.reactionType}</span>
                ))}
                <button onClick={() => reactToPost("LIKE")}>Like</button>
                <button onClick={() => reactToPost("DISLIKE")}>Dislike</button>
            </div>
            <div className="comment-container">
                {comments.length > 0 && comments.map(comment => <Comment key={comment.commentId} comment={comment} />)}
                {(!comments || comments.length === 0) && <p>No comments</p>}
                <input
                type="text"
                placeholder="Add a comment.."
                value={commentText}
                onChange={e => setCommentText(e.target.value)}
                required
                />
                <button onClick={commentOnPost}>Add comment</button>
            </div>
        </div>
    )
}

export default PostComponent;