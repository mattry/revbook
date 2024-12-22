/*
    This component will serve as the parent for search functionality and display components
    The search bar will be in the nav-bar at all times the user is logged in
    When the search is submitted, the search results component will be displayed
    Search results component will display the returned response of users and posts that match the search term 
*/

import axios from "axios";
import { useState } from "react";
import SearchDisplay from "./SearchDisplay";
import { useNavigate } from "react-router-dom";

const SearchComponent = () => {

    const navigate = useNavigate();

    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState(null);
    const [loading, setLoading] = useState(false);

    const search = async (e) => {

        e.preventDefault();

        // don't perform search for white space
        if(searchTerm.trim() === "") return;

        const request = {
            searchTerm: searchTerm
        };

        setLoading(true);

        try{
            const response = await axios.post("http://localhost:8080/search", request);
            navigate("/results", { state: { searchResults: response.data } });
            // clear the form after search
            setSearchTerm("");
        } catch (error) {
            console.error("Error performing search: ", error);
        } finally {
            setLoading(false);
        }
    }

    if(loading){
        return(<p>Loading...</p>);
    }

    return (
        <>
        <form onSubmit={search}>
            <input 
                type="text"
                palceholder="Search for Revbook for users or posts.."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button>Search</button>
        </form>
        </>
    )

}

export default SearchComponent;