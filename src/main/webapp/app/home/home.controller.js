(function() {
    'use strict';

    angular
        .module('lexisNexisHack2017App')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'PushService', '$state', '$timeout'];

    function HomeController ($scope, Principal, LoginService, PushService, $state, $timeout) {
        var vm = this;

        PushService.initialize();

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register () {
            $state.go('register');
        }

        vm.testPush = function(){
            $timeout(function(){
                Push.create('Hello World!',
                {
                    icon: '/content/images/ln_kb.png'
                });
            }, 5000);
        };
    }
})();
