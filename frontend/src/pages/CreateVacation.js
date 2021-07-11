import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';
import Button from '../components/Button';
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

const CreateTeam = () => {
  const { setUser } = useContext(AuthContext);
  const [form, setForm] = useState({
    name: '',
    reason: null,
  });
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const history = useHistory();

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.createVacation({...form, startDate, endDate});
    //   alert('Project created');
    //   const { data } = await api.getCurrentUser();
    //   setUser(data);
    //   // TODO: redirect here to project? or handle otherwise
    //   history.goBack();
    } catch (err) {
      console.log('Error', err);
      alert(err.response.data.message);
    }
  };

  return (
    <Layout>
      <div className="m-auto flex flex-col border p-10 w-96 rounded-md">
        <h1 className="font-bold text-3xl">Add vacation</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="w-full mb-5">
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1">
                  Vacation name
            </label>
            <input
              value={form.name}
              onChange={handleInput}
              name="name"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              type="text"
              placeholder="E.g., Trip to Spain 2021"
            />
            <div className="flex flex-col mt-3">
            <label for="cars" className="block text-sm font-medium text-gray-700 mb-1">Vacation reason:</label>
            <select name="reason" id="reason" className="border-gray-300 rounded-md p-2 border" onChange={handleInput} value={form.reason}>
                <option value="HOSPITAL">Hospital</option>
                <option value="FAMILY">Family</option>
                <option value="HOLIDAY">Holiday</option>
                <option value="OTHER">Other</option>
            </select>
            </div>
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1 mt-3">
                  Start Date
            </label>
            <DatePicker selected={startDate} onChange={date => setStartDate(date)} className="border border-gray-300 rounded-md p-2 w-full" />
            <label for="company_website" class="block text-sm font-medium text-gray-700 mb-1 mt-3">
                  End Date
            </label>
            <DatePicker selected={endDate} onChange={date => setEndDate(date)} className="border border-gray-300 rounded-md p-2 w-full" />
          </div>
          <Button 
            type="submit"
             >
              Create Vacation
          </Button>
        </form>
      </div>
    </Layout>
  );
};

export default CreateTeam;
