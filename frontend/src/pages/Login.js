import React, { useContext, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';

const Login = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
  });

  const { setUser } = useContext(AuthContext);

  const history = useHistory();

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.login(form.email, form.password);
      await new Promise(resolve => setTimeout(() => resolve(), 1000));
      const { data } =  await api.getCurrentUser();
      console.log(data);
      setUser(data);
      history.push('/');
    } catch (err) {
      console.log('Error', err);
      setForm((state) => ({ ...state, password: '' }));
    }
  };

  return (
    <Layout>
      <div className="m-auto flex flex-col p-10 w-96  border rounded-md">
        <h1 className="font-bold text-3xl">Login</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="space-y-2 w-full mb-5">
            <input
              value={form.email}
              onChange={handleInput}
              name="email"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="email"
              type="text"
            />
            <input
              value={form.password}
              onChange={handleInput}
              name="password"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="password"
              type="password"
            />
          </div>
          <button className="bg-blue-500 text-white font-bold py-2 rounded-md" type="submit">
            Login
          </button>
        </form>
        <div className="mt-8 text-sm text-center">Don't have an account yet? 
            <Link to="/register" className="ml-1 text-blue-400 hover:text-blue-600">
            Register
            </Link>
        </div>
      </div>
    </Layout>
  );
};

export default Login;
