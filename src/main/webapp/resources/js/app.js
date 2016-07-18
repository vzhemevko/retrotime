'use strict';

var retroApp = angular.module('retroApp', ['ngRoute', 'ngCookies', 'ui.bootstrap', 'ngTagsInput', 'ngResource', 
	'ngAnimate', 'monospaced.elastic', 'ngAside'
]);

retroApp.config(function($routeProvider, $httpProvider) {
	$routeProvider.when('/login', {
		templateUrl: 'resources/templates/login.html',
		controller: 'loginController'
	});
	$routeProvider.when('/home', {
		templateUrl: 'resources/templates/home.html',
		controller: 'homeController'
	});
	$routeProvider.when('/retro', {
		templateUrl: 'resources/templates/retro.html',
		controller: 'retroController'
	});
	$routeProvider.otherwise({
		redirectTo: '/login'
	});

	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});


retroApp.run(['$location', '$rootScope', '$log', 'authService', 'retroService',
	function($location, $rootScope, $log, authService, retroService) {
		// try to keep user logged in after page refresh
		authService.restoreAuthFromCookies();

		function handleRouteChangeStart() {
			// redirect to login page if not logged in
			if ($location.path() !== '/login' && !authService.isAuthenticated()) {
				$log.info('User is not authenticated for this resource. ' + 'Redirecting to login page...');
				$location.path('/login');
			}
			if (($location.path() === '/login' || $location.path() === '') && authService.isAuthenticated()) {
				$log.info('User is authenticated no need to authenticate. ' + 'Redirecting to home page...');
				$location.path('/home');
			}
			else {
				$log.info('Rendering resource...' + $location.path());
			}
		}
		$rootScope.$on('$routeChangeStart', handleRouteChangeStart());
	}
]);