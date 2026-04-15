package org.studyeasy.springstrater.Controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HomeRestController {


Logger logger =LoggerFactory.getLogger(HomeRestController.class);
    @GetMapping("/")
    public String home() {

        logger.info("Home API called");
        return "simple rest api";
    }

 
}