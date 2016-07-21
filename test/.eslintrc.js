module.exports = {
  "env": {
    "browser": true,
    "commonjs": true,
    "es6": true,
    "mocha": true,
  },
  "parserOptions": {
    "ecmaFeatures": {
      "experimentalObjectRestSpread": true,
      "jsx": true,
    },
  },
  "rules": {
    "no-console": ["error"],
  },
  "globals": {
    "expect": false,
    "chai": false,
  },
}
