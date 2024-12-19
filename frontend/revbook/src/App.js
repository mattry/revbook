import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import UserRegistration from './components/userRegistration';
import UserLogin from './components/userLogin';
import LandingPage from './components/landingPage';
import UserProfileComponent from './components/user-components/UserProfileComponent';
import NavBar from './NavBar';

function App() {
  return (
    <Router>
      <div className="App">
        <NavBar />
      </div>
      <Routes>
          <Route path ="/register" element={<UserRegistration />} />
          <Route path ="/login" element={<UserLogin />} />
          <Route path ="/" element={<LandingPage />} />
          <Route path="/user/:id" element={<UserProfileComponent />} />
      </Routes>
    </Router>
  );
}

export default App;
