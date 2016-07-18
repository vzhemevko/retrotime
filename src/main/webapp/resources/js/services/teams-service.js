/**
 * Created by vzhemevko on 03.09.15.
 */
'use strict';

retroApp.factory('teamsService', ['$log', '$resource', 'notifyService',
    function ($log, $resource, notifyService) {

        var teams = $resource("api/teams", {},
            // configuration
            {
                'get': {
                    method: 'GET',
                    isArray: true
                },
                'save': {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    isArray: true
                },
                'delete': {
                    method: 'DELETE',
                    isArray: true
                }
            })


        function fetchList($scope) {
            teams.get(
                // success handler
                function (data) {
                    $scope.teamsList = data;
                    $log.info("Got a teams list...");
                },
                // error handler
                function () {
                    $log.warn("Failed to get a teams list :(")
                    //todo show error
                });
        };

        function createOrUpdateTeam(teamsData, $scope) {
            $log.info('Modifying a team...');
            teams.save({
                    teamName: teamsData.teamName,
                    teamId: teamsData.teamId
                },
                // request body
                JSON.stringify(teamsData.users),
                // success handler
                function (data) {
                    $scope.teamsList = data;
                    if ($scope.isTeamUpdate) {
                        $log.info("The team was updated...");
                        notifyService.showSuccess('The "' + teamsData.teamName + '" team was updated.', $scope);
                    } else {
                        $log.info("A new team was created...");
                        notifyService.showSuccess('The "' + teamsData.teamName + '" team was created.', $scope);
                    }

                },
                // error handler
                function () {
                    if ($scope.isTeamUpdate) {
                        $log.error('The ' + teamsData.teamName + ' team was not updated...');
                        notifyService.showError('Error. The "' + teamsData.teamName + '" team was not updated. Please try again.', $scope);
                    } else {
                        $log.error('The ' + teamsData.teamName + ' team was not created...');
                        notifyService.showError('Error. The "' + teamsData.teamName + '" team was not created. Please try again.', $scope);

                    }
                }
            );
        }

        function deleteTeam(teamId, $scope) {
            $log.info('Deleting a new team...');

            teams.delete({
                    teamId: teamId
                },
                // success handler
                function (data) {
                    $scope.teamsList = data;
                    // reset
                    $scope.isRetroOpened = false;
                    $scope.teamIdOpened = ""; // team is closed
                    $log.info("The team was deleted...");
                    notifyService.showSuccess('The team was deleted.', $scope);
                },
                // error handler
                function () {
                    $log.error('New team was not deleted...');
                    notifyService.showError('Error. Team was not deleted. Please refresh the page and try again.', $scope);
                }
            );
        }

        return {
            teams: teams,
            fetchList: fetchList,
            createOrUpdateTeam: createOrUpdateTeam,
            deleteTeam: deleteTeam
        }; // return service's interface
    }
]);