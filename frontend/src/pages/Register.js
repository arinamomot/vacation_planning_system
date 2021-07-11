import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import Button from '../components/Button';
import Layout from '../components/Layout';
import api from '../utils/api';

const Register = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    position: '',
});

  const history = useHistory();

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.register(form);
      history.push('/login');
    } catch (err) {
      console.log('Error', err);
      setForm({});
    }
  };

  return (
    <Layout>
      <div className="m-auto flex flex-col border p-10 w-96 rounded-md">
        <h1 className="font-bold text-3xl mb-2 text-center">Register</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="space-y-2 w-full mb-5">
            <input
              value={form.email}
              onChange={handleInput}
              name="email"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="Email"
              type="email"
            />
            <input
              value={form.firstName}
              onChange={handleInput}
              name="firstName"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="First name"
            />
            <input
              value={form.lastName}
              onChange={handleInput}
              name="lastName"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="Last name"
            />
            <input
              value={form.position}
              onChange={handleInput}
              name="position"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="Work position"
            />
            <input
              value={form.password}
              onChange={handleInput}
              name="password"
              className="bg-gray-100 px-5 py-2 w-full rounded-md"
              required
              placeholder="Password"
              type="password"
            />
          </div>
          <Button>
              Register
          </Button>
        </form>
        <div className="mt-8 text-sm text-center">Already signed up?
        <Link to="/login" className="ml-1 text-blue-400 hover:text-blue-600">
          Login here
        </Link></div>
      </div>
    </Layout>
  );
};

export default Register;
