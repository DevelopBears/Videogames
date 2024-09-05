package org.grizzielicious.VideoGames.controllers;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dto.Plataforma;
import org.grizzielicious.VideoGames.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/plataformas")
public class PlataformaController {


    @Autowired
    private PlataformaService service;


}
