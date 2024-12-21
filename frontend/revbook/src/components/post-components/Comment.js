import { useState, useEffect } from "react";
import axios from "axios";
import ReactionsDisplay from "./ReactionsDisplay";
import { useUser } from "../UserContext";
import { Link } from "react-router-dom";

const Comment = ({ comment }) => {
    const [commentsVisible, setCommentsVisible] = useState(false);
    const [commentText, setCommentText] = useState('');
    const [childComments, setChildComments] = useState([]);
    const { user } = useUser();

    const commentOnComment = async () => {
        if (commentText.trim()) {
            try {
                await axios.post(`http://localhost:8080/comments/${comment.commentId}/reply`, {
                    commentText: commentText,
                    posterId: user.userId,
                });
                setCommentText("");
                getChildComments();
            } catch (error) {
                console.error("Error adding child comment:", error);
            }
        }
    };

    const getChildComments = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/comments/${comment.commentId}/replies`);
            setChildComments(response.data || []);
        } catch (error) {
            console.error("Error getting child comments:", error);
        }
    };

    useEffect(() => {
        getChildComments();
    }, []);

    return (
        <div className="comment-container">
            <h5>
            <Link to={`/user/${comment.posterId}`} className="user-link">
                    {comment.firstName} {comment.lastName}
            </Link>
            </h5>
            <p>{comment.commentText}</p>
            <small>Posted at: {new Date(comment.timePosted).toLocaleString()}</small>

            <ReactionsDisplay entityId={comment.commentId} entityType="comment" />


            <span onClick = {() => setCommentsVisible(!commentsVisible)}>
                {commentsVisible ? "Hide replies" : "Click to view replies"}
            </span>

            {commentsVisible && (
                <div className="comment-children">
                    {childComments.map((childComment) => (
                        <Comment key={childComment.commentId} comment={childComment} />
                    ))}
                </div>
            )}

            <div className="add-comment-reply">
                <input
                    type="text"
                    placeholder="Add a comment..."
                    value={commentText}
                    onChange={(e) => setCommentText(e.target.value)}
                    required
                />
                <button onClick={commentOnComment}>Reply</button>
            </div>
        </div>
    );
};

export default Comment;