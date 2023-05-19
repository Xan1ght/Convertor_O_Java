package convertor.java;

import java.io.*;

// Обработка ошибок
class Error {
    static void Message(String Msg) {
        int ELine = Location.Line;
        int Pos = Location.ErrorPos;
        while (Text.Ch != Text.chEOL && Text.Ch != Text.chEOT) {
            Text.NextCh();
        }
        if (Text.Ch == Text.chEOT) {
            System.out.println();
        }
        for (int i = 0; i < Pos; i++) {
            System.out.print(' ');
        }
        System.out.println("^");
        System.out.println("Строка (" + ELine + ") Ошибка: " + Msg);
        System.out.println();
        System.out.print("Нажмите ВВОД(Enter)");
        try {
            while (System.in.read() != '\n' );
        } catch (IOException e) {};
        System.exit(0);
    }

    static void Expected(String Msg) {
        Message("Ожидается " + Msg);
    }

    static void Warning(String Msg) {
        System.out.println();
        System.out.println("Предупреждение: " + Msg);
    }
}