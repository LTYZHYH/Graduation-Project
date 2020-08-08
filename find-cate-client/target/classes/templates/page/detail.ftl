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
    <link rel="stylesheet" href="/scss/bootstrap.css"/>
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
            $.ajax({url:"${backserver}/food/gets?shopId=${shopId}",
                // dataType:"json",
                type: 'get',
                contentType: "application/x-www-form-urlencoded",

                success:function(data){
                    var json = eval(data);
                    $.each(json.content,function (key,food) {
                        var foodId=food.foodId;
                        var foodPhoto=food.foodPhoto;
                        var foodName=food.foodName;
                        var foodPrice=food.foodPrice;
                        var typeName=food.type.typeName;
                        var value = "<tr><td> <img src=\"${imgserver}/"+foodPhoto+"\" width=\"150\" height=\"150\" class=\"img-fluid\" alt=\"#\"></td>"
                                +"<td>"+foodName+"</td>"+
                                "<td>"+typeName+"</td>" +
                                "<td>"+foodPrice+"</td>"+
                                "</tr>"
                        ;
                        $("#tb").append(value);
                    })
                },
                error:function () {
                }
            });
        });
        $(document).ready(function(){
            page = 1;
            last = false;
            $("#listbottom").hide();
            GetTheShopInformation();
            GetCommityList();
            hideAndShow();//隐藏或显示回复框
            scrollBottom();
            $("#sendCommity").click(function () {
                if ("${jwtToken}"=="null") {
                    alert("请登录");
                    location.href="/user/login";
                    return false;
                }
                if ($("#commityContent").val().trim().length >= 1) {
                    $.ajax({url:"http://localhost:12344/commity/comment",
                        // dataType:"json",
                        type: 'post',
                        contentType: 'application/json',
                        data:JSON.stringify(GetCommityJsonData()),
                        beforeSend: function(request) {
                            request.setRequestHeader("Jwt-Token","${jwtToken}");
                        }
                        ,success:function(data){
                            var json = eval(data);
                            $("#commityContent").val('');
                            $("#commitylist").empty();
                            GetCommityList();
                        }
                    });
                } else {
                    alert("输入不能为空！");
                }
                return false;
            });
        });
        function scrollBottom() {
            $(window).scroll(

                    function() {
                        var scrollTop = $(this).scrollTop();
                        var scrollHeight = $(document).height();
                        var windowHeight = $(this).height();
                        if (scrollTop + windowHeight == scrollHeight) {
                            // 此处是滚动条到底部时候触发的事件，在这里写要加载的数据，或者是拉动滚动条的操作
                            getCommityAjax();
                        }
                    });
        }
        function getCommityAjax() {
            if (!last) {
                var ajaxurl;
                ajaxurl = "${backserver}/shop/getone?shopId=${shopId}&number="+page;
                page = page+1;
                $.ajax({
                    url:ajaxurl,
                    type: 'get',
                    contentType: 'application/json',
                    success:function (data) {
                        var json = eval(data);
                        createCommityElement(json);
                    }
                });
            } else {
                $("#listbottom").show();
            }

        }
        function GetTheShopInformation() {
            $.ajax({
                url:"${backserver}/shop/getone?shopId=${shopId}",
                type: 'get',
                contentType: 'application/json',
                success: function (data) {
                    var json = eval(data);
                    $("#shop_name").text(json.content[0].shopName);
                    $("#shop_address").text(json.content[0].shopAddress);
                    $("#shop_telenumber").text(json.content[0].shopTelenumber);
                    $("#userimg").append("<img class=\"img-fluid\" src=\"${imgserver}/"+data.content[0].user.userPhoto +"\"  width=\"130\" height=\"130\" >");
                    $("#usernametitle").text(data.content[0].user.userName);
                    $("#usertel").text(data.content[0].user.userTelenumber);
                }
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
                    <#--$("#userimg").append("<img class=\"img-fluid\" src=\"${imgserver}/"+data.content[0].userPhoto +"\"  width=\"130\" height=\"130\" >");-->
                    $("#navbarDropdownMenuLink").text(data.content[0].userName+">>");
                    if(data.content[0].admin.adminId==data.content[0].id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');
                }
            })
        }
        //回复评论
        function replyCommity(commityId) {
            if ("${jwtToken}"=="null") {
                alert("请登录");
                location.href="/user/login";
                return false;
            }
            if ($("#content"+commityId).val().trim().length>=1){
                $.ajax({url:"http://localhost:12344/reply/replys",
                    // dataType:"json",
                    type: 'post',
                    contentType: 'application/json',
                    data:JSON.stringify(GetReplyJsonData(commityId)),
                    beforeSend: function(request) {
                        request.setRequestHeader("Jwt-Token","${jwtToken}");
                    }
                    ,success:function(data){
                        var json = eval(data);
                        $(".replyArea").toggle();
                        $("#commitylist").empty();
                        GetCommityList();
                    }
                });
            } else {
                alert("输入不能为空！");
            }
            return false;
        }
        //获取reply的json数据
        function GetReplyJsonData(commityId) {
            var json = {
                "commity_id": commityId,
                "reply_content": $("#content"+commityId).val()
            };
            return json;
        }
        //获取commity的json数据
        function GetCommityJsonData() {
            var json = {
                "shop_id":${shopId},
                "comment":$("#commityContent").val()
            }
            return json;
        }
        //隐藏或显示回复框
        function hideAndShow() {
            $(document).click(function(e) {
                if (e.target.tagName == "A" && e.target.className=='replyShow') {
                    var id = e.target.id
                    $("#replyArea"+id).toggle();
                }
            });
        }
        //获取所有评论
        function GetCommityList() {
            $.ajax({
                url:"${backserver}/commity/gets?shopId=${shopId}",
                type:'get',
                contentType:'application/json',
                success:function (data) {
                    var json = eval(data);
                    createCommityElement(json);
                    $("#commity_total").text(json.totalElements+"个评论");
                }
            })
        }
        //循环显示评论
        function createCommityElement(data) {
            var modelList = data.numberOfElements;
            var commity = data.content;
            last = data.last;
            if(modelList>0){

                for(var i=0; i<modelList; i++){
                    // var option="<option value=\""+modelList[i].modelId+"\"";
                    // if(_LastModelId && _LastModelId==modelList[i].modelId){
                    //     option += " selected=\"selected\" "; //默认选中
                    //     _LastModelId=null;
                    // }
                    var value ="<div class=\"customer-review_wrap\">" +
                            "<div class=\"customer-img\">" +
                            "<img src=\"${imgserver}/"+commity[i].user.userPhoto+"\" width=\"78\" height=\"78\" class=\"img-fluid\" alt=\"#\">" +
                            "<p>"+ commity[i].user.userName +"</p>" +
                            "</div>" +
                            "<div class=\"customer-content-wrap\">" +
                            "<div class=\"customer-content\">" +
                            "<div class=\"customer-review\">" +
                            "" +
                            "<span></span>" +
                            "<span></span>" +
                            "<span></span>" +
                            "<span></span>" +
                            "<span class=\"round-icon-blank\"></span>" +
                            "<p>"+ commity[i].commityTime + "</p>" +
                            "</div>" +
                            "" +
                            "</div>" +
                            "<p id=\"commity_content\" class=\"customer-text\">" + commity[i].commityContent +"</p>" +
                            "<a style='margin: 0 0 0 460px' id=\""+commity[i].commityId+"\" class='replyShow'><span class=\"icon-like\"></span>回复</a>"+
                            "<hr>"+
                            "<p id = \"replylist"+commity[i].commityId+"\"></p>"+
                            "<ul>" +
                            "</ul>" +
                            //回复表单从下开始
                            "<div class='replyArea' id = \"replyArea"+commity[i].commityId+"\" style=\"padding: 50px 50px 10px;\">" +
                            "<form class=\"bs-example bs-example-form\" id='reply' role=\"form\" onsubmit='return replyCommity("+commity[i].commityId+")'>"+
                            "   <div class=\"row\">"+
                            "       <div class=\"input-group\">"+
                            "           <textarea form='reply' name=\"content\" id=\"content"+commity[i].commityId +"\"  style=\"width:350px;height:200px;\">"+
                            "</textarea>"+
                            "           <span class=\"input-group-btn\">"+
                            "               <input id=\"send\" style='margin-top: -53px' class=\"btn btn-default\" type=\"submit\" >"+
                            "           </span>"+
                            "       </div>"+
                            "   </div>"+
                            "</form>"+
                            "</div>"+
                            //回复表单到上结束
                            "</div>" +
                            "</div>" ;
                    $("#commitylist").append(value);
                    //遍历回复列表
                    $.each(commity[i].replyList,function (key,reply) {
                        var value ="<div style='margin:15px 0 0 20px'>"+
                                "<div class=\"customer-review\">"+
                                "<p>回复人："+ reply.user.userName +"</p>"+
                                "</div>"+
                                "<br/>"+
                                "<p style='margin-top: -15px' class=\"customer-text\">" + reply.replyContent +"</p>"+
                                "<br/>"+
                                "<p >"+ reply.replyTime +"</p>"+
                                "<br/>"+
                                "</div>"+
                                "<hr>";
                        $("#replylist"+commity[i].commityId).append(value);
                    })

                }
                $(".replyArea").hide();
            }
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
                                    <script>
                                        $(document).ready(function(){
                                            GetUserInfomation();
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
<!--============================= BOOKING =============================-->
<div>
    <!-- Swiper -->
    <div class="swiper-container">
        <div class="swiper-wrapper">

            <div class="swiper-slide">
                <a href="/images/reserve-slide2.jpg" class="grid image-link">
                    <img src="/images/reserve-slide2.jpg" class="img-fluid" alt="#">
                </a>
            </div>
            <div class="swiper-slide">
                <a href="/images/reserve-slide1.jpg" class="grid image-link">
                    <img src="/images/reserve-slide1.jpg" class="img-fluid" alt="#">
                </a>
            </div>
            <div class="swiper-slide">
                <a href="/images/reserve-slide3.jpg" class="grid image-link">
                    <img src="/images/reserve-slide3.jpg" class="img-fluid" alt="#">
                </a>
            </div>
            <div class="swiper-slide">
                <a href="/images/reserve-slide1.jpg" class="grid image-link">
                    <img src="/images/reserve-slide1.jpg" class="img-fluid" alt="#">
                </a>
            </div>
            <div class="swiper-slide">
                <a href="/images/reserve-slide2.jpg" class="grid image-link">
                    <img src="/images/reserve-slide2.jpg" class="img-fluid" alt="#">
                </a>
            </div>
            <div class="swiper-slide">
                <a href="/images/reserve-slide3.jpg" class="grid image-link">
                    <img src="/images/reserve-slide3.jpg" class="img-fluid" alt="#">
                </a>
            </div>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination swiper-pagination-white"></div>
        <!-- Add Arrows -->
        <div class="swiper-button-next swiper-button-white"></div>
        <div class="swiper-button-prev swiper-button-white"></div>
    </div>
</div>
<!--//END BOOKING -->
<!--============================= RESERVE A SEAT =============================-->
<section class="reserve-block">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h5 id="shop_name">Tasty Hand-Pulled Noodles</h5>
                <p><span>$$$</span>$$</p>
                <p class="reserve-description">Innovative cooking, paired with fine wines in a modern setting.</p>
            </div>
            <div class="col-md-6">
                <div class="reserve-seat-block">
                    <div class="reserve-rating">
                        <span>9.5</span>
                    </div>
                    <div class="review-btn">
                        <a href="#commity" class="btn btn-outline-danger">WRITE A REVIEW</a>
                        <span id="commity_total">34 reviews</span>
                    </div>
                    <div class="reserve-btn">
                        <div class="featured-btn-wrap">
                            <a href="#" class="btn btn-danger">RESERVE A SEAT</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!--//END RESERVE A SEAT -->
<!--============================= 菜单区 =============================-->
<section class="light-bg booking-details_wrap">
    <div class="container">
        <div class="row">
            <div class="col-md-8 responsive-wrap">
                <div class="booking-checkbox_wrap">
                    <div class="booking-checkbox">
                        <h2>菜谱</h2>
                        <br/><br/>
                        <div class="with:80%">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>菜图</th>
                                    <th>菜名</th>
                                    <th>类型</th>
                                    <th>价格</th>
                                </tr>
                                </thead>
                                <tbody id="tb">



                                </tbody>
                            </table>
                        </div>
                        <hr>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Bike Parking</span>
                            </label> </div>
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Wireless Internet  </span>
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Smoking Allowed  </span>
                            </label> </div>
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Street Parking</span>
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Special</span>
                            </label> </div>
                        <div class="col-md-4">
                            <label class="custom-checkbox">
                                <span class="ti-check-box"></span>
                                <span class="custom-control-description">Accepts Credit cards</span>
                            </label>
                        </div>
                    </div>
                </div>
                <!--============================= 评论区 =============================-->
                <div class="booking-checkbox_wrap mt-4">
                    <a name="review"></a>
                    <h5 id="commity">评论区</h5>
                    <hr>
                    <div style="padding: 50px 50px 10px;">
                        <form class="bs-example bs-example-form"  id='commity' role="form" onsubmit='return false'>
                            <div class="row">
                                <div class="input-group">
                                    <textarea form='commity' name="commityContent" id="commityContent"  style="width:550px;height:150px;"></textarea>
                                    <span class="input-group-btn">
                                        <input id="sendCommity" style='margin-top: -122px' class="btn btn-default" type="submit" >
                                    </span>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div id="commitylist">
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-md-4">
                            <div class="featured-btn-wrap">
                                <a href="#" class="btn btn-danger" id="listbottom">到底了</a>
                            </div>
                        </div>
                    </div>
                    <hr>
                </div>
            </div>
            <div class="col-md-4 responsive-wrap">
                <div class="contact-info">
                    <!--<img src="/images/map.jpg" class="img-fluid" alt="#">-->
                    <div class="address">
                        <span class="icon-location-pin"></span>
                        <p id="shop_address"> Doyers St<br> New York, NY 10013<br> b/t Division St & St James Pl <br> Chinatown, Civic Center</p>
                    </div>
                    <div class="address">
                        <span class="icon-screen-smartphone"></span>
                        <p id="shop_telenumber"> +44 20 7336 8898</p>
                    </div>
                    <div class="address">
                        <span class="icon-link"></span>
                        <p>https://burgerandlobster.com</p>
                    </div>
                    <div class="address">
                        <span class="icon-clock"></span>
                        <p>Mon - Sun 09:30 am - 05:30 pm <br>
                            <span class="open-now">OPEN NOW</span></p>
                    </div>
                    <!--<a href="#" class="btn btn-outline-danger btn-contact">SEND A MESSAGE</a>-->
                </div>
                <div class="follow">
                    <div class="follow-img">
                        <div id="userimg">

                        </div>
                        <#--<img src="/images/follow-img.jpg" class="img-fluid" alt="#">-->
                        <h6 id="usernametitle">Christine Evans</h6>
                        <span id="usertel">New York</span>
                    </div>
                    <!--<ul class="social-counts">-->
                    <!--<li>-->
                    <!--<h6>26</h6>-->
                    <!--<span>Listings</span>-->
                    <!--</li>-->
                    <!--<li>-->
                    <!--<h6>326</h6>-->
                    <!--<span>Followers</span>-->
                    <!--</li>-->
                    <!--<li>-->
                    <!--<h6>12</h6>-->
                    <!--<span>Followers</span>-->
                    <!--</li>-->
                    <!--</ul>-->
                    <!--<a href="#">FOLLOW</a>-->
                </div>
            </div>
        </div>
    </div>
</section>
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