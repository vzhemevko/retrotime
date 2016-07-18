/**
 * Created by vzhemevko on 04.09.15.
 */

'use strict';

retroApp.controller('retroPropsController', function ($scope, $modalInstance, inputData, notifyService) {

    $scope.title = inputData.title;

    $scope.inputCaption = inputData.inputCaption;

    $scope.output = inputData.defaultOutput;

    $scope.ok = function () {
        if (!$scope.output) {
            notifyService.showError('Please add a name for retrospective.', $scope);
        } else {
            $modalInstance.close($scope.output);
        }
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    }
});