var app = angular.module('app', ['ngStorage', 'ngRoute'])

app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: 'products.html',
            controller: 'productsController'
        })
        .when("/cart", {
            templateUrl: 'cart.html',
            controller: 'cartController'
        })
        .when("/orders", {
            templateUrl: 'orders.html',
            controller: 'orderController'
        })
        .when("/orders/orderinfo/:id", {
            templateUrl: 'orderinfo.html',
            controller: 'orderController'
    })
});


app.controller('userController', function ($scope, $rootScope, $http, $localStorage) {

    $scope.cartProductList = "";
    $scope.orderList = "";


    $scope.userIsLoggedInView = function () {
        $scope.logButton = "выйти";
        $scope.regButtonView = {visible: false};
        $scope.navButtonView = {visible: true};
        $scope.logFormView = {visible: false};
    };

    $scope.userIsLoggedOutView = function () {
        $scope.logButton = "войти";
        $scope.regButtonView = {visible: true};
        $scope.navButtonView = {visible: false}
        $scope.logFormView = {visible: false};
        $scope.regFormView = {visible:false};
        document.location='#!/';
    };

    if ($localStorage.springWebUser) {
        $scope.userIsLoggedInView();
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.springWebUser.token;
    } else {
        $scope.userIsLoggedOutView()
    }

    $scope.changeVisible = function (dom) {
        dom.visible ? dom.visible = false : dom.visible = true;
    };



    $scope.logInOutButton = function () {
        if ($scope.logButton === "выйти") {
            $scope.clearUser();
            $scope.userIsLoggedOutView();
        } else {
            $scope.changeVisible($scope.logFormView);
        }
    };

    $scope.regButton = function () {
        $scope.changeVisible($scope.regFormView);
    };

    $scope.tryToAuth = function () {
        $http.post("/app/getAuth", $scope.user)
            .then(function successCallback(response) {
                    if (response.data.token) {
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                        $localStorage.springWebUser = {username: $scope.user.username, token: response.data.token};

                        $scope.user.username = null;
                        $scope.user.password = null;
                        $scope.userIsLoggedInView();
                    }
                }, function errorCallback(response) {
                    alert(response.data.message)
                }
            );
    };


    $scope.tryToRegist = function () {
        $http.post("/app/getRegister", $scope.registration)
            .then(function successCallback () {
                alert("registration success")
                $scope.registration = null;
            }, function errorCallback (errors) {
                let alertList = [];
                for (let i = 0; i < Object.keys(errors.data).length ; i++) {
                    let key = Object.keys(errors.data)[i];
                    let value = Object.values(errors.data)[i];
                    alertList.push(key + " : " + value);
                }
                alert(alertList.join("\n"));
            })
    };


    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };


    $scope.authTimeIsOver = function () {
        alert("время авторизации истекло!")
        $scope.clearUser();
        $scope.userIsLoggedOutView()
    };


    $rootScope.isUserLoggedIn = function () {
        return $localStorage.springWebUser;
    };

});


app.controller('productsController', function ($scope, $http) {

    const contextPath = "/app/api/v1/products";


    $scope.loadProducts = function (page) {
        $http({
            url: contextPath + "/showAll",
            method: 'GET',
            params: {
                page: page,
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null
            }
        }).then(function (response) {
            $scope.productsList = response.data.content;

            let totalPages = response.data.totalPages;
            let pagesList = [];
            for (let i = 1; i <= totalPages; i++) {
                pagesList[i - 1] = i;
            }
            $scope.pages = pagesList;
        });
    };


    $scope.loadProducts();

});


app.controller('cartController', function ($scope, $http) {


    $scope.showCart = function () {
        $http({
            url: "/app/cart/showCart",
            method: 'GET'
        }).then(function successCallback(response) {
            $scope.cartProductList = response.data;
            $scope.totalPrice = 0;
            for (let i = 0; i < response.data.length; i++) {
                $scope.totalPrice += response.data[i].productDto.price * response.data[i].quantity;
            }
        }, function errorCallback(error) {
            if (error.status === 401) {
                $scope.authTimeIsOver();
            }
        })
    };


    $scope.showCart();

});


app.controller('orderController', function ($scope, $http, $routeParams) {


    $scope.showOrders = function () {
        $http({
            url: "/app/cart/showOrders",
            method: 'GET'
        }).then(function successCallback (response) {
            $scope.orderList = response.data;
        }, function errorCallback (error) {
            if (error.status === 401) {
                $scope.authTimeIsOver();
            }
        })
    };


    $scope.orderInfo = function () {
        $http({
            url: "/app/cart/orderInfo",
            method: 'GET',
            params: {
                orderId: $routeParams['id']
            }
        }).then(function successCallback (response){
            $scope.totalPrice = 0;
            for (let i = 0; i < response.data.length; i++) {
                $scope.totalPrice += response.data[i].productDto.price * response.data[i].quantity;
            }
            $scope.selectedOrder = response.data;
        }, function errorCallback (error){
            if (error.status === 401) {
                $scope.authTimeIsOver();
            }
        })
    }

    if ($routeParams['id']) {
        $scope.orderInfo();
    } else {
        $scope.showOrders();
    }

})

