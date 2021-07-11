import React, { useEffect, useState } from 'react';
import AuthContext from './context/AuthContext';
import Routes from './routes';
import api from './utils/api';
import './components/tailwind.css';

const App = () => {
  const [user, setUser] = useState({
    id: null,
    username: null,
    loading: true,
  });


  useEffect(() => {
    const getCurrentUserAndRedirect = async () => {
      try {
        const { data } = await api.getCurrentUser();
        setUser({ ...data, loading: false });
      } catch (err) {
          console.log(window.location.pathname);
          if (window.location.pathname !== '/login'){
            // can't use React router here because it's being inited only lower
            window.location.replace("/login");
          }
      }
    };

    getCurrentUserAndRedirect();
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      <Routes logged={user.id} />
    </AuthContext.Provider>
  );
};

export default App;
