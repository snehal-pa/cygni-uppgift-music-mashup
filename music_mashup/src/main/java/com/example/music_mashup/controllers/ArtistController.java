package com.example.music_mashup.controllers;

import com.example.music_mashup.services.MusicBrainzService;
import com.example.music_mashup.services.WikidataService;
import com.example.music_mashup.services.WikipediaService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rest/artist")
public class ArtistController {

    @Autowired
    private MusicBrainzService musicBrainzService;
    @Autowired
    private WikidataService wikidataService;
    @Autowired
    private WikipediaService wikipediaService;

    // http://localhost:8080/rest/artist/ed3f4831-e3e0-4dc0-9381-f5649e9df221  ---> example
    @GetMapping("/{id}")
    @RateLimiter(name = "artistRateLimit", fallbackMethod = "artistFallBack")
    public ResponseEntity getArtist(@PathVariable String id) {    //5b11f4ce-a62d-471e-81fc-a69a8278c7da
        var artist = musicBrainzService.getArtistByMbId(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter a valid mbid");
        }
        if (artist.getMbId().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This is not an artist's mbid");
        }
        return ResponseEntity.status(HttpStatus.OK).body(artist);
    }

    public ResponseEntity artistFallBack(String name, io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {
        System.out.println("Rate limit applied no further calls are accepted");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "1"); //retry after one second

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders) //send retry header
                .body("Too many request - No further calls are accepted");
    }


}
