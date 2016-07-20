'use strict'

import React, { PropTypes } from 'react'
import { cyclicIndex } from 'app/arrays'

const NEXT = 1
const PREV = -1

function cycle(options, current, step) {
  var currentIndex = options.indexOf(current)
  var cycledIndex = cyclicIndex(currentIndex + step, options)
  return options[cycledIndex]
}

export default function CycleButtons({options, value, onSelect}) {
  const prev = cycle(options, value, PREV)
  const next = cycle(options, value, NEXT)

  return <div className="btn-group" data-toggle="buttons">
    <button className="btn btn-prev btn-sm btn-default" onClick={() => onSelect(prev)}>
      &lt;
    </button>
    <button className="btn btn-next btn-sm btn-default" onClick={() => onSelect(next)}>
      &gt;
    </button>
  </div>
}
