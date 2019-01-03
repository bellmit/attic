require.config({
	shim: {
		'bootstrap/tooltip': {
			deps: ['jquery'],
			exports: 'jQuery.fn.tooltip'
		},
		'angular': {
			deps: ['jquery'],
			exports: 'angular'
		}
	}
	// urlArgs: "bust=" +  (new Date()).getTime() // TODO remove before takeoff
});

define(['angular', 'redshift', 'domReady!'],
function( angular) {
	angular.bootstrap(document, ['redshift']);
});
