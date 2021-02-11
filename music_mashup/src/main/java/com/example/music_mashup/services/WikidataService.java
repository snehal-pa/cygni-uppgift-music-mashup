package com.example.music_mashup.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WikidataService {
    private static final RestTemplate wikidataRestTemplate = new RestTemplate();

    @Autowired
    private WikipediaService wikipediaService;


    public String getDescription(String qId) {
        String wikidataUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&ids="
                + qId + "&format=json&props=sitelinks";

        try {
            var wikidataMap = wikidataRestTemplate.getForObject(wikidataUrl, Map.class);
            var entities = (Map<String, Object>) wikidataMap.get("entities");
            var siteLinks = (Map<String, Object>) ((Map<String, Object>) entities.get(qId)).get("sitelinks");
            var enWiki = (Map<String, Object>) siteLinks.get("enwiki");
            String title = (String) enWiki.get("title");
            String description = wikipediaService.getDescription(title);
            return description;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
