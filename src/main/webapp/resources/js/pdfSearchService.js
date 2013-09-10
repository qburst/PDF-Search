angular.module('searchApp').factory('pdfSearchService', function() {
	var service = {};
	service.findResults = function(keyword, callback) {
		var matches = [];
		if (keyword) {
			$.ajax({
				type : "GET",
				processData : false,
				url : 'search?query=' + keyword,
				contentType : 'application/json; charset=utf-8',
				dataType : 'json',
				success : function(data) {
					callback(data.aaData);
				},
				error: function(){
					callback();
				}
			});
		}
	};
	service.findMyFiles = function(callback){
		$.ajax({
			type : "GET",
			processData : false,
			url : 'myfiles',
			contentType : 'application/json; charset=utf-8',
			dataType : 'json',
			success : function(data) {
				callback(data.aaData);
			}
		});
	}
	return service;
});
