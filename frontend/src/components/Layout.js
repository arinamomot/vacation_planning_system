import React, { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import api from '../utils/api';
import Button from './Button';
import Dashboard from './Dashboard';
import UserDropdown from './dropdown/UserDropdown';

const regularLink = `text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium`;
const activeLink = `bg-gray-900 text-white px-3 py-2 rounded-md text-sm font-medium`;

const Layout = ({ children }) => {
  const { user } = useContext(AuthContext);
  const location = useLocation();
  const isLoggedIn = !!user.id;

  return (
    <div className="min-h-screen flex flex-col">
      <nav class="bg-gray-800">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <img class="h-8 w-8" src="https://tailwindui.com/img/logos/workflow-mark-indigo-500.svg" alt="Workflow" />
          </div>
          <Link to="/"><div className="text-lg font-semibold ml-3 text-white">Holiday Planner</div></Link>
          <div class="hidden md:block">
            {isLoggedIn && <div class="ml-12 flex items-baseline space-x-4">
              <Link to="/" className={location.pathname === '/' ? activeLink : regularLink}>Team</Link>
              {user.team && <Link to="/vacation" className={location.pathname === '/vacation' ? activeLink : regularLink}>Vacation</Link>}
            </div>}
          </div>
        </div>
        <div class="hidden md:block">
          <div class="ml-4 flex items-center md:ml-6">
            {isLoggedIn ? <>
                {user.team && <Link to="/vacation/create">
                    <Button className="mr-6">
                    Add Vacation
                    </Button>
              </Link>}
              <Link to="/notifications">
                <button class="bg-gray-800 p-1 rounded-full text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white">
              <span class="sr-only">View notifications</span>
              <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
              </svg>
            </button>
            </Link>
            <div className="ml-4 relative">
                <UserDropdown />
            </div></>: (<>
              <Link to="/login">
                <Button color="light" className="mr-2">
                  Log In
                </Button>
              </Link>
              <Link to="/register">
                <Button color="secondary">Sign Up</Button>
              </Link>
            </>)}
          </div>
        </div>
        <div class="-mr-2 flex md:hidden">
          <button type="button" class="bg-gray-800 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white" aria-controls="mobile-menu" aria-expanded="false">
            <span class="sr-only">Open main menu</span>
        
            <svg class="block h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <svg class="hidden h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>
    </nav>
      <Dashboard>{children}</Dashboard>
    </div>
  );
};

export default Layout;
