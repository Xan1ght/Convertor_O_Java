package convertor.java;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class Converter {
    static boolean
        haveImportIn    = false,
        haveConst       = false,
        haveVar         = false;

    static String
        NameModule      = "";

    static int t = 1;
    static boolean isTab = true;


    static void TabAdd() {
        t++;
    }

    static void TabDel() {
        t--;
    }

    static void Tab() {
        for (int i = 0; i < t; i++) {
            System.out.print("\t");
        }
    }

    static void Import() {
        if (haveImportIn) {
            System.out.println("import java.util.Scanner;\n");
        }
    }


    static void Module() {
        System.out.println("public class " + NameModule + " {");
        Tab();
        System.out.println("public static void main(String[] args) {");
        TabAdd();

        while (Scan.Lex != Scan.lexCONST && Scan.Lex != Scan.lexVAR && Scan.Lex != Scan.lexBEGIN) {
            Scan.NextLex();
        }
    }

    static void Const() {
        if (haveConst) {
            Scan.NextLex();

            while (Scan.Lex != Scan.lexVAR && Scan.Lex != Scan.lexBEGIN) {
                Tab();
                System.out.print("final int " + Scan.Name);
                Scan.NextLex();
                Scan.NextLex();
                System.out.println(" = " + Scan.Num + ";");
                Scan.NextLex();
                Scan.NextLex();
            }
        }
    }

    static void Var() {
        if (haveVar) {
            Tab();
            System.out.print("int ");
            Scan.NextLex();
            System.out.print(Scan.Name);
            Scan.NextLex();

            while (Scan.Lex != Scan.lexBEGIN) {
                if (Scan.Lex == Scan.lexComma) {
                    Scan.NextLex();
                    System.out.print(", " + Scan.Name);
                    Scan.NextLex();
                }

                if (Scan.Lex == Scan.lexColon) {
                    Scan.NextLex();
                    Scan.NextLex();
                    Scan.NextLex();
                }

                if (Scan.Lex  == Scan.lexName) {
                    Scan.NextLex();
                    System.out.print(", " + Scan.Name);
                    Scan.NextLex();
                    Scan.NextLex();
                }
            }

            System.out.println(";\n");
        }
    }

    static void Java() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Location.Path.substring(0, Location.Path.lastIndexOf('.'))+".java");
            PrintStream filePrintStream = new PrintStream(fileOutputStream);
            PrintStream originalOut = System.out;
            System.setOut(filePrintStream);

            Import();
            Module();
            Const();
            Var();

            Scan.NextLex();

            while (Scan.Lex != Scan.lexDot) {
                while (!Scan.Name.equals(NameModule)) {
                    if (Scan.Lex == Scan.lexName) {
                        if (isTab) {
                            Tab();
                        }

                        if (Scan.Name.equals("INC")) {
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print(Scan.Name + "++");
                            Scan.NextLex();
                            Scan.NextLex();
                        } else if (Scan.Name.equals("DEC")) {
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print(Scan.Name + "--");
                            Scan.NextLex();
                            Scan.NextLex();
                        } else if (Scan.Name.equals("ABS")) {
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print("Math.abs(" + Scan.Name + ")");
                            Scan.NextLex();
                            Scan.NextLex();
                        } else if (Scan.Name.equals("HALT")) {
                            System.out.print("System.exit(0)");
                            Scan.NextLex();
                        } else if (Scan.Name.equals("MIN")) {
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print("Math.min("+ Scan.Name);
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print(", "+ Scan.Name + ")");
                            Scan.NextLex();
                            Scan.NextLex();
                        } else if (Scan.Name.equals("MAX")) {
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print("Math.max("+ Scan.Name);
                            Scan.NextLex();
                            Scan.NextLex();
                            System.out.print(", "+ Scan.Name + ")");
                            Scan.NextLex();
                            Scan.NextLex();
                        } else if (Scan.Name.equals("In")) {
                            Scan.NextLex();
                            if (Scan.Lex == Scan.lexDot) {
                                Scan.NextLex();
                                if (Scan.Name.equals("Open")) {
                                    System.out.print("Scanner scanner = new Scanner(System.in)");
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    Scan.NextLex();
                                } else if (Scan.Name.equals("Int")) {
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    System.out.print(Scan.Name + " = scanner.nextInt();");
                                    Scan.NextLex();
                                    Scan.NextLex();
                                }
                            }
                        } else if ((Scan.Name.equals("Out"))) {
                            Scan.NextLex();
                            if (Scan.Lex == Scan.lexDot) {
                                Scan.NextLex();
                                if (Scan.Name.equals("Ln")) {
                                    System.out.print("System.out.println()");
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    Scan.NextLex();
                                } else if (Scan.Name.equals("Int")) {
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    String str = "" + Scan.Name;
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    if (Scan.Lex == Scan.lexName) {
                                        System.out.println("if (" + Scan.Name + " > 0) {");
                                        TabAdd();
                                        Tab();
                                        System.out.println("String.format(\"%\" + " + Scan.Name + " + \"d\", " + str + ");");
                                        TabDel();
                                        Tab();
                                        System.out.println("} else (" + Scan.Name + " != 0) {");
                                        TabAdd();
                                        Tab();
                                        System.out.println("String.format(\"%\" + " + Scan.Name + " + \"d\", " + str + ");");
                                        TabDel();
                                        Tab();
                                        System.out.println("}");
                                    } else {
                                        if (Scan.Num > 0) {
                                            System.out.println("System.out.print(\" \".repeat(" + Scan.Num + " - (\" \" + " + str + ").length()) + " + str + ");");
                                        } else {
                                            System.out.println("System.out.print("+ str + ");");
                                        }
                                    }
                                    Scan.NextLex();
                                    Scan.NextLex();
                                    Scan.NextLex();
                                }
                            }
                        } else {
                            System.out.print(Scan.Name);
                            Scan.NextLex();
                        }
                    } else if (Scan.Lex == Scan.lexNum) {
                        System.out.print(Scan.Num);
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexAss) {
                        isTab = false;
                        System.out.print(" = ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexPlus) {
                        isTab = false;
                        System.out.print(" + ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexMinus) {
                        isTab = false;
                        System.out.print(" - ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexMult) {
                        isTab = false;
                        System.out.print(" * ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexDIV) {
                        isTab = false;
                        System.out.print(" / ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexMOD) {
                        isTab = false;
                        System.out.print(" % ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexWHILE) {
                        Tab();
                        isTab = false;
                        System.out.print("while (");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexDO) {
                        System.out.println(") {");
                        TabAdd();
                        isTab = true;
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexEQ) {
                        isTab = false;
                        System.out.print(" == ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexNE) {
                        isTab = false;
                        System.out.print(" != ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexLE) {
                        isTab = false;
                        System.out.print(" <= ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexLT) {
                        isTab = false;
                        System.out.print(" < ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexGE) {
                        isTab = false;
                        System.out.print(" >= ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexGT) {
                        isTab = false;
                        System.out.print(" > ");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexIF) {
                        Tab();
                        isTab = false;
                        System.out.print("if (");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexELSIF) {
                        Tab();
                        isTab = false;
                        System.out.print("} elsif (");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexELSE) {
                        Tab();
                        System.out.print("} else {");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexTHEN) {
                        System.out.println(") {");
                        TabAdd();
                        isTab = true;
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexSemi) {
                        isTab = true;
                        System.out.println(";");
                        Scan.NextLex();
                    } else if (Scan.Lex == Scan.lexEND) {
                        Scan.NextLex();
                        TabDel();
                        isTab = true;
                        if (Scan.Lex == Scan.lexSemi) {
                            Tab();
                            System.out.println("}");
                            Scan.NextLex();
                        }
                    }
                }
                Scan.NextLex();
            }
            Tab();
            System.out.println("}");
            System.out.println("}");

            filePrintStream.close();
            fileOutputStream.close();

            System.setOut(originalOut);

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println();
        System.out.println("Конвертация завершена");
    }
}
