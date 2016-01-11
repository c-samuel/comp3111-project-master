/*global todomvc, angular, Firebase */
'use strict';
/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the $firebaseArray service
 * - exposes the model to the template and provides event handlers
 */
todomvc.controller('TodoCtrl', ['$scope', '$location', '$firebaseArray', '$sce',
  '$localStorage', '$window', '$timeout',
  function($scope, $location, $firebaseArray, $sce, $localStorage, $window,
    $timeout) {
    // set local storage
    $scope.$storage = $localStorage;
    var scrollCountDelta = 10;
    $scope.maxQuestion = scrollCountDelta;
    var splits = $location.path().trim().split("/");
    var roomId = angular.uppercase(splits[1]);
    if (!roomId || roomId.length === 0) {
      roomId = "TEST";
    }
    // change this URL for your app
    var firebaseURL = "https://comp3111-qroom.firebaseio.com/";
    var appURL = "https://comp3111-qroom.firebaseapp.com/";
    $scope.roomId = roomId;
    var url = firebaseURL + roomId + "/questions/";
    var urlReplies = firebaseURL + roomId + "/replies/";
    var firebaseRef = new Firebase(firebaseURL);
    var echoRef = new Firebase(url);
    var echoRefReplies = new Firebase(urlReplies);
    var query = echoRef.orderByChild("order");
    var queryReplies = echoRefReplies.orderByChild("order");
    var rooms = [];
    // check if the room is in the hot room list, if not add it 
    firebaseRef.on('child_changed', function(childSnapshot, prevChildKey) {
      if (rooms.length > 10) {
        rooms.pop();
      }
      var room = childSnapshot.key(); //get the roomId
      var flag = true;
      var now = new Date().getTime();
      rooms.forEach(function(element, index, array) {
        if (element.id == room) {
          flag = false; //room already in the list
          array[index].st = true; //update the state
        }
        if (now - element.ts > 60000) {
          array[index].st = false;
        }
      });
      if (flag) {
        rooms.push({
          id: room,
          ts: new Date().getTime(),
          st: true,
          ref: appURL + "question.html#/" + room
        });
        $scope.rooms = rooms;
      }
    });
    $scope.todos = $firebaseArray(query);
    $scope.todosReplies = $firebaseArray(queryReplies);
    $scope.editedTodo = null;
    //calculate post time
    $scope.timeAgo = function(past) {
      var now = new Date().getTime();
      var ts = past;
      var delta = (now - ts) / 1000;
      var interval = delta - delta % 1;
      if (interval < 60) {
        return "just now";
      } else if (interval > 59 && interval < 3600) {
        var x = interval / 60;
        var min = x - x % 1;
        if (min == 1) {
          return min + " min" + " ago";
        } else return min + " mins" + " ago";
      } else if (interval > 3599 && interval < 86400) {
        var x = interval / 3600;
        var hr = x - x % 1;
        if (hr == 1) {
          return hr + " hour" + " ago";
        } else return hr + " hours" + " ago";
      } else if (interval > 86399 && interval < 604800) {
        var x = interval / 86400;
        var day = x - x % 1;
        if (day == 1) {
          return day + " day" + " ago";
        } else return day + " days" + " ago";
      } else {
        return new Date(past).toString();
      }
    }
    $scope.getFileName = function() {
        //this gets the full url
        var url = document.location.href;
        //this removes the anchor at the end, if there is one
        url = url.substring(0, (url.indexOf("#") == -1) ? url.length : url.indexOf(
          "#"));
        //this removes the query after the file name, if there is one
        url = url.substring(0, (url.indexOf("?") == -1) ? url.length : url.indexOf(
          "?"));
        //this removes everything before the last slash in the path
        url = url.substring(url.lastIndexOf("/") + 1, url.length);
        //return
        alert(url);
        return url;
      }
      // pre-precessing for collection
    $scope.$watchCollection('todos', function() {
      var total = 0;
      $scope.todos.forEach(function(todo) {
        // Skip invalid entries so they don't break the entire app.
        if (!todo || !todo.head) {
          return;
        }
        total++;
      });
      $scope.totalCount = total;
      $scope.absurl = $location.absUrl();
    }, true);
    //filter words detector, return true is detected
    $scope.filterWord = function($string) {
      var str = $string;
      //foul words library of regular expressions
      var filterRule =
        /\bass\b|\bfuck\b|\bshit\b|\bbitch\b|\bfucking\b/gi;
      var wRegExp = new RegExp(filterRule);
      return wRegExp.test(str);
    };
    // Get the first sentence and rest
    $scope.getFirstAndRestSentence = function($string) {
      var head = $string;
      var desc = '';
      var separators = [".", "?", "!", '\n'];
      var firstIndex = -1;
      for (var i in separators) {
        var index = $string.indexOf(separators[i]);
        if (index == -1) continue;
        if (firstIndex == -1) {
          firstIndex = index;
          continue;
        }
        if (firstIndex > index) {
          firstIndex = index;
        }
      }
      if (firstIndex != -1) {
        head = $string.slice(0, firstIndex + 1);
        desc = $string.slice(firstIndex + 1);
      }
      return [head, desc];
    };
    $scope.addTodo = function() {
      var newTodo = $scope.input.wholeMsg.trim();
      var todoID = $scope.input.email;
      var tag = "";
      if (!newTodo.length) {
        return;
      }
      if (newTodo.indexOf("#") != -1) {
        var values = newTodo.split("#");
        tag = values[1];
        newTodo = values[0];
      }
      var firstAndLast = $scope.getFirstAndRestSentence(newTodo);
      var head = firstAndLast[0];
      var desc = firstAndLast[1];
      var image = null;
      $scope.todos.$add({
        wholeMsg: newTodo,
        head: head,
        desc: Autolinker.link(desc, {
          newWindow: false,
          stripPrefix: false
        }),
        timestamp: new Date().getTime(),
        tags: tag,
        email: todoID,
        echo: 0,
        order: 0,
        image: image,
        newQuestion: true
      });
      // remove the posted question and email address in the input
      $scope.input.wholeMsg = '';
      $scope.input.email = '';
      $scope.input.searchMsg = '';
      $scope.input.searchEmail = '';
      // set time interval to 5s to prevent user keep posting questions
      $scope.setPostTimeInterval();
    };
    //for like button
    $scope.addEcho = function(todo) {
      $scope.editedTodo = todo;
      if ($scope.$storage[todo.$id] == "echoed") {
        todo.echo = todo.echo - 1;
        todo.order = todo.order + 1;
        $scope.$storage[todo.$id] = "";
      } else if ($scope.$storage[todo.$id] == "d_echoed") {
        todo.echo = todo.echo + 2;
        todo.order = todo.order - 2;
        $scope.$storage[todo.$id] = "echoed";
      } else {
        todo.echo = todo.echo + 1;
        todo.order = todo.order - 1;
        $scope.$storage[todo.$id] = "echoed";
      }
      $scope.todos.$save(todo);
    };
    //for dislike button
    $scope.subEcho = function(todo) {
      $scope.editedTodo = todo;
      if ($scope.$storage[todo.$id] == "d_echoed") {
        todo.echo = todo.echo + 1;
        todo.order = todo.order - 1;
        $scope.$storage[todo.$id] = "";
      } else if ($scope.$storage[todo.$id] == "echoed") {
        todo.echo = todo.echo - 2;
        todo.order = todo.order + 2;
        $scope.$storage[todo.$id] = "d_echoed";
      } else {
        todo.echo = todo.echo - 1;
        todo.order = todo.order + 1;
        $scope.$storage[todo.$id] = "d_echoed";
      }
      $scope.todos.$save(todo);
    };
    $scope.editTodo = function(todo) {
      $scope.editedTodo = todo;
      $scope.$storage.editMsg[todo.$id] = todo.wholeMsg + "#" + todo.tags;
      $scope.originalTodo = angular.extend({}, $scope.editedTodo);
    };
    $scope.doneEditing = function(todo) {
      var editTodo = ($scope.$storage.editMsg[todo.$id]).trim();
      if (editTodo) {
        $scope.editedTodo = todo;
        var tag = todo.tags;
        var wholeMsg = editTodo;
        if (editTodo.indexOf("#") != -1) {
          var values = editTodo.split("#");
          tag = values[1];
          wholeMsg = values[0];
        }
      }
    };
    // pre-precessing for collection
    $scope.$watchCollection('todos', function() {
      var total = 0;
      $scope.todos.forEach(function(todo) {
        // Skip invalid entries so they don't break the entire app.
        if (!todo || !todo.head) {
          return;
        }
        total++;
      });
      $scope.totalCount = total;
      $scope.absurl = $location.absUrl();
    }, true);
    //filter words detector, return true is detected
    $scope.filterWord = function($string) {
      var str = $string;
      //foul words library of regular expressions
      var filterRule =
        /\bass\b|\bfuck\b|\bshit\b|\bbitch\b|\bfucking\b/gi;
      var wRegExp = new RegExp(filterRule);
      return wRegExp.test(str);
    };
    // Get the first sentence and rest
    $scope.getFirstAndRestSentence = function($string) {
      var head = $string;
      var desc = '';
      var separators = [".", "?", "!", '\n'];
      var firstIndex = -1;
      for (var i in separators) {
        var index = $string.indexOf(separators[i]);
        if (index == -1) continue;
        if (firstIndex == -1) {
          firstIndex = index;
          continue;
        }
        if (firstIndex > index) {
          firstIndex = index;
        }
      }
      if (firstIndex != -1) {
        head = $string.slice(0, firstIndex + 1);
        desc = $string.slice(firstIndex + 1);
      }
      return [head, desc];
    };
    $scope.addTodo = function() {
      var newTodo = $scope.input.wholeMsg.trim();
      var todoID = $scope.input.email;
      var tag = "";
      if (!newTodo.length) {
        return;
      }
      if (newTodo.indexOf("#") != -1) {
        var values = newTodo.split("#");
        tag = values[1];
        newTodo = values[0];
      }
      var firstAndLast = $scope.getFirstAndRestSentence(newTodo);
      var head = firstAndLast[0];
      var desc = firstAndLast[1];
      $scope.todos.$add({
        wholeMsg: newTodo,
        head: head,
        desc: Autolinker.link(desc, {
          newWindow: false,
          stripPrefix: false
        }),
        timestamp: new Date().getTime(),
        tags: tag,
        email: todoID,
        echo: 0,
        order: 0,
        newQuestion: true,
      });
      // remove the posted question and email address in the input
      $scope.input.wholeMsg = '';
      $scope.input.email = '';
      $scope.input.searchMsg = '';
      $scope.input.searchEmail = '';
      // set time interval to 5s to prevent user keep posting questions
      $scope.setPostTimeInterval();
    };
    //for like button
    $scope.addEcho = function(todo) {
      $scope.editedTodo = todo;
      if ($scope.$storage[todo.$id] == "echoed") {
        todo.echo = todo.echo - 1;
        todo.order = todo.order + 1;
        $scope.$storage[todo.$id] = "";
      } else if ($scope.$storage[todo.$id] == "d_echoed") {
        todo.echo = todo.echo + 2;
        todo.order = todo.order - 2;
        $scope.$storage[todo.$id] = "echoed";
      } else {
        todo.echo = todo.echo + 1;
        todo.order = todo.order - 1;
        $scope.$storage[todo.$id] = "echoed";
      }
      $scope.todos.$save(todo);
    };
    //for dislike button
    $scope.subEcho = function(todo) {
      $scope.editedTodo = todo;
      if ($scope.$storage[todo.$id] == "d_echoed") {
        todo.echo = todo.echo + 1;
        todo.order = todo.order - 1;
        $scope.$storage[todo.$id] = "";
      } else if ($scope.$storage[todo.$id] == "echoed") {
        todo.echo = todo.echo - 2;
        todo.order = todo.order + 2;
        $scope.$storage[todo.$id] = "d_echoed";
      } else {
        todo.echo = todo.echo - 1;
        todo.order = todo.order + 1;
        $scope.$storage[todo.$id] = "d_echoed";
      }
      $scope.todos.$save(todo);
    };
    $scope.editTodo = function(todo) {
      $scope.editedTodo = todo;
      $scope.$storage.editMsg[todo.$id] = todo.wholeMsg + "#" + todo.tags;
      $scope.originalTodo = angular.extend({}, $scope.editedTodo);
    };
    $scope.doneEditing = function(todo) {
      var editTodo = ($scope.$storage.editMsg[todo.$id]).trim();
      if (editTodo) {
        $scope.editedTodo = todo;
        var tag = todo.tags;
        var wholeMsg = editTodo;
        if (editTodo.indexOf("#") != -1) {
          var values = editTodo.split("#");
          tag = values[1];
          wholeMsg = values[0];
        }
        var firstAndRest = $scope.getFirstAndRestSentence(wholeMsg);
        var head = firstAndRest[0];
        var desc = firstAndRest[1];
        todo.wholeMsg = wholeMsg;
        todo.head = head;
        todo.desc = desc;
        todo.tags = tag;
        $scope.todos.$save(todo);
      } else {
        $scope.editedTodo = null;
      }
    };
    $scope.revertEditing = function(todo) {
      todo.wholeMsg = $scope.originalTodo.wholeMsg;
    };
    $scope.removeTodo = function(todo) {
      $scope.todos.$remove(todo);
    };
    $scope.addReply = function(todo) {
      var newTodo = $scope.input.replyMsg.trim();
      if (newTodo.length < 5) {
        console.log("Reply too short");
        return;
      }
      $scope.todos.$save(todo);
      // add to reply array
      $scope.todosReplies.$add({
        questionID: todo.$id,
        desc: newTodo,
        timestamp: new Date().getTime(),
        order: 0,
      });
      // remove the posted question in the input
      $scope.input.replyMsg = '';
      $scope.todos.$save(todo);
      // set the post time of reply (allow reply again in 5s)
      $scope.setPostTimeInterval();
    };
    $scope.increaseMax = function() {
      if ($scope.maxQuestion < $scope.totalCount) {
        $scope.maxQuestion += scrollCountDelta;
      }
    };
    $scope.toTop = function toTop() {
      $window.scrollTo(0, 0);
    };
    if ($location.path() === '') {
      $location.path('/');
    }
    $scope.location = $location;
    // autoscroll
    angular.element($window).bind("scroll", function() {
      if ($window.innerHeight + $window.scrollY >= $window.document.body
        .offsetHeight) {
        console.log('Hit the bottom2. innerHeight' + $window.innerHeight +
          "scrollY" + $window.scrollY + "offsetHeight" + $window.document
          .body.offsetHeight);
        // update the max value
        $scope.increaseMax();
        // force to update the view (html)
        $scope.$apply();
      }
    });
    // set post time interval to prevent user repeating post questions
    $scope.setPostTimeInterval = function() {
      // disable the button after post button clicked
      $scope.disableTimeInterval = true;
      // time count down
      $scope.postTimeCounter = 5;
      // refresh and display the time count
      $scope.onTimeout = function() {
        $scope.postTimeCounter--;
        mytimeout = $timeout($scope.onTimeout, 1000);
      }
      var mytimeout = $timeout($scope.onTimeout, 1000);
      // terminal the timer after 5 seconds
      $timeout(function() {
        $timeout.cancel(mytimeout);
        $scope.disableTimeInterval = false;
        $scope.postTimeCounter = 0;
      }, 5000);
    }
  }
]);
