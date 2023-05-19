package convertor.java;


/*
    Хлюпин Дмитрий, ПМ-21
    Использованная структура: Перемешанная таблица с цепочками
    Анализируемый язык: Си++
*/

// Лексический анализатор
class Scanner {

    static int NAMELEN = 100;    // Наибольшая длина имени
    static int N = 127;         // Объем таблицы, где 70% заполняется
    static int NMAX = 182;      // Количество всех лексем

    final static int
        lexNone = 0,
        lexName = 1,
        lexCharacter = 2,
        lexString = 3,

        lexNumInt = 4,

        lexNum = 5,             /* число-в10 */     lexNumOct = 17,         /* ч-в8 */      lexNumHex = 23,             /* ч-в16 */
        lexNumU = 6,            /* ч-в10-U */       lexNumOctU = 18,        /* ч-в8-U */    lexNumHexU = 24,            /* ч-в16-U */
        lexNumUL = 7,           /* ч-в10-UL */      lexNumOctUL = 19,       /* ч-в8-UL */   lexNumHexUL = 25,           /* ч-в16-UL */
        lexNumULL = 8,          /* ч-в10-ULL */     lexNumOctULL = 20,      /* ч-в8-ULL */  lexNumHexULL = 26,          /* ч-в16-ULL */
        lexNumL = 9,            /* ч-в10-L */       lexNumOctL = 21,        /* ч-в8-L */    lexNumHexL = 27,            /* ч-в16-L */
        lexNumLL = 10,          /* ч-в10-LL */      lexNumOctLL = 22,       /* ч-в8-LL */   lexNumHexLL = 28,           /* ч-в16-LL */
        lexNumBin = 11,         /* ч-вbin */        lexNumBinL = 15,        /* ч-вbin-L */  lexNumBinLL = 16,           /* ч-вbin-LL */
        lexNumBinU = 12,        /* ч-вbin-U */      lexNumBinUL = 13,       /* ч-вbin-UL */ lexNumBinULL = 14,          /* ч-вbin-ULL */

        lexNumReal = 29,

        lexNumFloat = 30,       /* ч-float */       lexNumDouble = 31,      /* ч-double */  lexNumDoubleL = 32,         /* ч-double-L */

        lexArray = 36,          /* ARRAY */         lexBegin = 39,          /* BEGIN */     lexBy = 40,                 /* BY */
        lexCase = 43,           /* CASE */          lexConst = 48,          /* CONST */     lexDiv = 49,                /* DIV */
        lexDo = 57,             /* DO */            lexElse = 60,           /* ELSE */      lexElsif = 61,              /* ELSIF */
        lexEnd = 63,            /* END */           lexExit = 64,           /* EXIT */      lexFor = 68,                /* FOR */
        lexIf = 70,             /* IF */            lexImport = 71,         /* IMPORT */    lexIn = 69,                 /* IN */
        lexIs = 72,             /* IS */            lexLoop = 73,           /* LOOP */      lexMod = 74,                /* MOD */
        lexModule = 75,         /* MODULE */        lexNil = 76,            /* NIL */       lexOf = 77,                 /* OF */
        lexOr = 82,             /* OR */            lexPointer = 85,        /* POINTER */   lexProcedure = 86,          /* PROCEDURE */
        lexRecord = 87,         /* RECORD */        lexRepeat = 88,         /* REPEAT */    lexReturn = 90,             /* RETURN */
        lexThen = 105,          /* THEN */          lexTo = 106,            /* TO */        lexType = 107,              /* TYPE */
        lexUntil = 108,         /* UNTIL */         lexVar = 111,           /* VAR */       lexWhile = 115,             /* WHILE */
        lexWith = 108,          /* WITH */

