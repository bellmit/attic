'use strict';

import React, { PropTypes } from 'react';

export const NEXT = 'next';
export const PREV = 'prev';

const CycleButtons = React.createClass({
  propTypes: {
    onCycle: PropTypes.func.isRequired,
  },

  render() {
    return <div className="btn-group" data-toggle="buttons">
      <button className="btn btn-sm btn-default" onClick={() => this.props.onCycle(PREV)}>
        &lt;
      </button>
      <button className="btn btn-sm btn-default" onClick={() => this.props.onCycle(NEXT)}>
        &gt;
      </button>
    </div>;
  },
});

export default CycleButtons;
