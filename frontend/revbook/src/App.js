import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import UserRegistration from './components/userRegistration';
import UserLogin from './components/userLogin';
import LandingPage from './components/landingPage';

function App() {
  return (
    <Router>
      <Routes>
          <Route path ="/register" element={<UserRegistration />} />
          <Route path ="/login" element={<UserLogin />} />
          <Route path ="/" element={<LandingPage />} />
      </Routes>
    </Router>
  );
}

export default App;
