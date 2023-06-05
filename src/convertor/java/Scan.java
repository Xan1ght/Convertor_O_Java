package convertor.java;


/*
    Хлюпин Дмитрий, ПМ-21
    Использованная структура: Перемешанная таблица с цепочками
    Анализируемый язык: Си++
*/

// Лексический анализатор
class Scan {

    static int NAMELEN = 100;   // Наибольшая длина имени
    static int N = 127;         // Объем таблицы, где 70% заполняется
    static int NMAX = 182;      // Количество всех лексем

    final static int
            lexNone   = 0,
            lexName   = 1,   lexNum    = 2,
            lexMODULE = 3,   lexIMPORT = 4,
            lexBEGIN  = 5,   lexEND    = 6,
            lexCONST  = 7,   lexVAR    = 8,
            lexWHILE  = 9,   lexDO     = 10,
            lexIF     = 11,  lexTHEN   = 12,
            lexELSIF  = 13,  lexELSE   = 14,
            lexMult   = 15,  lexDIV    = 16,  lexMOD    = 17,
            lexPlus   = 18,  lexMinus  = 19,
            lexEQ     = 20,  lexNE     = 21,
            lexLT     = 22,  lexLE     = 23,
            lexGT     = 24,  lexGE     = 25,
            lexDot    = 26,  lexComma  = 27,  lexColon  = 28,
            lexSemi   = 29,  lexAss    = 30,
            lexLPar   = 31,  lexRPar   = 32,
            lexEOT    = 33;


    // Текущая лексема
    static int Lex;
    // Строковое значение имени
    private static final StringBuffer Buf = new StringBuffer(NAMELEN);
    static String Name;

    // Значение числовых литералов
    static int Num;

    // Класс для хэш-таблицы
    static public class Item {
        String key;
        int data;
        Item next;

        Item() {}

        Item(String key, int data, Item next) {
            this.key = key;
            this.data = data;
            this.next = next;
        }
    }

    // Клас хэш-таблица
    static public class tChainHash {
        Item items;
    }

    // Создание хэш-таблицы
    public static tChainHash[] H = new tChainHash[N];



    // Проверка лексемы на ключевое слово
    private static int TestKW() {
        Item p = Search(H, Name);

        if (p != null) {
            return p.data;
        } else {
            return lexName;
        }
    }


    // Иниацилизация хэщ-таблицы
    private static void InitChainHash() {
        for (int i = 0; i < N; i++) {
            H[i] = new tChainHash();
        }
    }


    /* Плохая функция */
    private static int ChainHash(String K) {
        final int BASE = 17;
        long m = 0;

        for (int i = 0; i < K.length(); i++) {
            m = BASE * m + Character.toUpperCase(K.charAt(i));
        }

        return Math.abs((int)(m % N));
    }


    // Добавление ключевого слова в хэш-таблицу
    public static void Add2ChainHash(tChainHash[] T, String K, int D) {
        int h = ChainHash(K);
        Item p = T[h].items;

        while (p != null && K != p.key) {
            p = p.next;
        }

        if (p == null) {
            Item q = new Item(K, D, T[h].items);
            T[h].items = q;
        }
    }


    // Поиск
    public static Item Search(tChainHash[] T, String K) {
        int h = ChainHash(K);
        Item p = T[h].items;

        while (p != null && !K.equals(p.key)) {
            p = p.next;
        }

        return p;
    }


    // Нахождение идентификатора(имени/ключевого слова)
    private static void Ident() {
        int i = 0;
        Buf.setLength(0);

        do {
            if (i++ < NAMELEN) {
                Buf.append((char) Text.Ch);
            } else {
                Error.Message("Слишком длинное имя");
            }

            Text.NextCh();
        } while (Character.isLetterOrDigit((char)Text.Ch));


        Name = Buf.toString();  // Полученное слово из строки
        Lex = TestKW();         // Проверка на ключевое слово
    }


    // Нахождение числа (бинарное, восьмеричное, десятичное, шестнадцатеричное и все суффиксы)
    private static void Number() {
        Lex = lexNum;
        Num = 0;
        do {
            int d = Text.Ch - '0';

            if ((Integer.MAX_VALUE - d)/10 >= Num) {
                Num = 10 * Num + d;
            } else {
                Error.Message("Превышено число");
            }

            Text.NextCh();
        } while (Character.isDigit((char)Text.Ch));
    }

//    // Шестнадцатеричное число
//    private static void HexNumber() {
//        do {
//            Text.NextCh();
//        } while ((Text.Ch >= '0' && Text.Ch <= '9') || (Text.Ch >= 'A' && Text.Ch <= 'F') || (Text.Ch >= 'a' && Text.Ch <= 'f'));
//    }
//
//    // Десятичное число
//    private static void DecNumber() {
//        do {
//            Text.NextCh();
//        } while (Character.isDigit((char)Text.Ch));
//    }


