var mockMode = true;
var debugMode = false;
var baseURL = 'http://localhost:8020'
var userid = 1;

var app = angular.module('Brewmaster', ['ngMaterial']);
app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});


app.controller('AppCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	//this block used to get the recommended beers, and set to a scope variable
	if (mockMode) {
		$scope.beers = [
		 	{
			  	"bname": "Thunderbird Lager",
			  	"breweryName": "UBC Brewery",
			  	"type": "Lager",
			  	"abv": "5.1%",
			  	"ibu": "10 bitterness units",
			  	"imageLocation": "images/stock-beer.jpg"
		  	},
		  	{	
			  	"bname": "Passive Aggressive",
			  	"breweryName": "Brassneck",
			  	"type": "IPA",
			  	"abv": "5.3%",
			  	"ibu": "2 bitterness units",
			  	"imageLocation": "images/ipa.jpg"
		  	},
		  	{
			  	"bname": "Southern Hop",
			  	"breweryName": "Main Street Brewery",
			  	"type": "IPA",
			  	"abv": "6.1%",
			  	"ibu": "20 bitterness units",
			  	"imageLocation": "images/stock-beer.jpg"
			},
			{
			  	"bname": "Sun God Wheat Ale",
			  	"breweryName": "R&B Brewery",
			  	"type": "Hefeweizen",
			  	"abv": "5.6%",
			  	"ibu": "2 bitterness units",
			  	"imageLocation": "images/TownHallHefeweizen.jpg"
			}
	  	]
  	} else {
  		$http({
		    method: 'GET',
		    url: baseURL + '/recommendedbeers?userid=' + userid
		}).then(function successCallback(response) {
			console.log('recommended beers are ' + JSON.stringify(response.data));
		    $scope.beers = response.data;
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});	
  	}

  	//called when a vendor is clicked
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
			  }, function errorCallback(response) {
			  	//called when an error is produced
			   
			  });
  		}
	}

	//called when the ratings button is clicked
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

//controller used for search functionality
app.controller('SearchCtrl', function($scope, $http, $timeout, $rootScope) {
    $scope.beer = {};
    $scope.beerTypeCategories = ["IPA", "Pilsner", "Porter", "Stout", "Lager"];
    $scope.abvCategories = ["<4%", "4-4.99%", "5-5.99%", "6-6.99%", ">7%" ];
    $scope.ibuCategories = ["<10", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69", ">70"];
    $scope.ratingCategories = ["1", "2", "3", "4"];
    $rootScope.searchResults = [];

    $scope.submitSearch = function(ev){
  
    	if (mockMode){
	    	$rootScope.searchResults = [
			 	{
				  	"bname": "PLACEHOLDER 1",
				  	"breweryName": "UBC Brewery",
				  	"type": "Lager",
				  	"abv": "5.1%",
				  	"ibu": "10 bitterness units",
				  	"imageLocation": "images/stock-beer.jpg"
			  	},
			  	{	
				  	"bname": "PLACEHOLDER 2",
				  	"breweryName": "Brassneck",
				  	"type": "IPA",
				  	"abv": "5.3%",
				  	"ibu": "2 bitterness units",
				  	"imageLocation": "images/ipa.jpg"
			  	},
			  	{
				  	"bname": "PLACEHOLDER 3",
				  	"breweryName": "Main Street Brewery",
				  	"type": "IPA",
				  	"abv": "6.1%",
				  	"ibu": "20 bitterness units",
				  	"imageLocation": "images/stock-beer.jpg"
				},
				{
				  	"bname": "PLACEHOLDER 4",
				  	"breweryName": "R&B Brewery",
				  	"type": "Hefeweizen",
				  	"abv": "5.6%",
				  	"ibu": "2 bitterness units",
				  	"imageLocation": "images/TownHallHefeweizen.jpg"
				}
	  		];
	  		console.log('the search results are ' + JSON.stringify($scope.searchResults));
	  		console.log('searchResults[0].name is now ' + $scope.searchResults[0].name);
	    } else {
	    	var url = convertBeerToURL();
	    	conosle.log('making HTTP request to ' + url);
	    	$http({
			    method: 'GET',
			    url: url
			}).then(function successCallback(response) {
				console.log('received a response of ' + JSON.stringify(response.data));
			    $rootScope.searchResults = response.data;
			    console.log("the search results are " + JSON.stringify($scope.searchResults));
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});	

	    }

	    function convertBeerToURL(){
	    	//!!! implement this
	    	// return 'http://localhost:8020/?/recommendedbeers?userid=1';
	    	var url = baseURL + '/beers';
	    	if (Object.keys($scope.beer).length === 0) {
	    		console.log('making request to ' + url);
	    		return url;
	    	} else {
	    		var str = jQuery.param($scope.beer);
	    		url = url + '?' + str;
	    		console.log('making url request to ' + url);
				return url;
			}
	    }
	
    }

});


//controller used for advanced dialog windows
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

// renee-->all login/authentication functionality should sit here
app.controller('LoginCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	$rootScope.uuid = null;
	$scope.username = null;
	$scope.password = null;


	//called when login button is clicked
	$scope.login = function(){
		if ($rootScope.uuid === null){
			//TODO: not logged in, need to show login prompt and retrieve username and password fields
			alert('user will login here');
			//...




		} else {
			//TODO: already logged in
			alert('user has already logged in');
			//...




		}
	}


	$scope.signup = function(){
		//TODO: implement this, need to show prompt to get username and password fields 
		//and create an entry in the db accordingly
		alert('signup window will be shown here');
		//...



	}

});