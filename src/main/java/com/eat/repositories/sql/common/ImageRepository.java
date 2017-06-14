package com.eat.repositories.sql.common;

import com.eat.models.common.Image;
import com.eat.models.common.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByType(ImageType imageType);

}