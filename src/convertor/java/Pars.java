package convertor.java;

// Распознаватель
class Pars {
    // Компиляция счетчика лексем
    static void Compile() {
        Scanner.NextLex();

        while (Scanner.Lex != Scanner.lexEOT) {
            if (Scanner.Lex != Scanner.lexNone) {
//                Counter.Add();
            }
            Scanner.NextLex();
        }
    }

    // Компиляция задачи А
    static void CompileA() {

        System.out.println("\n\nКомпиляция задачи А завершена.");

//        Counter.InfoAllLex();

        System.out.println("\nКомпиляция других подзадач завершена.");

//        Counter.InfoLex();
    }
}