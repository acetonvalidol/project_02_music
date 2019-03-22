package project2;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.TreeMap;
import java.util.ArrayList;

public class Cataloging {
    private String way;
    private TreeMap<String, Doer> doer;
    private ArrayList<File> files;

    public Cataloging(String way) {
        this.way = way;
        this.doer = new TreeMap<>();
        this.files = new ArrayList<>();
    }

    public void dissection() throws InvalidDataException, IOException, UnsupportedTagException {
        File directory = new File(way);
        files = listFilesForFolder(directory);
        for (File file : files) {
            Mp3File mp3file = new Mp3File(file);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                String artist = (id3v2Tag.getArtist() != null) ? (id3v2Tag.getArtist()) : ("null");
                String title = (id3v2Tag.getTitle() != null) ? (id3v2Tag.getTitle()) : ("null");
                String album = (id3v2Tag.getAlbum() != null) ? (id3v2Tag.getAlbum()) : ("null");
                Song song = new Song(title, file.getPath(), album, mp3file.getLengthInSeconds());
                if (doer.get(artist) != null)
                    doer.get(artist).addSong(song);
                else
                    doer.put(artist, new Doer(artist, song));
            }
        }
    }

    public void doHTML1(String way) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            PrintWriter writer = new PrintWriter(way + "/HTML_1.html", "UTF-8");
            String str = "";

            for (Doer doer : doer.values()) {
                str += doer.getName();
                for (Album album : doer.getAlbums().values()) {
                    str += album.getName();
                    for (Song song : album.getSongList()) {
                        str += song.getName() + " " + Song.secondsToDuration(song.getDuration());
                    }
                }
            }
            writer.println(str);
            writer.close();
            System.out.println("HTML_1.html успешно записан!\n");
            }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Ошибка записи HTML_1.html!\n");
        }
    }

    public void doHTML2(String way) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            HashMap<File, String> fileStringHashMap = new HashMap<>();
            for (int i = 0; i < files.size() - 1; i++) {
                fileStringHashMap.put(files.get(i), DigestUtils.md5Hex(Files.newInputStream(Paths.get(files.get(i).getPath()))));
            }
            List<String> duplicates = fileStringHashMap.values().stream()
                    .filter(i -> Collections.frequency(fileStringHashMap.values(), i) > 1)
                    .distinct()
                    .collect(Collectors.toList());
            PrintWriter writer = new PrintWriter(way + "/HTML_2.html", "UTF-8");

            final String[] str = {""};
            int k = 1;

            for (String duplicate : duplicates) {
                str[0] += "Duplicates" + (k++);
                fileStringHashMap.forEach((i, j) -> {
                    if (j.equals(duplicate)) {
                        str[0] += i.getPath();
                    }
                });
            }
            writer.println(str[0]);
            writer.close();
            System.out.println("HTML_2.html успешно записан!\n");
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Ошибка записи HTML_2.html!\n");
        }
    }

    public void doHTML3(String way) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            PrintWriter writer = new PrintWriter(way + "/HTML_3.html", "UTF-8");
            String str = "";

            for (Doer doer : doer.values()) {
                for (Album album : doer.getAlbums().values()) {
                    ArrayList<String> songsNames = new ArrayList<>();
                    album.getSongList().forEach(e -> songsNames.add(e.getName()));
                    List<Song> songs = album.getSongList().stream()
                            .filter(i -> Collections.frequency(songsNames, i.getName()) > 1)
                            .collect(Collectors.toList());
                    if (!songs.isEmpty()) {
                        str += doer.getName() + ", " + album.getName() + ", " + songs.get(0).getName();
                        for (Song song : songs) {
                            if (song.getName().equals(songs.get(0).getName()))
                                str += song.getPath();
                        }
                        songs.remove(0);
                    }
                }
            }
            writer.println(str);
            writer.close();
            System.out.println("HTML_3.html успешно записан!\n");
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Ошибка записи HTML_3.html!\n");
        }
    }

    private ArrayList<File> listFilesForFolder(File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesForFolder(fileEntry));
            }
            else {
                if (fileEntry.getName().endsWith(".mp3"))
                    files.add(fileEntry);
            }
        }
        return files;
    }
}
