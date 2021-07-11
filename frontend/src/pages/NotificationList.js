import React, { useEffect, useState } from 'react';
import Button from '../components/Button';
import Layout from '../components/Layout';
import api from '../utils/api';

const capitalize = (value) =>
  value.charAt(0).toUpperCase() + value.slice(1).toLowerCase()

const notificationTypeMap = {
    'REQUEST_TO_TEAM': 'Request to Join Team',
    'REQUEST_RESULT': 'Request to Join Team - Decision',
    'REQUEST_TO_TEAM_RESULT': 'Request to Join Team - Decision',
}

const NotificationList = () => {
  const [data, setData] = useState({
    notifications: null,
    teams: null,
    users: null,
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (loading){
        return;
    }

    const getData = async () => {
      try {
        const {data: notifications} = await api.getNotifications();
        const {data: teams} = await api.getTeams();
        const {data: users} = await api.getUsers();
        setData({ notifications, teams, users});
      } catch (err) {
        console.log('Error', err);
      }
    };

    getData();
  }, [loading]);

  if (!data.notifications){
      return null
  }

  return (<Layout>
    <h2 className="font-bold text-xl mr-auto text-gray-700 mb-4">Your Notifications</h2>
    <div>
        {data.notifications.length ? <div>
            {data.notifications.map(notification => <Notification notification={notification} teams={data.teams} users={data.users} loading={loading} setLoading={setLoading} />)}
        </div> : 'You do not have any notifications yet.'}
    </div>
    </Layout>);
};


const getStateColor = (state) => {
    switch (state) {
      case 'IN_PROGRESS':
        return 'bg-blue-400';
      case 'ACCEPTED':
          return 'bg-green-400';
      case 'DECLINED':
           return 'bg-red-400';
      default:
        return 'bg-gray-300';
    }
  };

const getSuccessBadge = (notification) => {
    return <span
                className={`font-bold ml-4 inline-flex items-center justify-center px-2 py-1 md:px-2 md:py-2 text-xs font-semibold leading-none text-white rounded-md ${notification.successful ? 'bg-green-400' : 'bg-red-400'}`}
            >
                {notification.successful ? 'Approved' : 'Denied'}
            </span>
}

const Notification = ({ notification, teams, users, loading, setLoading }) => {

    const originator = users.find(({id}) => id === notification.originatorUserId);

    return (
            <div className="p-5 border hover:bg-gray-50 rounded-md transition duration-400 mb-4 relative">
                <div className="font-semibold mb-2 flex items-baseline">{notificationTypeMap[notification.notificationType]} {notification.notificationType === 'REQUEST_TO_TEAM_RESULT' && getSuccessBadge(notification)}</div>
                <div className="flex justify-between">
                    Team: {(teams.find(({id}) => id === notification.teamId) || {}).name || 'Team not found'}
                </div>
                <div className="flex justify-between items-end">
                    <div className="mt-6 text-gray-600">
                        By: { originator ? `${originator.firstName} ${originator.lastName}` : 'User not found'} on {new Date(notification.created).toLocaleDateString()} at {new Date(notification.created).toLocaleTimeString()}
                    </div>
                    {notification.notificationType === 'REQUEST_TO_TEAM' && notification.state === 'IN_PROGRESS' && <div className="flex">
                        <Button 
                            disabled={loading}
                            color="secondary"
                            className="mr-4" onClick={async () => {
                            setLoading(true);
                            await api.acceptRequestToTeam(notification.id, notification.teamId, notification.originatorUserId);
                            setLoading(false);
                        }}>Accept</Button>
                        <Button  disabled={loading} color="alert" onClick={async () => {
                            setLoading(true);
                            await api.declineRequestToTeam(notification.id, notification.teamId)
                            setLoading(false);
                        }}>Decline</Button>
                        </div>}
                </div>
                {notification.state && <span
                className={`font-bold absolute top-1 right-1 inline-flex items-center justify-center px-2 py-1 md:px-2 md:py-2 text-xs font-semibold leading-none text-white transform translate-x-2 -translate-y-1/2 rounded-md ${getStateColor(notification.state)}`}
            >
                {capitalize(notification.state).split('_').join(' ')}
            </span>}
            </div>
         );
}

export default NotificationList;
