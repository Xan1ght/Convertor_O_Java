package convertor.java;
import java.io.*;

// Драйвер исходного текста
class Text {

    static final int  TABSIZE = 3;      // Размер табуляции
    static final char chSPACE = ' ';    // Пробел
    static final char chTAB   = '\t';   // Табуляция
    static final char chEOL   = '\n';   // Конец строки
    static final char chEOT   = '\0';   // Конец текста

    static boolean Ok = false;
    static String Message = "Ошибка при чтении файла";
    static int Ch = chEOT;

    private static InputStream f;

    static void NextCh() {
        try {
            Location.ErrorPos = Location.Pos;
            if ((Ch = f.read()) == -1) {
                Ch = chEOT;
            } else if (Ch == '\n') {
                System.out.println();
                Location.Line++;
                Location.ErrorPos = Location.Pos;
                Location.Pos = 0;
                Ch = chEOL;
            } else if (Ch == '\r') {
                NextCh();
            } else if (Ch != '\t') {
                System.out.write(Ch);
                Location.Pos++;
            } else {
                do {
                    System.out.print(' ');
                } while ( ++Location.Pos % TABSIZE != 0 );
            }
        } catch (IOException e) {};
    }


    static void Reset() {
        try {
            f = new FileInputStream(Location.Path);
            Ok = true;
            Message = "Ok";
            Location.Pos = 0;
            Location.Line = 1;
            NextCh();
        } catch (IOException e) {
            Ok = false;
            Message = "Входной файл не найден";
        }
    }


    static void Close() {
        try {
            f.close();
        } catch (IOException e) {};
    }
}