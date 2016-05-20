'use strict';

import React, { PropTypes } from 'react';

export const NEXT = 'next';
export const PREV = 'prev';

export default function CycleButtons({onCycle}) {
  return <div className="btn-group" data-toggle="buttons">
    <button className="btn btn-sm btn-default" onClick={() => onCycle(PREV)}>
      &lt;
    </button>
    <button className="btn btn-sm btn-default" onClick={() => onCycle(NEXT)}>
      &gt;
    </button>
  </div>;
}
