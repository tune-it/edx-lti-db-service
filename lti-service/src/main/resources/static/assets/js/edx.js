
var app = angular.module('BlankApp', ['ngAria', 'ngAnimate', 'ngMaterial', 'ngSanitize']);

app.controller('AppCtrl', function($sce, $scope, $http) {

    let PS2content = "user@datastorage:~";

    jQuery(function($) {
        $('#shell').terminal(function(command, term) {
            if (command !== '') {
                $http({
                    method: 'POST',
                    url: '/api/rest/lti/shell/sql/query',
                    data: "query=" + command,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (_data, status, headers, config) {
                    term.echo(_data, {raw: true});
                }).error(function (_data, status, header, config) {
                    term.echo(_data, {raw: true});
                });
            } else {
                this.echo('');
            }

        }, {
            greetings: '',
            height: 736,
            name: 'sql_shell',
            prompt: PS2content,
        });
    });
});
