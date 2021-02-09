package com.example.music_mashup.controllers;

import com.example.music_mashup.service.ApiServices.MusicBrainzService;
import com.example.music_mashup.service.ApiServices.WikidataService;
import com.example.music_mashup.service.ApiServices.WikipediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/artist")
public class ArtistController {

    @Autowired
    private MusicBrainzService musicBrainzService;
    @Autowired
    private WikidataService wikidataService;
    @Autowired
    private WikipediaService wikipediaService;

    @GetMapping()
    public ResponseEntity getArtist(){
        return ResponseEntity.status(HttpStatus.OK).body(musicBrainzService.getArtistById("qwew"));
    }

    @GetMapping("/des")
    public ResponseEntity getDescription(){
        return ResponseEntity.status(HttpStatus.OK).body(wikidataService.getDescription("Q11649"));
    }

    @GetMapping("/wikides")
    public ResponseEntity wikiDescription(){
        return ResponseEntity.status(HttpStatus.OK).body(wikipediaService.getDescription("Metallica"));
    }


}
