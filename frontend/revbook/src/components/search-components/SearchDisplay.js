import PostComponent from "../post-components/PostComponent";
import { Link } from "react-router-dom";

const SearchDisplay = ({results}) => {

    const { users, posts } = results;

    const displayUsers = () => {
        if (!users || users.length === 0) return <p>No users found.</p>

        return users.map((user) => (
            <p className="user-result">
                <Link to={`/user/${user.userId}`} className="user-link">
                    {user.firstName} {user.lastName}
                </Link>
            </p>
        ));
    };

    const displayPosts = () => {
        if (!posts || users.length === 0) return <p>No posts found.</p>

        return posts.map((post) =>(
            <PostComponent key={post.postId} post={post} />
        ))
    }

    return (
        <>
        <h3 className="results-heading">Users: </h3>
        <br/>
        {displayUsers()}
        <br/>
        <h3 className="results-heading">Posts: </h3>
        <br/>
        {displayPosts()}
        </>
    )
}

export default SearchDisplay;