package com.example.moviecatalogue.model;

import jakarta.persistence.*;

@Entity
public class Movie {

    @Id
    private int id;  // Use TMDb movie id as primary key

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String overview;

    @Column(length = 500)
    private String poster_path;
    private String release_date;
    private Double vote_average;

    // Default constructor
    public Movie() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
