package top.hcode.blog.common.constant;

public interface ResultCodeStatus {
    // 正常
    Integer OK=200;
    // 服务器错误
    Integer SERVER_ERROR=500;
    // 认证异常
    Integer USER_NOT_LOGIN=401;
    // 参数错误
    Integer PARAM_ERROR=400;
}
