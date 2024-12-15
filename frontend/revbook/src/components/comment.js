import { useState, useEffect } from "react";
import axios from "axios";



const Comment = ({ comment }) => {
    const [commentText, setCommentText] = useState('');
    const [childComments, setChildComments] = useState([]);
    const [loaded, setLoaded] = useState(false);
    const user = JSON.parse(sessionStorage.getItem('user'));

    const commentOnComment = async () => {
        if (commentText.trim()) {
            try {
                const response = await axios.post(`http://localhost:8080/comments/${comment.commentId}/reply`, {
                    commentText: commentText,
                    posterId: user.userId
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
            if (response.data && response.data.length > 0) {
                setChildComments(response.data);
                setLoaded(true);
            }
        } catch (error) {
            console.error("Error getting child comments: ", error);
        }
    };

    useEffect(() => {
        if (!loaded) { 
            getChildComments(); 
        }
    }, [loaded]);

    return (
        <>
        <hr/>
        <h5>{comment.firstName} {comment.lastName}</h5>
        <p>{comment.commentText}</p>
        <small>Posted at: {new Date(comment.timePosted).toLocaleString()}</small>
        <div>
            {childComments.length > 0 ? (
                 childComments.map(childComment => <Comment key={childComment.commentId} comment={childComment} />)
                 ) : ( 
                 <p>No comments</p> )}    
        </div>
        <input
            type="text"
            placeholder="Add a comment.."
            value={commentText}
            onChange={e => setCommentText(e.target.value)}
            required
        />
        <button onClick={commentOnComment}>Add comment</button>
        <hr/>
        </>
    )
}

export default Comment;