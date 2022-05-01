package com.caq.commonutils;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义向前端返回的数据格式
 * 分别是状态，状态对应代码，提示信息，一个map集合键值对是String和对象
 * 如状态success对应状态码为20000，提示信息为传递参数成功，返回的数据封装到一个集合data里面
 */

@Data
public class R<T> {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    private R() {
    }

    /**请求成功，封装一个成功的静态方法方便后面做调用
     * 返回的结果是一个R对象
     * @return
     */
    public static R ok(){
        R r = new R();
        //因为我们的属性是私有的，所有通过getset方法来赋值和获取
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("您的请求成功了！！！");
        return r;
    }

    /**
     * 失败的静态方法，同上
     */
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.FALSE);
        r.setMessage("您的请求失败了！！！");
        return r;
    }

    /**
     * 链式编程的原理就是返回一个this对象，就是返回本身，达到链式效果。
     * 我们经常用的 StringBuffer 就是 实现了链式的写法。
     * builder.append("blake").append("bob").append("alice").append("linese").append("eve");
     * return this返回当前对象之后可以继续`.`执行下一方法
     * @param key
     * @param value
     * @return
     */
    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    //返回的数据可能是一个map，没有键
    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    //自定义异常类返回消息可以用到
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    //自定义异常类返回消息可以用到
    public R code(Integer code){
        this.setCode(code);
        return this;
    }




}
