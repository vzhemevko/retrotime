/**
 * Created by vzhemevko on 05.09.15.
 */
'use strict';

retroApp.controller('teamPropsController', function ($scope, $modalInstance, $http, inputData, notifyService, usersService) {

    $scope.title = inputData.title;

    $scope.inputCaption = inputData.inputCaption;

    $scope.teamName = inputData.defaultOutput;

    $scope.teamId = inputData.teamId;

    $scope.users = [];

    if ($scope.teamId) {
        // this means we are editing existing team
        usersService.getUserTagsByTeamId(inputData.teamId, $scope);
    }

    $scope.loadTags = function (query) {
        console.log(query);
        return $http.get("api/users?query=" + query);
    };

    $scope.ok = function () {
        if (!$scope.teamName) {
            notifyService.showError('Please add a team name.', $scope);
        } else if (!$scope.users.length) {
            notifyService.showError('Please add at least one team member.', $scope);
        } else {
            $scope.isListVaild = true;
            angular.forEach($scope.users, function (user) {
                if (!user.userId) {
                    notifyService.showError("Cannot find such user ' " + user.text + " '.", $scope);
                    $scope.isListVaild = false;
                    return;
                }
            });
            if ($scope.isListVaild) {
                var output = {
                    teamName: $scope.teamName,
                    teamId: $scope.teamId,
                    users: $scope.users
                }
                $modalInstance.close(output);
            }
        }
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    }
});