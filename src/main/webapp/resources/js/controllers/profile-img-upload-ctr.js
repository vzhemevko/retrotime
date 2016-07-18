/**
 * Created by vzhemevko on 20.09.15.
 */
'uses strict'

retroApp.controller('profileImgUploadCtr', function ($scope, $modalInstance, inputData, notifyService) {

    $scope.preview = function () {
        var reader = new FileReader();
        reader.readAsDataURL($scope.file);

        reader.onloadend = function () {
            $scope.imageSrc = reader.result;
            $scope.$apply();
        }

        /* reader.readAsBinaryBuffer(file);*/
    }


    $scope.ok = function () {
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    }
});

retroApp.directive("ngFileSelect", function () { // TODO move to directive folder
    return {
        link: function ($scope, el) {
            el.bind("change", function (e) {

                $scope.file = (e.srcElement || e.target).files[0];
                $scope.preview();
            })
        }
    }
});


retroApp.directive("imageResize", [ // TODO move to directive folder
    "$parse",
    function ($parse) {
        return {
            link: function (scope, elm, attrs) {
                var imageSize;
                imageSize = $parse(attrs.imageSize)(scope);
                elm.bind("load", function () {
                    var canvas, ctx
                    canvas = document.createElement("canvas");
                    canvas.width = imageSize;
                    canvas.height = imageSize;
                    ctx = canvas.getContext("2d");
                    ctx.drawImage(elm[0], 0, 0, imageSize, imageSize);
                    elm.attr('src', canvas.toDataURL("image/jpeg"));
                });
            }
        };
    }
]);