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
            page = 1;
            elemenum = 1;
            last = false;
            $("#listbottom").hide();
            scrollBottom();
            GetShopList();
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
                if (getUrlParam("name")==null){
                    ajaxurl = "${backserver}/shop/search?lng=" + getUrlParam("lng") + "&lat=" + getUrlParam("lat")+"&number="+page;
                }
                else{
                    ajaxurl = "${backserver}/shop/search?lng=" + getUrlParam("lng") + "&lat=" + getUrlParam("lat")+"&number="+page + "&name=" +getUrlParam("name");
                }
                page = page+1;
                $.ajax({
                    url:ajaxurl,
                    type: 'get',
                    contentType: 'application/json',
                    success:function (data) {
                        var json = eval(data);
                        createShopElement(json);
                    }
                });
            } else {
                $("#listbottom").show();
            }

        }

        function GetShopList() {
            var url;
            if (getUrlParam("name")==null){
                url = "${backserver}/shop/search?lng=" + getUrlParam("lng") + "&lat=" + getUrlParam("lat");
            }
            else{
                url = "${backserver}/shop/search?lng=" + getUrlParam("lng") + "&lat=" + getUrlParam("lat") + "&name=" +getUrlParam("name");
            }
            $.ajax({
                url:url,
                type: 'get',
                contentType: 'application/json',
                success:function (data) {
                    var json = eval(data);
                    $("#resultNum").text("有"+json.totalElements+"个结果");
                    createShopElement(json);
                }
            })
        }
        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return decodeURI(r[2]); return null; //返回参数值
        }
        function createShopElement(data) {
            var modelList = data.numberOfElements;
            var shops = data.content;
            last = data.last;
            if(modelList>0){

                for(var i=0; i<modelList; i++){
                    // var option="<option value=\""+modelList[i].modelId+"\"";
                    // if(_LastModelId && _LastModelId==modelList[i].modelId){
                    //     option += " selected=\"selected\" "; //默认选中
                    //     _LastModelId=null;
                    // }
                    var value ="<div class=\"col-sm-6 col-lg-12 col-xl-6 featured-responsive\" >" +
                            "                       <div class=\"featured-place-wrap\">" +
                            "                                <a href=\"/shop/page?shopId="+shops[i].shopId+"\">" +
                            "                                    <img src=\"${imgserver}/"+ shops[i].shopPhoto +"\" class=\"img-fluid\" alt=\"#\">" +
                            "                                    <span class=\"featured-rating-green \">"+(elemenum)+"</span>" +
                            "                                    <div class=\"featured-title-box\">" +
                            "                                        <h6>"+shops[i].shopName+"</h6>" +
                            "                                        <p>Restaurant </p> <span>• </span>" +
                            "                                        <p>3 Reviews</p> <span> • </span>" +
                            "                                        <p><span>$$$</span>$$</p>" +
                            "                                        <ul>" +
                            "                                            <li><span class=\"icon-location-pin\"></span>" +
                            "                                                <p>"+ shops[i].shopAddress+"</p>" +
                            "                                            </li>" +
                            "                                            <li><span class=\"icon-screen-smartphone\"></span>" +
                            "                                                <p>"+ shops[i].shopTelenumber+"</p>" +
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
                    $("#shoplist").append(value);

                    //addMarker(new BMap.Point(shops[i].shopLng, shops[i].shopLat));
                    // addMarker(new BMap.Point(0, 0));
                    if (i == 0) {
                        var point = new BMap.Point(shops[i].shopLng, shops[i].shopLat);
                        map.centerAndZoom(point, 9);
                        // 编写自定义函数,创建标注
                        var marker = new BMap.Marker(point);
                        map.addOverlay(marker);
                        var content="商铺名称：" + shops[i].shopName +"<br>商铺地址："+shops[i].shopAddress+"<br>商铺电话："+shops[i].shopTelenumber+"";
                        addMarker(new BMap.Point(shops[i].shopLng, shops[i].shopLat),(elemenum++),content);
                        // var label = new BMap.Label(shops[i].shopName,{offset:new BMap.Size(20,-10)});
                        // marker.setLabel(label);
                    } else {
                        var content="商铺名称：" + shops[i].shopName +"<br>商铺地址："+shops[i].shopAddress+"<br>商铺电话："+shops[i].shopTelenumber+"";
                        addMarker(new BMap.Point(shops[i].shopLng, shops[i].shopLat),(elemenum++),content);
                    }
                }
            }
        }
        function GetUserInfomation(jwtToken) {
            $.ajax({
                url:"${backserver}/user/info",
                type: "get",
                contentType: 'application/json',
                beforeSend: function(request) {
                    request.setRequestHeader("Jwt-Token",jwtToken);
                },
                success: function(data){
                    var json = eval(data);
                    $("#userimg").append("<img class=\"img-fluid\" src=\"${imgserver}/"+data.content[0].userPhoto +"\"  width=\"130\" height=\"130\" >");

                    $("#navbarDropdownMenuLink").text(data.content[0].userName+">>");
                    if(data.content[0].admin.adminId==data.content[0].id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');

                }
            })
        }
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
                                <#if Session.jwtToken?exists>
                                    <li class="nav-item dropdown">
                                        <script>
                                            $(document).ready(function(){
                                                GetUserInfomation("${jwtToken}");
                                            });
                                        </script>
                                        <a class="nav-link" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            用户名
                                            <span class="icon-arrow-down"></span>
                                        </a>
                                        <div id="userDeal" class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                            <a class="dropdown-item" href="/user/info">个人主页</a>
                                            <a class="dropdown-item" href="/open">开店</a>
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
    <!--//END HEADER -->
    <!--============================= DETAIL =============================-->
    <section>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-7 responsive-wrap">
                    <div class="row detail-filter-wrap">
                        <div class="col-md-4 featured-responsive">
                            <div class="detail-filter-text" >
                                <p id="resultNum">34 Results For <span>Restaurant</span></p>
                            </div>
                        </div>
                        <div class="col-md-8 featured-responsive">
                            <div class="detail-filter">
                                <p>Filter by</p>
                                <form class="filter-dropdown">
                                    <select class="custom-select mb-2 mr-sm-2 mb-sm-0" id="inlineFormCustomSelect">
                                      <option selected>Best Match</option>
                                      <option value="1">One</option>
                                      <option value="2">Two</option>
                                      <option value="3">Three</option>
                                    </select>
                                </form>
                                <form class="filter-dropdown">
                                    <select class="custom-select mb-2 mr-sm-2 mb-sm-0" id="inlineFormCustomSelect1">
                                      <option selected>Restaurants</option>
                                      <option value="1">One</option>
                                      <option value="2">Two</option>
                                      <option value="3">Three</option>
                                    </select>
                                </form>
                                <div class="map-responsive-wrap">
                                    <a class="map-icon" href="#"><span class="icon-location-pin"></span></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row detail-checkbox-wrap">
                        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-3">

                            <label class="custom-control custom-checkbox">
                                <input type="checkbox" class="custom-control-input">
                                <span class="custom-control-indicator"></span>
                                <span class="custom-control-description">Bike Parking</span>
                              </label>
                        </div>
                        
                        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-3">

                            <label class="custom-control custom-checkbox">
                                <input type="checkbox" class="custom-control-input">
                                <span class="custom-control-indicator"></span>
                                <span class="custom-control-description">Pets Friendly</span>
                            </label>

                        </div>
                    </div>
                    <div class="row light-bg detail-options-wrap" id="shoplist">
                        <#--<div class="col-sm-6 col-lg-12 col-xl-6 featured-responsive" >-->
                            <#--<div class="featured-place-wrap">-->
                                <#--<a href="/shop/page?shopId=">-->
                                    <#--<img src="images/featured1.jpg" class="img-fluid" alt="#">-->
                                    <#--<span class="featured-rating-orange ">6.5</span>-->
                                    <#--<div class="featured-title-box">-->
                                        <#--<h6>Burger &amp; Lobster</h6>-->
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

                    </div>
                    <div class="row justify-content-center light-bg">
                        <div class="col-md-4">
                            <div class="featured-btn-wrap">
                                <div  class="btn btn-danger" id="listbottom">到底了</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-5 responsive-wrap map-wrap">
                    <div class="map-fix">
                        <!-- data-toggle="affix" -->
                        <!-- Google map will appear here! Edit the Latitude, Longitude and Zoom Level below using data-attr-*  -->
                        <div id="map" data-lat="40.674" data-lon="-73.945" data-zoom="14"></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--//END DETAIL -->
    <!--============================= FOOTER =============================-->
    <footer class="main-block dark-bg">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="copyright">

                        <p>网络162&nbsp&nbsp&nbsp&nbsp<a href="#" target="_blank" title="关于我们">关于我们</a></p>
                        
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
        // $(".map-icon").click(function() {
        //     $(".map-fix").toggle();
        // });
    </script>
    <script>
        // Want to customize colors? go to snazzymaps.com

        function myMap() {
            // var maplat = $('#map').data('lat');
            // var maplon = $('#map').data('lon');
            // var mapzoom = $('#map').data('zoom');
            map = new BMap.Map("map");


            // 随机向地图添加25个标注
            // var bounds = map.getBounds();
            // var sw = bounds.getSouthWest();
            // var ne = bounds.getNorthEast();
            // var lngSpan = Math.abs(sw.lng - ne.lng);
            // var latSpan = Math.abs(ne.lat - sw.lat);
            // for (var i = 0; i < 25; i ++) {
            //     var point = new BMap.Point(sw.lng + lngSpan * (Math.random() * 0.7), ne.lat - latSpan * (Math.random() * 0.7));
            //     addMarker(point);
            // }
            // Styles a map in night mode.
            // var map = new google.maps.Map(document.getElementById('map'), {
            //     center: {
            //         lat: maplat,
            //         lng: maplon
            //     },
            //     zoom: mapzoom,
            //     scrollwheel: false
            // });
            // var marker = new google.maps.Marker({
            //     position: {
            //         lat: maplat,
            //         lng: maplon
            //     },
            //     map: map,
            //     title: 'We are here!'
            // });
        }
        function addMarker(point, shopName, content){
            var label = new BMap.Label(shopName,{offset:new BMap.Size(20,-10)});
            var marker = new BMap.Marker(point);
            map.addOverlay(marker);
            marker.setLabel(label);
            addClickHandler(content,marker);
        }
        function addClickHandler(content,marker){
            marker.addEventListener("click",function(e){
                openInfo(content,e)}
            );
        }
        function openInfo(content,e){
            var opts = {
                width : 250,     // 信息窗口宽度
                height: 80,     // 信息窗口高度
                //title : "商铺" , // 信息窗口标题
                enableMessage:true//设置允许信息窗发送短息
            };
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
            var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow,point); //开启信息窗口
        }
    </script>
    <!-- Map JS (Please change the API key below. Read documentation for more info) -->
    <#--<script src="https://maps.googleapis.com/maps/api/js?callback=myMap&key=AIzaSyDMTUkJAmi1ahsx9uCGSgmcSmqDTBF9ygg"></script>-->
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=mdOAAKh0CVM7V6buxZuOhhEPIstA4Gfp&callback=myMap"></script>
</body>

</html>