        lexPlus = 116,          /* + */             lexMinus = 119,         /* - */
        lexStar = 122,          /* * */             lexSlash = 124,         /* / */
        lexCaret = 128,         /* ^ */
        lexTilde = 160,         /* ~ */
        lexAmpersand = 130,     /* & */             lexPipe = 133,          /* | */
        lexAssign = 139,        /* = */             lexHash = 158,          /* # */
        lexColon = 141,         /* : */             lexColon_Eq = 141,      /* := */        lexSemicolon = 143,         /* ; */
        lexDot = 144,           /* . */             lexDouble_Dot = 146,    /* .. */        lexComma = 147,             /* , */
        lexLess = 148,          /* < */             lexLess_Eq = 149,       /* <= */
        lexGreater = 152,       /* > */             lexGreater_Eq = 153,    /* >= */
        lexOpen_Paren = 161,    /* ( */             lexClose_Paren = 162,   /* ) */
        lexOpen_Brace = 163,    /* { */             lexClose_Brace = 164,   /* } */
        lexOpen_Bracket = 165,  /* [ */             lexClose_Bracket = 166, /* ] */

        lexBackslash_Alert_Or_Bell = 167,       /* \a */
        lexBackslash_Backspace = 168,           /* \b */
        lexBackslash_Escape = 169,              /* \e */
        lexBackslash_Form_Feed = 170,           /* \f */
        lexBackslash_Newline = 171,             /* \n */
        lexBackslash_Carriage_Return = 172,     /* \r */
        lexBackslash_Tab = 173,                 /* \t */
        lexBackslash_Vertical_Tab = 174,        /* \v */
        lexBackslash_Backslash = 175,           /* \\ */
        lexBackslash_Question_Mark = 176,       /* \? */
        lexBackslash_Single_Quote =177,         /* \' */
        lexBackslash_Double_Quote = 178,        /* \" */
        lexBackslash_Octal_Value = 179,         /* \ooo */
        lexBackslash_Hexadecimal_Value = 180,   /* \hhh */
        lexBackslash_Null_Character = 181,      /* \0 */

        lexEOT = 182;               /* \0 */


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
            if (Text.Ch != '_') {
                if (i++ < NAMELEN) {
                    Buf.append((char) Text.Ch);
                } else {
                    Error.Message("Слишком длинное имя");
                }

                Text.NextCh();
                BackSlash();
            }

            if (Text.Ch == '_') {
                if (i++ < NAMELEN) {
                    Buf.append((char) Text.Ch);
                } else {
                    Error.Message("Слишком длинное имя");
                }
                Text.NextCh();
                BackSlash();
                if (Text.Ch == '_') {
                    if (i++ < NAMELEN) {
                        Buf.append((char) Text.Ch);
                    } else {
                        Error.Message("Слишком длинное имя");
                    }
                    Error.Warning("Двойное подчеркивание в идентификаторах зарезервировано в С++");
                    Text.NextCh();
                    BackSlash();
                }
            }

