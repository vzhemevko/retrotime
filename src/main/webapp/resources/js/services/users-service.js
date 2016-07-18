/**
 * Created by vzhemevko on 01.09.15.
 */
'use strict';

retroApp.factory('usersService', ['$log', '$resource', '$http', '$rootScope', 'notifyService', 'cookieService', 'authService',
    function ($log, $resource, $http, $rootScope, notifyService, cookieService, authService) {

        var users = $resource("api/users", {},
            // configuration
            {
                'get': {
                    method: 'GET',
                    isArray: true
                },
                'update': {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    isArray: true
                },
                'save': {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    isArray: false
                }

            })


        function fetchList($scope) {
            users.get(
                // success handler
                function (data) {
                    $scope.usersList = data;
                    $log.info("Got a users list...");
                },
                // error handler
                function () {
                    $log.warn("Failed to get a users list :(")
                    //todo show error
                })
        }


        function getUserTagsByTeamId(teamId, $scope) {
            users.get({
                    teamId: teamId
                },
                // success handler
                function (data) {
                    $scope.users = data;
                    $log.info("Got user tags for a team...");
                },
                // error handler
                function () {
                    $log.warn("Failed to get user tags for a team :(")
                    //todo show error
                });
        }


        function getScrumMaterTags($scope) {
            users.get({
                    smlist: ''
                },
                // success handler
                function (data) {
                    $scope.sMasters = data;
                    $log.info("Got scrum master tags...");
                },
                // error handler
                function () {
                    $log.warn("Failed to get scrum master tags :(")
                    //todo show error
                });
        }


        function updateSMastersList(sMasters, $scope) {
            $log.info('Modifying the scrum masters list...');
            users.update(
                // request body
                JSON.stringify(sMasters),
                // success handler
                function (data) {
                    $log.info("The scrum masters list was updated...");
                    notifyService.showSuccess('The scrum masters list was updated.', $scope);
                },
                // error handler
                function () {
                    $log.error('The scrum masters list was not updated');
                    notifyService.showError('The scrum masters list was not updated. Please try again.', $scope);
                }
            );
        }

        function updateUserPersonalName($scope) {
            var user = $rootScope.currentUser
            if (!$scope.userPersonalName) {
                user.personalName = '';
            }
            else {
                user.personalName = $scope.userPersonalName;
            }
            saveUser(user);
        }

        function saveUser($scope) {
            $log.info('Saving a user...');
            var seen = [];
            users.save(
                // request body
                JSON.stringify($scope.userToUpdate, function (key, val) {
                    if (val != null && typeof val == "object") {
                        if (seen.indexOf(val) >= 0) {
                            return;
                        }
                        seen.push(val);
                    }
                    return val;
                }),

                // success handler
                function (user) {
                    $log.info("The user was saved.");

                    var credsEncoded;
                    var notifyMessage;
                    if ($scope.passwordUpdate) {
                        $scope.passwordUpdate = false;
                        credsEncoded = btoa(user.username
                            + ":" + $rootScope.currentUser.passwd);
                        notifyMessage = 'Your password was updated.';
                    }
                    else {
                        // it's not a password update. Use existing creds.
                        credsEncoded = $rootScope.authData;
                        notifyMessage = 'Your personal data was updated.';
                    }
                    authService.setAuthData(user, credsEncoded);
                    initUserData($scope);
                    notifyService.showSuccess(notifyMessage, $scope);
                },
                // error handler
                function (response) {
                    $log.error('The user was not saved.');
                    var notifyMessage;
                    if ($scope.passwordUpdate) {
                        notifyMessage = 'Cannot update your password.';
                    }
                    else {
                        notifyMessage = 'Cannot update your personal data. Probably login name or email '
                            + 'or personal name already exists. Please try others. ';
                    }
                    //var user = JSON.parse(response.config.data);
                    //authService.setAuthData(user, $rootScope.authData);
                    initUserData($scope);
                    notifyService.showError(notifyMessage, $scope);
                }
            );
        }

        function getUserPersonalName() {
            if ($rootScope.currentUser) {
                return $rootScope.currentUser.personalName || "";
            }
            else {
                return "";
            }
        }

        function getUserEmail() {
            if ($rootScope.currentUser) {
                return $rootScope.currentUser.email || "";
            }
            else {
                return "";
            }

        }

        function changePasswd($scope) {
            $log.info("Updating user's password...");
            $scope.passwordUpdate = true;
            if (!checkExistingPasswd($scope.oldPasswd)) {
                notifyService.showError('Wrong old password', $scope);

            } else if (!$scope.newPasswd || !$scope.reNewPasswd) {
                notifyService.showError('Please provide your new password', $scope);

            } else if ($scope.newPasswd !== $scope.reNewPasswd) {
                notifyService.showError('Confirmation of the new password does not match', $scope);

            } else {
                $scope.userToUpdate = {}
                angular.copy($rootScope.currentUser, $scope.userToUpdate);
                $scope.userToUpdate.passwd = $scope.newPasswd;
                saveUser($scope);
            }
        }

        function updatePersonalData($scope) {
            $scope.userToUpdate = {}
            angular.copy($rootScope.currentUser, $scope.userToUpdate);

            $scope.userToUpdate.username = $scope.currentUsername;
            $scope.userToUpdate.personalName = $scope.userPersonalName;
            $scope.userToUpdate.email = $scope.currentEmail;

            saveUser($scope);
        }

        function checkExistingPasswd(passwd) {
            var authData = $rootScope.authData;
            var authDataDec = atob(authData);
            var exPasswd = authDataDec.substring((authDataDec.indexOf(':') + 1));
            if (exPasswd === passwd) {
                return true;
            }
            else {
                return false;
            }
        }

        function initUserData($scope) {
            $scope.userPersonalName = getUserPersonalName();
            $scope.currUsername = authService.getUsername();
            $scope.currentEmail = getUserEmail();
            if (!$scope.userPersonalName) {
                $scope.greeting = $scope.currUsername;
            }
            else {
                $scope.greeting = $scope.userPersonalName;
            }
        }

        return {
            users: users,
            fetchList: fetchList,
            getUserTagsByTeamId: getUserTagsByTeamId,
            getScrumMaterTags: getScrumMaterTags,
            updateSMastersList: updateSMastersList,
            saveUser: saveUser,
            updateUserPersonalName: updateUserPersonalName,
            getUserPersonalName: getUserPersonalName,
            getUserEmail: getUserEmail,
            changePasswd: changePasswd,
            updatePersonalData: updatePersonalData,
            initUserData: initUserData
        }
    }
]);