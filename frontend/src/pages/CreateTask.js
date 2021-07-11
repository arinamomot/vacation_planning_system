import React, { useContext, useEffect, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import moment from 'moment';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import { createTask, getCurrentUsersByProject } from '../utils/api';

const todayDate = new Date();

const clean = (obj) => {
  for (let propName in obj) {
    if (obj[propName] === null || obj[propName] === undefined) {
      delete obj[propName];
    }
  }
  return obj;
};

const CreateTask = () => {
  const [form, setForm] = useState({
    title: '',
    description: '',
    asignee: {},
    deadline: '',
    storyPoints: '',
  });
  const [members, setMembers] = useState([]);
  const [checked, setChecked] = useState([]);
  const { user } = useContext(AuthContext);
  const { id, sprintId } = useParams();
  const history = useHistory();

  const isAdministrator = user.authorities.some((i) => `${id}ADMINISTRATOR` === i.authority);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const membersRes = await getCurrentUsersByProject({ id });
        setMembers(membersRes.data);
      } catch (err) {
        console.log('Error', err);
      }
    };
    fetchData();
  }, []);

  const handleInput = (e) => setForm((state) => ({ ...state, [e.target.name]: e.target.value }));
  const handleDeadline = (e) => setForm((state) => ({ ...state, deadline: new Date(e.target.value) }));
  const handleAsignee = (e) =>
    setForm((state) => ({ ...state, asignee: members.find((i) => i.user.id.toString() === e.target.value).user }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createTask({ id, data: clean(form) });
      alert('Task created');
      history.goBack();
    } catch (err) {
      console.log('Error', err);
      alert(err.response.data.message);
    }
  };

  return (
    <Layout>
      <div className="m-auto flex flex-col shadow-md border p-10 w-96">
        <h1 className="font-bold text-3xl">Create project</h1>
        <form className="flex flex-col mt-5" onSubmit={handleSubmit}>
          <div className="space-y-2 w-full mb-5">
            <input
              value={form.title}
              onChange={handleInput}
              name="title"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              placeholder="Title"
            />
            <textarea
              value={form.description}
              onChange={handleInput}
              name="description"
              className="bg-gray-100 px-5 py-2 w-full"
              required
              placeholder="Description"
            />
            <div className="flex overflow-scroll space-x-3">
              {members.map((i) => (
                <label key={i.user.id} className="flex-shrink-0	 bg-gray-100 p-2">
                  <input type="radio" required onChange={handleAsignee} value={i.user.id} className="mr-2" name="group" />
                  {i.user.username}
                </label>
              ))}
            </div>
            {isAdministrator && (
              <>
                <input
                  value={moment(form.deadline || todayDate).format('YYYY-MM-DD')}
                  onChange={handleDeadline}
                  name="title"
                  className="bg-gray-100 px-5 py-2 w-full"
                  required
                  placeholder="Title"
                  type="date"
                  min={moment(todayDate).format('YYYY-MM-DD')}
                />
                <input
                  value={form.storyPoints}
                  onChange={handleInput}
                  name="storyPoints"
                  className="bg-gray-100 px-5 py-2 w-full"
                  required
                  placeholder="Storypoints"
                  type="number"
                  min={0}
                />
              </>
            )}
          </div>
          <button className="bg-blue-500 text-white font-bold py-2" type="submit">
            Create
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default CreateTask;
