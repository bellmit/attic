module.exports = {
  "env": {
    "browser": true,
    "commonjs": true,
    "es6": true,
    "mocha": true,
  },
  "extends": "eslint:recommended",
  "parserOptions": {
    "ecmaFeatures": {
      "experimentalObjectRestSpread": true,
      "jsx": true,
    },
    "sourceType": "module",
  },
  "plugins": [
    "react",
  ],
  "rules": {
    "indent": ["error", 2],
    "linebreak-style": ["error", "unix"],
    "semi": ["error", "never"],
    "no-console": ["error"],
    "react/jsx-uses-vars": ["error"],
    "react/jsx-uses-react": ["error"],
    "react/react-in-jsx-scope": ["error"],
  },
  "globals": {
    "expect": false,
    "chai": false,
  },
}
