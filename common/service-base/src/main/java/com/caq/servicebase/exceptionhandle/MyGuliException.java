package com.caq.servicebase.exceptionhandle;

import com.caq.commonutils.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyGuliException extends RuntimeException {

    //状态码
    private Integer code;

    //异常信息
    private String msg;
}
