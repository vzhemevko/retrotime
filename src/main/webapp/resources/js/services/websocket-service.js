/**
 * Created by vzhemevko on 13.09.15.
 */
'use strict';

retroApp.factory('webSocketService', ['$q', '$rootScope', '$location', '$log', '$timeout', '$http','notifyService', 
                'commonService',

    function ($q, $rootScope, $location, $log, $timeout, $http, notifyService, commonService) {

        var ws = {} // not initialized.


        function isConnected() {
            return ws && ws.readyState === 1 /*open state id*/;
        }

        function close() {
            ws && ws.close();
        }

        function fixConnection($scope) {
            $scope.isReconnection = true;
            if ($scope.reconAttempts === undefined) {
                $scope.reconAttempts = 1;
            } else {
                $scope.reconAttempts++;
            }

            if ($scope.reconAttempts < 2) { // try only once to recnnect.
                notifyService.showWarning('Lost connection with others. Trying to fix it...You can try to refresh the page.', $scope);
                $timeout(function () {
                    init($scope)
                }, 2000); // 2 sec
            } else {
                notifyService.showError('Lost connection with others. Cannot fix it...Please try to refresh the page.', $scope);
            }
        }

        function init($scope) {
            var protocol = "ws://"
            var host = $location.host();
            var port = $location.port();
            var path = $location.path();

            try {
                if ($rootScope.openedRetroId && !isConnected()) {
                    // TODO chane hardcoded app name
                    var connectionURL = protocol + host + ":" + port + "/retrotime/sock/" + $rootScope.openedRetroId;
                    $log.info("WebSocket: Connecting to : " + connectionURL);
                    ws = new WebSocket(connectionURL);
                    ws.webSocketConnUuid = generateUuid();
                }
            } catch (err) {
                $log.error("WebSocket: Caught an error: " + err.message);
            }

            ws.onclose = function () {
                $log.info("WebSocket: Connetction closed.");
                //fixConnection($scope)
            }

            ws.onopen = function () {
                try {
                    $log.info("WebSocket: Socket connection has been established.");
                    if ($scope.isReconnection) {
                        notifyService.showSuccess('Connection has been established.', $scope);
                        $scope.isReconnection = false;
                        $scope.reconAttempts = 0;
                    }
                } catch (err) {
                    $log.error("WebSocket: Caught an error: " + err.message);
                }
            };

            ws.onmessage = function (message) {
                try {

                    var contentPart
                    if (commonService.isJSON(message.data)) {
                        contentPart = JSON.parse(message.data);
                    } else {
                        return;
                    }

                    if (contentPart.lockType) {

                        if (contentPart.senderUuid === ws.webSocketConnUuid) {
                            return; // no need to lock our selfs
                        }

                        var lockPart;
                        if (contentPart.partId === 4 /*action items*/) {
                            $scope.retroCont.actionItems.senderName = contentPart.senderName;
                            lockPart = 'actionItemsLock';

                        } else {
                            $scope.usersContentMap[contentPart.userId].partsMap[contentPart.partId].senderName = contentPart.senderName;
                            lockPart = 'userId' + contentPart.userId + 'partId' + contentPart.partId + 'showLock';
                        }
                        if (contentPart.locked) {
                            $log.info("WebSocket: Recieved a lock for userId : " + contentPart.userId + " partId : " + contentPart.partId);
                            $scope[lockPart] = true;


                        } else {
                            $log.info("WebSocket: Recieved an unlock for userId : " + contentPart.userId + " partId : " + contentPart.partId);
                            $scope[lockPart] = false;
                        }
                        $scope.$apply();
                        return;
                    } else {
                        // Not a lock It's an update content message
                        if (contentPart.partId === 4 /*action items*/) {
                            $scope.retroCont.actionItems.text = contentPart.text
                        } else {
                            var userContent = $scope.usersContentMap[contentPart.userId];
                            if (!userContent) {
                                addNonExistingUser(contentPart, $scope);
                            }
                            $scope.usersContentMap[contentPart.userId].partsMap[contentPart.partId].text = contentPart.text;

                        }
                    }
                    $scope.$apply();
                } catch (err) {
                    $log.error("WebSocket: Caught an error when recieving a websocket message: " + err.message);
                }
            };

            ws.onerror = function (event) {
                $log.error("WebSocket: error:" + event);
            }
        }

        function sendMessage(contentPart) {
            if (!ws) {
                $log.warn("WebSocket: Web Socket connection has not been established. Cannot send a message :(");
                return
            } else {
                $log.info("WebSocket: Sending a websocket message...")

                try {
                    if (contentPart) {

                        contentPart.senderUuid = ws.webSocketConnUuid;
                        contentPart.senderName = $rootScope.currentUser.personalName;

                        var message = JSON.stringify(contentPart);
                        /*  // preserve newlines, etc - use valid JSON
                         message = message.replace(/\\n/g, "\\n")
                         .replace(/\\'/g, "\\'")
                         .replace(/\\"/g, '\\"')
                         .replace(/\\&/g, "\\&")
                         .replace(/\\r/g, "\\r")
                         .replace(/\\t/g, "\\t")
                         .replace(/\\b/g, "\\b")
                         .replace(/\\f/g, "\\f");
                         // remove non-printable and other non-valid JSON chars
                         message = message.replace(/[\u0000-\u0019]+/g,"");*/

                        if (commonService.isJSON(message)) {

                            ws.send(message);

                        } else {
                            $log.warn("WebSocket: message [ " + message + " ] cannot be sent. It's not a JSON");
                        }


                    }
                } catch (err) {
                    $log.error("WebSocket: Caught an error when sending a websocket message: " + err.message);
                }

            }
        }

        function addNonExistingUser(receivedPart, $scope) {
            // clone first user object in the map
            var firstContentObj = Object.keys($scope.usersContentMap)[0]
            var userContent = {};
            angular.copy($scope.usersContentMap[firstContentObj], userContent);
            // reset all props values of cloned object and set
            // user values from received content part.

            userContent.userId = receivedPart.userId;
            userContent.username = receivedPart.username;
            userContent.personalName = receivedPart.personalName;

            angular.forEach(userContent.partsMap, function (part) {
                part.contentId = receivedPart.contentId;
                part.userId = receivedPart.userId;
                part.username = receivedPart.username;
                if (part.partId === receivedPart.partId) {
                    part.text = receivedPart.text;
                } else {
                    part.text = "";
                }
            });
            $scope.usersContentMap[receivedPart.userId] = userContent;
        }

        function generateUuid() {
            function s4() {
                return Math.floor((1 + Math.random()) * 0x10000)
                    .toString(16)
                    .substring(1);
            }

            return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                s4() + '-' + s4() + s4() + s4();
        }

        return {
            sendMessage: sendMessage,
            init: init,
            isConnected: isConnected,
            close: close
        };
    }
])