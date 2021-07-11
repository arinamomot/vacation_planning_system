import React, { useContext, useEffect, useState } from 'react';
import Layout from '../components/Layout';
import { Link } from 'react-router-dom';
import api from '../utils/api';
import AuthContext from '../context/AuthContext';
import Button from '../components/Button';

const Home = () => {
  const [data, setData] = useState({
    team: null,
    teams: null,
  });
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const { data } = await api.getCurrentTeam();
        console.log(data);
        if (data){
            setData({ team: data });
        } else {
            const { data: teams } = await api.getTeams();
            setData({ teams });
        }
      } catch (err) {
        console.log('Error', err);
      }
    };
    fetchData();
  }, []);

  const isAdmin = user.role === 'ADMIN';

  console.log(data);
  return (
    <Layout>
      {!data.team && data.teams && (
        <div className="flex flex-col">
            <div className="flex justify-between">
                <h2 className="font-bold text-3xl mb-8">Teams to join</h2>
                <Link to="/team/create">
                    <Button>
                        Create team
                    </Button>
                </Link>
            </div>
        {data.teams.length ? <div>{data.teams.map(team => <Team team={team}/>)}</div> : <div>There are no teams created</div>}
        </div>)}
      {data.team && (<div className="flex flex-col">
        <h2 className="font-bold text-3xl">{data.team.name}</h2>
        <div className="flex mb-6 mt-8 items-end">
            <h2 className="font-bold text-2xl mr-auto text-gray-700">Groups</h2>
            <Link to={`/team/${data.team.id}/group/create`}><Button>Create Group</Button></Link>
        </div>
        {data.team.groups.length ? <div>{data.team.groups.map(group => <Group group={group} teamId={data.team.id} />)}</div> : <div>There are no groups yet in this team. You can create a group if you are admin.</div>}
        </div>)
      }
      {isAdmin && <div>
        <Button color="flat" className="border border-gray-600 text-gray-600 hover:text-red-600" onClick={async () => {
            await api.deleteTeam(data.team.id);
            window.location.reload();
        }}>Delete team</Button>
      </div>}
    </Layout>
  );
};

const Team = ({team}) => {
    const [requestedToJoin, setRequestedToJoin] = useState(false);
    const [loading, setLoading] = useState(false);

    return <div className={`h-20 p-5 border rounded-md transition duration-400 mb-4 flex justify-between items-center`}>
      <div className={`text-2xl font-bold capitalize text-gray-500`}>{team.name}</div>
      {requestedToJoin ? <div className="font-semibold text-green-400">Join Request Sent!</div> :<Button color="secondary" loading={loading} onClick={async () => {
          setLoading(true);
          await api.requestToJoinTeam(team.id);
          setLoading(false);
          setRequestedToJoin(true)
      }}>Request to join</Button>}
    </div>;
}

const Group = ({group, teamId}) => (
  <Link to={`/team/${teamId}/group/${group.id}`}>
    <div className={`h-20 p-5 cursor-pointer border rounded-md transition duration-400 mb-4 flex justify-between items-center hover:bg-gray-50`}>
      <div className={`text-2xl font-bold capitalize text-gray-500`}>{group.name}</div>
      {/* {group.currUserMember && <div className="text-gray-400 text-sm">You are a member of this team.</div>} */}
    </div>
  </Link>
);

export default Home;
