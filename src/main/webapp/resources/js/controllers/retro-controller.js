/**
 * Created by vzhemevko on 08.09.15.
 */
'use strict';

retroApp.controller("retroController",
    function ($scope, $rootScope, retroService) {


        $scope.$on('$locationChangeStart', function (event, next, current) {
            retroService.leaveRetro();
        });

        $scope.init = function () {
            retroService.openRetro($scope);
        };

        $scope.genPartElemLockShowId = function (userId, partId) {
            // generates and id for particular part of the content
            // bsed on userId and partId. So later it can be used to show 
            // the lock.
            var res = 'userId' + userId + 'partId' + partId + 'showLock';
            $scope[res];
            return res;
        }

        $scope.save = function (contentPart) {
            retroService.save(contentPart);
        }

        $scope.sendLock = function (contentPart) {
            retroService.sendLock(contentPart);
        }

        $scope.sendUnLock = function (contentPart) {
            retroService.sendUnLock(contentPart);
        }

        /* $scope.checkTextAreaReadonly = function(username) {
            if (username !== currentUsername) {
                return true;
            }
            else {
                return false;
            }
         }*/
    }
);