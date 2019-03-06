
var app = angular.module('BlankApp', ['ngAria', 'ngAnimate', 'ngMaterial', 'ngSanitize']);

app.controller('AppCtrl', function($sce, $scope, $http) {

    let PS2content = "postgres=>";

    jQuery(function($) {
        $('#shell').terminal(function(command, term) {
            if (command !== '') {
                term.pause();
		        $.ajax({
                    method: 'POST',
                    url: '/api/rest/lti/shell/sql/query',
                    data: "query=" + command,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (_data, status, headers, config) {
                    term.echo(_data, {raw: true}).resume();
                }).error(function (_data, status, header, config) {
                    term.echo(_data, {raw: true}).resume();
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
