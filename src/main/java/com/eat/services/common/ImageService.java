package com.eat.services.common;

import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Image;
import com.eat.models.common.enums.ImageType;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.repositories.sql.common.ImageRepository;
import com.eat.services.b2b.OfferService;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import com.eat.services.b2c.AppUserService;
import com.eat.services.recommender.SuggestionCategoryService;
import com.eat.utils.graphic.PhotoMerge;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageService {

    public static final String IMAGES_FOLDER = "images";
    public static final String PLACES_FOLDER = "places";
    public static final String USERS_FOLDER = "users";
    public static final String OFFERS_FOLDER = "users";
    public static final String CATEGORIES_FOLDER = "categories";
    public static final String SYSTEM = "system";

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private SuggestionCategoryService categoryService;

    @Autowired
    private PhotoMerge photoMerge;

    @Autowired
    private PlaceDetailService detailService;

    public void uploadPlaceGalleryImages(Long id, ArrayList<MultipartFile> files) {
        Place place = placeService.findById(id);
        String path = IMAGES_FOLDER + File.separator + PLACES_FOLDER + File.separator + id.toString();
        files.forEach(file -> {
            String imageUrl = amazonS3Service.uploadImage(file, path);
            if (imageUrl != null) {
                place.getImages().add(Image.of()
                        .type(ImageType.PLACE_GALLERY)
                        .imageUrl(imageUrl)
                        .create());
                placeService.update(place);
            }
        });
    }

    public void deletePlaceGalleryImage(Long id, Long imageId) {
        Place place = placeService.findById(id);
        Image image = findById(imageId);
        place.getImages().remove(image);
        placeService.update(place);
    }

    public void uploadPlaceCategoryImage(Long id, MultipartFile file) {
        Place place = placeService.findById(id);
        String path = IMAGES_FOLDER + File.separator + PLACES_FOLDER + File.separator + id.toString();
        String imageUrl = amazonS3Service.uploadImage(file, path);
        if (imageUrl != null) {
            List<Image> oldCategoryImages = place.getImages().stream()
                    .filter(image -> findPaidCategoryImages.or(findFreeCategoryImages).test(image))
                    .collect(Collectors.toList());
            if (!oldCategoryImages.isEmpty()) {
                oldCategoryImages.forEach(image -> place.getImages().remove(image));
            }

            place.getImages().add(Image.of()
                    .type(ImageType.PAID_PLACE_CATEGORY)
                    .imageUrl(imageUrl)
                    .create());
            placeService.update(place);
        }
    }
    private Predicate<Image> findPaidCategoryImages = image -> image.getType().equals(ImageType.PAID_PLACE_CATEGORY);

    private Predicate<Image> findFreeCategoryImages = image -> image.getType().equals(ImageType.FREE_PLACE_CATEGORY);

    public void uploadFreePlaceCategoryImage(Long id, File file) {
        Place place = placeService.findById(id);
        String path = IMAGES_FOLDER + File.separator + PLACES_FOLDER + File.separator + id.toString();
        String imageUrl = amazonS3Service.uploadImage(file, path);
        if (imageUrl != null) {
            List<Image> freePlaceCategoryImages = place.getImages().stream()
                    .filter(image -> image.getType() == ImageType.FREE_PLACE_CATEGORY)
                    .collect(Collectors.toList());
            if (!freePlaceCategoryImages.isEmpty()) {
                place.getImages().removeAll(freePlaceCategoryImages);
            }
            place.getImages().add(Image.of()
                    .type(ImageType.FREE_PLACE_CATEGORY)
                    .imageUrl(imageUrl)
                    .create());
            placeService.update(place);
        }
    }

    public void uploadCategoryImage(Long categoryId, ArrayList<MultipartFile> files) {
        SuggestionCategory category = categoryService.findById(categoryId);
        String path = IMAGES_FOLDER + File.separator + CATEGORIES_FOLDER + File.separator + categoryId.toString();
        String imageUrl = amazonS3Service.uploadImage(files.get(0), path);
        if (imageUrl != null) {
            category.setImage(Image.of()
                    .type(ImageType.CATEGORY)
                    .imageUrl(imageUrl)
                    .create());
            categoryService.update(category);
        }
    }

    public void uploadSystemImages(ImageType type, List<MultipartFile> files) {
        String path = IMAGES_FOLDER + File.separator + SYSTEM + File.separator + type.name();
        files.forEach(file -> {
            String imageUrl = amazonS3Service.uploadImage(file, path);
            if (imageUrl != null) {
                save(Image.of()
                        .imageUrl(imageUrl)
                        .type(type)
                        .create());
            }
        });
    }

    public void setPlaceDetailImage(Long placeDetId, Long imageId) {
        PlaceDetail placeDetail = detailService.findById(placeDetId);
        Image image = findById(imageId);
        placeDetail.setImage(image);
        detailService.update(placeDetail);
    }

    public void setPlaceCategoryImage(Long placeId) {
        Place place = placeService.findById(placeId);
        List<Image> placeCategoriesImages = findAllByImageType(ImageType.PLACE_CATEGORY);

        File uploadMergedImage = null;

        Optional<PlaceDetail> placeType = place.getPlaceDetails().stream()
                .filter(placeDetail -> placeDetail.getPlaceDetailType().equals(PlaceDetailType.PLACE_TYPE))
                .findFirst();

        if (placeType.isPresent()) {
            PlaceDetail placeDetail = detailService.findById(placeType.get().getId());
            String placeTypeImageUrl = placeDetail.getImage().getImageUrl();

            String placeCategoryImageUrl = placeCategoriesImages
                    .get(RandomUtils.nextInt(0, placeCategoriesImages.size())).getImageUrl();
            try {
                uploadMergedImage = photoMerge.mergeImages(new URL(placeCategoryImageUrl), new URL(placeTypeImageUrl));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            uploadFreePlaceCategoryImage(placeId, uploadMergedImage);
        }
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public Image findById(Long id) {
        return imageRepository.findOne(id);
    }

    public void delete(Long id) {
        imageRepository.delete(id);
    }

    public Image update(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public List<Image> findAllByImageType(ImageType imageType) {
        return imageRepository.findAllByType(imageType);
    }

}