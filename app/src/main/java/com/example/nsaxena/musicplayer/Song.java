package com.example.nsaxena.musicplayer;

/**
 * Created by root on 19/12/17.
 */

class Song {
    private String mSongTitle;
    private String mSongUrl;
    private String mSongArtists;
    private String mSongImage;

    public Song(String songTitle, String songUrl, String songArtists, String songImage) {
        mSongTitle = songTitle;
        mSongUrl = songUrl;
        mSongArtists = songArtists;
        mSongImage = songImage;
    }

    String getSongTitle() {
        return mSongTitle;
    }

    void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }

    String getSongUrl() {
        return mSongUrl;
    }

    void setSongUrl(String songUrl) {
        mSongUrl = songUrl;
    }

    String getSongArtists() {
        return mSongArtists;
    }

    void setSongArtists(String songArtists) {
        mSongArtists = songArtists;
    }

    String getSongImage() {
        return mSongImage;
    }

    void setSongImage(String songImage) {
        mSongImage = songImage;
    }

    @Override
    public String toString() {
        return "Song{" +
                "mSongTitle='" + mSongTitle + '\'' +
                ", mSongUrl='" + mSongUrl + '\'' +
                ", mSongArtists='" + mSongArtists + '\'' +
                ", mSongImage='" + mSongImage + '\'' +
                '}';
    }
}
