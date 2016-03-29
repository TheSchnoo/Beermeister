var mockMode = true;
var debugMode = false;
var app = angular.module('Brewmaster', ['ngMaterial']);
app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});

app.controller('AppCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	if (mockMode) {
		$scope.beers = [
		 	{
			  	"name": "Thunderbird Lager",
			  	"brewery": "UBC Brewery",
			  	"type": "Lager",
			  	"abv": "5.1%",
			  	"ibu": "10 bitterness units",
			  	"imageLocation": "images/stock-beer.jpg"
		  	},
		  	{	
			  	"name": "Passive Aggressive",
			  	"brewery": "Brassneck",
			  	"type": "IPA",
			  	"abv": "5.3%",
			  	"ibu": "2 bitterness units",
			  	"imageLocation": "images/ipa.jpg"
		  	},
		  	{
			  	"name": "Southern Hop",
			  	"brewery": "Main Street Brewery",
			  	"type": "IPA",
			  	"abv": "6.1%",
			  	"ibu": "20 bitterness units",
			  	"imageLocation": "images/stock-beer.jpg"
			},
			{
			  	"name": "Sun God Wheat Ale",
			  	"brewery": "R&B Brewery",
			  	"type": "Hefeweizen",
			  	"abv": "5.6%",
			  	"ibu": "2 bitterness units",
			  	"imageLocation": "images/TownHallHefeweizen.jpg"
			}
	  	]
  	} else {
  		$http({
		    method: 'GET',
		    url: 'http://localhost:8020/?/recommendedbeers?userid=1'
		}).then(function successCallback(response) {
			console.log('recommended beers are ' + JSON.stringify(response.data));
		    $scope.beers=response.data;
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});	
  	}

  	$scope.showVendors = function(ev, beer) {
  		if (mockMode){
  			$rootScope.vendors = [
				{
					"storeName":"Darby's Liquor Store"
				},
				{
					"storeName":"UBC Liquor Store"
				},
				{	
					"storeName":"Legacy Liquor Store"
				}
			];
			var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
			    $mdDialog.show({
			        controller: DialogController,
			      	templateUrl: 'app/vendordialog.html',
			      	parent: angular.element(document.body),
			      	targetEvent: ev,
			      	clickOutsideToClose:true,
			      	fullscreen: useFullScreen
			    });

  		} else {
  			console.log('making HTTP GET Request');
  			var baseURL =  'http://localhost:8020/?/vendors/'
  			$http({
			  method: 'GET',
			  url: baseURL + beer.name
			}).then(function successCallback(response) {
			    $rootScope.vendors = response.data;
			    console.log(JSON.stringify(response.data));
			 //    $rootScope.vendors = [
				// 	{
				// 		"name":"Darby's Liquor Store"
				// 	},
				// 	{
				// 		"name":"UBC Liquor Store"
				// 	},
				// 	{	
				// 		"name":"Legacy Liquor Store"
				// 	}
				// ];
			    console.log('successfully got vendors of' + response.data);
			    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
			    $mdDialog.show({
			        controller: DialogController,
			      	templateUrl: 'app/vendordialog.html',
			      	parent: angular.element(document.body),
			      	targetEvent: ev,
			      	clickOutsideToClose:true,
			      	fullscreen: useFullScreen
			    });

				// $mdDialog.show(
				// 	$mdDialog.alert()
				// 	.parent(angular.element(document.querySelector('#popupContainer')))
				// 	.clickOutsideToClose(true)
				// 	.title('Showing ratings for ' + beer.name)
				// 	.textContent(JSON.stringify(response.data))
				// 	.ariaLabel('Alert Dialog Demo')
				// 	.ok('Got it!')
				// 	.targetEvent(ev)
			 //    );
			  }, function errorCallback(response) {
			    // console.log("BLAHHH");
			  });

  		}
  		// console.log(JSON.stringify($rootScope.vendors));

	}

  // 	$scope.showVendors = function(ev, beer) {
  //   // Appending dialog to document.body to cover sidenav in docs app
  //   // Modal dialogs should fully cover application
  //   // to prevent interaction outside of dialog
	 //    $mdDialog.show(
		// 	$mdDialog.alert()
		// 	.parent(angular.element(document.querySelector('#popupContainer')))
		// 	.clickOutsideToClose(true)
		// 	.title('Showing vendors for ' + beer.name)
		// 	.textContent('You can specify some description text in here.')
		// 	.ariaLabel('Alert Dialog Demo')
		// 	.ok('Got it!')
		// 	.targetEvent(ev)
	 //    );

		// // console.log("clicked on a beer's vendors" + beer.name);
  // 	};

  	$scope.showRatings = function(ev, beer) {

	    $mdDialog.show(
			$mdDialog.alert()
			.parent(angular.element(document.querySelector('#popupContainer')))
			.clickOutsideToClose(true)
			.title('Showing ratings for ' + beer.name)
			.textContent('You can specify some description text in here.')
			.ariaLabel('Alert Dialog Demo')
			.ok('Got it!')
			.targetEvent(ev)
	    );

		// console.log("clicked on a beer's vendors" + beer.name);
  	};

});

