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
                body:  'Here is some important information',
                link: 'https://www.lexisnexis.com/en-us/home.page',
                sendTime: new Date()
            };
        };

        vm.resetDefaults();

        vm.localPush = function() {
            console.log("vm.message", vm.message);

            PushService.notify({
                title: vm.message.title,
                icon: vm.message.icon,
                body: vm.message.body
            });
        };

        vm.schedulePush = function(){
            var notification = {
                title: vm.message.title,
                body: vm.message.body,
                clickAction: vm.message.link,
                icon: vm.message.icon,
                link: vm.message.link,
                sendTime: vm.message.sendTime,
                sendTo: PushService.getToken()
            };

            PushService.scheduleNotification(notification).then(function(response){
                console.log("scheduleNotification", response);
            });
        }

        // highlights code on the page
        $('pre code').each(function(i, block) {
            hljs.highlightBlock(block);
        });
    }
})();
