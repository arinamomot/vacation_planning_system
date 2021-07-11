import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Home from './pages/Home';
import CreateTeam from './pages/CreateTeam';
import Login from './pages/Login';
import Register from './pages/Register';
import CreateGroup from './pages/CreateGroup';
import Group from './pages/Group';
import CreateVacation from './pages/CreateVacation';
import VacationList from './pages/VacationList';
import NotificationList from './pages/NotificationList';

const Routes = ({ logged }) => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/" component={Home} />
      <Route exact path="/team/create" component={CreateTeam} />
      <Route exact path="/team/:id/group/create" component={CreateGroup} />
      <Route exact path="/team/:id/group/:groupId" component={Group} />
      <Route exact path="/vacation/create" component={CreateVacation} />
      <Route exact path="/vacation" component={VacationList} />
      <Route exact path="/notifications" component={NotificationList} />
      <Route path="/login" component={Login} />
      <Route path="/register" component={Register} />
    </Switch>
  </BrowserRouter>
);

export default Routes;
