package org.aws.gome.assignment.controllers;

import org.aws.gome.assignment.services.AnalyzedPhotosService;
import org.aws.gome.assignment.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
public class RESTController {

    private final StorageService storageService;

    @Autowired
    public RESTController(AnalyzedPhotosService storageService) {this.storageService = storageService;}


    @GetMapping("/")
    public String uploadPage(Model model) {
        return "index";
    }

    // Upload an image
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            System.out.printf("upload single file :: %s %s\n", file.getName(), file.getSize());

            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "index";
    }
}
