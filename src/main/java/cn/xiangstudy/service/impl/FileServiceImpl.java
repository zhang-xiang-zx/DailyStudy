package cn.xiangstudy.service.impl;

import cn.xiangstudy.service.FileService;
import cn.xiangstudy.utils.DateUtils;
import cn.xiangstudy.utils.InspectUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-10-12 9:15
 */
@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadFile(MultipartFile file) {
        String uploadFilePath = System.getProperty("user.dir") + "\\uploadFile\\";
        Date nowDate = DateUtils.nowDate();
        String strDate = DateUtils.dateToStr(nowDate, "yyyyMMddHHmmss");
        try {

            String originalFilename = file.getOriginalFilename();
            if(originalFilename != null){
                boolean isHave = InspectUtils.strIsHave(originalFilename, ".");
                if(isHave){
                    String[] split = originalFilename.split("\\.");
                    originalFilename = split[0] + strDate + "." + split[1];
                }else {
                    originalFilename = originalFilename + strDate;
                }
            }
            File uploadFile = new File(uploadFilePath + originalFilename);
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fileName){
        try{
            String uploadFilePath = System.getProperty("user.dir") + "\\uploadFile\\";
            File file = new File(uploadFilePath + fileName);
            byte[] data = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header("Content-Disposition","attachment; filename=\"" + fileName + "\"")
                    .header("Content-Length", String.valueOf(data.length))
                    .body(data);
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

