package com.example.cygni.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    private String mbId;
    private String name;
    private String description;
    private String country;
    private List<Album> albums;

}
