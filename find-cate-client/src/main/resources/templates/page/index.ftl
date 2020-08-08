<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="Colorlib">
    <meta name="description" content="#">
    <meta name="keywords" content="#">
    <!-- Page Title -->
    <title>Find Cate</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,400i,500,700,900" rel="stylesheet">
    <!-- Simple line Icon -->
    <link rel="stylesheet" href="css/simple-line-icons.css">
    <!-- Themify Icon -->
    <link rel="stylesheet" href="css/themify-icons.css">
    <!-- Hover Effects -->
    <link rel="stylesheet" href="css/set1.css">
    <!-- Main CSS -->
    <link rel="stylesheet" href="css/style.css">
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>

    <script type="text/javascript">
        $(document).ready(function(){
            Search();
            elemenum = 1;
            getRandomShop();
        });
        function GetUserInfomation(jwtToken) {
            $.ajax({
                url:"${backserver}/user/adminInfo",
                type: "get",
                contentType: 'application/json',
                beforeSend: function(request) {
                    request.setRequestHeader("Jwt-Token",jwtToken);
                },
                success: function(data){
                    var json = eval(data);
                    $("#userimg").append("<img class=\"img-fluid\" src=\"${imgserver}/"+data.content[0].userPhoto +"\"  width=\"130\" height=\"130\" >");

                    $("#navbarDropdownMenuLink").text(data.userName+">>");
                    $("#usertel").text(data.userTelenumber);
                        if(data.admin.adminId == data.id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                        $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');

                }
            })
        }

        function Search() {
            $("#search").click(function(){
                var mapurl= "http://api.map.baidu.com/geocoder/v2/?address="+$("#addr").val()+"&output=json&ak=mdOAAKh0CVM7V6buxZuOhhEPIstA4Gfp&callback=SearchCallBack";
                $.ajax({
                    url: mapurl,
                    type: "get",
                    dataType : "jsonp",
                    jsonp: "callbackparam",
                    jsonpCallback:"success_jsonpCallback",
                    success: function (data) {
                        // var json = eval(data);
                        // var lnglat = "lng=" + json.result.location.lng + "&lat=" + json.result.location.lat;
                        // console.log(lnglat);
                    }
                })
            })
        }
        // function Search() {
        //     $("#search").click(function(){
        //         var mapurl= "http://api.map.baidu.com/geocoder/v2/?address="+$("#addr").val()+"&output=json&ak=mdOAAKh0CVM7V6buxZuOhhEPIstA4Gfp&callback=SearchCallBack";
        //         $.get(mapurl);
        //     })
        // }
        //
        function SearchCallBack(data) {
            var json = eval(data);
            if ($("#shoptext").val().trim().length <1)
                var lnglat = "lng=" + json.result.location.lng + "&lat=" + json.result.location.lat;
            else
                var lnglat = "lng=" + json.result.location.lng + "&lat=" + json.result.location.lat +"&name="+$("#shoptext").val();
            $(location).attr('href', "/list?"+lnglat);
        }

        function getRandomShop() {
            $.ajax({
                url:"${backserver}/shop/random",
                type: 'get',
                contentType: 'application/json',
                success:function (data) {
                    var json = eval(data);
                    createShopElementByRandom(json);
                }
            })
        }

        function createShopElementByRandom(data) {
            var shops = data;

            $.each(shops,function (key,randomShop) {

                // var option="<option value=\""+modelList[i].modelId+"\"";
                // if(_LastModelId && _LastModelId==modelList[i].modelId){
                //     option += " selected=\"selected\" "; //默认选中
                //     _LastModelId=null;
                // }
                var value = "<div class=\"col-md-4 featured-responsive\" >" +
                        "                       <div class=\"featured-place-wrap\">" +
                        "                                <a href=\"/shop/page?shopId=" + randomShop.shopId + "\">" +
                        "                                    <img src=\"${imgserver}/" + randomShop.shopPhoto + "\" class=\"img-fluid\" alt=\"#\">" +
                        "                                    <span class=\"featured-rating-green \">" + (elemenum++) + "</span>" +
                        "                                    <div class=\"featured-title-box\">" +
                        "                                        <h6>" + randomShop.shopName + "</h6>" +
                        "                                        <p>Restaurant </p> <span>• </span>" +
                        "                                        <p>3 Reviews</p> <span> • </span>" +
                        "                                        <p><span>$$$</span>$$</p>" +
                        "                                        <ul>" +
                        "                                            <li><span class=\"icon-location-pin\"></span>" +
                        "                                                <p>" + randomShop.shopAddress + "</p>" +
                        "                                            </li>" +
                        "                                            <li><span class=\"icon-screen-smartphone\"></span>" +
                        "                                                <p>" + randomShop.shopTelenumber + "</p>" +
                        "                                            </li>" +
                        "                                            <li><span class=\"icon-link\"></span>" +
                        "                                                <p>https://burgerandlobster.com</p>" +
                        "                                            </li>" +
                        "                                        </ul>" +
                        "                                        <div class=\"bottom-icons\">\n" +
                        "                                            <div class=\"open-now\">OPEN NOW</div>\n" +
                        "                                            <span class=\"ti-heart\"></span>\n" +
                        "                                            <span class=\"ti-bookmark\"></span>\n" +
                        "                                        </div>" +
                        "                                    </div>" +
                        "                                </a>" +
                        "                            </div></div>";
                $("#randomShops").append(value);

            })
        }
    </script>
</head>

<body>
    <!--============================= HEADER =============================-->
    <div class="nav-menu">
        <div class="bg transition">
            <div class="container-fluid fixed">
                <div class="row">
                    <div class="col-md-12">
                        <nav class="navbar navbar-expand-lg navbar-light">
                            <a class="navbar-brand" href="/index">Find Cate</a>
                            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                                <span class="icon-menu"></span>
                            </button>
                            <div class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
                                <ul class="navbar-nav">
                                    <#if Session.jwtToken?exists>
                                        <script>
                                            $(document).ready(function(){
                                                GetUserInfomation("${jwtToken}");
                                            });
                                        </script>
                                        <li class="nav-item dropdown">
                                            <a class="nav-link" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                             用户名
                                             <span class="icon-arrow-down"></span>
                                            </a>
                                            <div id="userDeal" class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                                <a class="dropdown-item" href="/user/info">个人主页</a>
                                                <a class="dropdown-item" href="/open">开店</a>
                                                <a class="dropdown-item" href="/active">审核</a>
                                                <a class="dropdown-item" href="/user/quit">退出登录</a>
                                                <!--<a class="dropdown-item" href="#"></a>-->
                                                <!--<a class="dropdown-item" href="#">Something else here</a>-->
                                            </div>
                                        </li>
                                    <#else>
                                        <li>
                                            <a href="/user/login" class="btn btn-outline-light top-btn"></span> 登录</a>
                                        </li>
                                        <li><a href="/user/register" class="btn btn-outline-light top-btn"><span class="ti-plus"></span> 注册</a></li>
                                    </#if>
                                </ul>
                            </div>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- SLIDER -->
    <section class="slider d-flex align-items-center">
        <!-- <img src="images/slider.jpg" class="img-fluid" alt="#"> -->
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="col-md-12">
                    <div class="slider-title_box">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="slider-content_wrap">
                                    <h1>与世界分享你所知道的美食</h1>
                                    <h5>Let's uncover the best places to eat, drink, and shop nearest to you.</h5>
                                </div>
                            </div>
                        </div>
                        <div class="row d-flex justify-content-center">
                            <div class="col-md-10">
                                <form class="form-wrap mt-4" action="listing.ftl" method="get" onsubmit="return false">
                                    <div class="btn-group" role="group" aria-label="Basic example">
                                        <input type="text" placeholder="输入想要查找的商铺" id="shoptext" class="btn-group1">
                                        <input type="text" placeholder="成都" value="成都" id="addr" class="btn-group2">
                                        <button type="submit" id="search" class="btn-form"><span class="icon-magnifier search-icon"></span>搜索<i class="pe-7s-angle-right"></i></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--// SLIDER -->
    <!--//END HEADER -->
    <!--============================= FIND PLACES =============================-->

    <!--//END FIND PLACES -->
    <!--============================= FEATURED PLACES =============================-->
    <section class="main-block light-bg">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="styled-heading">
                        <h3>随机推荐</h3>
                    </div>
                </div>
            </div>
            <div class="row" id="randomShops">
                <#--<div class="col-md-4 featured-responsive">-->
                    <#--<div class="featured-place-wrap">-->
                        <#--<a href="detail.ftl">-->
                            <#--<img src="images/featured1.jpg" class="img-fluid" alt="#">-->
                            <#--<span class="featured-rating-orange">6.5</span>-->
                            <#--<div class="featured-title-box">-->
                                <#--<h6>Burger & Lobster</h6>-->
                                <#--<p>Restaurant </p> <span>• </span>-->
                                <#--<p>3 Reviews</p> <span> • </span>-->
                                <#--<p><span>$$$</span>$$</p>-->
                                <#--<ul>-->
                                    <#--<li><span class="icon-location-pin"></span>-->
                                        <#--<p>1301 Avenue, Brooklyn, NY 11230</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-screen-smartphone"></span>-->
                                        <#--<p>+44 20 7336 8898</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-link"></span>-->
                                        <#--<p>https://burgerandlobster.com</p>-->
                                    <#--</li>-->

                                <#--</ul>-->
                                <#--<div class="bottom-icons">-->
                                    <#--<div class="open-now">OPEN NOW</div>-->
                                    <#--<span class="ti-heart"></span>-->
                                    <#--<span class="ti-bookmark"></span>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</a>-->
                    <#--</div>-->
                <#--</div>-->
                <#--<div class="col-md-4 featured-responsive">-->
                    <#--<div class="featured-place-wrap">-->
                        <#--<a href="detail.ftl">-->
                            <#--<img src="images/featured2.jpg" class="img-fluid" alt="#">-->
                            <#--<span class="featured-rating-green">9.5</span>-->
                            <#--<div class="featured-title-box">-->
                                <#--<h6>Joe’s Shanghai</h6>-->
                                <#--<p>Restaurant </p> <span>• </span>-->
                                <#--<p>3 Reviews</p> <span> • </span>-->
                                <#--<p><span>$$$</span>$$</p>-->
                                <#--<ul>-->
                                    <#--<li><span class="icon-location-pin"></span>-->
                                        <#--<p>1301 Avenue, Brooklyn, NY 11230</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-screen-smartphone"></span>-->
                                        <#--<p>+44 20 7336 8898</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-link"></span>-->
                                        <#--<p>https://burgerandlobster.com</p>-->
                                    <#--</li>-->

                                <#--</ul>-->
                                <#--<div class="bottom-icons">-->
                                    <#--<div class="closed-now">CLOSED NOW</div>-->
                                    <#--<span class="ti-heart"></span>-->
                                    <#--<span class="ti-bookmark"></span>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</a>-->
                    <#--</div>-->
                <#--</div>-->
                <#--<div class="col-md-4 featured-responsive">-->
                    <#--<div class="featured-place-wrap">-->
                        <#--<a href="detail.ftl">-->
                            <#--<img src="images/featured3.jpg" class="img-fluid" alt="#">-->
                            <#--<span class="featured-rating">3.2</span>-->
                            <#--<div class="featured-title-box">-->
                                <#--<h6>Tasty Hand-Pulled Noodles</h6>-->
                                <#--<p>Restaurant </p> <span>• </span>-->
                                <#--<p>3 Reviews</p> <span> • </span>-->
                                <#--<p><span>$$$</span>$$</p>-->
                                <#--<ul>-->
                                    <#--<li><span class="icon-location-pin"></span>-->
                                        <#--<p>1301 Avenue, Brooklyn, NY 11230</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-screen-smartphone"></span>-->
                                        <#--<p>+44 20 7336 8898</p>-->
                                    <#--</li>-->
                                    <#--<li><span class="icon-link"></span>-->
                                        <#--<p>https://burgerandlobster.com</p>-->
                                    <#--</li>-->

                                <#--</ul>-->
                                <#--<div class="bottom-icons">-->
                                    <#--<div class="open-now">OPEN NOW</div>-->
                                    <#--<span class="ti-heart"></span>-->
                                    <#--<span class="ti-bookmark"></span>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</a>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="row justify-content-center">-->
                <#--<div class="col-md-4">-->
                    <#--<div class="featured-btn-wrap">-->
                        <#--<a href="#" class="btn btn-danger">查看全部</a>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
        </div>
        </div>
    </section>
    <div class="tlinks">Collect from <a href="http://www.cssmoban.com/" >企业网站模板</a></div>
    <!--//END FEATURED PLACES -->
    <!--============================= CATEGORIES =============================-->
    <section class="main-block">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="styled-heading">
                        <h3>类似网站</h3>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 category-responsive">
                    <a href="http://www.dianping.com/" class="category-wrap">
                        <div class="category-block">
                                <img src="images/小吃.png" class="img-fluid" alt="#">
                            <h6>大众点评</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://www.meituan.com/" class="category-wrap">
                        <div class="category-block">
                                <img src="images/中餐.png" class="img-fluid" alt="#">
                            <h6>美团</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://www.ele.me/home/" class="category-wrap">
                        <div class="category-block">
                                <img src="images/西餐.png" class="img-fluid" alt="#">
                            <h6>饿了么</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://star.ele.me/waimai?qt=find" class="category-wrap">
                        <div class="category-block">
                                <img src="images/寿司.png" class="img-fluid" alt="#">
                            <h6>饿了么星选</h6>
                        </div>
                    </a>
                </div>
            </div>

            <div class="row">
                <div class="col-md-3 category-responsive">
                    <a href="https://www.4008823823.com.cn/kfcios/Html/seasonpromotion.html?utm_source=baidu&utm_medium=cpc&utm_campaign=%E8%82%AF%E5%BE%B7%E5%9F%BA&utm_content=%E5%93%81%E7%89%8C" class="category-wrap">
                        <div class="category-block">
                            <img src="images/餐饮.png" class="img-fluid" alt="#">
                            <h6>肯德基</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://foursquare.com/" class="category-wrap">
                        <div class="category-block">
                                <img src="images/酒店餐厅.png" class="img-fluid" alt="#">
                            <h6>Foursquare</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://www.mcdonalds.com.cn/" class="category-wrap">
                        <div class="category-block">
                                <img src="images/餐厅.png" class="img-fluid" alt="#">
                            <h6>麦当劳</h6>
                        </div>
                    </a>
                </div>
                <div class="col-md-3 category-responsive">
                    <a href="https://www.4008123123.com/phhs_ios/Special.htm?utm_source=baidu&utm_medium=cpc&utm_campaign=%E5%BF%85%E8%83%9C%E5%AE%A2&utm_content=%E5%93%81%E7%89%8C%E5%AE%98%E7%BD%91" class="category-wrap">
                        <div class="category-block">
                            <img src="images/早餐.png" class="img-fluid" alt="#">
                            <h6>必胜客</h6>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </section>
    <!--//END CATEGORIES -->
    <!--============================= FOOTER =============================-->
    <footer class="main-block dark-bg">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="copyright">
                        
                        <p>网络162&nbsp&nbsp&nbsp&nbsp<a href="#" target="_blank" title="关于我们">关于我们<p></p>
                        
                        <ul>
                            <li><a href="#"><span class="ti-facebook"></span></a></li>
                            <li><a href="#"><span class="ti-twitter-alt"></span></a></li>
                            <li><a href="#"><span class="ti-instagram"></span></a></li>
                        </ul>

                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!--//END FOOTER -->




    <!-- jQuery, Bootstrap JS. -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <script>
        $(window).scroll(function() {
            // 100 = The point you would like to fade the nav in.

            if ($(window).scrollTop() > 100) {

                $('.fixed').addClass('is-sticky');

            } else {

                $('.fixed').removeClass('is-sticky');

            };
        });
    </script>
</body>

</html>
