<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8"/>
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

    <#--<script src="/open/scriptaculous/lib/prototype.js" type="text/javascript"></script>-->
    <script src="/open/scriptaculous/src/effects.js" type="text/javascript"></script>
    <script type="text/javascript" src="/open/validation.js"></script>
    <link title="style1" rel="stylesheet" href="/open/style.css" type="text/css" />
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>
    <#assign JwtToken=Session.jwtToken>
    <script type="text/javascript">
        $(document).ready(function(){
            $.ajax({url:"${backserver}/food/gets?shopId=${shopId}",
                // dataType:"json",
                type: 'get',
                contentType: "application/x-www-form-urlencoded",
                beforeSend:function(request){
                    request.setRequestHeader("Jwt-Token","${JwtToken}");
                },
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
                                '<td><a id="'+foodId+'" class="deletefood"  href="" onclick="return false;">删除</a></td>' +
                                "</tr>"
                        ;
                        $("#tb").append(value);
                    })
                },
                error:function () {

                }

            });
        });
    </script>
    <script type="text/javascript">
        $(document).ready(function(){
            $(document).click(function(e){
                console.log(window.location.href);
                if(e.target.tagName=="A" && e.target.className=="deletefood" ){
                    var id = {
                        "food_id":$(e.target).attr("id"),
                        "shop_id":${shopId}
                    };

                    $.ajax({url:"${backserver}/food/unserve",
                        // dataType:"json",
                        type: 'delete',
                        contentType: "application/json",
                        data:JSON.stringify(id),
                        beforeSend:function(request){
                            request.setRequestHeader("Jwt-Token","${JwtToken}");
                        },
                        success:function(){
                            alert("删除成功");
                            location.href=window.location.href;
                        },
                        error:function () {
                            alert("删除失败");
                            location.href=window.location.href;
                        }

                    });
                }
            });
        });
    </script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#serve").click(function () {
                UpdatePhoto();
                sleep(1000);
                serve();

            });
        })
        function serve() {
            $.ajax({
                url: "${backserver}/food/serve",
                // dataType:"json",
                type: 'post',
                contentType: 'application/json',
                data:JSON.stringify(GetJsonData()),
                beforeSend: function(request) {
                    request.setRequestHeader("Jwt-Token","${jwtToken}");
                },
                success:function(){
                    alert("提交成功");
                    location.href=window.location.href;
                },
                error:function () {
                    alert("提交失败");
                    location.href=window.location.href;
                }

            });
        }
        function GetJsonData() {
            var json = {
                "food_name": $("#field4").val(),
                "food_photo": imgname,
                "food_price": parseFloat($("#field5").val()),
                "shop_id": ${shopId},
                "type_id": parseInt($("#field6").val())
            };
            return json;
        }
        function UpdatePhoto() {
            var imgnamelist = $("#inputProfilePhoto").val().split("\\");
            imgname = imgnamelist[imgnamelist.length-1];
            $.ajaxFileUpload({
                url:'${imgserver}/upfile/', //你处理上传文件的服务端
                secureuri:false,
                fileElementId:'inputProfilePhoto',//与页面处理代码中file相对应的ID值

                dataType: 'json', //返回数据类型:看后端返回的是什么数据,在IE下后端要设置请求头的Content-Type:text/html; charset=UTF-8
                success: function (data, status) {
                },
                error: function(data, status, e){ //提交失败自动执行的处理函数
                    alert(e);
                }
            });
        }
        function sleep(delay) {
            var start = (new Date()).getTime();
            while ((new Date()).getTime() - start < delay) {
                continue;
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

                    $("#navbarDropdownMenuLink").text(data.content[0].userName+">>");
                    $("#usertel").text(data.content[0].userTelenumber);if(data.content[0].admin.adminId==data.content[0].id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');
                }
            })
        }
    </script>
</head>
<body style="font-size: 1.4rem;">
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
                                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                            <a class="dropdown-item" href="/user/info">个人主页</a>
                                            <!--<a class="dropdown-item" href="#"></a>-->
                                            <!--<a class="dropdown-item" href="#">Something else here</a>-->
                                        </div>
                                    </li>
                                <#else>
                                        <script>location.href="/user/login"</script>
                                </#if>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
    </div>
</div>
<br/>
<div class="container">
<div class="form_content">

    <form onsubmit="return false">

        <fieldset>
            <legend>上菜</legend>

            <div class="form-row">
                <div class="field-label"><label for="field4">菜名</label>:</div>
                <div class="field-widget"><input name="shopName" id="field4" class="required" title="菜名" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field5">价格</label>:</div>
                <div class="field-widget"><input name="shopTelenumber" id="field5" class="required validate-email" title="价格" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field6">类型</label>:</div>

                <select id="field6" name="field6" class="validate-selection" title="类型">
                <option value="1">中餐</option>
                <option value="2">辣菜</option>
                <option value="3">酸菜</option>
                <option value="4">西餐</option>
                </select>
            </div>
            <div class="form-row">
                <div class="field-label"><label for="field7">图片</label>:</div>
                <input id="inputProfilePhoto" name="field7" type="file" accept="image/jpeg,image/gif,image/png" tabindex="1" title="图片">
            </div>
        </fieldset>


        <input type="submit" id="serve" class="submit" value="提交" />
    </form>
</div>
<br/>
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
            <th>删除</th>
        </tr>
        </thead>
        <tbody id="tb">



        </tbody>
    </table>
</div>
</div>
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
<!--<div class="form-group">-->
<!--<div class="col-sm-2 control-label">-->
<!--<a href="/toAdd" th:href="" class="btn btn-info">上一页</a>-->
<!--</div>-->
<!--<div class="col-sm-2 control-label">-->
<!--<a href="/toAdd" th:href="" class="btn btn-info">下一页</a>-->
<!--</div>-->
<!--</div>-->
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- Magnific popup JS -->
<script src="/js/jquery.magnific-popup.js"></script>
<!-- Swipper Slider JS -->
<script src="/js/swiper.min.js"></script>

<script type="text/javascript" src="/js/ajaxfileupload.js"></script>

</body>
</html>