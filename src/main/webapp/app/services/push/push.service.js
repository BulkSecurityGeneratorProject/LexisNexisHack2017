(function() {
    'use strict';

    angular
        .module('lexisNexisHack2017App')
        .factory('PushService', PushService);

    PushService.$inject = [];

    function PushService() {

        function initialize(){

            console.log("initialize push");
            //navigator.serviceWorker.register('/firebase-messaging-sw.js');

            /*
            self.addEventListener("install", function(evt){
                console.log("install called");
            });

            self.addEventListener("activate", function(evt){
                console.log("activate called");
            });
            */

            /*
            navigator.serviceWorker.register("/firebase-messaging-sw.js", {scope: "firebase-cloud-messaging-push-scope"}).then(function (registration) {
                const messaging = firebase.messaging();
                messaging.useServiceWorker(registration);
                console.log('ServiceWorker registration');
            }).catch(function (err) {
                // registration failed :(
                console.log('ServiceWorker registration failed: ', err);
            });
            */

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
                        icon: '/content/images/ln_kb.png',
                        body: message.notification.body,
                        click_action: message.click_action
                    });

                }
                //serviceWorkerLocation: "/bower_components/push-fcm-plugin" //"/firebase-messaging-sw.js" //bower_components/push-fcm-plugin/firebase-messaging-sw.js
            };
            //firebase.initializeApp(config);

            Push.config({
                FCM: config
            });

            var x = Push.FCM().then(function(FCM){
                console.log("FCM", FCM);
                FCM.getToken().then(function(token) {
                    console.log("Initialized with token " + token);
                }).catch(function(tokenError) {
                    throw tokenError;
                });

            }).catch(function(initError) {
                console.log("initError", initError);
                throw initError;
            });

            console.log("Josh was here", x);

        }

        return {
            initialize: initialize
        };
    }
})();
