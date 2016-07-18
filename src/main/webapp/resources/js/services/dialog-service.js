/**
 * Created by vzhemevko on 04.09.15.
 */
'use strict';

retroApp.factory('dialogService', ['$uibModal', '$log', '$filter', '$aside', 'teamsService', 'retroService', 'usersService', 'notifyService',
    function ($uibModal, $log, $filter, $aside, teamsService, retroService, usersService, notifyService) {

        function trigerModal(input, callback) {
            var modalInstance = $uibModal.open(input);
            modalInstance.result.then(callback);
        }

        function openEditSMastersList($scope) {
            var input = {
                templateUrl: 'resources/templates/scrum-masters-dialog.html',
                controller: 'scrumMastersDialogCtr',
                resolve: {
                    inputData: {
                        title: "Scrum masters"
                    }
                }
            }
            var callback = function (sMasters) {
                usersService.updateSMastersList(sMasters, $scope);
            }
            trigerModal(input, callback);
        }


        function openProfileImgUpload($scope) {

            var input = {
                templateUrl: 'resources/templates/image-upload-dialog.html',
                controller: 'profileImgUploadCtr',
                size: 'sm',
                resolve: {
                    inputData: {}
                }
            }
            var callback = function (teamsData) {
            }
            trigerModal(input, callback);
        }

        function openCreateNewTeam($scope) {

            var input = {
                templateUrl: 'resources/templates/team-props-dialog.html',
                controller: 'teamPropsController',
                resolve: {
                    inputData: {
                        title: "Create new team",
                        inputCaption: "Team name",
                        defaultOutput: ""
                    }
                }
            }
            var callback = function (teamsData) {
                $scope.isTeamUpdate = false;
                teamsService.createOrUpdateTeam(teamsData, $scope);
            }
            trigerModal(input, callback);
        }

        function openEditTeam(teamId, teamName, $scope) {
            var input = {
                templateUrl: 'resources/templates/team-props-dialog.html',
                controller: 'teamPropsController',
                resolve: {
                    inputData: {
                        title: "Edit team",
                        inputCaption: "Team name",
                        defaultOutput: teamName,
                        teamId: teamId
                    }
                }
            }
            var callback = function (teamsData) {
                $scope.isTeamUpdate = true;
                teamsService.createOrUpdateTeam(teamsData, $scope);
            }
            trigerModal(input, callback);
        }


        function deleteTeam(teamId, teamName, $scope) {
            var input = {
                templateUrl: 'resources/templates/confirmation-dialog.html',
                controller: 'confDialogController',
                resolve: {
                    inputData: {
                        title: "Delete team",
                        confText: 'Are you sure you want to delete the "' + teamName + '" team ?',
                        teamId: teamId
                    }
                }
            }
            var callback = function () {
                teamsService.deleteTeam(teamId, $scope);
            }
            trigerModal(input, callback);
        }

        function openCreateNewRetroEvent($scope) {
            var todayDate = $filter('date')(new Date(), 'dd MMM yyyy');

            var input = {
                templateUrl: 'resources/templates/retro-props-dialog.html',
                controller: 'retroPropsController',
                resolve: {
                    inputData: {
                        title: "Create new retrospective",
                        inputCaption: "Sprint retrospective name",
                        defaultOutput: "Sprint#  - retro on " + todayDate
                    }
                }
            }

            var callback = function (retroName) {
                $scope.isRetroUpdate = false;
                retroService.createOrUpdateRetro(retroName, $scope);
            }

            if (!$scope.isRetroOpened) {
                notifyService.showError('Please open a team to add a new retrospective.', $scope);
            } else {
                trigerModal(input, callback);
            }

        }


        function openUpdateRetroEvent(existRetroName, $scope) {
            var input = {
                templateUrl: 'resources/templates/retro-props-dialog.html',
                controller: 'retroPropsController',
                resolve: {
                    inputData: {
                        title: "Update retrospective",
                        inputCaption: "Sprint retrospective name",
                        defaultOutput: existRetroName
                    }
                }
            }

            var callback = function (retroName) {
                $scope.isRetroUpdate = true;
                retroService.createOrUpdateRetro(retroName, $scope);
            }

            if (!$scope.isRetroOpened) {
                notifyService.showError('Please open a team to add a new retrospective.', $scope);
            } else {
                trigerModal(input, callback);
            }

        }

        function deleteRetro(retroId, retroName, $scope) {
            var input = {
                templateUrl: 'resources/templates/confirmation-dialog.html',
                controller: 'confDialogController',
                resolve: {
                    inputData: {
                        title: "Delete retrospective",
                        confText: 'Are you sure you want to delete the "' + retroName + '" retrospective ?'
                    }
                }
            }
            var callback = function () {
                retroService.deleteRetro(retroId, $scope);
            }
            trigerModal(input, callback);
        }

        function openUserMenu() {
            var asideInstance = $aside.open({
                templateUrl: 'resources/templates/user-menu-dialog.html',
                controller: 'homeController',
                placement: 'right',
                size: 'sm'
            });
        }

        return {
            trigerModal: trigerModal,
            openEditSMastersList: openEditSMastersList,
            openProfileImgUpload: openProfileImgUpload,
            openCreateNewTeam: openCreateNewTeam,
            openEditTeam: openEditTeam,
            deleteTeam: deleteTeam,
            openCreateNewRetroEvent: openCreateNewRetroEvent,
            openUpdateRetroEvent: openUpdateRetroEvent,
            openUserMenu: openUserMenu,
            deleteRetro: deleteRetro
        };
    }
]);