var baseURL = 'http://localhost:8080';

var app = angular.module('Brewmaster', ['ngMaterial']);
app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});

app.controller('AppCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	$rootScope.storeId = null;

});

app.controller('SearchCtrl', function($scope, $http, $timeout, $rootScope, $mdMedia, $mdDialog) {
    $scope.beer = {};
    $scope.beerTypeCategories = ["IPA", "Pilsner", "Porter", "Stout", "Lager"];
    $scope.abvCategories = ["<4%", "4-4.99%", "5-5.99%", "6-6.99%", ">7%" ];
    $scope.ibuCategories = ["<10", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69", ">70"];
    $scope.ratingCategories = ["1", "2", "3", "4"];
    $rootScope.searchResults = [];



    $scope.submitSearch = function(ev){
    	if($rootScope.storeId === null){
    		$mdDialog.show(
				$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.textContent('Yo dawg, please sign in first')
					.ariaLabel('Alert Dialog Demo')
					.ok('Got it!')
					.targetEvent(ev)
	    	);


    	} else {
    		var url = convertBeerToURL($scope.beer);
	    	if (url === baseURL + '/beers'){
	    		url = url + '?storeId=' + $rootScope.storeId;

	    	} else {
	    		url = url + '&storeId=' + $rootScope.storeId;

	    	}
	    	
	    	console.log('making HTTP GET request to ' + url);
	    	$http({
			    method: 'GET',
			    url: url
			}).then(function successCallback(response) {
				// console.log('received a response of ' + JSON.stringify(response.data));
			    $rootScope.searchResults = response.data;
			    console.log('rootScope.searchResults are now ' + JSON.stringify($rootScope.searchResults));
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
			$rootScope.loading = false;
			console.log('rootScope.loading is ' + $rootScope.loading);
	    }
	
    }
    $scope.showAdditionalInfo = function(ev, beer){
    	console.log('showing additional information for ' + beer.bname);
    	var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
	    $mdDialog.show({
	        controller: AdditionalInfoDialogCtrl,
	      	templateUrl: 'additionalinfodialog.html',
	      	parent: angular.element(document.body),
	      	targetEvent: ev,
	      	clickOutsideToClose:true,
	      	fullscreen: useFullScreen,
	      	locals:{
	      		beer: beer
	      	}
	    });


	    function AdditionalInfoDialogCtrl($scope, $mdDialog, beer){
        	$scope.beer = beer;
        	console.log('the selected beer is ' + JSON.stringify(beer));

        	$scope.showVendorPage = function(ev, vendor){
        		console.log('attempting to show vendor page for' + vendor);
        		$mdDialog.show({
			        controller: VendorPageCtrl,
			      	templateUrl: '../app/vendortemplate.html',
			      	parent: angular.element(document.body),
			      	targetEvent: ev,
			      	clickOutsideToClose:true,
			      	fullscreen: useFullScreen,
			      	locals:{
			      		vendor: vendor
			      	}
			    });


        	}
		}

    }

    $scope.addToInventory = function(ev, beer){
    	var url = baseURL + '/vendors?bname=' + beer.bname + '&storeId=' + $rootScope.storeId;

    	if ($rootScope.storeId === null){
    		//!!! TODO: implement this
    		console.log('login first!!')
    	} else {
    		console.log('making HTTP POST to ' + url + ' with no payload');
    		$http({
			    method: 'POST',
			    url: url,
			}).then(function successCallback(response) {
				console.log('received a response of ' + JSON.stringify(response.data));
				console.log('trying to update search information');
				
				var url = convertBeerToURL($scope.beer);

		    	if (url === baseURL + '/beers'){
		    		url = url + '?storeId=' + $rootScope.storeId;

		    	} else {
		    		url = url + '&storeId=' + $rootScope.storeId;

		    	}
		    	
		    	console.log('making HTTP GET request to ' + url);
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
				    console.log('received a response of ' + JSON.stringify(response.data));
					console.log('trying to update search information');
					
					var url = convertBeerToURL($scope.beer);

			    	if (url === baseURL + '/beers'){
			    		url = url + '?storeId=' + $rootScope.storeId;

			    	} else {
			    		url = url + '&storeId=' + $rootScope.storeId;

			    	}
			    	
			    	console.log('making HTTP GET request to ' + url);
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

				});


			
			}, function errorCallback(response) {
					//error stuff here
			});
    	}

    }

    $scope.removeFromInventory = function(ev, beer){
    	console.log('will remove from inventory here');
    	var url = baseURL + '/vendors?bname=' + beer.bname + '&storeid=' + $rootScope.storeId;
    	console.log('making DELETE request to ' + url);
    	$http({
	    	method: 'DELETE',
	    	url: url
		}).then(function successCallback(response) { //@@@
			console.log('received a response of ' + JSON.stringify(response.data));
			console.log('trying to update search information');
			
			var url = convertBeerToURL($scope.beer);

	    	if (url === baseURL + '/beers'){
	    		url = url + '?storeId=' + $rootScope.storeId;

	    	} else {
	    		url = url + '&storeId=' + $rootScope.storeId;

	    	}
	    	
	    	console.log('making HTTP GET request to ' + url);
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
			
		}, function errorCallback(response) {
	    // called asynchronously if an error occurs
	    // or server returns response with an error status.
		});	
    }

});

app.controller('VendorLoginCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	$scope.loginInfo = {};
	$scope.signupInfo = {};
	console.log('setting storeId to null');
	// $scope.storeId = null;

	//called when login button is clicked
	$scope.showLoginPrompt = function(ev){
		if ($rootScope.storeId === null){
			var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
		    $mdDialog.show({
		        controller: LoginPromptCtrl,
		      	templateUrl: 'vendorlogindialog.html',
		      	parent: angular.element(document.body),
		      	targetEvent: ev,
		      	clickOutsideToClose:true,
		      	fullscreen: useFullScreen
		    });

		//if we already have a UUID, the user has already logged in
		} else {
			$mdDialog.show(
				$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.textContent('Yo dawg, you\'ve already logged in!')
					.ariaLabel('Alert Dialog Demo')
					.ok('Got it!')
					.targetEvent(ev)
	    	);


		}
	}

	function LoginPromptCtrl($scope, $mdDialog){
		console.log('got to loginpromptctrl');

		$scope.sendLoginInfo = function(ev){
			console.log('sending login info of: ' + JSON.stringify($scope.loginInfo));
			if (!$scope.loginInfo.hasOwnProperty('storeName') || !$scope.loginInfo.hasOwnProperty('password')){
				$mdDialog.show(
					$mdDialog.alert()
						.parent(angular.element(document.querySelector('#popupContainer')))
						.clickOutsideToClose(true)
						.textContent('Yo dawg, you need to provide us with both a username and password.')
						.ariaLabel('Alert Dialog Demo')
						.ok('Got it!')
						.targetEvent(ev)
		    	);

			//THE FOLLOWING CODE BLOCK IS UNTESTED, WAITING FOR LOGIN API IMPLEMENTATION
			} else {
				var str = jQuery.param($scope.loginInfo);
				var url = baseURL + '/vendor-login?' + str
				console.log('Making GET request to ' + url);
				$http({
			    	method: 'GET',
			    	url: url
				}).then(function successCallback(response) {
					if (response.data.authenticated === false){
						$mdDialog.show(
							$mdDialog.alert()
								.parent(angular.element(document.querySelector('#popupContainer')))
								.clickOutsideToClose(true)
								.textContent('Login Failed. Check that username and password are correct.')
								.ariaLabel('Alert Dialog Demo')
								.ok('Got it!')
								.targetEvent(ev)
							);
		    	
					} else {
						$rootScope.storeId = response.data.storeId;
						$mdDialog.show(
							$mdDialog.alert()
								.parent(angular.element(document.querySelector('#popupContainer')))
								.clickOutsideToClose(true)
								.textContent('Login info authenticated. Welcome back.')
								.ariaLabel('Alert Dialog Demo')
								.ok('Got it!')
								.targetEvent(ev)
						);
		    		
					}



				}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				});	
			}

		}
	}

	$scope.showLogoutPrompt = function(ev){
		$rootScope.storeId = null;
		$rootScope.searchResults = null;
		$mdDialog.show(
				$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.textContent('Logout Successful')
					.ariaLabel('Alert Dialog Demo')
					.ok('Got it!')
					.targetEvent(ev)
	    	);
	}


	$scope.showSignupPrompt = function(ev) {
		console.log('trying to show signup prompt');
		console.log('$scope.storeId is ' + $scope.storeId); 
		if ($scope.storeId === null){
			console.log('null storeid detected');
			var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
		    $mdDialog.show({
		        controller: SignupPromptCtrl,
		      	templateUrl: 'vendorsignupdialog.html',
		      	parent: angular.element(document.body),
		      	targetEvent: ev,
		      	clickOutsideToClose:true,
		      	fullscreen: useFullScreen
		    });

		//if we already have a UUID, the user has already logged in
		} else {
			console.log('$scope.storeName is ' + $scope.storeName);
			$mdDialog.show(
				$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.textContent('Yo dawg, you\'ve already logged in!')
					.ariaLabel('Alert Dialog Demo')
					.ok('Got it!')
					.targetEvent(ev)
	    	);
		}
	}


	function SignupPromptCtrl($scope, $mdDialog){
   
        $scope.sendSignupInfo = function(ev){
			console.log('sending signup info of: ' + JSON.stringify($scope.signupInfo));
			if (!$scope.signupInfo.hasOwnProperty('storeName') || !$scope.signupInfo.hasOwnProperty('password')
				|| !$scope.signupInfo.hasOwnProperty('address')){


				$mdDialog.show(
					$mdDialog.alert()
						.parent(angular.element(document.querySelector('#popupContainer')))
						.clickOutsideToClose(true)
						.textContent('Yo dawg, you need to provide us with both a username and password.')
						.ariaLabel('Alert Dialog Demo')
						.ok('Got it!')
						.targetEvent(ev)
	    		);

			//THE FOLLOWING CODE BLOCK IS UNTESTED, WAITING FOR LOGIN API IMPLEMENTATION
			} else {
				var url = baseURL + '/vendor-signup';
				console.log('Making POST request to ' + url + " with a body of " + JSON.stringify($scope.signupInfo));
				$http({
			    	method: 'POST',
			    	url: url,
			    	//TODO: check that the data payload is correct
			    	data: $scope.signupInfo
				}).then(function successCallback(response) {
					console.log('received a response of' + JSON.stringify(response.data));
					if (response.data.created === false){
						$mdDialog.show(
							$mdDialog.alert()
								.parent(angular.element(document.querySelector('#popupContainer')))
								.clickOutsideToClose(true)
								.textContent('Signup failed. Try a different username.')
								.ariaLabel('Alert Dialog Demo')
								.ok('Got it!')
								.targetEvent(ev)
						);
		    	
					} else {
						$rootScope.storeId = response.data.storeId;
						console.log('$scope.storeId is now ' + $rootScope.storeId);
						console.log('user account created');
						$mdDialog.show(
							$mdDialog.alert()
								.parent(angular.element(document.querySelector('#popupContainer')))
								.clickOutsideToClose(true)
								.textContent('Signup successful. Welcome to Brewmeister.')
								.ariaLabel('Alert Dialog Demo')
								.ok('Got it!')
								.targetEvent(ev)
						);
		    		
					}



				}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				});	
			}
		}
    }
   
});


//HELPERS

function convertBeerToURL(beer){

	    	// return 'http://localhost:8020/?/recommendedbeers?userid=1';
	    	var url = baseURL + '/beers';
	    	if (Object.keys(beer).length === 0) {
	    		console.log('the converted URL is ' + url);
	    		return url;
	    	} else {
	    		var str = jQuery.param(beer);
	    		url = url + '?' + str;
	    		console.log('the converted URL is ' + url);
				return url;
			}
	    }