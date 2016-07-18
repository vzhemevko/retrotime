/**
 * Created by vzhemevko on 14.09.15.
 */
'use strict';

retroApp.factory('notifyService', ['$timeout',
    function ($timeout) {

        function showError(txt, $scope) {
            $scope.isShowError = true;
            $scope.isShowWarning = false;
            $scope.isShowSuccess = false;
            showMsq(txt, $scope);
        }

        function showSuccess(txt, $scope) {
            $scope.isShowError = false;
            $scope.isShowWarning = false;
            $scope.isShowSuccess = true;
            showMsq(txt, $scope);
        }

        function showWarning(txt, $scope) {
            $scope.isShowError = false;
            $scope.isShowWarning = true;
            $scope.isShowSuccess = false;
            showMsq(txt, $scope);
        }


        function showMsq(txt, $scope) {
            $scope.isShowNotification = true;
            $scope.notifyText = txt;
            $timeout(function () {
                $scope.isShowNotification = false;
                //$scope.notifyText = "";
            }, 7000); // 7 sec
        }

        return {
            showError: showError,
            showSuccess: showSuccess,
            showWarning: showWarning
        }
    }
]);