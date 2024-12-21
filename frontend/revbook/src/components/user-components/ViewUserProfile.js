/*
    when viewing a profile users should be able to see their posts
    when viewing a profile users should be able to (un)follow them
    when viewing a profile users should be able to see that users followers
    when viewing a profile users should be able to see that users following
*/

import ConnectionDisplay from "./ConnectionDisplay";
import ConnectionManagement from "./ConnectionManagement";
import UserPostsComponent from "./UserPostsComponent";

const ViewUserProfile = ({displayUser, posts, followers, following, getFollowers, getFollowing}) => {

    return(
        <div className="profile-container">
            <h3>{displayUser.firstName} {displayUser.lastName}</h3>
            <ConnectionDisplay followers={followers} following={following} />
            <ConnectionManagement displayUser={displayUser} getFollowers={getFollowers} getFollowing={getFollowing} /><br/>
            <br/>
            <hr/>
            <br/>
            <UserPostsComponent posts={posts}/>
        </div>
    )

}

export default ViewUserProfile;