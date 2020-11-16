package org.aws.gome.assignment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {

    // Upload an image
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @GetMapping
    public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            System.out.printf("upload single file :: %s %s", file.getName(), file.getSize());

            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
