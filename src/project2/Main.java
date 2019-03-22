package project2;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InvalidDataException, UnsupportedTagException {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите путь: ");
        String way = in.nextLine();
        Cataloging cataloging = new Cataloging(way);
        cataloging.dissection();
        cataloging.doHTML1(way);
        cataloging.doHTML2(way);
        cataloging.doHTML3(way);
    }
}
