package com.eat.aspects.common;

import com.eat.models.b2b.PlaceDetail;
import com.eat.models.common.Tag;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.common.TagService;
import com.eat.utils.converters.PlaceDetailConverter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TagUpdaterAspect {

    @Autowired
    private TagService tagService;

    @Autowired
    private PlaceDetailService detailService;

    @Autowired
    private PlaceDetailConverter detailConverter;

    @AfterReturning(value = "execution(* com.eat.services.b2b.PlaceDetailService.save(..))")
    public void placeDetailSaveAfterReturning(JoinPoint joinPoint) {
        PlaceDetail placeDetail = (PlaceDetail) joinPoint.getArgs()[0];
        tagService.save(detailConverter.toTag(placeDetail));
    }

    @Before(value = "execution(* com.eat.services.b2b.PlaceDetailService.update(..))")
    public void placeDetailUpdateAfterReturning(JoinPoint joinPoint) {
        PlaceDetail updatedPlaceDetail = (PlaceDetail) joinPoint.getArgs()[0];
        PlaceDetail oldPlaceDetail = detailService.findById(updatedPlaceDetail.getId());
        Tag tag = tagService.findByNameAndType(oldPlaceDetail.getName(),
                detailConverter.toTagType(oldPlaceDetail.getPlaceDetailType()));
        if (tag == null) {
            tag = detailConverter.toTag(updatedPlaceDetail);
        }
        tag.setName(updatedPlaceDetail.getName());
        tagService.update(tag);
    }

    @Before(value = "execution(* com.eat.services.b2b.PlaceDetailService.delete(..))")
    public void placeDetailRemoveBefore(JoinPoint joinPoint) {
        Long detailId = (Long) joinPoint.getArgs()[0];
        PlaceDetail detail = detailService.findById(detailId);
        if (detail != null) {
            Tag tag = tagService.findByNameAndType(detail.getName(),
                    detailConverter.toTagType(detail.getPlaceDetailType()));
            if (tag != null) {
                tagService.delete(tag);
            }
        }
    }

}