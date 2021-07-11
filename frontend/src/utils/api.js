import axios from 'axios';

// TODO: put URLs to env vars
const accountServiceApi = axios.create({
    baseURL: 'http://localhost:8102/holidayPlanner/',
    timeout: 1000,
    headers: {'Content-Type': 'application/json'},
    withCredentials: true,
});

const notificationServiceApi = axios.create({
    baseURL: 'http://localhost:8080/rest/',
    timeout: 1000,
    headers: {'Content-Type': 'application/json'},
    withCredentials: true,
});


const logoutUser = async () => {
  await accountServiceApi.post('/j_spring_security_logout');
  await notificationServiceApi.post('/j_spring_security_logout');
};

const getCurrentUser = async () => {
  await notificationServiceApi.get('/users/current');
  return accountServiceApi.get('/rest/users/current');
};

const login = async (username, password) => {
  // Spring security expects from data, not json
  const formData = new FormData();
  formData.append('username', username);
  formData.append('password', password);

  const config = {
        headers: {
            'content-type': 'multipart/form-data'
        }
    }

  const user = await accountServiceApi.post('/j_spring_security_check', formData, config);
  await notificationServiceApi.post('/j_spring_security_check', formData, config);

  console.log(user);

  return user;
};

const register = async ({
    email,
    firstName, 
    lastName, 
    password,
    position,
}) => {
    // const loginData = { username, password };
  
    const { data: user } = await accountServiceApi.post('/rest/users', {
        admin: false,
        username: email, // map email to username internally for Spring security
        password,
        role: "USER",
        position,
        firstName, 
        lastName
    });
    await notificationServiceApi.post('/users', { username: email, password, userId: user.id });
  
    return user;
};

const createTeam = async ({
    name,
    closestDayLimit, 
    furthestDayLimit, 
}) => {
    const { data: team } = await accountServiceApi.post('/rest/teams', {
        name,
        closestDayLimit, 
        furthestDayLimit, 
    });
  
    return team;
};

const createGroup = async ({
    name,
}) => {
    const { data: group } = await accountServiceApi.post('/rest/groups', {
        name,
    });
  
    return group;
};

const getGroup = async (groupId) => {
    const { data: group } = await accountServiceApi.get(`/rest/groups/${groupId}`);
  
    return group;
};

const getCurrentTeam = () => {
    return accountServiceApi.get('/rest/teams/current');
}

const joinGroup = ({groupId, userId}) => {
    return accountServiceApi.put(`/rest/groups/${groupId}/members/${userId}`);
}

const leaveGroup = ({groupId, userId}) => {
    return accountServiceApi.delete(`/rest/groups/${groupId}/members/${userId}`);
}

const createVacation = ({
    name, 
    reason, 
    startDate,
    endDate,
}) => {
    const vacationDays = [];
    for (let d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        const dateToAdd = new Date(d);
        if (dateToAdd.getDay() === 0 || dateToAdd.getDay() === 6){
            // weekend day, don't add to vacation
            continue;
        } 

        vacationDays.push(new Date(d));
    }

    const vacationDay = {
        name,
        priority: 'MEDIUM',
        reason: 'HOLIDAY',
    }

    const promises = vacationDays.map((day) => {
         return accountServiceApi.post(`/rest/vacationDays`, {
            ...vacationDay,
            day,
        });
    });

    return Promise.all(promises);
}

const getCurrentUserVacations = () => {
    return accountServiceApi.get('/rest/vacationDays/current');
}

const getNotifications = () => {
    return notificationServiceApi.get('/notifications');
}

const deleteTeam = (teamId) => {
    return accountServiceApi.delete(`/rest/teams/${teamId}`);
}

const getTeams = () => {
    return accountServiceApi.get('/rest/teams');
}

const requestToJoinTeam = (teamId) => {
    return notificationServiceApi.post(`/team/${teamId}/join-request`);
}

const getUsers = () => {
    return accountServiceApi.get('/rest/users');
}

const addMemberToTeam = (userId) => {
    return accountServiceApi.put(`/rest/teams/current/members/${userId}`)
}

const acceptRequestToTeam = async (requestId, teamId, userId) => {
    await addMemberToTeam(userId);
    return notificationServiceApi.post(`/team/${teamId}/join-request/${requestId}/accept`)
}

const declineRequestToTeam = (requestId, teamId) => {
    return notificationServiceApi.post(`/team/${teamId}/join-request/${requestId}/decline`)
}

const getVacationDaysByTeam = (teamId) => {
    return accountServiceApi.get(`/rest/vacationDays/team/${teamId}`)
}

const api = {
    register, 
    login,
    getCurrentUser,
    getCurrentTeam,
    getTeams,
    logoutUser,
    createTeam,
    createGroup,
    getGroup,
    joinGroup,
    leaveGroup,
    createVacation,
    getCurrentUserVacations,
    getNotifications,
    deleteTeam,
    requestToJoinTeam,
    getUsers,
    acceptRequestToTeam,
    declineRequestToTeam,
    getVacationDaysByTeam,
}

export default api;
