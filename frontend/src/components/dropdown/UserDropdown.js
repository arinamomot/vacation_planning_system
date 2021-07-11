import React, { useContext } from 'react';
import DropdownItem from './DropdownItem';
import DropdownContainer from './DropdownContainer';
import DropdownButton from './DropdownButton';
import useDropdown from './useDropdown';
import api from '../../utils/api';
import AuthContext from '../../context/AuthContext';

const userDropdownOptions = (user) => [
  {
      name: <div className="font-semibold">
          <div>{user.firstName} {user.lastName}</div>
          {user.username}
      </div>,
      notInteractive: true, 
  },
  {
    name: 'Logout',
  },
];

const UserDropdown = ({ simpleLayout }) => {
    const handleLogout = async () => {
        await api.logoutUser();
        window.location.reload();
    };

    const { user } = useContext(AuthContext);

  const {
    dropdownRef,
    isOpen,
    handleMenuClick,
    handleItemClick,
  } = useDropdown(handleLogout);
  const options = userDropdownOptions(user);

  return (
    <>
      <DropdownButton
        imgSrc="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
        imgAlt="User Avatar"
        handleMenuClick={handleMenuClick}
      />
      {isOpen && (
        <DropdownContainer dropdownRef={dropdownRef}>
          {options.map((option) => (
            <DropdownItem
              option={option}
              handleItemClick={handleItemClick}
              key={option.name}
            >
              {option.name}
            </DropdownItem>
          ))}
        </DropdownContainer>
      )}
    </>
  );
};

export default UserDropdown;
