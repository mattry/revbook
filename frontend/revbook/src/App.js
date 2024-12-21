import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import LandingPage from './components/LandingPage';
import UserProfileComponent from './components/user-components/UserProfileComponent';
import NavBar from './NavBar';
import { UserProvider } from './components/UserContext';
import ResultsPage from './components/search-components/ResultsPage';
import Modal from "react-modal";

Modal.setAppElement("#root");

function App() {
  return (
    <UserProvider>
      <Router>
        <div className="App">
          <NavBar />
        </div>
        <div className='container'>
          <Routes>
              <Route path ="/" element={<LandingPage />} />
              <Route path="/user/:id" element={<UserProfileComponent />} />
              <Route path="/results" element={<ResultsPage />} />
          </Routes>
        </div>
      </Router>
    </UserProvider>
  );
}

export default App;
