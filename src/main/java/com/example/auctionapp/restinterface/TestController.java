package com.example.auctionapp.restinterface;

import com.example.auctionapp.accessors.S3Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class TestController {
    @Autowired
    private S3Accessor s3Accessor;

//    @GetMapping("/test")
//    public void queryS3() {
//        s3Accessor.getS3Object("5154jECTK5L._SL1000_.jpg");
//    }

//    @PostMapping("/test")
//    public void uploadS3(@RequestParam("image") MultipartFile image, @RequestParam String test) throws IOException {
//        System.out.println(test);
//        s3Accessor.putS3Object(image);
//    }
}
