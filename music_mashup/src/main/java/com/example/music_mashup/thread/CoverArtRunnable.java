package com.example.music_mashup.thread;

import com.example.music_mashup.model.Album;
import com.example.music_mashup.services.CoverArtArchiveService;

import java.util.List;

public class CoverArtRunnable implements Runnable {


    private String title;
    private String id;
    private List<Album> albums;

    public CoverArtRunnable(String title, String id, List<Album> albums) {
        this.title = title;
        this.id = id;
        this.albums = albums;
    }

    @Override
    public void run() {

        String image = CoverArtArchiveService.getImage(id);
        Album album = new Album(title, id, image);
        albums.add(album);
    }
}
