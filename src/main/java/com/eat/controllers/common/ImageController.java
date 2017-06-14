package com.eat.controllers.common;

import com.eat.models.common.Image;
import com.eat.models.common.enums.ImageType;
import com.eat.services.common.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/upload/place/{id}/gallery")
    public ResponseEntity uploadPlaceGalleryImages(@PathVariable("id") Long id,
                                                   @RequestParam("files") ArrayList<MultipartFile> files) {
        imageService.uploadPlaceGalleryImages(id, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "delete/place/{id}/gallery")
    public ResponseEntity deletePlaceGalleryImage(@PathVariable(value = "id") Long id,
                                                  @RequestParam(value = "imageId") Long imageId) {
        imageService.deletePlaceGalleryImage(id, imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload/place/{id}/category")
    public ResponseEntity uploadPlaceCategoryImage(@PathVariable("id") Long id,
                                                   @RequestParam("files") MultipartFile file) {
        imageService.uploadPlaceCategoryImage(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/upload/place/{id}/category/default")
    public ResponseEntity uploadPlaceDefaultCategoryImage(@PathVariable("id") Long id) {
        imageService.setPlaceCategoryImage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/upload/place-detail/{id}")
    public ResponseEntity uploadPlaceDefaultCategoryImage(@PathVariable("id") Long id,
                                                          @RequestParam(value = "imageId") Long imageId) {
        imageService.setPlaceDetailImage(id, imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload/category/{id}")
    public ResponseEntity uploadCategoryImage(@PathVariable("id") Long id,
                                              @RequestParam("files") ArrayList<MultipartFile> files) {
        imageService.uploadCategoryImage(id, files);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload/system")
    public ResponseEntity uploadSystemImage(@RequestParam("type") ImageType type,
                                            @RequestParam("files") ArrayList<MultipartFile> files) {
        imageService.uploadSystemImages(type, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/system/find-all")
    public List<Image> findAllByType(@RequestParam("type") ImageType imageType) {
        return imageService.findAllByImageType(imageType);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteImage(@PathVariable(value = "id") Long imageId) {
        imageService.delete(imageId);
        return ResponseEntity.ok().build();
    }

}