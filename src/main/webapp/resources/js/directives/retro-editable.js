/**
 * Created by vzhemevko on 02.10.15.
 */
'use strict'

retroApp.directive('retroEditable', function ($parse, $timeout) {


    return {
        restrict: 'A',
        link: function ($scope, element, attrs) {
            element.on('click', function () {
                var editedPart = $parse(attrs.part)($scope);
                $scope.sendLock(editedPart);
                attrs.$set('readonly', false);

                console.log("Start editing retro content part...")

                element.on('blur', function () {
                    var editedPart = $parse(attrs.part)($scope);

                    $scope.save(editedPart);

                    $timeout(function () {
                        $scope.sendUnLock(editedPart);
                    }, 1000); // 1 sec

                    attrs.$set('readonly', true);

                    console.log("End editing retro content part...");
                });

            })
        }
    }
})