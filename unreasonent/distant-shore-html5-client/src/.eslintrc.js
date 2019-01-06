module.exports = {
  "env": {
    "browser": true,
    "commonjs": true,
    "es6": true,
  },
  "parserOptions": {
    "ecmaFeatures": {
      "experimentalObjectRestSpread": true,
      "jsx": true,
    },
  },
  "rules": {
    "no-console": ["error", {
      allow: ["warn", "error"],
    }],
  },
  "globals": {
    "appConfig": false,
  },
}
