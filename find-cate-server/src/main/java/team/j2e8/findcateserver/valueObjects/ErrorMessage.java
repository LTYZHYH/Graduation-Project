package team.j2e8.findcateserver.valueObjects;

public enum ErrorMessage {
    EMPTY_LOGIN_NAME("用户名不能为空"), EMPTY_PASSWORD("密码不能为空"),
    EMPTY_MOBILE("电话号码不能为空"),
    ERROR_LOGIN__NAME_OR_PASSWORD("用户名或密码错误"),
    ERROR_MOBILE_OR_PASSWORD("电话号码或密码错误"),
    ACCOUNT_NOT_EXIST("账号不存在"),
    ENTITY_ALREADY_EXIST("已存在，操作失败"),
    INVALID_TOKEN("身份认证令牌验证失败，请重新登录"),
    OVERRIDE_CONTROL("您的账号权限不正常"),
    DATA_CONFLICT("数据处理冲突"),
    NOT_FOUND("请求的资源未找到"), REQUIRED_ARGUMENT_ERROR("请求参数为空或错误"),
    INVALID_OBJECT_SELECTION("对象查询表达式select中包含有错误，请确认语法是否正确或查询的对象属性是否存在");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
