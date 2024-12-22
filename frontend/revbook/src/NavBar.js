import { Link } from "react-router-dom";
import { useUser } from "./components/UserContext";
import SearchComponent from "./components/search-components/SearchComponent";

const NavBar = () => {

    const { user, clearUser } = useUser();

    const handleLogout = () => {
        clearUser();
    }

    return (
        <div id="navbar">
            <h2>RevBook</h2>
            {user ? 
            <>
                <SearchComponent />
                <div className="links">
                    <Link to="/">Home</Link>
                    <Link to={`/user/${user.userId}`}>Profile</Link>
                    <Link to="/" onClick={handleLogout}>Logout</Link>
                </div>
            </>
            : null}
        </div>
    )


}

export default NavBar;