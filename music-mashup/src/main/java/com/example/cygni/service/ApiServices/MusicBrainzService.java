package com.example.cygni.service.ApiServices;

import com.example.cygni.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MusicBrainzService {

    @Autowired
    private  WikidataService wikidataService;

    @Autowired
    private WikipediaService wikipediaService;

    private static final RestTemplate restTemplate = new RestTemplate();

    public Artist getArtistById(String id) {
        String musicBrainzUrl = "http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?&f\n" +
                "mt=json&inc=url-rels+release-groups";

        try {
            var artistMap = restTemplate.getForObject(musicBrainzUrl, Map.class);
            String mbId = (String) artistMap.get("id");
            String name = (String) artistMap.get("name");
            String country = (String) artistMap.get("country");

            List<Map<String, Object>> relations = (List<Map<String, Object>>) artistMap.get("relations");

            String description = getDescriptionFromWikidataOrWikipedia(relations);
            Artist artist = new Artist();
            artist.setMbId(mbId);
            artist.setCountry(country);
            artist.setName(name);
            artist.setDescription(description);

            return artist;


        } catch (Exception e) {
            return null;
        }

    }

    private String getDescriptionFromWikidataOrWikipedia(List<Map<String, Object>> relations) {
        //String description;
        var wikipedia = relations.stream()
                .filter(o -> o.get("type").equals("wikipedia")).findFirst();
        // TODO: 07/02/2021  
        if (wikipedia.isPresent()) {
            String title = "";
            return wikipediaService.getDescription(title);
        } else {
            var wikidata = relations.stream()
                    .filter(o -> o.get("type").equals("wikidata")).findFirst();
            if (wikidata.isPresent()) {
                var wiki = wikidata.get();
                var qId = ((Map<String, String>) wiki.get("url")).get("resource").split("/")[4];
                //return "";
                return wikidataService.getDescription(qId);
            }
        }
        return null;
    }
}
