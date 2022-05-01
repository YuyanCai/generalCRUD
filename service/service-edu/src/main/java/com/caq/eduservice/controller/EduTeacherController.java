package com.caq.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caq.commonutils.R;
import com.caq.eduservice.entity.EduTeacher;
import com.caq.eduservice.entity.vo.TeacherQuery;
import com.caq.eduservice.service.EduTeacherService;
import com.caq.servicebase.exceptionhandle.MyGuliException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Pyy
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/eduService/eduTeacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 增加讲师的方法
     * 接受前端传递给后端的json字符串的请求体的，后端使用@RequestBody接收数据时，
     * 请求一般是POST因为post请求把数据都放在请求体中了
     * 而RequestParam使用在GET请求中，可以接受普通元素、数组、集合、对象
     * 一个方法里，@RequestBody只能有一个，@RequestParam可以有多个。
     * https://juejin.cn/post/6886641620578467853
     */
    @ApiOperation("增加讲师")
    @PostMapping("addTeacher")
    public R saveTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 删除讲师
     */
    @ApiOperation("删除讲师")
    @DeleteMapping("removeTeacher")
    public R removeTeacher(@PathVariable String id) {
        boolean b = eduTeacherService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 按id改变讲师
     */
    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {

        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 获取某个讲师信息
     */
    @ApiOperation("获得讲师信息")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    /**
     * 实现分页查询
     */
    @ApiOperation("分页查询")
    @GetMapping("getTeacherPageCondition/{current}/{limit}")
    public R getTeacher(@PathVariable long current,
                        @PathVariable long limit) {
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);
        eduTeacherService.page(eduTeacherPage, null);
        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("records",records);
        return R.ok().data(map);
    }

    /**
     * 分页多条件查询
     */
    @ApiOperation("分页查询")
    @PostMapping("getTeacherPageCondition/{current}/{limit}")
    public R getTeacher(@PathVariable long current,
                        @PathVariable long limit,
                        @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.like("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.like("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.like("gmt_modified",end);
        }

        //新添加的显示在前面
        wrapper.orderByDesc("gmt_create");

        eduTeacherService.page(eduTeacherPage, wrapper);
        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        return R.ok().data("total",total).data("records",records);
    }

    /**
     * 获取全部讲师信息
     */
    @ApiOperation("获得全部讲师信息")
    @GetMapping("getAllTeacher")
    public R getTeacher() {

        List<EduTeacher> list = eduTeacherService.list(null);
        try {
            int a = 10/0;
        } catch (Exception e) {
            throw new MyGuliException(20001,"执行了自定义异常类");
        }
        return R.ok().data("teacher", list);
    }

}

