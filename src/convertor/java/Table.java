package convertor.java;

// ќ«Ґ¬Ґ­в в Ў«Ёжл Ё¬Ґ­
class Obj {       // ’ЁЇ § ЇЁбЁ в Ў«Ёжл Ё¬Ґ­
   String Name;   // Љ«оз Ї®ЁбЄ             
   int Cat;       // Љ вҐЈ®аЁп Ё¬Ґ­Ё        
   int Typ;       // ’ЁЇ                    
   int Val;       // ‡­ зҐ­ЁҐ               
   Obj Prev;      // “Є § вҐ«м ­  ЇаҐ¤. Ё¬п 
}

// ’ Ў«Ёж  Ё¬Ґ­
class Table {

// Љ вҐЈ®аЁЁ Ё¬с­
   static final int 
      catConst  = 1, catVar    = 2, 
      catType   = 3, catStProc = 4,
      catModule = 5, catGuard  = 6;

// ’ЁЇл
   static final int 
      typNone = 0, typInt = 1, typBool = 2;

private static Obj Top;    //“Є § вҐ«м ­  ўҐаиЁ­г бЇЁбЄ     
private static Obj Bottom; //“Є § вҐ«м ­  Є®­Ґж бЇЁбЄ 
private static Obj CurrObj;

// €­ЁжЁ «Ё§ жЁп в Ў«Ёжл
static void Init() {
   Top = null;
}

// „®Ў ў«Ґ­ЁҐ н«Ґ¬Ґ­в 
static void Enter(String N, int C, int T, int V) {
   Obj P = new Obj();
   if (N == "Out.Ln"){
      System.out.println();
   }
   P.Name = new String(N);
   P.Cat = C;
   P.Typ = T;
   P.Val = V;
   P.Prev = Top;
   Top = P;
   //System.out.println(Top.Val);
}

static void OpenScope() {
   Enter("", catGuard, typNone, 0);
   if ( Top.Prev == null )
      Bottom = Top;
}

static void CloseScope() {
   while( Top.Cat != catGuard ){
      Top = Top.Prev;
   }
   Top = Top.Prev;
}

static Obj NewName(String Name, int Cat) {
   if (Name == "Out.Ln"){
      System.out.println();
   }
   Obj obj = Top;
   while(
      obj.Cat != catGuard && 
      obj.Name.compareTo(Name) != 0 
   )
      obj = obj.Prev;
   if ( obj.Cat == catGuard ) {
      obj = new Obj();
      obj.Name = new String(Name);
      obj.Cat = Cat;
      obj.Val = 0;
      obj.Prev = Top;
      Top = obj;
      }
   else
      Error.Message("Џ®ўв®а­®Ґ ®Ўкпў«Ґ­ЁҐ Ё¬Ґ­Ё");
   return obj;
}

static Obj Find(String Name) {
   Obj obj;

   Bottom.Name = new String(Name);
   obj=Top;
//   System.out.println(Top.Val);
   for( obj=Top; obj.Name.compareTo(Name)!=0; obj=obj.Prev );
   if( obj == Bottom )
      Error.Message("ЌҐ®Ўкпў«Ґ­­®Ґ Ё¬п");

   //System.out.println(obj.Val);
   return obj;
}

static Obj FirstVar() {
   CurrObj = Top;
   return NextVar();
}

static Obj NextVar() {
   Obj VRef;

   while( CurrObj != Bottom && CurrObj.Cat != catVar )
      CurrObj = CurrObj.Prev;
   if( CurrObj == Bottom )
      return null;
   else {
      VRef = CurrObj;
      CurrObj = CurrObj.Prev;
      return VRef;
   }
}

}
