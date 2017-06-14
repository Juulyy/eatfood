package com.eat.controllers.ui;

import com.eat.services.common.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/ui/image")
public class ImageControllerUI {

    @Autowired
    private ImageService imageService;

    @GetMapping(value = {"", "/"})
    public String showImageLoader() {
        return "imageLoader";
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("files") ArrayList<MultipartFile> files) {
        imageService.uploadPlaceGalleryImages(2L, files);
        return "redirect:/ui/welcome";
    }

}
