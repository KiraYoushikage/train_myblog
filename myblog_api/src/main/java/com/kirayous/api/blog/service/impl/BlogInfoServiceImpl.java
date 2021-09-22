package com.kirayous.api.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kirayous.api.blog.service.BlogInfoService;
import com.kirayous.api.service.MyRedisService;
import com.kirayous.api.service.impl.RedisServiceImpl;
import com.kirayous.common.constant.RedisPrefixConst;
import com.kirayous.common.exception.MyServeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.api.blog.service.impl
 * @date 2021/9/18 10:39
 */
@Service
public class BlogInfoServiceImpl implements BlogInfoService {



    @Autowired
    MyRedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNotice(String notice) {

        if (StringUtils.isBlank(notice))throw new MyServeException("公告不能为空！");
        redisService.set(RedisPrefixConst.NOTICE, notice);
        String res=(String)redisService.get(RedisPrefixConst.NOTICE);
        if (StringUtils.isBlank(res)) System.out.println("获取到的值为空值");
        else
            System.out.println(res);
//        System.out.println(res);
    }

    @Override
    public String getNotice() {
        Object value = redisService.get(RedisPrefixConst.NOTICE);
        return Objects.nonNull(value) ? value.toString() : "发布你的第一篇公告吧";
    }
}
