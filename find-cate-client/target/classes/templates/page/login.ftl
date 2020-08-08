
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>登录</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Free HTML5 Template by FreeHTML5.co" />
	<meta name="keywords" content="free html5, free template, free bootstrap, html5, css3, mobile first, responsive" />
	

  

  	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content=""/>
	<meta property="og:image" content=""/>
	<meta property="og:url" content=""/>
	<meta property="og:site_name" content=""/>
	<meta property="og:description" content=""/>
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />

	<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
	<link rel="shortcut icon" href="favicon.ico">

	<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700,300' rel='stylesheet' type='text/css'>
	
	<link rel="stylesheet" href="/login/css/bootstrap.min.css">
	<link rel="stylesheet" href="/login/css/animate.css">
	<link rel="stylesheet" href="/login/css/style.css">

	<!-- Modernizr JS -->
	<script src="/login/js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="/js/respond.min.js"></script>
	<![endif]-->
		<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
		</script>

		<script type="text/javascript">
            $(document).ready(function(){
                $("#div1").hide();
                $("#div2").hide();
                $("#login").click(function(){
						$.ajax({
							async : false,
							url:"http://localhost:12344/user/login",
							// dataType:"json",
						type: 'post',
                        contentType: 'application/json',
                        data:JSON.stringify(GetJsonData()),
							success:function(data){
							var jsons = eval(data);
                    		$.ajax({
								async : false,
								headers:{
									'JWT-token':jsons.token,
								},
								url:"${backserver}/user/adminInfo",
								type: "get",
								contentType: 'application/json',
								success: function(result){
									if(result.content[0].admin == null){
										$("#div2").show();
									} else {
										$(location).attr('href', '/user/afterlogin?jwtToken='+jsons.token);
									}
								},
								error:function (result) {
								}
							});
                        },
                        error:function () {
                            $("#div1").show();
                        }

                    });
                });
            });
            function GetJsonData() {
                var json = {
                    "user_email": $("#user_name").val(),
                    "password": $("#password").val()
                };
                return json;
            }
		</script>
	</head>
	<body>

		<div class="container">
			 <div class="row">
				<div class="col-md-12 text-center">
					<ul class="menu">
						<!--<li class="active"><a href="index.ftl">Style 1</a></li>-->
						<!--<li><a href="index2.html">Style 2</a></li>-->
						<!--<li><a href="index3.html">Style 3</a></li>-->
						<li><a class="navbar-brand" href="/index">Travel</a></li>
					</ul>
				</div>
			</div>
            <div class="copyrights">Collect from <a href="http://www.cssmoban.com/"  title="网站模板">网站模板</a></div>
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					

					<!-- Start Sign In Form -->
					<form  class="fh5co-form animate-box" data-animate-effect="fadeIn" onsubmit="return false">
						<h2>登录</h2>
						<div class="form-group">
							<div id="div1" class="alert alert-success" role="alert">用户名或者密码错误</div>
							<div id="div2" class="alert alert-success" role="alert">您不是管理员</div>
						</div>
						<div class="form-group">
							<label for="user_name" class="sr-only">用户名</label>
							<input type="text" class="form-control" id="user_name" name="user_name" placeholder="Username" autocomplete="off">
						</div>
						<div class="form-group">
							<label for="password" class="sr-only">密码</label>
							<input type="password" class="form-control" id="password" name="password" placeholder="Password" autocomplete="off">
						</div>
<#--						<div class="form-group">-->
<#--							<label for="remember"><input type="checkbox" id="remember"> Remember Me</label>-->
<#--						</div>-->
<#--						<div class="form-group">-->
<#--							<p>没有账号？ <a href="register">注册</a>-->
<#--						</div>-->
						<div class="form-group">
							<input type="submit" id="login" value="Sign In" class="btn btn-primary">
						</div>
					</form>
					<!-- END Sign In Form -->

				</div>
			</div>
			<div class="row" style="padding-top: 60px; clear: both;">
				<!--<div class="col-md-12 text-center"><p><small>&copy; All Rights Reserved. More Templates <a href="http://www.cssmoban.com/" target="_blank" title="模板之家">模板之家</a> - Collect from <a href="http://www.cssmoban.com/" title="网页模板" target="_blank">网页模板</a></small></p></div>-->
			</div>
		</div>
	
	<!-- jQuery -->
	<script src="/login/js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="/login/js/bootstrap.min.js"></script>
	<!-- Placeholder -->
	<script src="/login/js/jquery.placeholder.min.js"></script>
	<!-- Waypoints -->
	<script src="/login/js/jquery.waypoints.min.js"></script>
	<!-- Main JS -->
	<script src="/login/js/main.js"></script>


	</body>
</html>

