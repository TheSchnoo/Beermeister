var app = angular.module('Brewmaster', ['ngMaterial']);
app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey');
});

app.controller('PopulateCtrl', function($scope, $mdDialog, $mdMedia, $rootScope, $http) {
	$scope.populateDB = function(ev){
		$http
	}

});