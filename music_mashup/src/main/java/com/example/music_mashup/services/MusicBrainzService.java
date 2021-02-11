package com.example.music_mashup.services;

import com.example.music_mashup.model.Album;
import com.example.music_mashup.model.Artist;
import com.example.music_mashup.thread.MyRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MusicBrainzService {

    @Autowired
    private WikidataService wikidataService;

    @Autowired
    private WikipediaService wikipediaService;


    private static final RestTemplate restTemplate = new RestTemplate();

    public Artist getArtistByMbId(String id) {

        try{
            // checking if mbid is a valid uuid
            UUID.fromString(id);

            String musicBrainzUrl = "http://musicbrainz.org/ws/2/artist/" + id + "?&fmt=json&inc=url-rels+release-groups";

            try {
                var artistMap = restTemplate.getForObject(musicBrainzUrl, Map.class);
                String mbId = (String) artistMap.get("id");
                String name = (String) artistMap.get("name");
                String country = (String) artistMap.get("country");

                var relations = (List<Map<String, Object>>) artistMap.get("relations");

                String description = getDescriptionFromWikidataOrWikipedia(relations,name);

                var releaseGroups = (List<Map<String, Object>>) artistMap.get("release-groups");

                var albums = getAlbums(releaseGroups);

                Artist artist = new Artist(mbId, name, description, country, albums);

                return artist;


            } catch (Exception e) {

                // handle the case where string is not artist's mbid
                Artist artist= new Artist("","","","",null);
                return artist;
            }


        } catch (IllegalArgumentException exception){

            // handle the case where string is not valid UUID
            return null;
        }


    }

    private String getDescriptionFromWikidataOrWikipedia(List<Map<String, Object>> relations,String name) {
        //String description;
        var wikipedia = relations.stream()
                .filter(o -> o.get("type").equals("wikipedia")).findFirst();

        if (wikipedia.isPresent()) {

            return wikipediaService.getDescription(name);
        } else {
            var wikidata = relations.stream()
                    .filter(o -> o.get("type").equals("wikidata")).findFirst();
            if (wikidata.isPresent()) {
                var wiki = wikidata.get();
                var qId = ((Map<String, String>) wiki.get("url")).get("resource").split("/")[4];
                return wikidataService.getDescription(qId);
            }
        }
        return null;
    }


    private List<Album> getAlbums(List<Map<String, Object>> releaseGroups) {
        List<Album> albums = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();

        for (Map<String, Object> a : releaseGroups) {
            String title = (String) a.get("title");
            String id = (String) a.get("id");

            //using thread for every call to an coverArtArchive api to get image for each album

            MyRunnable runnable = new MyRunnable(title, id, albums);
            Thread thread = new Thread(runnable);
            threadList.add(thread);
            thread.start();
        }
        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        });
        Collections.reverse(albums);

        return albums;

    }
}
