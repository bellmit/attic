'use strict';

import React, { PropTypes } from 'react';
import asset from 'app/asset';
import * as options from '../character-options';

const SPRITE_HEIGHT = 240; // in SVG image units.
const SPRITE_WIDTH  = 128;

const FACINGS = options.FACINGS.reduce(
  (facings, facing, index) => {
    facings[facing] = -index * SPRITE_HEIGHT;
    return facings;
  },
  {}
);

const CharacterSprite = React.createClass({
  sprite(gender, hair, hat, outfit) {
    return [
      "B", // "battle" sprites
      SPRITE_WIDTH, "x", SPRITE_HEIGHT,
      gender,
      "1", // sprite group
      "H", hair,
      "-", hat,
      "C", outfit,
    ].join("");
  },

  render() {
    /*
     * A quick note about sprite sheets: each distinct outfit is pre-rendered to a single sprite sheet, with its
     * own, distinct asset URI. All of these sheets follow the same layout:
     *
     * * The sheet is divided horizontally in thirds, with one frame of animation per third. The first frame is on
     *   the left, with the animation proceeding right. However, the middle (2nd) frame is reused: once after the
     *   first frame of animation, and once after the third - left, centre, right, centre, left, centre, …
     *
     * * The sheet is divided vertically into quarters, with one facing per quarter. The bottom-left facing is at
     *   the top, followed by the top-left facing, the top-right facing, then finally the bottom-right facing. This
     *   is a simple clockwise procession.
     *
     * The actual animation uses an <svg> element with a bounding box that contains one sprite. The sprite sheet
     * itself is animated onto an image within the <svg>, where it's animated by sliding the image left and right
     * to render the frames. When the character turns, we shift the sheet up or down within the SVG to move the
     * appropriate facing into position.
     *
     * The top-left corner of the current sprite is at the 0 coordinate of the image, with the sprite displayed
     * below and to the right of that point. A sprite is 128 SVG units wide, and 240 svg units tall, though the use
     * of a viewBox to enforce the bounding box allows the SVG image itself to scale to arbitrary sizes without
     * distorting the sprite. (For ease of reference, the aspect ratio is 5 across by 8 tall. Rendering the image
     * with the wrong aspect ratio will show the sprite with the correct scale, but will reveal the extra animation
     * frames around the sprite itself.)
     */
    var {
      facing,
      gender,
      hair,
      hat,
      outfit,
      ...props,
    } = this.props;

    var facingOffset = FACINGS[facing || "bottom-left"];
    var sprite = this.sprite(gender, hair, hat, outfit);
    var path = asset(`/assets/images/characters/${sprite}.png`);

    return (
      <svg {...props}
        viewBox={`0 0 ${SPRITE_WIDTH} ${SPRITE_HEIGHT}`}>
        <image xlinkHref={path}
          x="0"
          y={facingOffset}
          height={/* one row of tiles per facing */ options.FACINGS.length * SPRITE_HEIGHT}
          width={/* three images per row */ 3 * SPRITE_WIDTH}>
          <animate calcMode="discrete"
            attributeName="x"
            begin="0s" dur="1s"
            values={[0, -1 * SPRITE_WIDTH, -2 * SPRITE_WIDTH, -1 * SPRITE_WIDTH, 0].join(";")}
            keyTimes="0; 0.25; 0.5; 0.75; 1"
            repeatCount="indefinite"/>
        </image>
      </svg>
    );
  },
});

module.exports = CharacterSprite;
