var mockMode = true;
var debugMode = false;
var app = angular.module('Brewmaster', ['ngMaterial']);
app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});

app.controller('AppCtrl', function($scope, $mdDialog, $mdMedia, $rootScope) {
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
  		//not mockmode beers
  	}

  	$scope.showVendors = function(ev, beer) {
  		if (mockMode){
  			$rootScope.vendors = [
				{
					"name":"Darby's Liquor Store"
				},
				{
					"name":"UBC Liquor Store"
				},
				{	
					"name":"Legacy Liquor Store"
				}
			];
  		} else {
  			//!!! implement this when webserver works
  		}
  		// console.log(JSON.stringify($rootScope.vendors));
	    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
	    $mdDialog.show({
	        controller: DialogController,
	      	templateUrl: 'app/vendordialog.html',
	      	parent: angular.element(document.body),
	      	targetEvent: ev,
	      	clickOutsideToClose:true,
	      	fullscreen: useFullScreen
	    });
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

app.controller('SearchCtrl', function($scope) {
    $scope.beer = {};
    $scope.beerTypeCategories = ["IPA", "Pilsner", "Porter", "Stout", "Lager"
    ];
    $scope.abvCategories = ["<4%", "4-4.99%", "5-5.99%", "6-6.99%", ">7%" ];
    $scope.ibuCategories = ["<10", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69", ">70"];
    $scope.ratingCategories = [">1 Star", ">2 Stars", ">3 Stars", ">4 Stars"];
    $scope.isBeerEmpty = jQuery.isEmptyObject($scope.beer);



    $scope.submitSearch = function(ev){
    	// console.log("Search submitted!");
    	// console.log("Submitting a search of: " + $scope.beer);
    	console.log("There are "+Object.keys($scope.beer).length+" attributes in beer");
    	if (jQuery.isEmptyObject($scope.beer)){
    		console.log('invalid search');
    	} else {
    		console.log('good search');
    	}
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
  	console.log('got to dialogcontroller');
  	$scope.vendors = $rootScope.vendors;
}


  // 	if (mockMode){
  // 		$scope.vendors = [
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
  // 	} else {
  // 		//make http get and set response to $scope.vendors
  // 	}

