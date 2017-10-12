(function () {
    'use strict';

    angular
        .module('lexisNexisHack2017App')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
