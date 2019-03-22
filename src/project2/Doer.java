package project2;

import java.util.TreeMap;

public class Doer {
    private String name;
    private TreeMap<String, Album> albums;

    public Doer(){}

    public Doer(String name){
        this.name = name;
        this.albums = new TreeMap<>();
    }

    public Doer(String name, Song song){
        this.name = name;
        this.albums = new TreeMap<>();
        addSong(song);
    }

    public String getName() {
        return name;
    }

    public TreeMap<String, Album> getAlbums() {
        return albums;
    }

    public void addSong(Song song){
        String nameAlbum = song.getNameAlbum();
        if(nameAlbum!=null&&albums.get(nameAlbum)!=null)
            albums.get(nameAlbum).addSong(song);
        else {
            albums.put(nameAlbum,new Album(song));
        }
    }

    public String describe(){
        String string = "Name: "+this.name+"\n";
        for(Album album:albums.values())
            string+=album.describe();
        return string;
    }
}
