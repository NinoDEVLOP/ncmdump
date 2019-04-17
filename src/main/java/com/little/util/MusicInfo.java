package com.little.util;

/**
 * @author created by qingchuan.xia
 */
public class MusicInfo {
    private String name;
    private String artist;
    private String album;
    private String format;
    private String filePath;

    @SuppressWarnings("unused")
    public MusicInfo(String name, String artist, String album, String format, String filePath) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.format = format;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
