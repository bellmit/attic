'use strict';

import React from 'react';
import { Link } from 'react-router';
import classNames from 'classnames';
import asset from 'App/asset';

const SPRITE_HEIGHT = 240; // in SVG image units.
const SPRITE_WIDTH  = 128;

const FACINGS = {
     "bottom-left":   0,
     "top-left":     -1 * SPRITE_HEIGHT,
     "top-right":    -2 * SPRITE_HEIGHT,
     "bottom-right": -3 * SPRITE_HEIGHT,
 };

const GENDERS = ["F", "M"];
const HAIRS = ["1", "2", "3"];
const HATS = ["0", "1", "2"];
const OUTFITS = ["1", "2", "3", "4", "5", "6"];

/*
 * Given an integer and an array, normalize the integer into an array slot, as if the array repeats indefinitely in both
 * directions. This is "just" `value mod array.length`, but JS' remainder operator doesn't have the right sign rules
 * for this use case.
 */
function mapToArrayIndex(value, array) {
    while (value < 0) {
        value += array.length;
    }
    while (value >= array.length) {
        value -= array.length;
    }
    return value;
};

/* Given an array, return a random index. */
function randomIndex(array) {
    return Math.floor(Math.random() * array.length);
}

var CharacterSprite = React.createClass({
    sprite(gender, hair, hat, outfit) {
        return [
            "B", // "battle" sprites
            SPRITE_WIDTH, "x", SPRITE_HEIGHT,
            GENDERS[gender] || GENDERS[0],
            "1", // sprite group
            "H", HAIRS[hair] || HAIRS[0],
            "-", HATS[hat] || HATS[0],
            "C", OUTFITS[outfit] || OUTFITS[0],
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
                    height={/* one row of tiles per facing */ 4 * SPRITE_HEIGHT}
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

var CharacterEditor = React.createClass({
    getInitialState() {
        return {
            name: "",
            gender: randomIndex(GENDERS),
            hat: randomIndex(HATS),
            hair: randomIndex(HAIRS),
            outfit: randomIndex(OUTFITS),
        };
    },

    onNameChange(event) {
        this.setState({
            name: event.target.value,
        });
    },

    onGenderChange(event) {
        this.setState({
            gender: event.target.value,
        });
    },

    onHatDown(event) {
        this.setState({
            hat: mapToArrayIndex(this.state.hat - 1, HATS),
        });
    },

    onHatUp(event) {
        this.setState({
            hat: mapToArrayIndex(this.state.hat + 1, HATS),
        });
    },

    onHairDown(event) {
        this.setState({
            hair: mapToArrayIndex(this.state.hair - 1, HAIRS),
        });
    },

    onHairUp(event) {
        this.setState({
            hair: mapToArrayIndex(this.state.hair + 1, HAIRS),
        });
    },

    onOutfitDown(event) {
        this.setState({
            outfit: mapToArrayIndex(this.state.outfit - 1, OUTFITS),
        });
    },

    onOutfitUp(event) {
        this.setState({
            outfit: mapToArrayIndex(this.state.outfit + 1, OUTFITS),
        });
    },

    render() {
        return (
            <div className="row">
                <div className="col-md-1">
                    <div className="form-group">
                        <div className="btn-group" data-toggle="buttons">
                            <button className="btn btn-sm btn-default" onClick={this.onHatDown}>
                                &lt;
                            </button>
                            <button className="btn btn-sm btn-default" onClick={this.onHatUp}>
                                &gt;
                            </button>
                        </div>
                    </div>

                    <div className="form-group">
                        <div className="btn-group" data-toggle="buttons">
                            <button className="btn btn-sm btn-default" onClick={this.onHairDown}>
                                &lt;
                            </button>
                            <button className="btn btn-sm btn-default" onClick={this.onHairUp}>
                                &gt;
                            </button>
                        </div>
                    </div>

                    <div className="form-group">
                        <div className="btn-group" data-toggle="buttons">
                            <button className="btn btn-sm btn-default" onClick={this.onOutfitDown}>
                                &lt;
                            </button>
                            <button className="btn btn-sm btn-default" onClick={this.onOutfitUp}>
                                &gt;
                            </button>
                        </div>
                    </div>

                    <div className="form-group">
                        <div className="btn-group" data-toggle="buttons">
                            {GENDERS.map((label, gender) => (
                                <label key={gender}
                                    className={classNames({
                                        "btn": true,
                                        "btn-sm": true,
                                        "btn-default": true,
                                        "active": this.state.gender == gender,
                                    })}>
                                    <input
                                        type="radio"
                                        name="gender"
                                        value={gender}
                                        onChange={this.onGenderChange}
                                        checked={this.state.gender == gender} /> {label}
                                </label>
                            ))}
                        </div>
                    </div>
                </div>

                <div className="col-md-2">
                    <CharacterSprite width="100" height="160" className="center-block" facing="bottom-right" {...this.state} />
                </div>

                <div className="col-md-7">
                    <div className="form-group">
                        <label>Name
                            <input type="text" className="form-control" placeholder="Character name…" value={this.state.name} onChange={this.onNameChange} />
                        </label>
                    </div>
                </div>
            </div>
        );
    },
});

var SquadEditor = module.exports = React.createClass({
    render() {
        return (
            <div className="container">
                <h1>Your Squad</h1>
                <CharacterEditor />
                <hr />
                <CharacterEditor />
                <hr />
                <CharacterEditor />
                <Link className="btn btn-primary" to="/">
                    Save &amp; return to lobby
                </Link>
            </div>
        );
    },
});
