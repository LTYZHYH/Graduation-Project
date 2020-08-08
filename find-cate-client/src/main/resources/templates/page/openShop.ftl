<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <#--<script src="/open/scriptaculous/lib/prototype.js" type="text/javascript"></script>-->
    <script src="/open/scriptaculous/src/effects.js" type="text/javascript"></script>
    <script type="text/javascript" src="/open/validation.js"></script>
    <#--<script type="text/javascript">-->
        <#---->
            <#---->
            <#--function setActiveStyleSheet(title) {-->
                <#--var i, a, main;-->
                <#--for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {-->
                    <#--if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {-->
                        <#--a.disabled = true;-->
                        <#--if(a.getAttribute("title") == title) a.disabled = false;-->
                    <#--}-->
                <#--}-->
            <#--}-->

    <#--</script>-->
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
    <link title="style1" rel="stylesheet" href="/open/style.css" type="text/css" />
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>

    <#assign JwtToken=Session.jwtToken>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#open").click(function(){
                UpdatePhoto();
                sleep(1000);
                $.ajax({url:"${backserver}/shop/open",
                    // dataType:"json",
                    type: 'post',
                    contentType: 'application/json',
                    data:JSON.stringify(GetJsonData())
                    ,
                    beforeSend:function(request){
                        request.setRequestHeader("Jwt-Token","${JwtToken}");
                    },
                    success:function(){
                        $(location).attr('href', '/index');
                    },
                    error:function () {

                    }

                });
            });
        });
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
                    $("#usertel").text(data.content[0].userTelenumber);
                    if(data.content[0].userEmail=="484499@qq.com")
                        $("#userDeal").append('<a class="dropdown-item" href="/active">审核</a>');
                    $("#userDeal").append('<a class="dropdown-item" href="/user/quit">退出登录</a>');
                }
            })
        }
        function GetJsonData() {
            var json = {
                "shop_name": $("#field4").val(),
                "shop_telenumber": $("#field5").val(),
                "shop_addr": $("#field6").val(),
                "password": $("#field7").val(),
                "shop_lng": $("#field10").val(),
                "shop_lat": $("#field11").val(),
                "shop_photo": imgname
            };
            return json;
        }
        function sleep(delay) {
            var start = (new Date()).getTime();
            while ((new Date()).getTime() - start < delay) {
                continue;
            }
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
    </script>
</head>
<body >
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
                                        <a class="dropdown-item" href="/index">返回</a>
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
<#--<div class="style_changer">-->
    <#--<div class="style_changer_text">Themes:</div>-->
    <#--<input type="submit" value="1" onclick="setActiveStyleSheet('style1');" />-->
    <#--<input type="submit" value="2" onclick="setActiveStyleSheet('style2');" />-->
    <#--<input type="submit" value="3" onclick="setActiveStyleSheet('style3');" />-->
<#--</div>-->

<div class="form_content" style="min-height: 620px">
    <form id="test" onsubmit="return false">

        <fieldset>
            <legend>店铺信息</legend>

            <div class="form-row">
                <div class="field-label"><label for="field4">店铺名</label>:</div>
                <div class="field-widget"><input name="shopName" id="field4" class="required" title="Enter your shopName" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field5">店铺电话</label>:</div>
                <div class="field-widget"><input name="shopTelenumber" id="field5" class="required validate-email" title="Enter your telephoneNumber" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field6">店铺地址</label>:</div>
                <#--<div class="field-widget">-->
                    <#--<select id="field6" name="field6" class="validate-selection" title="Choose your department">-->
                        <#--<option>Select one...</option>-->
                        <#--<option>Accounts</option>-->
                        <#--<option>Human Resources</option>-->
                        <#--<option>Information Technology</option>-->
                        <#--<option>Animal Management</option>-->
                        <#--<option>Ultimate Frisby</option>-->
                    <#--</select>-->
                <#--</div>-->
                <div class="field-widget"><input type="text" name="shopAddress" id="field6" class="required validate-password" title="Enter a shopAddress" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field7">用户密码</label>:</div>
                <div class="field-widget"><input type="password" name="pwd1" id="field7" class="required validate-password" title="Enter a password greater than 6 characters" /></div>
            </div>

            <div class="form-row">
                <div class="field-label"><label for="field9">确认密码</label>:</div>
                <div class="field-widget"><input type="password" name="pwd2" id="field8" class="required validate-password-confirm" title="Enter the same password for confirmation" /></div>
            </div>
            <div class="form-row">

                <div class="field-label"><label for="field9">店铺图片</label>:</div>

                <input id="inputProfilePhoto" name="field9" type="file" accept="image/jpeg,image/gif,image/png" tabindex="1" title="图片">

            </div>
            <div class="form-row">
                <div class="field-label"><label for="field10">店铺经度</label>:</div>
                <div class="field-label"><input name="lng" id="field10" class="optional" title="输入经度值" /></div>
            </div>
            <div class="form-row">
                <div class="field-label"><label for="field11">店铺纬度</label>:</div>
                <div class="field-label"><input name="lat" id="field11" class="optional" title="输入纬度值" /></div>
            </div>
        </fieldset>

        <#--<fieldset>-->
            <#--<legend class="optional">可选信息</legend>-->
             <#---->



            <#--&lt;#&ndash;<div class="form-row-select">&ndash;&gt;-->

                <#--&lt;#&ndash;<fieldset>&ndash;&gt;-->
                    <#--&lt;#&ndash;<legend class="optional">Ocupation</legend>&ndash;&gt;-->
                    <#--&lt;#&ndash;<label class="left">&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="radio" class="radio_input" name="field11" id="field11-male" value="1" />Artist <br />&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="radio" class="radio_input" name="field11" id="field11-female" value="2"/>Businessperson<br />&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="radio" class="radio_input" name="field11" id="field11-female" value="2"/>Factory worker<br />&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="radio" class="radio_input" name="field11" id="field11-female" value="2"/>Engineer<br />&ndash;&gt;-->
                        <#--&lt;#&ndash;<input type="radio" class="radio_input" name="field11" id="field11-female" value="2"/>Journalist<br />&ndash;&gt;-->

                    <#--&lt;#&ndash;</label>&ndash;&gt;-->



                <#--&lt;#&ndash;</fieldset>&ndash;&gt;-->

            <#--&lt;#&ndash;</div>&ndash;&gt;-->


        <#--</fieldset>-->
        <input type="submit" id="open" class="submit" value="Submit" /> <input class="reset" type="button" value="Reset" onclick="valid.reset(); return false" />
    </form>
</div>
<script type="text/javascript">
    function formCallback(result, form) {
        window.status = "valiation callback for form '" + form.id + "': result = " + result;
    }

    var valid = new Validation('test', {immediate : true, onFormValidate : formCallback});
    Validation.addAllThese([
        ['validate-password-confirm', 'please try again.', {
            equalToField : 'field8'
        }]
    ]);
</script>
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
</body>
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
</html>