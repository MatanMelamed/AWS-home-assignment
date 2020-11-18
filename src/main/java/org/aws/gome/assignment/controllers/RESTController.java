package org.aws.gome.assignment.controllers;

import org.aws.gome.assignment.handlers.AppHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RESTController {

    @Autowired
    private final AppHandler handler;

    @Autowired
    public RESTController(AppHandler storageService) {
        this.handler = storageService;
    }

    @GetMapping("/")
    public String root(Model model) {
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        handler.uploadPhoto(file);
        return "index";
    }

    @RequestMapping(value = "/showPhotos", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<String> getImages(@RequestParam("search_key") String searchKey, Model model) {
        return handler.getPhotosUrlsByLabel(searchKey);
    }
}
