define(['angular', 'redshift/lobby'],
function( angular) {
	return angular.module(
		'redshift',
		['redshift.lobby'],
		function($locationProvider, $routeProvider) {
			$locationProvider.html5Mode(true);
			$routeProvider
				.when('/', { templateUrl: '/fragments/lobby.html', controller: 'lobby' })
				.when('/:gameid', { templateUrl: '/fragments/game.html', controller: 'lobby' })
				.otherwise({redirectTo: '/'});
		}
	);
});
