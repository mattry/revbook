import { Link } from "react-router-dom";
import SearchComponent from "./components/search-components/SearchComponent";

const NavBar = () => {

    const user = JSON.parse(sessionStorage.getItem("user"));

    return (
        <div id="navbar">
            <h2>RevBook</h2>
            <SearchComponent />
            <div className="links">
                <Link to ="/">Home</Link>
                <Link to={`/user/${user.userId}`}>Profile</Link>
            </div>
        </div>
    )


}

export default NavBar;