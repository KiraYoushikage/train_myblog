package com.kirayous.common.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.kirayous.common.enums.PicExtEnum;
import com.kirayous.common.exception.MyServeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.utils
 * @date 2021/9/16 20:30
 */
@Component
public class OSSUtil {

    private static String url;

    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    @Value("${aliyun-oss.url}")
    public void setUrl(String url) {
        OSSUtil.url = url;
    }

    @Value("${aliyun-oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OSSUtil.endpoint = endpoint;
    }

    @Value("${aliyun-oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OSSUtil.accessKeyId = accessKeyId;
    }

    @Value("${aliyun-oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OSSUtil.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun-oss.bucketName}")
    public void setBucketName(String bucketName) {
        OSSUtil.bucketName = bucketName;
    }

    /**
     * 上传图片
     *
     * @param file       文件
     * @param targetAddr 目标路径
     * @return
     */
    public static String upload(MultipartFile file, String targetAddr) {
        // 获取不重复的随机名
        String fileName = String.valueOf(IdWorker.getId());
        // 获取文件的扩展名如png,jpg等
        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        //暂且只支持png和jpg格式的图片
        if (!checkSuffixIsLegalable(extension))throw new MyServeException("后缀不规范（暂时只支持jpg和png格式）");
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + fileName + extension;

        PutObjectResult result = null;
        try {
            // 创建ClientConfiguration实例，您可以根据实际情况修改默认参数。
//            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
// 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
//            conf.setSupportCname(true);
//            conf.setDefaultHeaders();
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 设置文件的访问权限为公共读。
//            ossClient.setObjectAcl(bucketName, , CannedAccessControlList.PublicRead);
            // 上传文件
            result = ossClient.putObject(bucketName, relativeAddr, file.getInputStream());
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (Objects.isNull(result)) return null;
        if (url.lastIndexOf('/') != -1) return url + "/" + relativeAddr;
        return url + relativeAddr;
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    /**
     *  检查上传的图片后缀是否合法
     * @param ext
     * @return
     */
    private static boolean checkSuffixIsLegalable(String ext) {
        ext=ext.substring(1).toLowerCase();
        return PicExtEnum.getAllPicExt().stream().anyMatch(Predicate.isEqual(ext));
    }

}