            BackSlash();
        } while (Character.isLetterOrDigit((char)Text.Ch));


        Name = Buf.toString();  // Полученное слово из строки
        Lex = TestKW();         // Проверка на ключевое слово
    }


    // Нахождение числа (бинарное, восьмеричное, десятичное, шестнадцатеричное и все суффиксы)
    private static void Number() {
        Lex = lexNum;
        Num = 0;

        if (Text.Ch == '0') {                                   // Проверка наличия у первого числа "0":
            Text.NextCh();
            BackSlash();
            if (Text.Ch == 'b' || Text.Ch == 'B') {                 // (1) Если "0b", значит число "должно быть" в bin
                Text.NextCh();
                BackSlash();
                if (Text.Ch == '0' || Text.Ch == '1') {             // "Должно быть"
                    BinNumber();
                    if (SearchSuffixU()) {                          // Если есть в конце U, то unsigned +
                        if (SearchSuffixL()) {                      // Если есть в конце L, то unsigned long +
                            if (SearchSuffixL()) {                  // Если есть в конце L, то unsigned long long.
                                Lex = lexNumBinULL;
                            } else {
                                Lex = lexNumBinUL;
                            }
                        } else {
                            Lex = lexNumBinU;
                        }
                    } else if (SearchSuffixL()) {                   // Если нет  в конце U, но есть L, то long +
                        if (SearchSuffixU()) {                      // Если есть в конце U, то то unsigned long +
                            Lex = lexNumBinUL;
                        } else if (SearchSuffixL()) {               // Если есть в конце L, то long long +
                            if (SearchSuffixU()) {                  // Если есть в конце U, то то unsigned long long.
                                Lex = lexNumBinULL;
                            } else {
                                Lex = lexNumBinLL;
                            }
                        } else {
                            Lex = lexNumBinL;
                        }
                    } else {                                        // Если нет  в конце U и L, то просто в bin.
                        Lex = lexNumBin;
                    }
                } else {
                    Error.Expected("символ бинарного числа");
                }
            } else if (Text.Ch == 'x' || Text.Ch == 'X') {          // (2) Если "0х", значит число "должно быть" в 16
                Text.NextCh();
                BackSlash();
                if ((Text.Ch >= '0' && Text.Ch <= '9') ||           // "Должно быть"
                        (Text.Ch >= 'A' && Text.Ch <= 'F') ||
                        (Text.Ch >= 'a' && Text.Ch <= 'f')) {
                    HexNumber();
                    if (SearchSuffixU()) {                          // Если есть в конце U, то unsigned +
                        if (SearchSuffixL()) {                      // Если есть в конце L, то unsigned long +
                            if (SearchSuffixL()) {                  // Если есть в конце L, то unsigned long long.
                                Lex = lexNumHexULL;
                            } else {
                                Lex = lexNumHexUL;
                            }
                        } else {
                            Lex = lexNumHexU;
                        }
                    } else if (SearchSuffixL()) {                   // Если нет  в конце U, но есть L, то long +
                        if (SearchSuffixU()) {                      // Если есть в конце U, то то unsigned long +
                            Lex = lexNumHexUL;
                        } else if (SearchSuffixL()) {               // Если есть в конце L, то long long +
                            if (SearchSuffixU()) {                  // Если есть в конце U, то то unsigned long long.
                                Lex = lexNumHexULL;
                            } else {
                                Lex = lexNumHexLL;
                            }
                        } else {
                            Lex = lexNumHexL;
                        }
                    } else {                                        // Если нет  в конце U и L, то просто в 16.
                        Lex = lexNumHex;
                    }
                } else {
                    Error.Expected("символ шестнадцатеричного числа");
                }
            } else if (Character.isDigit((char)Text.Ch)) {          // (3) Если "0 с цифрой", значит число "должно быть" в 8
                OctNumber();                                        // Просто в 8
                if (SearchSuffixU()) {                              // Если есть в конце U, то unsigned +
                    if (SearchSuffixL()) {                          // Если есть в конце L, то unsigned long +
                        if (SearchSuffixL()) {                      // Если есть в конце L, то unsigned long long.
                            Lex = lexNumOctULL;
                        } else {
                            Lex = lexNumOctUL;
                        }
                    } else {
                        Lex = lexNumOctU;
                    }
                } else if (SearchSuffixL()) {                       // Если нет  в конце U, но есть L, то long +
                    if (SearchSuffixU()) {                          // Если есть в конце U, то то unsigned long +
                        Lex = lexNumOctUL;
                    } else if (SearchSuffixL()) {                   // Если есть в конце L, то long long +
                        if (SearchSuffixU()) {                      // Если есть в конце U, то то unsigned long long.
                            Lex = lexNumOctULL;
                        } else {
                            Lex = lexNumOctLL;
                        }
                    } else {
                        Lex = lexNumOctL;
                    }
                } else {                                            // Если нет  в конце U и L, то просто в 8.
                    Lex = lexNumOct;
                }
            } else if (Text.Ch == '.') {                            // (4) Если "0.", значит число должно быть в double/float
                DecNumber();                                        // Просто в 10
                if (SearchSuffixE()) {                              // Если есть E, то 100% double/float + "должно быть"
                    if (SearchSuffixL()) {                          // Если есть в конце L, то long.
                        Lex = lexNumDoubleL;
                    } else if (SearchSuffixF()) {                   // Если нет  в конце L, но есть F, то float.
                        Lex = lexNumFloat;
                    } else {                                        // Если нет  в конце L и F, то просто в double.
                        Lex = lexNumDouble;
                    }
                } else if (SearchSuffixL()) {                       // Если нет E, но есть в конце L, то long.
                    Lex = lexNumDoubleL;
                } else if (SearchSuffixF()) {                       // Если нет E, но есть в конце F, то float.
                    Lex = lexNumFloat;
                } else {                                            // Если нет E, и нет   в конце L и F, то просто в double.
                    Lex = lexNumDouble;
                }
            } else {                                                // (5) Если "0" и только, значит число должно быть в 10\double\float
                if (SearchSuffixE()) {                              // Если есть E, то 100% double/float + проверка ошибки
                    if (SearchSuffixL()) {                          // Если есть в конце L, то long.
                        Lex = lexNumDoubleL;
                    } else if (SearchSuffixF()) {                   // Если нет  в конце L, но есть F, то float.
                        Lex = lexNumFloat;
                    } else {                                        // Если нет  в конце L и F, то просто в double.
                        Lex = lexNumDouble;
                    }
                } else if (SearchSuffixU()) {                       // Если есть в конце U, то unsigned +
                    if (SearchSuffixL()) {                          // Если есть в конце L, то unsigned long +
                        if (SearchSuffixL()) {                      // Если есть в конце L, то unsigned long long.
                            Lex = lexNumULL;
                        } else {
                            Lex = lexNumUL;
                        }
                    } else {
                        Lex = lexNumU;
                    }
                } else if (SearchSuffixL()) {                       // Если нет  в конце U, но есть L, то long +
                    if (SearchSuffixL()) {                          // Если есть в конце L, то long long.
                        Lex =lexNumLL;
                    } else {
                        Lex = lexNumL;
                    }
                } else {                                            // Если нет  в конце U и L, то просто в 10.
                    Lex = lexNum;
                }
            }
        } else {                                                // Проверка если будет любое число:
            DecNumber();                                            // "Должно быть" в 10
            if (SearchSuffixE()) {                                  // Если есть E, то 100% double/float + проверка ошибки
                if (SearchSuffixL()) {                              // Если есть в конце L, то long.
                    Lex = lexNumDoubleL;
                } else if (SearchSuffixF()) {                       // Если нет  в конце L, но есть F, то float.
                    Lex = lexNumFloat;
                } else {                                            // Если нет  в конце L и F, то просто в double.
                    Lex = lexNumDouble;
                }
            } else if (SearchSuffixU()) {                           // Если есть в конце U, то unsigned +
                if (SearchSuffixL()) {                              // Если есть в конце L, то unsigned long +
                    if (SearchSuffixL()) {                          // Если есть в конце L, то unsigned long long.
                        Lex = lexNumULL;
                    } else {
                        Lex = lexNumUL;
                    }
                } else {
                    Lex = lexNumU;
                }
            } else if (SearchSuffixL()) {                           // Если нет  в конце U, но есть L, то long +
                if (SearchSuffixU()) {                              // Если есть в конце U, то то unsigned long +
                    Lex = lexNumUL;
                } else if (SearchSuffixL()) {                       // Если есть в конце L, то long long +
                    if (SearchSuffixU()) {                          // Если есть в конце U, то то unsigned long long.
                        Lex = lexNumULL;
                    } else {
                        Lex = lexNumLL;
                    }
                } else {
                    Lex = lexNumL;
                }
            } else if (Text.Ch == '.') {
                DecNumber();
                if (SearchSuffixE()) {                              // Если есть E, то 100% double/float + проверка ошибки
                    if (SearchSuffixL()) {                          // Если есть в конце L, то long.
                        Lex = lexNumDoubleL;
                    } else if (SearchSuffixF()) {                   // Если нет  в конце L, но есть F, то float.
                        Lex = lexNumFloat;
                    } else {                                        // Если нет  в конце L и F, то просто в double.
                        Lex = lexNumDouble;
                    }
                } else if (SearchSuffixL()) {                       // Если нет E, но есть в конце L, то long.
                    Lex = lexNumDoubleL;
                } else if (SearchSuffixF()) {                       // Если нет E, но есть в конце F, то float.
                    Lex = lexNumFloat;
                } else {                                            // Если нет E, и нет   в конце L и F, то просто в double.
                    Lex = lexNumDouble;
                }
            } else {                                            // Если нет  в конце U и L, то просто в 10.
                Lex = lexNum;
            }
        }
    }


    // Бинарное число
    private static void BinNumber() {
        do {
            Text.NextCh();
            BackSlash();
        } while (Text.Ch == '0' || Text.Ch == '1');
    }

    // Восьмеричное число
    private static void OctNumber() {
        do {
            Text.NextCh();
            BackSlash();
        } while (Text.Ch >= '0' && Text.Ch <= '7');
    }

    // Шестнадцатеричное число
    private static void HexNumber() {
        do {
            Text.NextCh();
            BackSlash();
        } while ((Text.Ch >= '0' && Text.Ch <= '9') || (Text.Ch >= 'A' && Text.Ch <= 'F') || (Text.Ch >= 'a' && Text.Ch <= 'f'));
    }

    // Десятичное число
    private static void DecNumber() {
        do {
            Text.NextCh();
            BackSlash();
        } while (Character.isDigit((char)Text.Ch));
    }


    // Поиск суффикса Е
    private static boolean SearchSuffixE() {
        if (Text.Ch == 'E' || Text.Ch == 'e') {
            Text.NextCh();
            BackSlash();

            if (Text.Ch == '-') {
                Text.NextCh();
            } else if (Text.Ch == '+') {
                Text.NextCh();
            }

            BackSlash();

            if (Character.isDigit((char)Text.Ch)) {
                DecNumber();
            } else {
                Error.Expected("символ после суффикса");
            }
            return true;
        } else {
            return false;
        }
    }

    // Поиск суффикса F
    private static boolean SearchSuffixF() {
        if (Text.Ch == 'F' || Text.Ch == 'f') {
            Text.NextCh();
            BackSlash();
            return true;
        } else {
            return false;
        }
    }

    // Поиск суффикса U
    private static boolean SearchSuffixU() {
        if (Text.Ch == 'U' || Text.Ch == 'u') {
            Text.NextCh();
            BackSlash();
            return true;
        } else {
            return false;
        }
    }

    // Поиск суффикса L
    private static boolean SearchSuffixL() {
        if (Text.Ch == 'L' || Text.Ch == 'l') {
            Text.NextCh();
            BackSlash();
            return true;
        } else {
            return false;
        }
    }

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

    // Нахождение символа (не более 1 байта)
    private static void Character() {
        Text.NextCh();

        if (Text.Ch == '\\') {
            Text.NextCh();
            if (Text.Ch == 'n') {
                Text.NextCh();
            } else if (Text.Ch == 't') {
                Text.NextCh();
            } else if (Text.Ch == 'v') {
                Text.NextCh();
            } else if (Text.Ch == 'b') {
                Text.NextCh();
            } else if (Text.Ch == 'r') {
                Text.NextCh();
            } else if (Text.Ch == 'f') {
                Text.NextCh();
            } else if (Text.Ch == 'a') {
                Text.NextCh();
            } else if (Text.Ch == '\\') {
                Text.NextCh();
            } else if (Text.Ch == '?') {
                Text.NextCh();
            } else if (Text.Ch == '\'') {
                Text.NextCh();
            } else if (Text.Ch == '\"') {
                Text.NextCh();
            } else if (Text.Ch >= '0' && Text.Ch <= '3') {
                Text.NextCh();
                BackSlash();
                if (Text.Ch >= '0' && Text.Ch <= '7') {
                    Text.NextCh();
                    BackSlash();
                    if (Text.Ch >= '0' && Text.Ch <= '7') {
                        Text.NextCh();
                        BackSlash();
                    }
                }
            } else if (Text.Ch >= '4' && Text.Ch <= '7') {
                Text.NextCh();
                BackSlash();
                if (Text.Ch >= '0' && Text.Ch <= '7') {
                    Text.NextCh();
                    BackSlash();
                }
            } else if (Text.Ch == 'x') {
                Text.NextCh();
                BackSlash();
                if (!((Text.Ch >= '0' && Text.Ch <= '9') || (Text.Ch >= 'A' && Text.Ch <= 'F') || (Text.Ch >= 'a' && Text.Ch <= 'f'))) {
                    Error.Expected("символ шестнадцатеричного числа");
                }

                while (Text.Ch == '0') {
                    Text.NextCh();
                    BackSlash();
                }
                if ((Text.Ch >= '1' && Text.Ch <= '9') || (Text.Ch >= 'A' && Text.Ch <= 'F') || (Text.Ch >= 'a' && Text.Ch <= 'f')) {
                    Text.NextCh();
                    BackSlash();
                    if ((Text.Ch >= '0' && Text.Ch <= '9') || (Text.Ch >= 'A' && Text.Ch <= 'F') || (Text.Ch >= 'a' && Text.Ch <= 'f')) {
                        Text.NextCh();
                        BackSlash();
                    }
                }
            } else if (Text.Ch == '\n') {
                Text.NextCh();
                BackSlash();
                Text.NextCh();
            }
        } else if (Text.Ch == '\'') {
            Error.Expected("символ");
        } else {
            Text.NextCh();
        }

        BackSlash();

        if (Text.Ch != '\'') {
            Error.Expected("символ '");
        } else {
            Text.NextCh();
        }
    }


    // Нахождение строки
    private static void String() {
        do {
            Text.NextCh();
            if (Text.Ch == '\\') {
                Text.NextCh();
                if (Text.Ch == '\"') {
                    Text.NextCh();
                } else if (Text.Ch == Text.chEOL) {
                    Text.NextCh();
                    BackSlash();
                }
            }
        } while (Text.Ch != '\"' /*&& Text.Ch != '\\'*/ && Text.Ch != Text.chEOL && Text.Ch != Text.chEOT);

        if (Text.Ch == Text.chEOL || Text.Ch == Text.chEOT) {
            Error.Expected("символ строки");
//        } else if (Text.Ch == '\\') {
//            Text.NextCh();
//            if (Text.Ch == Text.chEOT) {
//                Error.Message("Не закончена строка");
//            } else {
//                String();
//            }
        } else {
            Text.NextCh();
        }
    }


    private static void BackSlash() {
        while (Text.Ch == '\\') {
            Text.NextCh();
            if (Text.Ch == Text.chEOL) {
                Text.NextCh();
            } else {
                Error.Expected("продолжение в следующей строке");
            }
        }
    }


    // Следующая лексема
    static void NextLex() {
        while (Text.Ch == Text.chSPACE || Text.Ch == Text.chTAB || Text.Ch == Text.chEOL) {
            Text.NextCh();
        }

        Location.LexPos = Location.Pos;

        if (Character.isLetter((char)Text.Ch)) {
            Ident();
        } else if (Text.Ch == '_') {
            Error.Warning("Не рекомендуется использовать подчеркивание в начале идентификатора в С++");
            Ident();
        } else if (Character.isDigit((char)Text.Ch)) {
            Number();
        } else {
            switch (Text.Ch) {
                case ';':
                    Text.NextCh();
                    Lex = lexSemicolon;
                    break;
                case ':':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexColon_Eq;
                    } else {
                        Lex = lexColon;
                    }
                    break;
                case '.':
                    Text.NextCh();
                    if (Text.Ch == '.') {
                        Text.NextCh();
                        Lex = lexDouble_Dot;
                    } else if (Character.isDigit((char)Text.Ch)) {
                        DecNumber();
                        if (SearchSuffixE()) {                              // Если есть E, то 100% double/float + проверка ошибки
                            if (SearchSuffixL()) {                          // Если есть в конце L, то long.
                                Lex = lexNumDoubleL;
                            } else if (SearchSuffixF()) {                   // Если нет  в конце L, но есть F, то float.
                                Lex = lexNumFloat;
                            } else {                                        // Если нет  в конце L и F, то просто в double.
                                Lex = lexNumDouble;
                            }
                        } else if (SearchSuffixL()) {                       // Если нет E, но есть в конце L, то long.
                            Lex = lexNumDoubleL;
                        } else if (SearchSuffixF()) {                       // Если нет E, но есть в конце F, то float.
                            Lex = lexNumFloat;
                        } else {                                            // Если нет E, и нет   в конце L и F, то просто в double.
                            Lex = lexNumDouble;
                        }
                    } else {
                        Lex = lexDot;
                    }
                    break;
                case ',':
                    Text.NextCh();
                    Lex = lexComma;
                    break;
                case '=':
                    Text.NextCh();
                    Lex = lexAssign;
                    break;
                case '<':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexLess_Eq;
                    } else {
                        Lex = lexLess;
                    }
                    break;
                case '>':
                    Text.NextCh();
                    if (Text.Ch == '=') {
                        Text.NextCh();
                        Lex = lexGreater_Eq;
                    } else {
                        Lex = lexGreater;
                    }
                    break;
                case '(':
                    Text.NextCh();
                    if (Text.Ch == '*') {
                        Comment();
                        NextLex();
                    } else {
                        Lex = lexOpen_Paren;
                    }
                    break;
                case ')':
                    Text.NextCh();
                    Lex = lexClose_Paren;
                    break;
                case '{':
                    Text.NextCh();
                    Lex = lexOpen_Brace;
                    break;
                case '}':
                    Text.NextCh();
                    Lex = lexClose_Brace;
                    break;
                case '[':
                    Text.NextCh();
                    Lex = lexOpen_Bracket;
                    break;
                case ']':
                    Text.NextCh();
                    Lex = lexClose_Bracket;
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
                    Lex = lexStar;
                    break;
                case '/':
                    Text.NextCh();
                    Lex = lexSlash;
                    break;
                case '&':
                    Text.NextCh();
                    Lex = lexAmpersand;
                    break;
                case '|':
                    Text.NextCh();
                    Lex = lexPipe;
                    break;
                case '^':
                    Text.NextCh();
                    Lex = lexCaret;
                    break;
                case '~':
                    Text.NextCh();
                    Lex = lexTilde;
                    break;
                case '#':
                    Text.NextCh();
                    Lex = lexHash;
                    break;
                case '\'':
                    Character();
                    Lex = lexCharacter;
                    break;
                case '\"':
                    if (Lex == lexString) {
                        String();
                        NextLex();
                    } else {
                        String();
                        Lex = lexString;
                    }
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

        Add2ChainHash(H,    "ARRAY",                lexArray);

        Add2ChainHash(H,    "BEGIN",                lexBegin);
        Add2ChainHash(H,    "BY",                   lexBy);

        Add2ChainHash(H,    "CASE",                 lexCase);
        Add2ChainHash(H,    "CONST",                lexConst);

        Add2ChainHash(H,    "DIV",                  lexDiv);
        Add2ChainHash(H,    "DO",                   lexDo);

        Add2ChainHash(H,    "ELSE",                 lexElse);
        Add2ChainHash(H,    "ELSIF",                lexElsif);
        Add2ChainHash(H,    "END",                  lexEnd);
        Add2ChainHash(H,    "EXIT",                 lexExit);

        Add2ChainHash(H,    "FOR",                  lexFor);

        Add2ChainHash(H,    "IF",                   lexIf);
        Add2ChainHash(H,    "IMPORT",               lexImport);
        Add2ChainHash(H,    "IN",                   lexIn);
        Add2ChainHash(H,    "IS",                   lexIs);

        Add2ChainHash(H,    "LOOP",                 lexLoop);

        Add2ChainHash(H,    "MOD",                  lexMod);
        Add2ChainHash(H,    "MODULE",               lexModule);

        Add2ChainHash(H,    "NIL",                  lexNil);

        Add2ChainHash(H,    "OF",                   lexOf);
        Add2ChainHash(H,    "OR",                   lexOr);

        Add2ChainHash(H,    "POINTER",              lexPointer);
        Add2ChainHash(H,    "PROCEDURE",            lexProcedure);

        Add2ChainHash(H,    "RECORD",               lexRecord);
        Add2ChainHash(H,    "REPEAT",               lexRepeat);
        Add2ChainHash(H,    "RETURN",               lexReturn);

        Add2ChainHash(H,    "THEN",                 lexThen);
        Add2ChainHash(H,    "TO",                   lexTo);
        Add2ChainHash(H,    "TYPE",                 lexType);

        Add2ChainHash(H,    "UNTIL",                lexUntil);

        Add2ChainHash(H,    "VAR",                  lexVar);

        Add2ChainHash(H,    "WHILE",                lexWhile);
        Add2ChainHash(H,    "WITH",                 lexWith);
    }
}