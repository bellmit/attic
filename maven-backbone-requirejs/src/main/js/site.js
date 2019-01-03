require.config({
	shim: {
		'bootstrap/tooltip': {
			deps: ['jquery'],
			exports: ['jQuery.fn.tooltip']
		}
	}
});

define(['jquery', 'bootstrap/tooltip'], function($) {
	$(function() {
		$('#hello-world').tooltip();
		$('#hello-world').on('click', function() {
			$('h1').text('Hi there!');
		});
	});
});
