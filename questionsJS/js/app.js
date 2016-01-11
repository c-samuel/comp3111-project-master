/*global angular */
/*jshint unused:false */
'use strict';

/**
* The main TodoMVC app module
* ngSanitize added
*
* @type {angular.Module}
*/
var todomvc = angular.module('todomvc', ['firebase', 'ngStorage', 'ngSanitize', 'ui.bootstrap', 'angular-smilies'])

.directive("mathjaxBind", function() {
    return {
        restrict: "A",
        controller: ["$scope", "$element", "$attrs", function($scope, $element, $attrs) {
            $scope.$watch($attrs.mathjaxBind, function(value) {
                var $script = angular.element("<script type='math/tex'>")
                    .html(value == undefined ? "" : value);
                $element.html("");
                $element.append($script);
                MathJax.Hub.Queue(["Reprocess", MathJax.Hub, $element[0]]);
            });
        }]
    };
});
