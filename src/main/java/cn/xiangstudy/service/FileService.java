package cn.xiangstudy.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangxiang
 * @date 2024-10-12 9:14
 */
public interface FileService {

    /**
     * @description: 上传文件
     * @author: zhangxiang
     * @date: 2024/10/12 9:21
     * @param: [file]
     * @return: void
     */
    String uploadFile(MultipartFile file);

    ResponseEntity<byte[]> downloadFile(String fileName);
}
