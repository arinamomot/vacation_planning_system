import React from 'react';

const AuthContext = React.createContext({
  user: {
    id: null,
    username: null,
  },
  setUser: (user) => {},
});

export default AuthContext;
