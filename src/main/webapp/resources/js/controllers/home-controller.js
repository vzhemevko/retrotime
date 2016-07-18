'use strict'

retroApp.controller('homeController',
    function homeController($scope, $uibModal, $rootScope, $location, $aside, authService,
                            teamsService, retroService, dialogService, notifyService, usersService) {

        $scope.initUserData = function () {
            usersService.initUserData($scope);
        }

        $scope.init = function () {
            if ($rootScope.showRegistrationWelcome) {
                notifyService.showSuccess("You were successfully registered. Welcome!", $scope)
                $rootScope.showRegistrationWelcome = false; // reset
            }

            teamsService.fetchList($scope); // $scope.teamsList
            retroService.fetchListByTeam($scope); // $scope.retroList
        };

        $scope.manageRetroList = function (teamId, teamName) {
            retroService.openTeamRetroList(teamId, teamName, $scope);
        }

        $scope.logout = function () {
            authService.clearAuthData($scope);
        }

        $scope.createTeam = function () {
            dialogService.openCreateNewTeam($scope);
        }

        $scope.editTeam = function (teamId, teamName) {
            dialogService.openEditTeam(teamId, teamName, $scope)
        }

        $scope.deleteTeam = function (teamId, teamName) {
            dialogService.deleteTeam(teamId, teamName, $scope);
        }

        $scope.createRetro = function () {
            dialogService.openCreateNewRetroEvent($scope);
        }

        $scope.updateRetro = function (retroId, retroName) {
            $scope.retroIdToUpdate = retroId;
            dialogService.openUpdateRetroEvent(retroName, $scope);
        }

        $scope.deleteRetro = function (retroId, retroName) {
            dialogService.deleteRetro(retroId, retroName, $scope);
        }

        $scope.openRetro = function (retroId) {
            $rootScope.openedRetroId = retroId;
            $location.path('/retro');
        }

        $scope.openProfileImgUpload = function () {
            dialogService.openProfileImgUpload();
        }

        $scope.openEditSMastersList = function () {
            dialogService.openEditSMastersList($scope);
        }

        $scope.openUserMenu = function () {
            dialogService.openUserMenu($scope);
        }

        $scope.updatePersonalName = function () {
            usersService.updateUserPersonalName($scope);
        }

        $scope.changePasswd = function () {
            usersService.changePasswd($scope);
        }

        $scope.updatePersonalData = function () {
            usersService.updatePersonalData($scope)
        }
    }
);