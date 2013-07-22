define(['backbone', 'view/lobby/game', 'text!template/lobby.ms'],
function( Backbone,     LobbyGameView,            lobbyTemplate) {
	return Backbone.View.extend({
		tagName: 'table',

		initialize: function() {
			this.$el.html(lobbyTemplate);
			this.$tbody = this.$('tbody');

			this.listenTo(this.model, 'add', this.addGame);
			this.listenTo(this.model, 'reset', this.gamesLoaded);
		},

		addGame: function(game) {
			var view = new LobbyGameView({
				model: game
			});
			this.$tbody.append(view.render().el);
		},

		gamesLoaded: function() {
			this.$tbody.empty();
			this.games.each(this.add, this);
		}
	});
});
