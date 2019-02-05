
var app = angular.module('BlankApp', ['ngAria', 'ngAnimate', 'ngMaterial']);

app.controller('AppCtrl', function($scope, $http) {

    $scope.terminalContent = "user@datastorage:~ ";

    $scope.processSql = function (e) {
        if(e.which === 13) {

            var query = $scope.sqlQuery;

            if(query === "") {
                setValues("");
            } else {

                /* create post query to LTI */

                $http({
                    method: 'POST',
                    url: '/api/rest/lti/shell/sql/query',
                    data: "query=" + query,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (_data, status, headers, config) {
                    setValues(_data);
                }).error(function (_data, status, header, config) {
                    setValues(_data);
                });

                /* ************************ */
            }
        }
    };

    function setValues(__res) {
        $scope.terminalContent = $scope.terminalContent + "\nuser@datastorage:~  " + $scope.sqlQuery + "\n" + __res;
        $scope.sqlQuery = "";
    }

});
