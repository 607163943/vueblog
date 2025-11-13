package com.project.blog.handler;


import com.project.blog.common.constant.ResultCodeStatus;
import com.project.blog.common.exception.ArticleException;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理文章模块业务异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ArticleException.class)
    public Result<Object> articleHandler(ArticleException e) {
        log.warn("文章模块业务异常:-------------->{}", e.getMessage(), e);
        return Result.error(ResultCodeStatus.PARAM_ERROR, e.getMessage());
    }

    /**
     * 处理用户模块业务异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    public Result<Object> userHandler(UserException e) {
        log.warn("用户模块业务异常:-------------->{}", e.getMessage(), e);
        return Result.error(ResultCodeStatus.PARAM_ERROR, e.getMessage());
    }

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result<Object> handle401(ShiroException e) {
        log.warn("shiro异常:------>", e);
        return Result.error(ResultCodeStatus.USER_NOT_LOGIN, e.getMessage());
    }

    /**
     * @Validated 校验错误异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> handler(MethodArgumentNotValidException e) {
        log.error("参数校验异常:-------------->", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.error(ResultCodeStatus.PARAM_ERROR, objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result<Object> handler(RuntimeException e) {
        log.error("运行时异常:-------------->", e);
        return Result.error(ResultCodeStatus.SERVER_ERROR, e.getMessage());
    }
}