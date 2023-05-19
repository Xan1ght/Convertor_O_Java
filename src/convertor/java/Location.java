package convertor.java;

// Текущая позиция в исходном тексте
class Location {
    static int Line;        // Номен строки
    static int Pos;         // Номер символа в строке
    static int ErrorPos;    // Номер символа в строке для ошибки
    static int LexPos;      // Позиция начала лексемы
    static String Path;     // Путь к файлу
}