import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/Button';
import Layout from '../components/Layout';
import AuthContext from '../context/AuthContext';
import useGroupId from '../hooks/useGroupId';
import api from '../utils/api';
import ArrowLeft from '../components/icons/ArrowLeft';

const Group = () => {
  const groupId = useGroupId();
  const [data, setData] = useState({
    group: null,
  });
  const [loading, setLoading] = useState(false);
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const getData = async () => {
      try {
        const group = await api.getGroup(groupId);
        setData({ group });
        console.log('members: ', group.members);
      } catch (err) {
        console.log('Error', err);
      }
    };
    if (loading){
        return;
    }

    getData();
  }, [loading]);

  const isMember = data.group && data.group.members.some(member => member.id === user.id);

  console.log(isMember);
  return (<Layout>
  {data.group && (<div>
    <h2 className="font-bold text-3xl flex items-center">
        <Link to="/">
            <span className="text-gray-500 hover:text-gray-900">
                <ArrowLeft />
            </span>
        </Link>
        <span className="ml-3 text-lg text-gray-800">Group Detail</span></h2>
    <div className="flex mb-6 mt-8 items-end">
            <h2 className="font-bold text-2xl mr-auto text-gray-700">{data.group.name}</h2>
            {!isMember ? <Button 
            loading={loading}
            onClick={async () => {
                setLoading(true);
                await api.joinGroup({ groupId: data.group.id, userId: user.id });
                setLoading(false);
            }}>Join Group</Button> : <Button 
            color="alert"
            loading={loading}
            onClick={async () => {
                setLoading(true);
                await api.leaveGroup({ groupId: data.group.id, userId: user.id });
                setLoading(false);
            }}>Leave Group</Button>}
        </div>
    <h2 className="font-bold text-xl mr-auto text-gray-700 mb-4 mt-10">Members</h2>
    <div>
        {data.group.members.length > 0 ? <div>
            {data.group.members.map(member => 
                (<div className="p-5 cursor-pointer border hover:bg-gray-50 rounded-md transition duration-400 mb-4">
                {member.firstName} {member.lastName}
            </div>))}
        </div> : 'There are no members yet.'}
    </div>

  </div>)}
</Layout>);
};

export default Group;
