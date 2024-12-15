import axios from "axios";
import Comment from "./comment";
import { useState, useEffect } from "react";

const PostComponent = ({ post }) => {
    const [commentText, setCommentText] = useState('');
    const [comments, setComments] = useState([]);
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

    useEffect(() => {
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

        getComments();
    }, []);

    return (
        <>
        <hr/>
        <h3>{post.poster.firstName} {post.poster.lastName } </h3>
        <p> {post.postText}</p>
        <small>Posted at: {new Date(post.timePosted).toLocaleString()}</small>
        <div>
            {comments.length > 0 && comments.map(comment => <Comment key={comment.commentId} comment={comment} />)}
            {(!comments || comments.length === 0) && <p>No comments</p>}
        </div>
        <input
            type="text"
            placeholder="Add a comment.."
            value={commentText}
            onChange={e => setCommentText(e.target.value)}
            required
        />
        <button onClick={commentOnPost}>Add comment</button>
        <hr/>
        </>
    )
}

export default PostComponent;