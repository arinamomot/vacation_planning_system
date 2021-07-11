import React from 'react';

const DropdownContainer = ({ children, dropdownRef }) => (
  <div
    className="origin-top-right absolute right-0 mt-2 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 overflow-hidden w-50"
    role="menu"
    aria-orientation="vertical"
    aria-labelledby="user-menu"
    ref={dropdownRef}
  >
    {children}
  </div>
);

export default DropdownContainer;
