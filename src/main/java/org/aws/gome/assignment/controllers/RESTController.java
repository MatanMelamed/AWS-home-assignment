package org.aws.gome.assignment.controllers;

import org.aws.gome.assignment.handlers.MainHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RESTController {

    @Autowired
    private final MainHandler handler;

    @Autowired
    public RESTController(MainHandler storageService) {
        this.handler = storageService;
    }


    @GetMapping("/")
    public String root(Model model) {
        return "index";
    }

    // Upload an image
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        handler.uploadPhoto(file);
        return "index";
    }

    @RequestMapping(value = "/showphotos", method = RequestMethod.GET)
    public String getImages(@RequestParam("search_key") String searchKey, Model model) {
        List<String> photosData = handler.getPhotosUrlsByLabel(searchKey);

        model.addAttribute("photos", photosData);
        System.out.println("finished get image");
        return "index";
    }
}
