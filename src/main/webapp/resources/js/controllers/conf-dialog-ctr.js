/**
 * Created by vzhemevko on 31.09.15.
 */
'use strict'

retroApp.controller('confDialogController', function ($scope, $modalInstance, inputData) {

    $scope.title = inputData.title;
    $scope.confText = inputData.confText;

    $scope.ok = function () {
        $modalInstance.close();
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    }
});