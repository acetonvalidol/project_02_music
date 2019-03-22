package project2;

public class Song {
    private String name;
    private String nameAlbum;
    private String way;
    private long duration;

    public Song(String name, String way, String nameAlbum, long duration){
        this.name = name;
        this.way = way;
        this.nameAlbum = nameAlbum;
        this.duration = duration;
    }

    public String getPath() {
        return way;
    }

    public String getName() {
        return name;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public long getDuration() {
        return duration;
    }

    public String describe(){
        return "\t\tName: "+this.name+"; time: "+ secondsToDuration(this.duration)+"; ("+this.way +")\n";
    }

    public static String secondsToDuration(long seconds) {
        if (seconds % 60 < 10)
            return seconds / 60 + ":0" + seconds % 60;
        else
            return seconds / 60 + ":" + seconds % 60;
    }
}
