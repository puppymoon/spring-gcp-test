package com.example.springgcptest.controller;

import com.example.springgcptest.service.CloudStorageService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
public class CloudStorageController {


    public CloudStorageController(HttpServletResponse response, CloudStorageService cloudStorageService) {
        this.response = response;
        this.cloudStorageService = cloudStorageService;
    }

    private HttpServletResponse response;

    private CloudStorageService cloudStorageService;

    @PostMapping("/listBuckets")
    public List<String> listBuckets() {
        return cloudStorageService.listBuckets();
    }

    @PostMapping("/listFiles")
    public List<String> listFiles() {
        return cloudStorageService.listObjects();
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestPart MultipartFile file) {
        try {
            return cloudStorageService.upload(file);
        } catch (IOException e) {
            log.error("ioex {}", e);
            return "fail";
        }
    }

    @PostMapping("download")
    public String download(@RequestParam("fileName") String fileName) {

        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            cloudStorageService.download(fileName, bos);
            bos.flush();
            return null;
        } catch (IOException e) {
            log.error("ioex {}", e);
            return "fail";
        }
    }
}
