app.directive('puttocart', function ($http) {
    return {
        link: function (scope, element, attrs) {
           element.on('click', function () {

               $http({
                   url: "/app/cart/putToCart",
                   method: 'POST',
                   params: {
                       product: attrs.product
                   }
               }).then(function successCallback (){
                   alert("продукт добавлен в корзину")
               }, function errorCallback (error){
                   if (error.status === 401) {
                       scope.authTimeIsOver();
                   }
               })
           })
        }
    }
})

app.directive('delfromcart', function ($http) {
    return {
        link: function (scope, element, attrs) {
            element.on('click', function () {


                $http({
                    url: "/app/cart/delFromCart",
                    method: 'DELETE',
                    params: {
                        cartProductId: attrs.id
                    }
                }).then(function successCallback (){
                    alert("категория товаров удалена из корзины")
                    scope.showCart();
                }, function errorCallback (error){
                    if (error.status === 401) {
                        scope.authTimeIsOver();
                    }
                })
            })
        }
    }
});

app.directive('createorder', function ($http) {
    return {
        link: function (scope, element, attrs) {
            element.on('click', function () {

                $http({
                    url: "/app/cart/createOrder",
                    method: 'POST',

                }).then(function successCallback (){
                    alert("заказ оформлен")
                    scope.showCart();
                }, function errorCallback (error){
                    if (error.status === 401) {
                        scope.authTimeIsOver();
                    }
                })
            })
        }
    }
});

