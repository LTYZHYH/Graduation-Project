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
    <!-- Favicons -->
    <link rel="shortcut icon" href="#">
    <!-- Page Title -->
    <title>Find Cate</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,400i,500,700,900" rel="stylesheet">
    <!-- Simple line Icon -->
    <link rel="stylesheet" href="/css/simple-line-icons.css">
    <!-- Themify Icon -->
    <link rel="stylesheet" href="/css/themify-icons.css">
    <!-- Hover Effects -->
    <link rel="stylesheet" href="/css/set1.css">
    <!-- Swipper Slider -->
    <link rel="stylesheet" href="/css/swiper.min.css">
    <!-- Magnific Popup CSS -->
    <link rel="stylesheet" href="/css/magnific-popup.css">
    <!-- Main CSS -->
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>

    <script type="text/javascript">
        $(document).ready(function(){
            page = 1;
            elemenum = 1;
            last = false;
            $("#listbottom").hide();
            GetUserInfomation();
            GetShopListByUser();
            scrollBottom();
        });
        $(document).ready(function(){
            toServeFood();
        });
        function scrollBottom() {
            $(window).scroll(

                    function() {
                        var scrollTop = $(this).scrollTop();
                        var scrollHeight = $(document).height();
                        var windowHeight = $(this).height();
                        if (scrollTop + windowHeight == scrollHeight) {
                            // 此处是滚动条到底部时候触发的事件，在这里写要加载的数据，或者是拉动滚动条的操作
                            getShopAjax();
                        }
                    });
        }
        function getShopAjax() {
            if (!last) {
                var ajaxurl;
                ajaxurl = "${backserver}/shop/getsbyuser?number="+page;
                page = page+1;
                $.ajax({
                    url:ajaxurl,
                    type: 'get',
                    contentType: 'application/json',
                    beforeSend: function(request) {
                        request.setRequestHeader("Jwt-Token","${jwtToken}");
                    },
                    success:function (data) {
                        var json = eval(data);
                        createShopElement(json);
                    }
                });
            } else {
                $("#listbottom").show();
            }

        }
        function toServeFood() {
            $(".open-now").click(function (e) {
                var id = e.target.id;
                $(location).attr('href', '/food?shopId='+id);
            })
        }
        function GetUserInfomation() {
            $.ajax({
                url:"${backserver}/user/info",
                type: "get",
                contentType: 'application/json',
                beforeSend: function(request) {
                    request.setRequestHeader("Jwt-Token","${jwtToken}");
                },
                success: function(data){
                    var json = eval(data);
                    $("#userimg").append("<img class=\"img-fluid\" src=\"${imgserver}/"+data.content[0].userPhoto +"\"  width=\"130\" height=\"130\" >");
                    $("#usernametitle").text(data.content[0].userName);
                    $("#navbarDropdownMenuLink").text(data.content[0].userName+">>");
                    $("#usertel").text(data.content[0].userTelenumber);
                    if(data.content[0].admin.adminId==data.content[0].id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');
                }
            })
        }
        function GetShopListByUser() {
            $.ajax({
                url:"${backserver}/shop/getsbyuser",
                type: 'get',
                contentType: 'application/json',
                beforeSend: function(request) {
                    request.setRequestHeader("Jwt-Token","${jwtToken}");
                },
                success:function (data) {
                    var json = eval(data);
                    createShopElement(json);
                }
            })
        }
        function createShopElement(data) {
            var modelList = data.numberOfElements;
            var shops = data.content;
            last = data.last;
            if (modelList > 0) {
                for (var i = 0; i < modelList; i++) {
                    // var option="<option value=\""+modelList[i].modelId+"\"";
                    // if(_LastModelId && _LastModelId==modelList[i].modelId){
                    //     option += " selected=\"selected\" "; //默认选中
                    //     _LastModelId=null;
                    // }
                    var value = "<div class=\"col-md-4 featured-responsive\" >" +
                            "                       <div class=\"featured-place-wrap\">" +
                            "                                <a href=\"/shop/page?shopId=" + shops[i].shopId + "\">" +
                            "                                    <img src=\"${imgserver}/"+shops[i].shopPhoto+"\" class=\"img-fluid\" alt=\"#\">" +
                            "                                    <span class=\"featured-rating-orange \">6.5</span>" +
                            "                                    <div class=\"featured-title-box\">" +
                            "                                        <h6>" + shops[i].shopName + "</h6>" +
                            "                                        <p>Restaurant </p> <span>• </span>" +
                            "                                        <p><span>$$$</span>$$</p>" +
                            "                                        <ul>" +
                            "                                            <li><span class=\"icon-location-pin\"></span>" +
                            "                                                <p>" + shops[i].shopAddress + "</p>" +
                            "                                            </li>" +
                            "                                            <li><span class=\"icon-screen-smartphone\"></span>" +
                            "                                                <p>" + shops[i].shopTelenumber + "</p>" +
                            "                                            </li>" +
                            "                                            <li><span class=\"icon-link\"></span>" +
                            "                                                <p>https://burgerandlobster.com</p>" +
                            "                                            </li>" +
                            "                                        </ul>" +
                            "                                </a>" +
                            "                                        <div class=\"bottom-icons\">\n" +
                            "                                            <a href=\"/food?shopId="+shops[i].shopId+"\"><div class=\"open-now\">上菜</div></a>\n" +
                            "                                            <span class=\"ti-heart\"></span>\n" +
                            "                                            <span class=\"ti-bookmark\"></span>\n" +
                            "                                        </div>" +
                            "                                    </div>" +
                            "                            </div></div>";
                    $("#shoplist").append(value);
                }
            }
        }
        <#--function GetTheShopInformation() {-->
        <#--$.ajax({-->
        <#--url:"${backserver}/shop/getone?shopId=${shopId}",-->
        <#--type: 'get',-->
        <#--contentType: 'application/json',-->
        <#--success: function (data) {-->
        <#--var json = eval(data);-->
        <#--$("#shop_name").text(json.content[0].shopName);-->
        <#--$("#shop_address").text(json.content[0].shopAddress);-->
        <#--$("#shop_telenumber").text(json.content[0].shopTelenumber)-->
        <#--}-->
        <#--})-->
        <#--}-->
        <#--function GetCommityList() {-->
        <#--$.ajax({-->
        <#--url:"${backserver}/shop/getone?shopId=${shopId}",-->
        <#--})-->
        <#--}-->
    </script>
</head>

<body>
<!--============================= HEADER =============================-->
<div class="dark-bg sticky-top">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <nav class="navbar navbar-expand-lg navbar-light">
                    <a class="navbar-brand" href="/index">Find Cate</a>
                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="icon-menu"></span>
                    </button>
                    <div class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
                        <ul class="navbar-nav">
                        <#--<#if Session.jwtToken?exists>-->
                        <#--<li class="nav-item dropdown">-->
                        <#--<a class="nav-link" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">-->
                        <#--用户名-->
                        <#--<span class="icon-arrow-down"></span>-->
                        <#--</a>-->
                        <#--<div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">-->
                        <#--<a class="dropdown-item" href="#">个人主页</a>-->
                        <#--<!--<a class="dropdown-item" href="#"></a>&ndash;&gt;-->
                        <#--<!--<a class="dropdown-item" href="#">Something else here</a>&ndash;&gt;-->
                        <#--</div>-->
                        <#--</li>-->
                        <#--<#else>-->
                        <#--<script>location.href="/user/login";</script>-->
                        <#--</#if>-->
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <#--用户名>-->
                                </a>
                                <div id="userDeal" class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                    <a class="dropdown-item" href="/user/info">个人主页</a>
                                    <a class="dropdown-item" href="/open">开店</a>
                                    <!--<a class="dropdown-item" href="#"></a>-->
                                    <!--<a class="dropdown-item" href="#">Something else here</a>-->
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
    </div>
</div>
<!--//END HEADER -->
<!--============================= BOOKING =============================-->
<section class="main-block light-bg">
    <div class="container">
        <div class="follow ">
            <div class="follow-img">
                <div id="userimg">

                </div>
                <h3 id="usernametitle">test</h3>
                <a class="editProfileLink" href="/user/edit">编辑资料</a>


                <span  id="usertel">Phoenix, AZ</span>
            </div>
        </div>

        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="styled-heading">
                    <h3>开的店铺</h3>
                </div>
            </div>
        </div>
        <div class="row" id="shoplist">

        </div>
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="featured-btn-wrap">
                    <a href="#" class="btn btn-danger" id="listbottom">到底了</a>
                </div>
            </div>
        </div>
    </div>
</section>
<!--//END BOOKING -->
<!--============================= RESERVE A SEAT =============================-->

<!--//END RESERVE A SEAT -->
<!--============================= BOOKING DETAILS =============================-->

<!--//END BOOKING DETAILS -->
<!--============================= FOOTER =============================-->
<footer class="main-block dark-bg">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="copyright">

                    <p>网络162&nbsp&nbsp&nbsp&nbsp<a href="#" target="_blank" title="关于我们">关于我们

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
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- Magnific popup JS -->
<script src="/js/jquery.magnific-popup.js"></script>
<!-- Swipper Slider JS -->
<script src="/js/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-container', {
        slidesPerView: 3,
        slidesPerGroup: 3,
        loop: true,
        loopFillGroupWithBlank: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });
</script>
<script>
    if ($('.image-link').length) {
        $('.image-link').magnificPopup({
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    }
    if ($('.image-link2').length) {
        $('.image-link2').magnificPopup({
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    }
</script>
</body>

</html>