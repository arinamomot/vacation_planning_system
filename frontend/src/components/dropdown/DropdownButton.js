import React from 'react';

const DropdownButton = ({ handleMenuClick, imgSrc, imgAlt }) => (
  <div>
    <button
      type="button"
      className="bg-gray-800 flex text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
      id="user-menu"
      aria-expanded="false"
      aria-haspopup="true"
      onClick={handleMenuClick}
    >
      <span className="sr-only">Open dropdown</span>
      <img className="h-8 w-8 rounded-full" src={imgSrc} alt={imgAlt} />
    </button>
  </div>
);

export default DropdownButton;
