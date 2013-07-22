define(['backbone', 'mustache', 'text!template/lobby/game.ms'],
function( Backbone,   Mustache,                  gameTemplate) {
	return Backbone.View.extend({
		template: Mustache.compile(gameTemplate),
		tagName: 'tr',

		initialize: function() {
			this.listenTo(this.model, 'change', this.render);
		},

		render: function() {
			this.$el.html(this.template(this.model.toJSON()));
			return this;
		}
	});
});
