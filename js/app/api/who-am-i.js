'use strict';

export default function whoAmI(idToken) {
  var options = {
    headers: new Headers({
      'Authorization': `Bearer ${idToken}`,
    }),
  };
  return fetch(`${appConfig.API_URL}/whoami`, options)
    .then(request => request.json())
}
