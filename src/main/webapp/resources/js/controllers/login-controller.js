/**
 * Created by vzhemevko on 31.09.15.
 */
'use strict';

retroApp.controller('loginController',
    function loginController($scope, authService) {

        $scope.submit = function (isLoginMode) {

            if (isLoginMode) {
                authService.authenticate($scope);
            } else {
                authService.register($scope);
            }
        }
    });