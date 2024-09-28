package org.grizzielicious.VideoGames.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ApiController {

    @GetMapping("/api")
    public ModelAndView getApiInfo() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }
}
