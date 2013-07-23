define(['angular', 'redshift/lobby'],
function( angular) {
	return angular.module(
		'redshift',
		['redshift.lobby'],
		function($routeProvider) {
			$routeProvider
				.when('/', { templateUrl: '/fragments/lobby.html', controller: 'lobby' })
				.otherwise({redirectTo: '/'});
		}
	);
});
