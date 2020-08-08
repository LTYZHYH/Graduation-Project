<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Travel App</title>
    <link rel="stylesheet" href="/scss/bootstrap.css"/>
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
    <!-- Swipper Slider -->
    <link rel="stylesheet" href="/css/swiper.min.css">
    <!-- Magnific Popup CSS -->
    <link rel="stylesheet" href="/css/magnific-popup.css">
    <!-- Main CSS -->
    <link rel="stylesheet" href="css/style.css">
    <script src="/login/js/modernizr-2.6.2.min.js"></script>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
    <#assign JwtToken=Session.jwtToken>
    <script type="text/javascript">
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
                    $("#navbarDropdownMenuLink").text(json.content[0].userName+">>");
                    $("#usertel").text(data.content[0].userTelenumber);
                    if(data.content[0].admin.adminId==data.content[0].id){
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                        $("#userDeal").append('<a class="dropdown-item" href="/user/login">退出登录</a>');
                    }
                }
            })
        }
        function getQueryString(name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        }
        function getStrategy(id) {
            $.ajax({
                url:"${backserver}/travelstrategy/getStrategyDetila",
                // dataType:"json",
                type: 'get',
                contentType: "application/x-www-form-urlencoded",
                // data: JSON.stringify(audit),
                data: {"strategyId": id},
                beforeSend:function(request){
                    request.setRequestHeader("Jwt-Token","${JwtToken}");
                },
                success:function(data){
                    var json = eval(data);
                        var theme=json.theme;
                        var writer = json.user.userName;
                        var issueTime =json.issueTime;
                        var cost = json.overheadCost;
                        var content = json.strategyContent;
                        var strpic1 = json.strategyPicture1;
                        var strpic2 = json.strategyPicture2;
                        var strpic3 = json.strategyPicture3;
                        var themeV = "<p>" + theme + "</p>";
                        var writerV = "<p>" + writer + "</p>";
                        var timeV = "<p>" + issueTime + "</p>";
                        var costV = "<p>" + cost + "</p>";
                        var contentV = "<p>" + content + "</p>";
                        $("#theme").append(themeV);
                        $("#writer").append(writerV);
                        $("#time").append(timeV);
                        $("#cost").append(costV);
                        $("#content").append(contentV);
                        $("#strpic1").append("<img id=\"sp1\" class=\"d-block w-100\" src=\"http://localhost:12344/travelstrategy/picture/"+strpic1+"\" width=\"auto\" height=\"720px\" alt=\"First slide\">");
                        $("#strpic2").append("<img id=\"sp2\" class=\"d-block w-100\" src=\"http://localhost:12344/travelstrategy/picture/"+strpic2+"\" width=\"auto\" height=\"720px\" alt=\"Second slide\">");
                        $("#strpic3").append("<img id=\"sp3\" class=\"d-block w-100\" src=\"http://localhost:12344/travelstrategy/picture/"+strpic3+"\" width=\"auto\" height=\"720px\" alt=\"Third slide\">");
                },
                error:function () {

                }

            });
        }
        // 这样调用：
        var id = getQueryString("strategyId");
        getStrategy(id);

        function retainStrategy() {
            var sid = getQueryString("strategyId");
            var data = {
                "strategyId": sid,
            }
            $.ajax({
                url: "${backserver}/travelstrategy/retainStrategy",
                // dataType:"json",
                type: 'post',
                contentType: "application/json",
                data: JSON.stringify(data),
                beforeSend: function (request) {
                    request.setRequestHeader("Jwt-Token", "${JwtToken}");
                },
                success: function () {
                    alert("执行操作：保留攻略");
                    $(location).attr('href', 'active');
                },
                error: function () {

                }
            });
        }
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#retainStrategy").click(function () {
                retainStrategy(id)
            });
            $("#blockStrategy").click(function () {
                // var id = getQueryString("strategyId");
                var data = {
                    "strategyId": id,
                };
                $.ajax({
                    async : false,
                    url: "${backserver}/travelstrategy/blockStrategy",
                    // dataType:"json",
                    type: 'post',
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    beforeSend: function (request) {
                        request.setRequestHeader("Jwt-Token", "${JwtToken}");
                    },
                    success: function () {
                        alert("执行操作：屏蔽攻略");
                        $(location).attr('href', 'active');
                    },
                    error: function () {

                    }
                });
            });
        });
    </script>
