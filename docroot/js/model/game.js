define(['backbone'], function(Backbone) {
	return Backbone.Model.extend({
		defaults: {
			config: {},
			"id": null,
			"name": "",
			"open": true,
			"status": "SETUP",
			"turn": 0
		}
	});
});