import React from 'react';


const DropdownItem = ({ option, handleItemClick, children }) => (
  <button
    className={`block px-3 py-2 text-sm text-gray-700 w-full border-b border-gray-200 ${option.notInteractive ? 'cursor-default' : 'hover:bg-gray-100'}`}
    role="menuitem"
    onClick={option.notInteractive ? null : (e) => handleItemClick(e, option)}
  >
    {children}
  </button>
);

export default DropdownItem;
