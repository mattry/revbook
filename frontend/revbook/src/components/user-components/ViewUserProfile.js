/*
    when viewing a profile users should be able to see their posts
    when viewing a profile users should be able to (un)follow them
    when viewing a profile users should be able to see that users followers
    when viewing a profile users should be able to see that users following
*/

import ConnectionDisplay from "./ConnectionDisplay";
import ConnectionManagement from "./ConnectionManagement";
import UserPostsComponent from "./UserPostsComponent";

const ViewUserProfile = ({displayUser, posts, followers, following}) => {

    return(
        <>
        <p>Viewing user</p>
        <h3>{displayUser.firstName} {displayUser.lastName}</h3>
        <ConnectionDisplay followers={followers} following={following} />
        <br/>
        <ConnectionManagement displayUser={displayUser} />
        <UserPostsComponent posts={posts}/>
        </>
    )

}

export default ViewUserProfile;