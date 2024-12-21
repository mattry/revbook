import { useUser } from './UserContext';
import PostForm from "./post-components/PostForm";
import UserFeed from "./post-components/UserFeed";
import AuthFormComponent from "./auth-form-components/AuthFormComponent";

// if a user is not logged in, they will be served the login form or registration form, with the ability to toggle between them 
// if a user is logged in, the landing page will show the homepage, which will contain posts from connected users and the ability to make a new post
const LandingPage = () => {

    // get user from context
    const { user } = useUser();

    if (!user) {
        return (
            <AuthFormComponent />
        );
    }

    return(
        <div className="landing-page">
            <h2 className="welcome-header">Welcome, {user.firstName} {user.lastName} </h2>
            <UserFeed />
        </div>
    );
};

export default LandingPage;