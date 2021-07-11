import React, { useContext, useEffect, useState } from 'react';
import Layout from '../components/Layout';
import VacationCalendar from '../components/VacationCalendar';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';

const VacationList = () => {
  const [data, setData] = useState({
    vacations: null,
    users: null,
  });
  const [loading, setLoading] = useState(false);
  const { user } = useContext(AuthContext);


  console.log(user);

  useEffect(() => {
     if (!user.team){
         return;
     } 
    const getData = async () => {
            try {
                console.log(user);
                const { data: vacations } = await api.getVacationDaysByTeam(user.team.id);
                const { data: users } = await api.getUsers();
                setData({ vacations, users });
            } catch (err) {
                console.log('Error', err);
            }
    };
    if (loading){
        return;
    }

    getData();
  }, [loading, user]);

  if (!data.vacations){
      return null
  }

  return (<Layout>
    <h2 className="font-bold text-xl mr-auto text-gray-700 mb-10">Your Team's Vacation Calendar</h2>
    <div>
        <VacationCalendar vacations={data.vacations} users={data.users} />
    </div>
    </Layout>);
};

export default VacationList;
