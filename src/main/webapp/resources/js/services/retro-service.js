/**
 * Created by vzhemevko on 03.09.15.
 */
'use strict';

retroApp.factory('retroService', ['$http', '$log', '$rootScope', '$timeout', '$location', 'cookieService',
    'webSocketService', 'notifyService',
    function ($http, $log, $rootScope, $timeout, $location, cookieService, webSocketService, notifyService) {

        function leaveRetro() {
            webSocketService.isConnected() && webSocketService.close();
        }

        function save(contentPart) {
            contentPart.lockType = false;
            updatePeers(contentPart);
        }

        function sendLock(contentPart) {
            var lockMsg = {};
            angular.copy(contentPart, lockMsg);
            lockMsg.text = ""; // reset text. It's only a lock msg
            lockMsg.lockType = true;
            lockMsg.locked = true;
            $log.info("Sending a lock to peers.");
            webSocketService.sendMessage(lockMsg);
        }

        function sendUnLock(contentPart, $scope) {
            var lockMsg = {};
            angular.copy(contentPart, lockMsg);
            lockMsg.text = ""; // reset text. It's only an unlock msg
            lockMsg.lockType = true;
            lockMsg.locked = false;
            $log.info("Sending an unlock peers.");
            webSocketService.sendMessage(lockMsg);
        }

        function updatePeers(contentPart) {
            if (contentPart.partId === 4 /*action items*/) {
                contentPart.username = $rootScope.currentUsername;
            }
            webSocketService.sendMessage(contentPart);
        }

        function openRetro($scope) {
            if (!$rootScope.openedRetroId) {
                // this means retro has been already opened
                // only the page was reloaded
                // try to set opened retro id from cookie.
                $rootScope.openedRetroId = cookieService.getOpenedRetroId();
            }
            var response = $http({
                url: "api/retro",
                method: "GET",
                params: {
                    retroId: $rootScope.openedRetroId
                }
            });

            response.success(function (data) {
                $scope.retroCont = data;
                checkIsCurrentUserInTeam($scope);
                cookieService.setOpenedRetroId($rootScope.openedRetroId);
                $log.info('Got retro data for a retro...');
                webSocketService.init($scope);
            });

            response.error(function () {
                $log.error('Could not get data for a retro...');
            });
        }


        function checkIsCurrentUserInTeam($scope) {
            $scope.usersContentMap = $scope.retroCont.usersContentMap;
            $scope.currentUsername = $rootScope.currentUsername;
            $scope.currentUserId = $rootScope.currentUserId;
            $scope.isCuurentUserInTeam = false;
            angular.forEach($scope.usersContentMap, function (content) {
                if (content.userId === $scope.currentUserId) {
                    $scope.isCurrentUserInTeam = true;
                }
            });

            if (!$scope.isCurrentUserInTeam) {
                addNonExistingUser($scope);
            }
        }

        function addNonExistingUser($scope) {
            // clone first user object in the map
            var firstUserId = Object.keys($scope.usersContentMap)[0]
            var userContent = {};
            angular.copy($scope.usersContentMap[firstUserId], userContent);
            // init content for current user.
            var response = $http({
                url: "api/contents",
                method: "POST",
                params: {
                    retroId: $rootScope.openedRetroId,
                    userId: $scope.currentUserId
                }
            });
            response.then(function successCallback(response) {
                userContent.id = response.data;
                 // reset all props values of cloned object and set current user values
                userContent.userId = $scope.currentUserId;
                userContent.username = $scope.currentUsername;
                userContent.personalName = $rootScope.currentUser.personalName;

                angular.forEach(userContent.partsMap, function (part) {
                    part.contentId = userContent.id; // reset. It's going to be a new content.
                    part.userId = $scope.currentUserId;
                    part.username = $scope.currentUsername;
                    part.personalName = $rootScope.currentUser.personalName;
                    part.text = "";
                });
                $scope.usersContentMap[$scope.currentUserId] = userContent;
                $log.info('Content for current user was init.');
            }, function errorCallback() {
                 $log.error('Content for current user was not init');
            });
        }

        function openTeamRetroList(teamId, teamName, $scope) {
            if (!teamId) {
                $log.warn("Team id cannot be null. Cannot get retro list");
                return; // TODO add error message;
            }
            if ($scope.teamIdOpened === teamId && $scope.isRetroOpened) {
                closeTeam($scope);
                return;
            } else if ($scope.teamIdOpened !== teamId && $scope.isRetroOpened) {

                closeTeam($scope);
                $timeout(function () {
                    openTeam(teamId, teamName, $scope);
                }, 500); // 0.5 sec


            } else {
                openTeam(teamId, teamName, $scope);
            }

        }

        function closeTeam($scope) {
            $scope.isRetroOpened = false;
            $scope.teamIdOpened = ""; // team is closed
            cookieService.setOpenedTeamId($scope.teamIdOpened);
        }

        function openTeam(teamId, teamName, $scope) {
            $scope.isRetroOpened = true;
            $scope.teamIdOpened = teamId; // team is opened
            $scope.teamName = teamName;
            cookieService.setOpenedTeamId(teamId);
            cookieService.setOpenedTeamName(teamName);
            fetchListByTeam($scope);
        }

        function fetchListByTeam($scope) {
            // TODO move this ifs to other function
            if (!$scope.teamIdOpened && !$scope.teamName) {
                $scope.teamIdOpened = cookieService.getOpenedTeamId();
                $scope.teamName = cookieService.getOpenedTeamName();
            }
            if (!$scope.teamIdOpened) {
                return;
            }

            var response = $http({
                url: "api/retro",
                method: "GET",
                params: {
                    teamId: $scope.teamIdOpened
                }
            });

            response.success(function (data) {
                $scope.retroList = data;
                $scope.isRetroOpened = true;
                $log.info('Got retro list for a team...');
            });

            response.error(function () {
                $log.error('Could not get retro list for a team...');
            });
        }

        function createOrUpdateRetro(retroName, $scope) {
            var response = $http({
                url: "api/retro",
                method: "POST",
                params: {
                    name: retroName,
                    teamId: $scope.teamIdOpened,
                    retroId: $scope.retroIdToUpdate
                }
            });

            response.success(function (data) {
                $scope.retroList = data;
                if ($scope.isRetroUpdate) {
                    $log.info("Retro was updated...");
                    notifyService.showSuccess('The "' + retroName + '" retrospective was updated.', $scope);
                    $scope.retroIdToUpdate = ""
                } else {
                    $log.info("A new retrospective was created...");
                    notifyService.showSuccess('The "' + retroName + '" retrospective was created.', $scope);
                }
            });

            response.error(function () {
                if ($scope.isRetroUpdate) {
                    $log.info("Retro was not update...");
                    notifyService.showError('The "' + retroName + '" retrospective was not updated. Please refresh the page and try again.', $scope);
                    $scope.retroIdToUpdate = ""
                } else {
                    $log.info("Retro was not created...");
                    notifyService.showError('The "' + retroName + '" retrospective was not created. Please refresh the page and try again.', $scope);
                }
            });
        }


        function deleteRetro(retroId, $scope) {
            var response = $http({
                url: "api/retro",
                method: "DELETE",
                params: {
                    retroId: retroId,
                    teamId: $scope.teamIdOpened

                }
            });

            response.success(function (data) {
                $scope.retroList = data;
                $log.info(';The retrospective was deleted');
                notifyService.showSuccess('The retrospective was deleted', $scope);
                $scope.retroIdToUpdate = ""
            });

            response.error(function () {
                $log.error('Retro was not deleted');
                notifyService.showError('The retrospective was not deleted', $scope);
                $scope.retroIdToUpdate = ""
            });

        }

        return {
            save: save,
            sendLock: sendLock,
            sendUnLock: sendUnLock,
            openRetro: openRetro,
            checkIsCurrentUserInTeam: checkIsCurrentUserInTeam,
            addNonExistingUser: addNonExistingUser,
            openTeamRetroList: openTeamRetroList,
            fetchListByTeam: fetchListByTeam,
            createOrUpdateRetro: createOrUpdateRetro,
            deleteRetro: deleteRetro,
            leaveRetro: leaveRetro
        };
    }
])