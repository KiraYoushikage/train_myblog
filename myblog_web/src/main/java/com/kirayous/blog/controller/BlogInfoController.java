package com.kirayous.blog.controller;

import com.kirayous.api.blog.service.BlogInfoService;
import com.kirayous.common.Result;
import com.kirayous.common.annotation.OptLog;
import com.kirayous.common.constant.OptTypeConst;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.blog.controller
 * @date 2021/9/18 10:08
 */
@RestController
public class BlogInfoController {

    @Autowired
    BlogInfoService blogInfoService;


    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改公告")
    @PutMapping("/admin/notice")
    public Result updateNotice(@RequestParam(value = "notice",required = true) String notice) {
        blogInfoService.updateNotice(notice);
        return Result.success().setMessage("修改成功！");
    }

    @ApiOperation(value = "查看公告")
    @GetMapping("/admin/notice")
    public Result getNotice() {
        String res=blogInfoService.getNotice();
        return Result.success().setMessage("查询成功").setData(res);
    }
}
