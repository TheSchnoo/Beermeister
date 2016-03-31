var baseURL = 'http://localhost:8080'

var app = angular.module('Brewmaster', ['ngMaterial']);
app.controller('AppCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
});

app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});

app.controller('SearchCtrl', function($scope, $http, $timeout, $rootScope, $mdDialog) {
    $scope.beer = {};
    $scope.beerTypeCategories = ["IPA", "Pilsner", "Porter", "Stout", "Lager"];
    $scope.abvCategories = ["<4%", "4-4.99%", "5-5.99%", "6-6.99%", ">7%" ];
    $scope.ibuCategories = ["<10", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69", ">70"];
    $scope.ratingCategories = ["1", "2", "3", "4"];
    $rootScope.searchResults = [];

    $scope.submitBeer = function(ev){
    	if (!validSubmission()) {
    		$mdDialog.show(
				$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.textContent('Yo dawg, check that all fields are entered, and that IBU/ABV are numbers')
					.ariaLabel('Alert Dialog Demo')
					.ok('Got it!')
					.targetEvent(ev)
	    	);

    	} else {
            var url = baseURL + '/beers';
            console.log('making POST to ' + url + ' with a payload of ' + JSON.stringify($scope.beer));
    		$http({
                method: 'POST',
                url: baseURL + '/beers'
            }).then(function successCallback(response) {
                //TODO: implement this
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            }); 

    	}

  
    }

    function validSubmission(){
    	if (!$scope.beer.hasOwnProperty('bname') || !$scope.beer.hasOwnProperty('breweryName') ||
            !$scope.beer.hasOwnProperty('abv') || !$scope.beer.hasOwnProperty('ibu') || 
            !$scope.beer.hasOwnProperty('description') || isNaN(parseFloat($scope.beer.abv)) || 
            isNaN(parseFloat($scope.beer.abv))) {
                return false;
        } else {
            return true;
        }
    }

});
