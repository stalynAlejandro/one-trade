import React, { useState } from 'react';

const FormControlWrapper: React.FC<any> = ({
  component: Component,
  onChange,
  value,
  checked = undefined,
  ...rest
}) => {
  const [innerValue, setInnerValue] = useState<any>(value || checked || '');
  const handleOnChange = onChange
    ? (val: any) => {
        setInnerValue(val);
        onChange(val);
      }
    : undefined;

  return (
    <Component
      checked={onChange ? innerValue : undefined}
      value={onChange ? innerValue : undefined}
      onChange={handleOnChange}
      {...rest}
    />
  );
};

export default FormControlWrapper;
