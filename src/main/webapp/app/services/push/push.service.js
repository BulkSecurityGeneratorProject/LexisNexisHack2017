(function() {
    'use strict';

    angular
        .module('lexisNexisHack2017App')
        .factory('PushService', PushService);

    PushService.$inject = ["$http"];

    function PushService($http) {

        var pushToken = null;

        function getToken(){
            return pushToken;
        }

        function initialize(){

            var config = {
                apiKey: "AIzaSyCpwn-7tMTkiedODQR2RLVVGLSbFWwVbLY",
                authDomain: "pushnotifications-4a87b.firebaseapp.com",
                databaseURL: "https://pushnotifications-4a87b.firebaseio.com",
                projectId: "pushnotifications-4a87b",
                storageBucket: "pushnotifications-4a87b.appspot.com",
                messagingSenderId: "140755737702",
                onMessage: function(message){
                    console.log("message", message);
                    Push.create(message.notification.title,
                        {
                            icon: 'https://pbs.twimg.com/profile_images/471304690026545152/MOL2TpT7_400x400.jpeg',
                            body: message.notification.body,
                            onClick: function(){
                                window.open(message.notification.click_action);
                            }
                        });
                },
                sendTokenToServer: function(token){
                    // This is the token tied to the user that you need to send to the Rest API
                    // NOTE: You may want to associate the token with the user profile
                    // You can do this with a Rest call to your application
                }
            };

            Push.config({
                FCM: config
            });

            Push.FCM().then(function(FCM){

                FCM.getToken().then(function(token) {
                    console.log("Initialized with token " + token);
                    pushToken = token;
                }).catch(function(tokenError) {
                    throw tokenError;
                });
                FCM.isTokenSentToServer();

            }).catch(function(initError) {
                console.log("initError", initError);
                throw initError;
            });


        }

        function notify(message){
            Push.create(message.title,
                {
                    icon: message.icon,
                    body: message.body
                });
        }

        function scheduleNotification(notification){

            // additional required properties of notification
            notification.data = null;
            notification.notificationId = null;
            notification.requireInteraction = false;
            notification.sent = false;
            notification.silent = false;
            notification.tag = null;
            notification.timeout = null;
            notification.vibrate = null;

            console.log("notification", notification);

            return $http.post('/api/notifications/', notification).then(function(response){
                return response;
            });

        }

        return {
            initialize: initialize,
            notify: notify,
            scheduleNotification: scheduleNotification,
            getToken: getToken
        };
    }
})();