    // Комментарий
    private static void Comment() {
        do {
            do {
                Text.NextCh();
            } while (Text.Ch != '*' && Text.Ch != Text.chEOT);

            if (Text.Ch == '*') {
                Text.NextCh();
            }
        } while (Text.Ch != ')' && Text.Ch != Text.chEOT);

        if (Text.Ch == Text.chEOT) {
            Location.LexPos = Location.Pos;
            Error.Message("Не закончен комментарий");
        } else {
            Text.NextCh();
        }
    }


    // Следующая лексема
    static void NextLex() {
        while (Text.Ch == Text.chSPACE || Text.Ch == Text.chTAB || Text.Ch == Text.chEOL) {
            Text.NextCh();
        }

        Location.LexPos = Location.Pos;

        if (Character.isLetter((char) Text.Ch)) {
            Ident();
        } else if (Character.isDigit((char)Text.Ch)) {
            Number();
        } else {
            switch (Text.Ch) {
                case ';':
                    Text.NextCh();
                    Lex = lexSemi;
                    break;
                case ':':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexAss;
                    } else {
                        Lex = lexColon;
                    }
                    break;
                case '.':
                    Text.NextCh();
                    Lex = lexDot;
                    break;
                case ',':
                    Text.NextCh();
                    Lex = lexComma;
                    break;
                case '=':
                    Text.NextCh();
                    Lex = lexEQ;
                    break;
                case '#':
                    Text.NextCh();
                    Lex = lexNE;
                    break;
                case '<':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexLE;
                    } else {
                        Lex = lexLT;
                    }
                    break;
                case '>':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexGE;
                    } else {
                        Lex = lexGT;
                    }
                    break;
                case '(':
                    Text.NextCh();
                    if (Text.Ch == '*') {
                        Comment();
                        NextLex();
                    } else {
                        Lex = lexLPar;
                    }
                    break;
                case ')':
                    Text.NextCh();
                    Lex = lexRPar;
                    break;
                case '+':
                    Text.NextCh();
                    Lex = lexPlus;
                    break;
                case '-':
                    Text.NextCh();
                    Lex = lexMinus;
                    break;
                case '*':
                    Text.NextCh();
                    Lex = lexMult;
                    break;
                case Text.chEOT:
                    Lex = lexEOT;
                    break;
                default:
                    Error.Message("Недопустимый символ");
            }
        }
    }


    // Инициализация сканнера
    static void Init() {
        InitChainHash();

        Add2ChainHash(H,    "ARRAY",                lexNone);

        Add2ChainHash(H,    "BEGIN",                lexBEGIN);
        Add2ChainHash(H,    "BY",                   lexNone);

        Add2ChainHash(H,    "CASE",                 lexNone);
        Add2ChainHash(H,    "CONST",                lexCONST);

        Add2ChainHash(H,    "DIV",                  lexDIV);
        Add2ChainHash(H,    "DO",                   lexDO);

        Add2ChainHash(H,    "ELSE",                 lexELSE);
        Add2ChainHash(H,    "ELSIF",                lexELSIF);
        Add2ChainHash(H,    "END",                  lexEND);
        Add2ChainHash(H,    "EXIT",                 lexNone);

        Add2ChainHash(H,    "FOR",                  lexNone);

        Add2ChainHash(H,    "IF",                   lexIF);
        Add2ChainHash(H,    "IMPORT",               lexIMPORT);
        Add2ChainHash(H,    "IN",                   lexNone);
        Add2ChainHash(H,    "IS",                   lexNone);

        Add2ChainHash(H,    "LOOP",                 lexNone);

        Add2ChainHash(H,    "MOD",                  lexMOD);
        Add2ChainHash(H,    "MODULE",               lexMODULE);

        Add2ChainHash(H,    "NIL",                  lexNone);

        Add2ChainHash(H,    "OF",                   lexNone);
        Add2ChainHash(H,    "OR",                   lexNone);

        Add2ChainHash(H,    "POINTER",              lexNone);
        Add2ChainHash(H,    "PROCEDURE",            lexNone);

        Add2ChainHash(H,    "RECORD",               lexNone);
        Add2ChainHash(H,    "REPEAT",               lexNone);
        Add2ChainHash(H,    "RETURN",               lexNone);

        Add2ChainHash(H,    "THEN",                 lexTHEN);
        Add2ChainHash(H,    "TO",                   lexNone);
        Add2ChainHash(H,    "TYPE",                 lexNone);

        Add2ChainHash(H,    "UNTIL",                lexNone);

        Add2ChainHash(H,    "VAR",                  lexVAR);

        Add2ChainHash(H,    "WHILE",                lexWHILE);
        Add2ChainHash(H,    "WITH",                 lexNone);

        NextLex();
    }
}