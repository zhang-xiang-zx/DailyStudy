package cn.xiangstudy.controller.file;

import cn.xiangstudy.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangxiang
 * @date 2024-10-12 9:07
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file){
        return fileService.uploadFile(file);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName){
        return fileService.downloadFile(fileName);
    }
}

