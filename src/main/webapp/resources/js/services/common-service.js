/**
 * Created by vzhemevko on 10/9/15.
 */
retroApp.factory('commonService', [
    function () {

        function isJSON(something) {
            if (typeof something != 'string')
                something = JSON.stringify(something);

            try {
                JSON.parse(something);
                return true;
            } catch (e) {
                return false;
            }
        }

        return {
            isJSON: isJSON
        }
    }
]);