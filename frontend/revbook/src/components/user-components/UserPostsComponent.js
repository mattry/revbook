import PostComponent from '../post-components/PostComponent.js';

const UserPostsComponent = ({posts}) => {

    if (posts.length > 0) {
        return (
            <div className="user-feed">
                {posts.map(post => <PostComponent key={post.postId} post={post} />)}
            </div>
        )
    }

    return (
        <>
        <p>No posts to display</p>
        </>
    );

}

export default UserPostsComponent;