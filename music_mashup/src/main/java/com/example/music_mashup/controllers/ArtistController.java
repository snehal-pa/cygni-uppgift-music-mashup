package com.example.music_mashup.controllers;

import com.example.music_mashup.service.ApiServices.MusicBrainzService;
import com.example.music_mashup.service.ApiServices.WikidataService;
import com.example.music_mashup.service.ApiServices.WikipediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/artist")
public class ArtistController {

    @Autowired
    private MusicBrainzService musicBrainzService;
    @Autowired
    private WikidataService wikidataService;
    @Autowired
    private WikipediaService wikipediaService;


    @GetMapping("/{id}")
    public ResponseEntity getArtist(@PathVariable String id) {
        var artist = musicBrainzService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("check if the mbId is correct");
        }
        if(artist.getMbId().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This is not an artist's mbid");
        }
        return ResponseEntity.status(HttpStatus.OK).body(artist);
    }

//    @GetMapping("/wiki/description")
//    public ResponseEntity getDescription() {
//        return ResponseEntity.status(HttpStatus.OK).body(wikidataService.getDescription("Q11649"));
//    }

    @GetMapping("/wiki/description")
    public ResponseEntity wikiDescription(@RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK).body(wikipediaService.getDescription(title));
    }


}
