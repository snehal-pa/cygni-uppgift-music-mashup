package com.example.music_mashup.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WikipediaService {
    private static final RestTemplate wikipediaRestTemplate = new RestTemplate();


    public String getDescription(String title) {
        String wikipediaUrl =
                "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=" + title;
        try {
            var wikipediaMap = wikipediaRestTemplate.getForObject(wikipediaUrl, Map.class);

            var queryMap = (Map<String, Object>) wikipediaMap.get("query");
            var pageMap = ((Map<String, Object>) queryMap.get("pages"))
                    .entrySet()
                    .stream().map(o -> o.getValue())
                    .findFirst().get();
            String description = (String) ((Map<String, Object>) pageMap).get("extract");
            return description;
        } catch (RestClientException e) {
            e.printStackTrace();
            return "";
        }
    }
}
