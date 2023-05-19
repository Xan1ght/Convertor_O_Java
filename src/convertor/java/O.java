package convertor.java;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class O {

    static void Init() {
        Text.Reset();
        if (!Text.Ok) {
            Error.Message(Text.Message);
        }
        Scanner.Init();
    }

    static void Done() {
        Text.Close();
    }


    public static void main(String[] args) {
        System.out.println("\nХлюпин Дмитрий, ПМ-21");
        System.out.print("Компилятор С++");

        if (args.length == 0) {
            Location.Path = null;
        } else {
            Location.Path = args[0];
        }

        if (Location.Path == null || (Location.Path.length() < 3 )) {
            System.out.println("\nФормат вызова:\n   <Входной файл>.<Формат файла>");
            System.exit(1);
        } else if (Location.Path.charAt(0) == '*') {
            String currentDirectory = System.getProperty("user.dir");
            Path dir = Paths.get(currentDirectory);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*" +
                    Location.Path.substring(Location.Path.lastIndexOf('.')))) {
                for (Path file : stream) {
                    Location.Path = file.toString();

                    System.out.println("\n\n\n" + "Название файла: " + file.getFileName());
                    System.out.println("\033[33m" +("=").repeat(100) + "\033[0m");

                    Init();                 // Инициализация текста и сканнера
                    Pars.Compile();         // Компиляция данного файла
                    Done();                 // Завершение работы данного файла

                    System.out.println("\n\033[33m" +("=").repeat(100) + "\033[0m");
                }

                Pars.CompileA();            // Компиляция задачи А

            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            }
        } else {
            System.out.println("\n\n\n" + "Название файла: " + Location.Path);
            System.out.println("\033[33m" +("=").repeat(100) + "\033[0m");

            Init();             // Инициализация текста и сканнера
            Pars.Compile();     // Компиляция файла
            Done();             // Завершение работы

            System.out.println("\n\033[33m" +("=").repeat(100) + "\033[0m");

            Pars.CompileA();    // Компиляция задачи А
        }
    }
}