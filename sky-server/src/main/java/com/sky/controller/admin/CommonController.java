package com.sky.controller.admin;



import com.aliyuncs.exceptions.ClientException;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {


    /**
     * 文件上上传
     * @param file
     * @return
     */
    @Autowired
    private AliOssUtil  aliOssUtil;
    @PostMapping("//upload")
    public Result<String> upload(MultipartFile file)  {
            log.info("文件上传:{}",file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀
            //lastindexof("."),返回最后一个字符串"."的索引。substring(索引),从索引位置截取到最后(包左不包右)
            //所以得出来的结果是".xxx"的形式
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //构造新的文件名称
            //获取一个随机uuid,再加上文件的拓展名(后缀名)得到文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //使用AliOssUtil工具类,形参为文件的字节形式和文件名称
            //文件名使用uuid生成,防止重复上传的文件名字一样而被覆盖
            //因文件名由名称和后缀(文件类型)组成,所以要先获取被上传文件的类型(后缀名)

            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
            //上传文件给阿里云oss后,阿里云会返回一个可以访问到此文件的url地址filePath
            //再把这个地址记录进数据库
        } catch (IOException | ClientException e) {
            //如果上传出bug
            //抛出定义好的错误信息(见com.sky.constant.MessageConstant)
            log.info("文件上传失败");
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}
