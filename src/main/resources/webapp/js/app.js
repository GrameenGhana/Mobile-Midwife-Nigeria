(function () {
    'use strict';

    /* App Module */

    angular.module('mmnaija', ['motech-dashboard', 'mmnaija.controllers', 'mmnaija.directives', 'mmnaija.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/mmnaija/', {templateUrl: '../mmnaija/resources/partials/say-hello.html', controller: 'HelloWorldController'});
    }]);
}());
