require.config({
	shim: {
		'bootstrap/tooltip': {
			deps: ['jquery'],
			exports: 'jQuery.fn.tooltip'
		},
		'underscore': {
			exports: '_'
		},
		'backbone': {
			deps: ['jquery', 'underscore'],
			exports: 'Backbone'
		}
	}
//	urlArgs: "bust=" +  (new Date()).getTime() // TODO remove before takeoff
});

define(['jquery', 'collection/lobby', 'view/lobby', 'domReady!'],
function(      $,    LobbyCollection,    LobbyView) {
	var lobbyGames = new LobbyCollection();
	var lobby = new LobbyView({
		el: $('#lobby'),
		model: lobbyGames
	});
	
	lobbyGames.fetch();
});