</head>
<body style="font-size: 1.4rem;">
<div class="dark-bg sticky-top">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <nav class="navbar navbar-expand-lg navbar-light">
                    <a class="navbar-brand" href="/index">Travel App</a>
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
                                        <#--                                    <a class="dropdown-item" href="/user/info">个人主页</a>-->
                                        <#--                                    <a class="dropdown-item" href="/user/quit">退出</a>-->
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
<br/>
<div  style="min-height: 2200px;">
    <div class="row" style="min-height: 800px;">
        <div style="text-align: center">
            <h2>旅游攻略</h2>
            <div id="strategyPhoto" style="width: 1822px;">

            </div>
            <div id="myCarousel" class="carousel slide bg-inverse w-75 ml-auto mr-auto" data-ride="carousel">
                <ol class="carousel-indicators">
<#--                    <li data-target="#myCarousel" data-slide-to="0" class=""></li>-->
<#--                    <li data-target="#myCarousel" data-slide-to="1" class="active"></li>-->
<#--                    <li data-target="#myCarousel" data-slide-to="2" class=""></li>-->
                </ol>
                <div class="carousel-inner" role="listbox">
                    <div id="strpic1" class="carousel-item">
<#--                        <img class="d-block w-100" src="https://via.placeholder.com/1280x720" alt="First slide">-->
<#--                        <div class="carousel-caption">-->
<#--                            <h3>First slide</h3>-->
<#--                            <p>This is the first slide.</p>-->
<#--                        </div>-->
                    </div>
                    <div id="strpic2" class="carousel-item active">
<#--                        <img class="d-block w-100" src="https://via.placeholder.com/1280x720" alt="Second slide">-->
<#--                        <div class="carousel-caption">-->
<#--                            <h3>Second slide</h3>-->
<#--                            <p>This is the second slide.</p>-->
<#--                        </div>-->
                    </div>
                    <div id="strpic3" class="carousel-item">
<#--                        <img class="d-block w-100" src="https://via.placeholder.com/1280x720" alt="Third slide">-->
<#--                        <div class="carousel-caption">-->
<#--                            <h3>Third slide</h3>-->
<#--                            <p>This is the third slide.</p>-->
<#--                        </div>-->
                    </div>
                </div>
                <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
            <br>
        </div>
        <div style="width: 100%">
            <div class="carousel slide bg-inverse w-75 ml-auto mr-auto" id="strategyText" style="padding-left: 100px; padding-right: 100px;">
                <div style="height: 120px">
                    <div style="float: left">
                        <h4>主题</h4>
                        <div id="theme" class="card card-body" style="width: 200px; height: 70px;  margin-right: 15px;">

                        </div>
                    </div>
                    <div style="float: left">
                        <h4>作者</h4>
                        <div id="writer" class="card card-body" style="width: 200px; height: 70px; margin-right: 15px;">

                        </div>
                    </div>
                    <div style="float: left">
                        <h4>发布时间</h4>
                        <div id="time" class="card card-body" style="width: 200px; height: 70px; margin-right: 15px;">

                        </div>
                    </div>
                    <div style="float: left">
                        <h4>开销</h4>
                        <div id="cost" class="card card-body" style="width: 200px; height: 70px; margin-right: 15px;">

                        </div>
                    </div>
                </div>
                <div id="content" class="card card-body" style="height: 1000px;">

                </div>
                <div style="height: 100px; text-align: center">
                    <div class="form-group" style="margin: 0 auto;padding-top: 30px">
                        <input id="retainStrategy" type="submit" class="btn btn-outline-success" value="保留"  style="width: 85px;height: 40px;margin: 10px">
                        <input id="blockStrategy" type="submit" class="btn btn-outline-danger" value="屏蔽" style="width: 85px;height: 40px;margin: 10px">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="text-muted" style="background-color: #212529;">
    <div class="container">
        <div>
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
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- Magnific popup JS -->
<script src="/js/jquery.magnific-popup.js"></script>
<!-- Swipper Slider JS -->
<script src="/js/swiper.min.js"></script>
<#--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
</body>

</html>