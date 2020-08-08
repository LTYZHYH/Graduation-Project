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
            // GetTheShopInformation();
            GetUserInfomation();
            UpdateUserInformation();
            UpdatePhoto();
        });
        function UpdateUserInformation() {
            $("#edit").click(function(){
                $.ajax({url:"${backserver}/user/update",
                    // dataType:"json",
                    type: 'post',
                    contentType: 'application/json',
                    data:JSON.stringify(GetJsonData()),
                    beforeSend: function(request) {
                        request.setRequestHeader("Jwt-Token","${jwtToken}");
                    },
                    success:function(){
                        $(location).attr('href', '/user/info');
                    },
                    error:function () {

                    }

                });
            });
        }
        function GetJsonData() {
            var json = {
                "new_user_name": $("#user_name").val(),
                "img_name": $("#user_photo").val()
            };
            return json;
        }
        function UpdatePhoto() {
            $("#inputProfilePhoto").change(function () {
                var imgnamelist = $("#inputProfilePhoto").val().split("\\");
                var imgname = imgnamelist[imgnamelist.length-1];
                $.ajaxFileUpload({
                    url:'${imgserver}/upfile/', //你处理上传文件的服务端
                    secureuri:false,
                    fileElementId:'inputProfilePhoto',//与页面处理代码中file相对应的ID值

                    dataType: 'json', //返回数据类型:看后端返回的是什么数据,在IE下后端要设置请求头的Content-Type:text/html; charset=UTF-8
                    success: function (data, status) {
                        $("#userheader").attr("src","${imgserver}/"+imgname);
                    },
                    error: function(data, status, e){ //提交失败自动执行的处理函数
                        alert(e);
                    }
                });
                sleep(1000);
                $("#userheader").attr("src","${imgserver}/"+imgname);
                $("#user_photo").attr("value",imgname)
            });
        }
        function sleep(delay) {
            var start = (new Date()).getTime();
            while ((new Date()).getTime() - start < delay) {
                continue;
            }
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
                    $("#userimg").append("<img id=\"userheader\" class=\"img-fluid\" src=\"${imgserver}/"+json.content[0].userPhoto +"\"  width=\"130\" height=\"130\" >");
                    $("#usernametitle").text(json.content[0].userName);
                    $("#navbarDropdownMenuLink").text(data.content[0].userName+">>");
                    $("#usertel").text(data.content[0].userTelenumber);
                    if(data.content[0].admin.adminId==data.content[0].id)
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');
                    // $("#usertel").text(json.content[0].userTelenumber);
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
                                    用户名
                                    <span class="icon-arrow-down"></span>
                                </a>
                                <div  id="userDeal" class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
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
        <div class="booking-checkbox_wrap">
            <h3>个人信息</h3>
            <div class="follow ">
                <div class="follow-img">
                    <div id="userimg">

                    </div>
                    <h3 id="usernametitle">文森 张</h3>

                </div>
            </div>

            <div id="profileSettings" class="col-md-4  " style="margin:auto">
                <input id="inputProfilePhoto" name="photo" type="file" accept="image/jpeg,image/gif,image/png" tabindex="1" title="User profile photo">
                <form method="post" onsubmit="return false">
                    <input id="user_photo" value="initphoto.jpg" style="display: none">

                    <div class="form-group">
                        <label for="user_name" class="sr-only">Name</label>
                        <input type="text" class="form-control" id="user_name" name="user_name" placeholder="用户名" autocomplete="off">
                    </div>
                    <#--<div class="form-group">-->
                        <#--<label for="user_email" class="sr-only">Email</label>-->
                        <#--<input type="email" class="form-control" id="user_email" name="user_email" placeholder="邮箱" autocomplete="off">-->
                    <#--</div>-->
                    <div class="form-group">
                        <label for="user_telenumber" class="sr-only">Email</label>
                        <input type="text" class="form-control" id="user_telenumber" name="user_telenumber" placeholder="电话" autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="password" class="sr-only">Password</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="密码" autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="re-password" class="sr-only">Re-type Password</label>
                        <input type="password" class="form-control" id="re-password" placeholder="重复密码" autocomplete="off">
                    </div>

                    <div class="form-group">
                        <input type="submit" id="edit" value="Save" class="btn btn-primary">
                    </div>
                </form>
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
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- Magnific popup JS -->
<script src="/js/jquery.magnific-popup.js"></script>
<!-- Swipper Slider JS -->
<script src="/js/swiper.min.js"></script>

<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
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
