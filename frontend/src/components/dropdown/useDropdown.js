import { useRef, useState } from 'react';
import { useHistory } from 'react-router-dom';
import useClickOutside from '../../hooks/useClickOutside';


const useDropdown = (handleClick) => {
  const history = useHistory();

  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);
  useClickOutside(dropdownRef, () => {
    if (isOpen) {
      setIsOpen(false);
    }
  });

  const handleItemClick = (
    event,
    { link, id }
  ) => {
    if (link) {
      history.push(link);
    } else if (handleClick) {
      handleClick(id);
    } else {
      // eslint-disable-next-line no-console
      console.error('Not implemented yet!');
    }

    if (isOpen) {
      setIsOpen(false);
    }
  };

  const handleMenuClick = () => {
    if (!isOpen) {
      setIsOpen(true);
    }
  };

  return { dropdownRef, isOpen, handleMenuClick, handleItemClick };
};

export default useDropdown;
