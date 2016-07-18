/**
 * Created by vzhemevko on 04.10.15.
 */
'use strict'

/**
 * Created by vzhemevko on 05.09.15.
 */
'use strict';

retroApp.controller('scrumMastersDialogCtr', function($scope, $modalInstance, $http, inputData, notifyService, usersService) {

    $scope.title = inputData.title;

    $scope.sMasters = usersService.getScrumMaterTags($scope);

    $scope.loadTags = function(query) {
        console.log(query);
        return $http.get("api/users?query=" + query);
    };

    $scope.ok = function() {
        if (!$scope.sMasters.length) {
            notifyService.showError('Please add at least one scrum master.', $scope);
        } else {
            $scope.isListVaild = true;
            angular.forEach($scope.sMasters, function(smaster) {
                if (!smaster.userId) {
                    notifyService.showError("Cannot find such user ' " + smaster.text + " '.", $scope);
                    $scope.isListVaild = false;
                    return;
                }
            });
            if ($scope.isListVaild) {
                $modalInstance.close($scope.sMasters);
            }
        }
    }

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    }
});