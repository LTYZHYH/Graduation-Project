
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>注册</title>
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
	<script src="js/respond.min.js"></script>
	<![endif]-->
        <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
        </script>

        <script type="text/javascript">
            $(document).ready(function(){
                $("#div1").hide();
                $("#register").click(function(){
                    $.ajax({url:"http://localhost:12344/user/register",
                        // dataType:"json",
                        type: 'post',
                        contentType: 'application/json',
                        data:JSON.stringify(GetJsonData())
                        ,success:function(){
                            $(location).attr('href', 'login');
                        },
                        error:function () {
                            $("#div1").show();
                        }

                    });
                });
            });
            function GetJsonData() {
                var json = {
                    "user_email": $("#user_email").val(),
                    "password": $("#password").val(),
                    "user_name": $("#user_name").val(),
                    "user_telenumber": $("#user_telenumber").val(),
                    "user_photo": $("#user_photo").val()
                };
                return json;
            }
        </script>
	</head>
	<body>

		<div class="container">
			<!-- <div class="row">
				<div class="col-md-12 text-center">
					<ul class="menu">
						<li class="active"><a href="index.ftl">Style 1</a></li>
						<li><a href="index2.html">Style 2</a></li>
						<li><a href="index3.html">Style 3</a></li>
					</ul>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12 text-center">
					<ul class="menu">
						<li><a class="navbar-brand" href="/index">Find Cate</a></li>
					</ul>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					

					<!-- Start Sign In Form -->
					<form  onsubmit="return false" class="fh5co-form animate-box" data-animate-effect="fadeIn">
						<h2>注册</h2>
                        <input id="user_photo" value="initphoto.jpg" style="display: none">
						<div class="form-group">
							<div class="alert alert-success" id="div1" role="alert">邮箱已被注册</div>
						</div>
						<div class="form-group">
							<label for="user_name" class="sr-only">Name</label>
							<input type="text" class="form-control" id="user_name" name="user_name" placeholder="用户名" autocomplete="off">
						</div>
						<div class="form-group">
							<label for="user_email" class="sr-only">Email</label>
							<input type="email" class="form-control" id="user_email" name="user_email" placeholder="邮箱" autocomplete="off">
						</div>
                        <div class="form-group">
                            <label for="user_telenumber" class="sr-only">Email</label>
                            <input type="email" class="form-control" id="user_telenumber" name="user_telenumber" placeholder="电话" autocomplete="off">
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
							<label for="remember"><input type="checkbox" id="remember"> Remember Me</label>
						</div>
						<div class="form-group">
							<p>Already registered? <a href="login">Sign In</a></p>
						</div>
						<div class="form-group">
							<input type="submit" id="register" value="Sign Up" class="btn btn-primary">
						</div>
					</form>
					<!-- END Sign In Form -->

				</div>
			</div>
			<div class="row" style="padding-top: 60px; clear: both;">
				<div class="col-md-12 text-center"><p><small>&copy;</small></p></div>
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

