'use strict';

import React from 'react';
import URI from 'urijs';

const endpointContext = require.context('./endpoints', true, /\.jsx?$/);
const endpoints = endpointContext.keys().map(endpointContext);

export default function Api(baseUrl = appConfig.API_URL) {
  this.baseUrl = new URI(baseUrl);
  this.token = undefined;
}
Api.prototype = Object.assign(Api.prototype,
  {
    resolve(path) {
      var resolvedPath = URI.joinPaths(this.baseUrl, path);
      return new URI(this.baseUrl)
        .path(resolvedPath)
        .toString();
    },

    changeToken(token) {
      this.token = token;
    },
  },
  ...endpoints
);

const apiContext = {
  api: React.PropTypes.object.isRequired,
}

export const ApiProvider = React.createClass({
  childContextTypes: apiContext,

  getChildContext() {
    return {
      api: this.props.api,
    };
  },

  render() {
    return React.Children.only(this.props.children);
  },
});

export function withApi(WrappedComponent, as='api') {
  return React.createClass({
    contextTypes: apiContext,

    displayName: `withApi(${WrappedComponent.displayName})`,

    render() {
      var {api} = this.context;
      var apiProps = {
        [as]: api,
      }

      return <WrappedComponent {...this.props} {...apiProps} />;
    },
  })
}
