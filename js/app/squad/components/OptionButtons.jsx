'use strict';

import classNames from 'classnames';
import React, { PropTypes } from 'react';

export const NEXT = 'next';
export const PREV = 'prev';

export default function OptionButtons({options, value, onSelect}) {
  return <div className="btn-group" data-toggle="buttons">
    {options.map(option => (
      <label key={option}
        className={classNames({
          "btn": true,
          "btn-sm": true,
          "btn-default": true,
          "active": value == option,
        })}>
        <input
          type="radio"
          value={option}
          onChange={event => onSelect(option)}
          checked={value == option} /> {option}
      </label>
    ))}
  </div>;
}
