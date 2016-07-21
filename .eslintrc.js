module.exports = {
  "root": true,
  "extends": "eslint:recommended",
  "parserOptions": {
    "sourceType": "module",
  },
  "plugins": [
    "react",
  ],
  "rules": {
    "indent": ["error", 2],
    "linebreak-style": ["error", "unix"],
    "semi": ["error", "never"],
    "react/jsx-uses-vars": "error",
    "react/jsx-uses-react": "error",
    "react/react-in-jsx-scope": "error",
  },
}
