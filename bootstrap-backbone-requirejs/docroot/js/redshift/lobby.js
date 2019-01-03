define(['angular'],
function( angular) {
	function lobby($scope, $http) {
		$http
			.get('/games.json')
			.success(function(data) {
				$scope.games = data;
			});
	}

	return angular.module(
		'redshift.lobby',
		[],
		function($controllerProvider) {
			$controllerProvider.register('lobby', lobby);
		}
	);
});
