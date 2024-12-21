import { useLocation } from "react-router-dom";
import SearchDisplay from "./SearchDisplay";

const ResultsPage = () => {
    const location = useLocation();

    // Check if `state` contains search results
    const searchResults = location.state?.searchResults;

    return (
        <div className="results-page">
            <h1>Search Results</h1>
            {
                searchResults ? 
                    ( <SearchDisplay results={searchResults} />)
                : 
                    (<p>No results to display.</p>)
             }
        </div>
    );
};

export default ResultsPage;