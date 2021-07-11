import React from 'react';
import Spinner from './Spinner';

const sizeClassnames = {
  big: 'py-3 px-6 text-base rounded-md',
  standard: 'py-2 px-6 text-sm rounded-md',
  small: 'px-2 py-1 text-xs rounded-md',
};

const colorClassnames = {
  primary:
    'bg-blue-600 text-white font-semibold shadow-md hover:bg-blue-400',
  neutral: 'text-button hover:bg-gray-200 text-gray-600',
  flat: 'text-gray-300 hover:text-gray-800',
  alert: 'bg-red-600 text-white font-semibold shadow-md hover:bg-red-400',
  light: 'hover:text-gray-300 text-white',
  secondary: 'bg-green-500 text-white hover:bg-green-600',
};

const Button = ({
  children,
  size = 'standard',
  color = 'primary',
  disabled,
  loading,
  icon,
  className = '',
  ...props
}) => (
  <button
    disabled={disabled || loading}
    className={`${sizeClassnames[size]} ${
      disabled
        ? 'bg-gray-300 text-white font-semibold shadow-none cursor-default'
        : colorClassnames[color]
    } font-bold flex items-center justify-center transition duration-500 ease-in-out ${className} ${
      disabled ? 'shadow-none' : ''
    }`}
    data-testid="button"
    {...props}
  >
    <span className={loading ? 'opacity-0' : `flex items-center`}>
      {icon ? <span className="mr-2 items-center">{icon}</span> : null}
      {children}
    </span>
    {loading ? (
      <span className="absolute">
        <Spinner size={size === 'small' ? '2' : '4'} />
      </span>
    ) : null}
  </button>
);

export default Button;
