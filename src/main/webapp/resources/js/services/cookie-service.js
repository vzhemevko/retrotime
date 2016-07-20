/**
 * Created by vzhemevko on 09.10.15.
 */
retroApp.factory('cookieService', ['$cookieStore', 'commonService',
    function ($cookieStore, commonService) {
        // TODO probably it makes sense to store all
        // this properties as single object
        var prop1 = "prop1";
        /*username*/
        var prop2 = "prop2";
        /*creds encoded*/
        var prop3 = "prop3";
        /*opened team id*/
        var prop4 = "prop4";
        /*opened retro id*/
        var prop5 = "prop5";
        /*opened team name*/
        var prop6 = "prop6";
        /*current user id*/
        var prop7 = "prop7";
        /*current user role*/
        var prop8 = "prop8";
        /*user dto object TODO move all above props to this object*/


        function setAuthData(credsEncoded) {
            $cookieStore.put(prop2, credsEncoded);
        }

        function getAuthData() {
            return $cookieStore.get(prop2);
        }

        function setCurrentUsername(username) {
            $cookieStore.put(prop1, username);
        }

        function setOpenedTeamId(teamId) {
            $cookieStore.put(prop3, teamId);
        }


        function setOpenedRetroId(retroId) {
            $cookieStore.put(prop4, retroId);
        }

        function setOpenedTeamName(teamName) {
            $cookieStore.put(prop5, teamName);
        }

        function setCurrentUserId(userId) {
            $cookieStore.put(prop6, userId);
        }

        function setCurrentUserRole(userId) {
            $cookieStore.put(prop7, userId);
        }

        function setCurrentUser(user) {
            var userJSON = JSON.stringify(user)
            $cookieStore.put(prop8, btoa(userJSON));
        }

        function getCurrentUserId() {
            return $cookieStore.get(prop6);
        }

        function getCurrentUsername() {
            return $cookieStore.get(prop1);
        }

        function getOpenedTeamId() {
            return $cookieStore.get(prop3);
        }


        function getOpenedRetroId() {
            return $cookieStore.get(prop4);
        }

        function getOpenedTeamName() {
            return $cookieStore.get(prop5);
        }

        function getCurrentUserRole() {
            return $cookieStore.get(prop7);
        }

        function getCurrentUser() {
            var userEnc = $cookieStore.get(prop8);
            if (userEnc) {
                var userDec = atob(userEnc);
                if (commonService.isJSON(userDec)) {
                    return JSON.parse(userDec);
                }
            }
            return "";
        }

        function clearCookies() {
            $cookieStore.remove('JSESSIONID');
            $cookieStore.remove(prop1);
            $cookieStore.remove(prop2);
            $cookieStore.remove(prop3);
            $cookieStore.remove(prop4);
            $cookieStore.remove(prop5);
            $cookieStore.remove(prop6);
            $cookieStore.remove(prop7);
            $cookieStore.remove(prop8);
        }

        return {
            setAuthData: setAuthData,
            getAuthData: getAuthData,
            setCurrentUsername: setCurrentUsername,
            setOpenedTeamId: setOpenedTeamId,
            setOpenedRetroId: setOpenedRetroId,
            setOpenedTeamName: setOpenedTeamName,
            setCurrentUserId: setCurrentUserId,
            setCurrentUserRole: setCurrentUserRole,
            setCurrentUser: setCurrentUser,
            getCurrentUserId: getCurrentUserId,
            getCurrentUsername: getCurrentUsername,
            getOpenedTeamId: getOpenedTeamId,
            getOpenedRetroId: getOpenedRetroId,
            getOpenedTeamName: getOpenedTeamName,
            getCurrentUserRole: getCurrentUserRole,
            getCurrentUser: getCurrentUser,
            clearCookies: clearCookies
        };
    }
]);