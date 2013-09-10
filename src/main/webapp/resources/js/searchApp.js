var searchApp = angular.module("searchApp", ['ui.bootstrap']);
searchApp.directive('fileUpload', function () {
    return {
        scope: true,        //create a new scope
        link: function (scope, el, attrs) {
            el.bind('change', function (event) {
                var files = event.target.files;
                //iterate files since 'multiple' may be specified on the element
                for (var i = 0;i<files.length;i++) {
                    //emit event upward
                    scope.$emit("fileSelected", { file: files[i] });
                }                                       
            });
        }
    };
});
searchApp.controller("SearchController", function($scope, $http, $dialog, pdfSearchService) {
	$scope.active = { one: true	};
	pdfSearchService.findMyFiles(function(results) {
		$scope.myFiles = results;
		$scope.$apply();
	});
	$scope.opts = {
		    backdrop: true,
		    keyboard: true,
		    backdropClick: true,
		    templateUrl:  'resources/template/show.html', 
		    controller: 'ShowController'
	};
	$scope.submitSearch = function() {
		$scope.results = [];
		var keyword = $scope.searchKeyword;
		pdfSearchService.findResults(keyword, function(results) {
			if(results){
				$scope.results = results;
				$('.results-div').hide();
				if(results.length > 0){
					$('.results-div').show();
				}				
			}
			if(!$scope.active.one)
				$scope.active = { one: true	};
			$scope.$apply();
		});
	};	
	$scope.show = function(item){
		angular.extend($scope.opts, {resolve: {item: function(){ return angular.copy(item); }}})
	    var d = $dialog.dialog($scope.opts);
	    d.open();
	};
	//listen for the file selected event
	$scope.files = [];
	$scope.limitExceeded = true;
    $scope.$on("fileSelected", function (event, args) {
        $scope.$apply(function () {            
            //add the file object to the scope's files collection
        	var file = args.file;
        	if(file.type == "application/pdf") file.response = "resources/images/success.png"
        	else  file.response = "resources/images/error.png"
        	if($scope.files.length > 10){
        		$scope.limitAlert = "Upload limit exceeded. Upload limit is 10 files";
        		$scope.limitExceeded = false;
        		event.preventDefault();
        	}
        	else{
        		$scope.files.push(file);
        		$scope.limitExceeded = true;
        	}
    	});
    });
    $scope.deleteFile = function(index) {
    	$scope.files.splice(index,1);
    	if($scope.files.length <= 10)
    		$scope.limitExceeded = true;
    };
    $scope.upload = function(){
    	$scope.uploading = true;
    	var d = $dialog.dialog({ backdrop: true, keyboard: false, backdropClick: false, dialogFade: false})
    	d.open('resources/template/progress_monitor.html');
    	if($scope.files.length > 0){
    		$http({
                method: 'POST',
                url: "upload",
                headers: { 'Content-Type': false },
                //This method will allow us to change how the data is sent up to the server
                // for which we'll need to encapsulate the model data in 'FormData'
                transformRequest: function (data) {
                    var formData = new FormData();
                    $.each(data.files,function(index,file){
                    	if(file.type == "application/pdf")
                    		formData.append("files[]", file);
                    });
                    return formData;
                },
                //Create an object that contains the model and files which will be transformed
                // in the above transformRequest method
                data: { files: $scope.files }
            }).
            success(function (data, status, headers, config) {
            	d.close(undefined);
            	$scope.showUploadStatus(data);
            	$scope.files = [];
                $scope.uploading = false;
            }).
            error(function (data, status, headers, config) {
            	d.close(undefined);
            	$scope.uploading = false;
            	alert("failed!");
            });
    	}
    };
    $scope.showUploadStatus = function(files){
    	var d = $dialog.dialog({ backdrop: true, keyboard: true, backdropClick: true, dialogFade: true, resolve: {files: function(){ return angular.copy(files); } }})
    	d.open('resources/template/upload_status.html', 'UploadController');
    }
});
searchApp.controller("ShowController", function($scope, item, dialog) {
	$scope.item = item;
	$scope.close = function(){
	   dialog.close(undefined);
	};
});
searchApp.controller("UploadController", function($scope, files, dialog) {
	$scope.files = files;
	$scope.close = function(){
	   dialog.close(undefined);
	};
});
searchApp.directive('drawVisualization', function ($dialog,$compile) {
    return {
		restrict: 'E',
        link: function (scope, element, attrs) {
        	scope.$watch('results', function (newVal, oldVal) {
        		if (!newVal || newVal == []) {
        	          return;
    	        }
        		var w = 1210, h = 360, nodes = newVal, highlightsArr = [];
        		var color = d3.scale.category20();
        		if($(".svg_vis").length > 0)
        			$(".svg_vis").remove();
        		if(nodes.length <= 0)
        			return;
        		var force = d3.layout.force()
        		    .gravity(0.35)
        		    .charge(function(d, i) { return i ? 0 : -2000; })
        		    .nodes(nodes)
        		    .size([w, h]);
        		force.start();
        		var svg = d3.select(element[0])
        			.append("svg:svg")
        		    .attr("width", w)
        		    .attr("height", h)
        		    .attr("class","svg_vis");
        		nodes.forEach(function(d,i){ 
        			highlightsArr.push(d.highlights.length); 
        		});
        		var min = 6, max = 50;
        		var bisect = d3.bisector(function(d) { return d; }).right;
        		highlightsArr.sort(function(a,b){return d3.ascending(a,b)});
        		if(bisect(highlightsArr,d3.median(highlightsArr)) == highlightsArr.length)
        			min = max; 
        		else if (bisect(highlightsArr,d3.median(highlightsArr))>(highlightsArr.length/2))
        			max = 30;       
        		var radiusScale = d3.scale.linear()
        			.domain(d3.extent(highlightsArr))
        			.range([min, max]);
        		svg.append("svg:rect")
        		    .attr("width", w)
        		    .attr("height", h);
        		var circle = svg.selectAll("circle")
        		    .data(nodes)
        		    .enter().append("svg:circle")
        		    .attr("r", function(d) {
        		    	d.radius = radiusScale(d.highlights.length);
        		    	return d.radius; 
        		    })
        		    .attr("ng-click",function(d){ 
        		    	scope["node"+d.id] = d;
        		        return "show(node"+d.id+");"
        		    })
        		    .style("fill", function(d, i) { return color(i); })
        		    .call(force.drag)
        		    .append("svg:title")
        		    .text(function(d){ return d.title; });
        		var card_overlay_html = $compile(svg[0])(scope);
        		$(element).parent().append(card_overlay_html);
        		force.on("tick", function(e) {
        			  var q = d3.geom.quadtree(nodes),
        			      i = 0,
        			      n = nodes.length;
        			  while (++i < n) q.visit(collide(nodes[i]));
        			  svg.selectAll("circle")
        			      .attr("cx", function(d) { return d.x = Math.max(d.radius, Math.min(w - d.radius, d.x))  })
        			      .attr("cy", function(d) { return d.y = Math.max(d.radius, Math.min(h - d.radius, d.y)); });
        		});
        		function collide(node) {
        			var r = node.radius + 16,
        				nx1 = node.x - r,
        				nx2 = node.x + r,
        				ny1 = node.y - r,
        				ny2 = node.y + r;
        			return function(quad, x1, y1, x2, y2) {
        				if (quad.point && (quad.point !== node)) {
        					var x = node.x - quad.point.x,
        					y = node.y - quad.point.y,
        					l = Math.sqrt(x * x + y * y),
        					r = node.radius + quad.point.radius;
        					if (l < r) {
        						l = (l - r) / l * .5;
        						node.x -= x *= l;
        						node.y -= y *= l;
        						quad.point.x += x;
        						quad.point.y += y;
        					}
        				}
        				return x1 > nx2 || x2 < nx1 || y1 > ny2 || y2 < ny1;
        			};
        		}
        	});
    	}
	}
});
