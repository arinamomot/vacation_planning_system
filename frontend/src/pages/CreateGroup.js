import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';
import Button from '../components/Button';

const CreateGroup = () => {
  const { setUser, user } = useContext(AuthContext);
  const [form, setForm] = useState({
    name: '',
  });
  console.log(user);
  const history = useHistory();

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const group = await api.createGroup(form);
      console.log('group: ', group);
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
        <h1 className="font-bold text-3xl">Create group</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="w-full mb-5">
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1">
                  Group name
            </label>
            <input
              value={form.name}
              onChange={handleInput}
              name="name"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              type="text"
              placeholder="Blah blah Group"
            />
          </div>
          <Button type="submit" disabled={!form.name}>
              Create Group
          </Button>
        </form>
      </div>
    </Layout>
  );
};

export default CreateGroup;
