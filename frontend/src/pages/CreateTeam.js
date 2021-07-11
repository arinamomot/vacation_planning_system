import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';
import Button from '../components/Button';

const CreateTeam = () => {
  const { setUser } = useContext(AuthContext);
  const [form, setForm] = useState({
    name: '',
    closestDayLimit: 0,
    furthestDayLimit: 0,
  });
  const history = useHistory();

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.createTeam(form);
      alert('Project created');
      const { data } = await api.getCurrentUser();
      setUser(data);
      // TODO: redirect here to project? or handle otherwise
      history.goBack();
    } catch (err) {
      console.log('Error', err);
      alert(err.response.data.message);
    }
  };

  return (
    <Layout>
      <div className="m-auto flex flex-col border p-10 w-96 rounded-md">
        <h1 className="font-bold text-3xl">Create project</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="w-full mb-5">
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1">
                  Team name
            </label>
            <input
              value={form.name}
              onChange={handleInput}
              name="name"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              type="text"
              placeholder="Blah blah Team"
            />
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1 mt-4">
                  Closest Day Limit
            </label>
            <input
              value={form.closestDayLimit}
              onChange={handleInput}
              name="closestDayLimit"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              type="number"
            />
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1 mt-4">
                  Furthest Day Limit
            </label>
            <input
              value={form.furthestDayLimit}
              onChange={handleInput}
              name="furthestDayLimit"
              id="furthestDayLimit"
              className="bg-gray-100 px-5 py-2 w-full mb-4"
              required
              placeholder="Name"
              type="number"
            />
          </div>
          <Button type="submit" disabled={!form.name || !typeof form.closestDayLimit === 'number' || !typeof form.furthestDayLimit === 'number'}>
              Create Team
          </Button>
        </form>
      </div>
    </Layout>
  );
};

export default CreateTeam;
