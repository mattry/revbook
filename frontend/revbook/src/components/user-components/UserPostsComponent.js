import PostComponent from '../postComponent.js';

const UserPostsComponent = ({posts}) => {

    if (posts.length > 0) {
        return (
            <>
            {posts.map(post => <PostComponent key={post.postId} post={post} />)}
            </>
        )
    }

    return (
        <>
        <p>No posts to display</p>
        </>
    );

}

export default UserPostsComponent;