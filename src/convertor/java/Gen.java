package convertor.java;

// ������� ����
class Gen {

static int PC;

static void Init() {
   PC = 0;
}

static void Cmd(int Cmd) {
   if( PC < OVM.MEMSIZE ) {
      OVM.M[PC] = Cmd;
      PC = PC + 1;
    //  System.out.println(PC);
   }
   else
      Error.Message("�������筮 ����� ��� ����");
}

static void Fixup(int A) {
   while( A > 0 ) {
      int temp = OVM.M[A-2];
      OVM.M[A-2] = PC;
      A = temp;
   }
}

static void Abs() {
   Cmd(OVM.cmDup);
   Cmd(0);
   Cmd(PC+3);
   Cmd(OVM.cmIfGE);
   Cmd(OVM.cmNeg);
}

static void Min() {
   Cmd(Integer.MAX_VALUE);
   Cmd(OVM.cmNeg);
   Cmd(1);
   Cmd(OVM.cmSub);
}

static void Odd() {
   Cmd(2);
   Cmd(OVM.cmMod);
   Cmd(1);
   Cmd(0); // ���� ���室� ���।
   Cmd(OVM.cmIfNE);
}

static void Const(int C) {
   Cmd(Math.abs(C));
   if ( C < 0 ) {
      Cmd(OVM.cmNeg);
   }
}

static void Comp(int Lex) {
   Cmd(0); // ���� ���室� ���।
   switch( Lex ) {
   case Scan.lexEQ : Cmd(OVM.cmIfNE); break;
   case Scan.lexNE : Cmd(OVM.cmIfEQ); break;
   case Scan.lexLE : Cmd(OVM.cmIfGT); break;
   case Scan.lexLT : Cmd(OVM.cmIfGE); break;
   case Scan.lexGE : Cmd(OVM.cmIfLT); break;
   case Scan.lexGT : Cmd(OVM.cmIfLE); break;
   }
}

static void Addr(Obj X) {
   Cmd(X.Val);   // � ⥪���� �祩�� ���� �।��饩 + 2
   X.Val = PC+1; // ����+2 = PC+1
  // System.out.println(PC);
}

static void AllocateVariables() {
   Obj VRef; // ��뫪� �� ��६����� � ⠡��� ����

   VRef = Table.FirstVar(); // ���� ����� ��६�����            
   while( VRef != null ) {
      if ( VRef.Val == 0 )
         Error.Warning(
            "Переменная " + VRef.Name + " не используется"
         );
      else if( PC < OVM.MEMSIZE ) {
         Fixup(VRef.Val);   // Адресная привязка
         PC++;
         }
      else
         Error.Message("�������筮 ����� ��� ��६�����");
      VRef = Table.NextVar();
     // System.out.println(PC);// Найти след. переменную
   }
}

}

