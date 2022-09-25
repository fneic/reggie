package com.zjj.reggie.controller;

import com.zjj.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${img.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null :"上传文件原命名为空异常";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newName = UUID.randomUUID() + suffix;
        try {
            file.transferTo(new File(basePath + newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(basePath + name));
             ServletOutputStream outputStream = response.getOutputStream()){
            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = bufferedInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
