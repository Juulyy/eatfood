package com.eat.controllers.ui;

import com.eat.models.b2b.Offer;
import com.eat.repositories.sql.b2b.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/ui/offers")
public class OfferControllerUI {

    @Autowired
    OfferRepository offerRepository;

    @RequestMapping(value = "/show-all", method = RequestMethod.GET)
    public String showOffers(Model model){
        List<Offer> offerList;

        offerList = offerRepository.findAll();

        model.addAttribute("offerList", offerList);

        return "offers";
    }
}
