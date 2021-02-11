package com.example.music_mashup.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CoverArtArchiveService {
    private static final RestTemplate coverArtTemplate = new RestTemplate();


    public static String getImage(String id) {
        String coverArtArchiveUrl = "http://coverartarchive.org/release-group/" + id;

        try {
            var coverArtMap = coverArtTemplate.getForObject(coverArtArchiveUrl, Map.class);
            var images = (List<Map<String, Object>>) coverArtMap.get("images");
            if (images.isEmpty()) {
                return "";
            }
            return (String) images.get(0).get("image");


        } catch (RestClientException e) {
            //System.err.println(e);
            return "";
        }
    }
}
