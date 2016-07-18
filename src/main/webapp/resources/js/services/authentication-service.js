/**
 * Created by vzhemevko on 02.09.15.
 */
'use strict';

retroApp.factory('authService', ['$http', '$rootScope', '$log', '$location', 'cookieService', 'notifyService',
    function ($http, $rootScope, $log, $location, cookieService, notifyService) {

        function register($scope) {
            var username = $scope.username;
            var personalName = $scope.personalName;
            var passwd = $scope.passwd;
            var email = $scope.email;

            var accountParam = btoa("username=" + username + ":" + "personalname=" + personalName + ":"
                + "email=" + email + ":" + "passwd=" + passwd);

            var response = $http.post("api/users/registration",
                // request body as string 
                '' + accountParam + '');

            response.success(function () {
                $rootScope.showRegistrationWelcome = true;
                $log.info('Successfully registered.');
                authenticate($scope);
            });

            response.error(function () {
                $log.info('Fail to register :(');
                notifyService.showError("There was an error processing registration. "
                    + "Probably login name or email or personal name already exists. Please try others.", $scope);
            });
        }

        function authenticate($scope) {
            var username = $scope.username;
            var passwd = $scope.passwd;
            var credsEncoded = btoa(username + ":" + passwd);
            var headers = {
                authorization: "Basic " + credsEncoded
            };

            var response = $http.get("api/users?username=" + username, {
                headers: headers
            });

            response.success(function (user) {
                $log.info('Successfully authenticated in...');
                setAuthData(user, credsEncoded);
                $location.path('/home');
            });

            response.error(function () {
                $log.info('Fail to authenticate...');
                //clearAuthData();
                $location.path('/login');
                notifyService.showError("Could not sign in. " + "Please check your username or password and try again.", $scope);
            });
        }

        function setAuthData(user, credsEncoded) {
            $log.info('Setting authentication data.');

            cookieService.setAuthData(credsEncoded);
            cookieService.setCurrentUsername(user.username);
            cookieService.setCurrentUserId(user.id);
            cookieService.setCurrentUserRole(user.role);
            cookieService.setCurrentUser(user);

            $rootScope.currentUserId = user.id;
            $rootScope.currentUserRole = user.role;
            $rootScope.currentUsername = user.username;
            $rootScope.authData = credsEncoded;
            $rootScope.currentUser = user;
            checkIsScrumMaster();

            $http.defaults.headers.common['Authorization'] = 'Basic ' + credsEncoded;
        }

        function clearAuthData() {
            $log.info('Clearing auth data and logging out.');
            // reset $rootScope
            for (var prop in $rootScope) {
                if (prop.substring(0, 1) !== '$') {
                    delete $rootScope[prop];
                }
            }
            cookieService.clearCookies();
            $http.defaults.headers.common.Authorization = 'Basic ';

            var response = $http.get("api/users/logout");

            response.success(function (user) {
                $log.info('Successfully logged out...');
                $location.path('/login');
            });

            response.error(function () {
                $log.info('Failed to log out...');
                notifyService.showError("Cannot not log out.", $scope);
            });
        }

        function isAuthenticated() {
            return $rootScope.authData ? true : false;
        }

        function restoreAuthFromCookies() {
            $rootScope.authData = cookieService.getAuthData();
            $rootScope.currentUsername = cookieService.getCurrentUsername();
            $rootScope.currentUserId = cookieService.getCurrentUserId();
            $rootScope.currentUserRole = cookieService.getCurrentUserRole();
            $rootScope.currentUser = cookieService.getCurrentUser();
            checkIsScrumMaster();

            if ($rootScope.authData) {
                $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.authData;
                $log.info('Authentication was restored.');
            } else {
                $log.info('User is not authenticated. Nothing to restore.');
            }
        }

        // todo probably move it usersService
        function getUsername() {
            return $rootScope.currentUsername || "";
        }

        // todo probably move it usersService
        function checkIsScrumMaster() {
            if ($rootScope.currentUserRole === 2 /*scrum master role id*/) {
                $rootScope.isScrumMaster = true;
            } else {
                $rootScope.isScrumMaster = false;
            }
            return $rootScope.isScrumMaster;
        }


        return {
            register: register,
            authenticate: authenticate,
            setAuthData: setAuthData,
            clearAuthData: clearAuthData,
            isAuthenticated: isAuthenticated,
            restoreAuthFromCookies: restoreAuthFromCookies,
            getUsername: getUsername,
            checkIsScrumMaster: checkIsScrumMaster
        }
    }
]);