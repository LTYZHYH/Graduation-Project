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
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>
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
        function getDisactiveStrategy(){
            $.ajax({url:"${backserver}/travelstrategy/getDisactiveStrategy",
                // dataType:"json",
                type: 'get',
                contentType: "application/x-www-form-urlencoded",
                beforeSend:function(request){
                    request.setRequestHeader("Jwt-Token","${JwtToken}");
                },
                success:function(data){
                    var json = eval(data);
                    var thvalue = "<tr>"+
                        "<th>ID</th>"+
                        "<th>主题</th>"+
                        "<th>发布时间</th>"+
                        "<th>地区</th>"+
                        "<th>审核通过</th>"+
                        "<th>审核不通过</th>"+
                        "</tr>"
                    $("#th").append(thvalue);
                    $.each(json,function (key,travelStrategy) {
                        var strategyId=travelStrategy.strategyId;
                        var theme=travelStrategy.theme;
                        var issueTime =travelStrategy.issueTime;
                        var area=travelStrategy.area;
                        var tbvalue = "<tr>" +
                            "<td>"+strategyId+"</td>"+
                            "<td>"+theme+"</td>"+
                            "<td>"+issueTime+"</td>"+
                            "<td>"+area+"</td>" +
                            '<td><a class="yes" id="'+strategyId+'"  href="#">通过</a></td>' +
                            '<td><a class="no" id="'+strategyId+'"  href="#">不通过</a></td>' +
                            "</tr>"
                        ;
                        $("#tb").append(tbvalue);
                    })
                },
                error:function () {

                }

            });
        }
        $(document).ready(function(){
                $("#page").hide();
                getDisactiveStrategy()
            });
    </script>
    <script type="text/javascript">
        function getAllStrategy(pageNumber){
            $.ajax({
                url: "${backserver}/travelstrategy/getAllStrategy",
                type: 'get',
                data: {"number": pageNumber},
                contentType: "application/x-www-form-urlencoded",
                beforeSend: function (request) {
                    request.setRequestHeader("Jwt-Token", "${JwtToken}");
                },
                success: function (data) {
                    var json = eval(data);
                    var modelList = json.numberOfElements;
                    var travelStrategy = json.content;
                    if (json.content.length != 0){
                        $("#th").find("tr").remove();
                        $("#tb").find("tr").remove();
                        var thvalue = "<tr>" +
                            "<th>ID</th>" +
                            "<th>主题</th>" +
                            "<th>地区</th>" +
                            "<th>发布者</th>" +
                            "<th>发布时间</th>" +
                            "</tr>"
                        $("#th").append(thvalue);
                        for (var i = 0; i < modelList; i++) {
                            var strategyId = travelStrategy[i].strategyId;
                            var theme = travelStrategy[i].theme;
                            var area = travelStrategy[i].area;
                            var useName = travelStrategy[i].user.userName;
                            var issueTime = travelStrategy[i].issueTime;
                            var tbvalue = "<tr>" +
                                "<td>" + strategyId + "</td>" +
                                "<td>" + theme + "</td>" +
                                "<td>" + area + "</td>"+
                                "<td>" + useName + "</td>" +
                                "<td>" + issueTime + "</td>" +
                                "</tr>"
                            ;
                            $("#tb").append(tbvalue);
                        }
                       key = 1;
                    } else {
                       key = 2;
                    }
                },
                error: function () {
                }
            });
            return key;
        }
        $(document).ready(function() {
            $(document).click(function (e) {
                if (e.target.tagName == "A" && e.target.className == "yes") {
                    var audit = {
                        "strategyId": $(e.target).attr("id"),
                        "auditResult": "pass"
                    };
                    $.ajax({
                        url: "${backserver}/travelstrategy/auditStrategy",
                        // dataType:"json",
                        type: 'post',
                        contentType: "application/json",
                        data: JSON.stringify(audit),
                        beforeSend: function (request) {
                            request.setRequestHeader("Jwt-Token", "${JwtToken}");
                        },
                        success: function () {
                            alert("执行操作：通过");
                            $(location).attr('href', 'active');
                        },
                        error: function () {

                        }

                    });
                }
                if (e.target.tagName == "A" && e.target.className == "no") {
                    var audit = {
                        "strategyId": $(e.target).attr("id"),
                        "auditResult": "unpass"
                    };
                    $.ajax({
                        url: "${backserver}/travelstrategy/auditStrategy",
                        // dataType:"json",
                        type: 'post',
                        contentType: "application/json",
                        data: JSON.stringify(audit),
                        beforeSend: function (request) {
                            request.setRequestHeader("Jwt-Token", "${JwtToken}");
                        },
                        success: function () {
                            alert("执行操作：不通过");
                            $(location).attr('href', 'active');
                        },
                        error: function () {

                        }

                    });
                }
                if (e.target.tagName == "A" && e.target.className == "deleteCommit") {
                    var audit = {
                        "commityId": $(e.target).attr("id"),
                    };
                    $.ajax({
                        url: "${backserver}/commity/deleteCommity",
                        // dataType:"json",
                        type: 'post',
                        contentType: "application/json",
                        data: JSON.stringify(audit),
                        beforeSend: function (request) {
                            request.setRequestHeader("Jwt-Token", "${JwtToken}");
                        },
                        success: function () {
                            alert("已删除");
                            document.getElementById("commit").click();
                            // $(location).attr('href', 'active');
                        },
                        error: function () {

                        }

                    });
                }
                if (e.target.tagName == "A" && e.target.className == "deleteReply") {
                    var audit = {
                        "replyId": $(e.target).attr("id"),
                    };
                    $.ajax({
                        url: "${backserver}/reply/deleteReply",
                        // dataType:"json",
                        type: 'post',
                        contentType: "application/json",
                        data: JSON.stringify(audit),
                        beforeSend: function (request) {
                            request.setRequestHeader("Jwt-Token", "${JwtToken}");
                        },
                        success: function () {
                            alert("已删除");
                            document.getElementById("reply").click();
                            // $(location).attr('href', 'active');
                        },
                        error: function () {

                        }

                    });
                }
            });
            $("#audit").click(function(){
                $("#th").find("tr").remove();
                $("#tb").find("tr").remove();
                $("#page").hide();
                getDisactiveStrategy()
            })
            $("#strategy").click(function () {
                // $(this).removeClass("nav-link");
                // $(this).addClass("nav-link active");
                $("#th").find("tr").remove();
                $("#tb").find("tr").remove();
                $("#page").hide();
                $.ajax({
                    url: "${backserver}/report/getReportStrategy",
                    type: 'get',
                    contentType: "application/json",
                    beforeSend: function (request) {
                        request.setRequestHeader("Jwt-Token", "${JwtToken}");
                    },
                    success: function (data) {
                        var json = eval(data);
                        var modelList = json.numberOfElements;
                        var travelStrategy = json.content;
                        var thvalue = "<tr>" +
                            "<th>ID</th>" +
                            "<th>主题</th>" +
                            "<th>发布者</th>" +
                            "<th>被举报原因</th>" +
                            "</tr>"
                        $("#th").append(thvalue);
                        for (var i = 0; i < modelList; i++) {
                            var strategyId = travelStrategy[i].strategyId;
                            var theme = travelStrategy[i].theme;
                            var useName = travelStrategy[i].user.userName;
                            var reportReason = travelStrategy[i].reportReason;
                            var tbvalue = "<tr>" +
                                "<td>" + strategyId + "</td>" +
                                "<td>" + "<a href=\'/strategy?strategyId="+ strategyId +"\'>" + theme + "</a>"+ "</td>" +
                                "<td>" + useName + "</td>" +
                                "<td>" + reportReason + "</td>" +
                                "</tr>"
                            ;
                            $("#tb").append(tbvalue);
                        }
                    },
                    error: function () {

                    }
                });
            });
            $("#commit").click(function () {
                $("#th").find("tr").remove();
                $("#tb").find("tr").remove();
                $("#page").hide();
                $.ajax({
                    url: "${backserver}/report/getReportCommit",
                    type: 'get',
                    contentType: "application/json",
                    beforeSend: function (request) {
                        request.setRequestHeader("Jwt-Token", "${JwtToken}");
                    },
                    success: function (data) {
                        var json = eval(data);
                        var modelList = json.numberOfElements;
                        var commit = json.content;
                        var thvalue = "<tr>" +
                            "<th>ID</th>" +
                            "<th>发布者</th>" +
                            "<th>内容</th>" +
                            "<th>操作</th>" +
                            "</tr>"
                        $("#th").append(thvalue);
                        for (var i = 0; i < modelList; i++) {
                            var commityId = commit[i].commityId;
                            var useName = commit[i].user.userName;
                            var commityContent = commit[i].commityContent;
                            var tbvalue = "<tr>" +
                                "<td>" + commityId + "</td>" +
                                "<td>" + useName + "</td>" +
                                "<td>" + commityContent + "</td>" +
                                '<td><a class="deleteCommit" id="'+commityId+'"  href="#">删除</a></td>'+
                                "</tr>"
                            ;
                            $("#tb").append(tbvalue);
                        }
                    },
                    error: function () {

                    }
                });
            });
            $("#reply").click(function () {
                $("#th").find("tr").remove();
                $("#tb").find("tr").remove();
                $("#page").hide();
                $.ajax({
                    url: "${backserver}/report/getReportReply",
                    type: 'get',
                    contentType: "application/json",
                    beforeSend: function (request) {
                        request.setRequestHeader("Jwt-Token", "${JwtToken}");
                    },
                    success: function (data) {
                        var json = eval(data);
                        var modelList = json.numberOfElements;
                        var reply = json.content;
                        var thvalue = "<tr>" +
                            "<th>ID</th>" +
                            "<th>发布者</th>" +
                            "<th>内容</th>" +
                            "<th>操作</th>" +
                            "</tr>"
                        $("#th").append(thvalue);
                        for (var i = 0; i < modelList; i++) {
                            var replyId = reply[i].replyId;
                            var useName = reply[i].user.userName;
                            var replyContent = reply[i].replyContent;
                            var tbvalue = "<tr>" +
                                "<td>" + replyId + "</td>" +
                                "<td>" + useName + "</td>" +
                                "<td>" + replyContent + "</td>" +
                                '<td><a class="deleteReply" id="'+replyId+'"  href="#">删除</a></td>'+
                                "</tr>"
                            ;
                            $("#tb").append(tbvalue);
                        }
                    },
                    error: function () {

                    }
                });
            });
            var pageNum = 0;
            $("#allStrategy").click(function () {
                $("#page").show();
                // $("#upPage").hide();
                $("#th").find("tr").remove();
                $("#tb").find("tr").remove();
                $("#nowPage").find("p").remove();
                // var showPage = pageNum + 1;
                // var nowPage = "<p>"+ "第 "+ showPage +" 页"+ "</p>";
                $("#nowPage").append(nowPage);
                getAllStrategy(pageNum)
            });
            $("#upPage").click(function () {
                if (pageNum != 0){
                    pageNum = pageNum - 1;
                    var showPage = pageNum + 1;
                    $("#nowPage").find("p").remove();
                    // var nowPage = "<p>"+ "第 "+ showPage +" 页"+ "</p>";
                    // $("#nowPage").append(nowPage);
                    $("#upPage").show();
                    $("#downPage").show();
                    getAllStrategy(pageNum)
                } else {
                    var showPage = pageNum + 1;
                    // $("#upPage").hide();
                    $("#nowPage").find("p").remove();
                    // var nowPage = "<p>"+ "第 "+ showPage +" 页"+ "</p>";
                    // $("#nowPage").append(nowPage);
                    $("#downPage").show();
                    getAllStrategy(pageNum)
                }
            });
            $("#downPage").click(function () {
                $("#nowPage").find("p").remove();
                pageNum = pageNum + 1;
                var showPage = pageNum + 1;
                var key = getAllStrategy(pageNum);
                if (key == 1){
                    $("#upPage").show();
                    $("#downPage").show();
                } else {
                    $("#upPage").show();
                    // $("#downPage").hide();
                    alert("这是最后一页了！");
                }
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
<div class="container-fluid" style="min-height: 800px">
    <div class="row" style="min-height: 800px">
        <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
            <ul class="nav nav-pills flex-column">
                <li class="nav-item">
                    <a class="list-group-item list-group-item-action" id="audit" href="#">攻略审核
                        <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="list-group-item list-group-item-action" id="strategy" href="#">被举报的攻略</a>
                </li>
                <li class="nav-item">
                    <a class="list-group-item list-group-item-action" id="commit" href="#">被举报的评论</a>
                </li>
                <li class="nav-item">
                    <a class="list-group-item list-group-item-action" id="reply" href="#">被举报的回复</a>
                </li>
                <li class="nav-item">
                    <a class="list-group-item list-group-item-action" id="allStrategy" href="#">全部攻略</a>
                </li>
            </ul>
        </nav>
        <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
            <h2>详情列表</h2>
            <div class="table-responsive">
                <table class="table table-stripedr">
                    <thead id="th">

                    </thead>
                    <tbody id="tb">

                    </tbody>
                </table>
                <div class="form-group" id="page" style="text-align:center">
                    <div class="col-sm-2 control-label" style="text-align:center">
                        <a href="#" id="upPage" class="btn btn-info">上一页</a>
                    </div>
                    <div id="nowPage" class="col-sm-2 control-label" style="text-align:center">

                    </div>
                    <div class="col-sm-2 control-label" style="text-align:center">
                        <a href="#" id="downPage" class="btn btn-info">下一页</a>
                    </div>
                </div>
            </div>
        </main>
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
</body>

</html>