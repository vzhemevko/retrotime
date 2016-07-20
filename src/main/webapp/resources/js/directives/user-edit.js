/**
 * Created by vzhemevko on 10/11/15.
 */
'use strict'

retroApp.directive('userEdit', function ($parse, $timeout) {
	
    return {
        restrict: 'A',
        link: function ($scope, element, attrs) {
            element.on('click', function () {

                attrs.$set('readonly', false);
                console.log("Start editing user properties...")

                element.on('blur', function () {

                    attrs.$set('readonly', true);
                    $scope.updatePersonalData();

                    console.log("End editing user properties...");
                });

            })
        }
    }
})