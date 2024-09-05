package org.grizzielicious.VideoGames.controllers;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dto.Estudio;
import org.grizzielicious.VideoGames.service.EstudioService;
//import org.hibernate.cache.spi.access.EntityDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/estudios")
public class EstudioController {

    @Autowired
    private EstudioService service;


}