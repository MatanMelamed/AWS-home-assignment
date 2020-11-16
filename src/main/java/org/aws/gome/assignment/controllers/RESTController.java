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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/getphotos", method = RequestMethod.GET)
    public String getImages(HttpServletRequest request, HttpServletResponse response) {
        handler.getPhotos();
        return "index";
    }
}
