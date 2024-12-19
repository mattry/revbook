import PostComponent from "../postComponent";

const SearchDisplay = ({results}) => {

    const { users, posts } = results;

    const displayUsers = () => {
        if (!users || users.length === 0) return <p>No users found.</p>

        return users.map((user) => (
            <p>{user.firstName} {user.lastName}</p>
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
        <h3>Users: </h3>
        {displayUsers()}
        <h3>Posts: </h3>
        {displayPosts()}
        </>
    )
}

export default SearchDisplay;