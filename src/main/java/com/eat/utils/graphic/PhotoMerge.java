package com.eat.utils.graphic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Component
public class PhotoMerge implements ServletContextAware {

    private ServletContext servletContext;

    private final Integer WIDTH = 491;
    private final Integer HEIGHT = 174;

    private final String IMAGE_FORMAT = "PNG";

    public File mergeImages(URL backgroundUrl, URL logoUrl) {
        BufferedImage backgroundImage = null;
        BufferedImage logoImage = null;

        try {
            backgroundImage = ImageIO.read(backgroundUrl);
            logoImage = ImageIO.read(logoUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        Point logoCoordinates = getStartingLogoCoordinates(HEIGHT, WIDTH, logoImage);

        BufferedImage combinedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics g = combinedImage.getGraphics();
        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(logoImage, logoCoordinates.x, logoCoordinates.y, null);

        File mergedImageFile = new File(tmpPath.get(), fullFileName.get());

        try {
            ImageIO.write(combinedImage, IMAGE_FORMAT, mergedImageFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return mergedImageFile;
    }

    private Supplier<String> tmpPath = () -> servletContext.getAttribute(ServletContext.TEMPDIR).toString();

    private Supplier<String> fileName = () -> "tmp-combined-".concat(UUID.randomUUID().toString());

    private Supplier<String> fullFileName = () -> fileName.get().concat(".").concat(IMAGE_FORMAT.toLowerCase());

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private Point getStartingLogoCoordinates(Integer backgroundHeight, Integer backgroundWidth, BufferedImage logoImage) {
        Integer backgroundHalfHeight = backgroundHeight / 2;
        Integer backgroundHalfWidth = backgroundWidth / 2;

        Integer logoHalfHeight = logoImage.getHeight() / 2;
        Integer logoHalfWidth = logoImage.getWidth() / 2;

        return new Point(backgroundHalfWidth - logoHalfWidth, backgroundHalfHeight - logoHalfHeight);
    }

}