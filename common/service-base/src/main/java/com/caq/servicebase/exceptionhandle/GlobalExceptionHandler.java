package com.caq.servicebase.exceptionhandle;

import com.caq.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回数据()
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常");
    }

    //    特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//返回数据(它不在controller中所以要加上ResponseBody注解)
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理....");
    }

    @ExceptionHandler(MyGuliException.class)
    @ResponseBody
    public R error(MyGuliException m){
        log.error(m.getMessage());
        return R.error().code(m.getCode()).message(m.getMsg());
    }
}
