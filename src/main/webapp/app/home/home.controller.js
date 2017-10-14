(function() {
    'use strict';

    angular
        .module('lexisNexisHack2017App')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'PushService', '$state'];

    function HomeController ($scope, Principal, LoginService, PushService, $state) {
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

        vm.resetDefaults = function(){
            vm.message = {
                title: 'Test Push',
                icon:  'https://pbs.twimg.com/profile_images/471304690026545152/MOL2TpT7_400x400.jpeg',
                body:  'Here is some important information'
            };
        };

        vm.resetDefaults();

        vm.testPush = function() {
            console.log("vm.message", vm.message);

            PushService.notify({
                title: vm.message.title,
                icon: vm.message.icon,
                body: vm.message.body
            });
        };

        // highlights code on the page
        $('pre code').each(function(i, block) {
            hljs.highlightBlock(block);
        });
    }
})();
