package com.kirayous.api.blog.service;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.api.blog.service.impl
 * @date 2021/9/18 10:39
 */
public interface BlogInfoService {


    /**
     * 修改公告
     * @param notice 公告
     */
    void updateNotice(String notice);

    /**
     * 后台查看公告
     * @return 公告
     */
    String getNotice();
}
