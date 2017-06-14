package com.eat.models.common;

import com.eat.services.common.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class TagTest {

    @Autowired
    private TagService service;

    public ArrayList<String> getTagsList() {
        ArrayList<String> tags = new ArrayList<>();
        /*tags.addAll(service.findByName("Hot").getName());
        tags.addAll(service.findByName("Non-Alcohol").getName());*/
        return tags;
    }

    @Test
    @Rollback(value = false)
    public void test_saveOnlyOne_success() {
      /*  service.save(
                Tag.of()
                        .name("Drinks")
                        .parent(null)
                        .type(Tag.TagType.MENU_ITEM_GROUP)
                        .create()
        );
        service.save(
                Tag.of()
                        .name("Main dishes")
                        .parent(null)
                        .type(Tag.TagType.MENU_ITEM_GROUP)
                        .create()
        );
        service.findAll().forEach(System.out::println);
        service.save(
                Tag.of()
                        .name("Hot")
                        .parent(null)
                        .type(Tag.TagType.CUSTOM)
                        .create()
        );
        service.save(
                Tag.of()
                        .name("Non-Alcohol")
                        .parent(null)
                        .type(Tag.TagType.CUSTOM)
                        .create()
        );
        service.save(
                Tag.of()
                        .name("Tea")
                        .parent(service.findByName("Drinks"))
                        .type(Tag.TagType.MENU_ITEM_CATEGORY)
                        .create()
        );
        service.findAllByType(Tag.TagType.MENU_ITEM_CATEGORY).forEach(System.out::println);
        service.save(
                Tag.of()
                        .name("Lipton")
                        .type(Tag.TagType.MENU_ITEM)
                        .parent(service.findByName("Tea"))
                        .create());

        List<Tag> items = service.findAllByType(Tag.TagType.MENU_ITEM);
        items.forEach(tags -> System.out.println(tags.toString() + " " + tags.getParent().toString() + " "
                + tags.getParent().getParent().toString()));*/

    }

}