app.controller('SearchCtrl', function($scope, $http, $timeout) {
    $scope.beer = {};
    $scope.beerTypeCategories = ["IPA", "Pilsner", "Porter", "Stout", "Lager"];
    $scope.abvCategories = ["<4%", "4-4.99%", "5-5.99%", "6-6.99%", ">7%" ];
    $scope.ibuCategories = ["<10", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69", ">70"];
    $scope.ratingCategories = [">1 Star", ">2 Stars", ">3 Stars", ">4 Stars"];
    $scope.searchResults = [];
    $scope.count = 10;
	//  	{
	// 	  	"name": "Thunderbird Lager",
	// 	  	"brewery": "UBC Brewery",
	// 	  	"type": "Lager",
	// 	  	"abv": "5.1%",
	// 	  	"ibu": "10 bitterness units",
	// 	  	"imageLocation": "images/stock-beer.jpg"
	//   	},
	//   	{	
	// 	  	"name": "Passive Aggressive",
	// 	  	"brewery": "Brassneck",
	// 	  	"type": "IPA",
	// 	  	"abv": "5.3%",
	// 	  	"ibu": "2 bitterness units",
	// 	  	"imageLocation": "images/ipa.jpg"
	//   	},
	//   	{
	// 	  	"name": "Southern Hop",
	// 	  	"brewery": "Main Street Brewery",
	// 	  	"type": "IPA",
	// 	  	"abv": "6.1%",
	// 	  	"ibu": "20 bitterness units",
	// 	  	"imageLocation": "images/stock-beer.jpg"
	// 	},
	// 	{
	// 	  	"name": "Sun God Wheat Ale",
	// 	  	"brewery": "R&B Brewery",
	// 	  	"type": "Hefeweizen",
	// 	  	"abv": "5.6%",
	// 	  	"ibu": "2 bitterness units",
	// 	  	"imageLocation": "images/TownHallHefeweizen.jpg"
	// 	}
	// ];

	// setInterval(function() {
 //        console.log($scope.count++);
 //        $scope.$apply() 
 //    }, 500);

    $scope.submitSearch = function(ev){
  
    	console.log('attempting to submit search');
    	if (mockMode){
    	// console.log("Search submitted!");
    	// console.log("Submitting a search of: " + $scope.beer);
	  
            $scope.count++;
            $scope.$apply();
   //  		console.log("$scope.count is now " + $scope.count);
	  //   	$scope.searchResults = [
			//  	{
			// 	  	"name": "PLACEHOLDER 1",
			// 	  	"brewery": "UBC Brewery",
			// 	  	"type": "Lager",
			// 	  	"abv": "5.1%",
			// 	  	"ibu": "10 bitterness units",
			// 	  	"imageLocation": "images/stock-beer.jpg"
			//   	},
			//   	{	
			// 	  	"name": "PLACEHOLDER 2",
			// 	  	"brewery": "Brassneck",
			// 	  	"type": "IPA",
			// 	  	"abv": "5.3%",
			// 	  	"ibu": "2 bitterness units",
			// 	  	"imageLocation": "images/ipa.jpg"
			//   	},
			//   	{
			// 	  	"name": "PLACEHOLDER 3",
			// 	  	"brewery": "Main Street Brewery",
			// 	  	"type": "IPA",
			// 	  	"abv": "6.1%",
			// 	  	"ibu": "20 bitterness units",
			// 	  	"imageLocation": "images/stock-beer.jpg"
			// 	},
			// 	{
			// 	  	"name": "PLACEHOLDER 4",
			// 	  	"brewery": "R&B Brewery",
			// 	  	"type": "Hefeweizen",
			// 	  	"abv": "5.6%",
			// 	  	"ibu": "2 bitterness units",
			// 	  	"imageLocation": "images/TownHallHefeweizen.jpg"
			// 	}
	  // 		];
	  // 		console.log('the search results are ' + JSON.stringify($scope.searchResults));
	  // 		console.log('searchResults[0].name is now ' + $scope.searchResults[0].name);
	    } else {
	    	var url = convertBeerToURL();
	    	$http({
			    method: 'GET',
			    url: url
			}).then(function successCallback(response) {
				console.log('recommended beers are ' + JSON.stringify(response.data));
			    $scope.searchResults=response.data;
			    console.log("the search results are " + JSON.stringify($scope.searchResults));
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});	

	    }

	  //   function convertBeerToURL(){
	  //   	//!!! implement this
	  //   	return 'http://localhost:8020/?/recommendedbeers?userid=1';
	  //   }
	
    }

});





function DialogController($scope, $mdDialog, $rootScope) {
  	$scope.hide = function() {
    	$mdDialog.hide();
  	};
  	$scope.cancel = function() {
    	$mdDialog.cancel();
  	};
  	$scope.answer = function(answer) {
    	$mdDialog.hide(answer);
  	}

}