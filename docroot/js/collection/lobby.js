define(['backbone', 'model/game'], function(Backbone, GameModel) {
	return Backbone.Collection.extend({
		model: GameModel,
		url: '/games.json'
	});
});