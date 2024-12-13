import './App.css';
import UserRegistration from './components/userRegistration';
import UserLogin from './components/userLogin';

function App() {
  return (
    <div className="App">
      <h1>Welcome to Revbook</h1><hr/>
      <UserRegistration />
      <hr/>
      <UserLogin />
      <hr/>
    </div>
  );
}

export default App;
