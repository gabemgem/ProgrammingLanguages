/* Generated By:JJTree&JavaCC: Do not edit this line. SalsaParserConstants.java */
package salsac;

public interface SalsaParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 9;
  int FORMAL_COMMENT = 10;
  int MULTI_LINE_COMMENT = 11;
  int ABSTRACT = 13;
  int AT = 14;
  int BEHAVIOR = 15;
  int BOOLEAN = 16;
  int BREAK = 17;
  int BYTE = 18;
  int CASE = 19;
  int CATCH = 20;
  int CHAR = 21;
  int CONST = 22;
  int CONTINUE = 23;
  int CURRENTCONTINUATION = 24;
  int _DEFAULT = 25;
  int DO = 26;
  int DOUBLE = 27;
  int ELSE = 28;
  int EXTENDS = 29;
  int FALSE = 30;
  int FINAL = 31;
  int FINALLY = 32;
  int FLOAT = 33;
  int FOR = 34;
  int GOTO = 35;
  int IF = 36;
  int IMPLEMENTS = 37;
  int IMPORT = 38;
  int INSTANCEOF = 39;
  int INT = 40;
  int INTERFACE = 41;
  int JOIN = 42;
  int LONG = 43;
  int MODULE = 44;
  int NATIVE = 45;
  int NEW = 46;
  int NULL = 47;
  int PRIVATE = 48;
  int PROTECTED = 49;
  int PUBLIC = 50;
  int RETURN = 51;
  int SHORT = 52;
  int STATIC = 53;
  int SUPER = 54;
  int SWITCH = 55;
  int SYNCHRONIZED = 56;
  int THIS = 57;
  int THROW = 58;
  int THROWS = 59;
  int _TOKEN = 60;
  int TRANSIENT = 61;
  int TRUE = 62;
  int TRY = 63;
  int VOID = 64;
  int VOLATILE = 65;
  int WHILE = 66;
  int INTEGER_LITERAL = 67;
  int DECIMAL_LITERAL = 68;
  int HEX_LITERAL = 69;
  int OCTAL_LITERAL = 70;
  int FLOATING_POINT_LITERAL = 71;
  int EXPONENT = 72;
  int CHARACTER_LITERAL = 73;
  int STRING_LITERAL = 74;
  int IDENTIFIER = 75;
  int LETTER = 76;
  int DIGIT = 77;
  int LPAREN = 78;
  int RPAREN = 79;
  int LBRACE = 80;
  int RBRACE = 81;
  int LBRACKET = 82;
  int RBRACKET = 83;
  int SEMICOLON = 84;
  int COMMA = 85;
  int DOT = 86;
  int MSG = 87;
  int CONT = 88;
  int TOKENNAMING = 89;
  int ASSIGN = 90;
  int GT = 91;
  int LT = 92;
  int BANG = 93;
  int TILDE = 94;
  int HOOK = 95;
  int COLON = 96;
  int EQ = 97;
  int LE = 98;
  int GE = 99;
  int NE = 100;
  int SC_OR = 101;
  int SC_AND = 102;
  int INCR = 103;
  int DECR = 104;
  int PLUS = 105;
  int MINUS = 106;
  int STAR = 107;
  int SLASH = 108;
  int BIT_AND = 109;
  int BIT_OR = 110;
  int XOR = 111;
  int REM = 112;
  int LSHIFT = 113;
  int RSIGNEDSHIFT = 114;
  int RUNSIGNEDSHIFT = 115;
  int PLUSASSIGN = 116;
  int MINUSASSIGN = 117;
  int STARASSIGN = 118;
  int SLASHASSIGN = 119;
  int ANDASSIGN = 120;
  int ORASSIGN = 121;
  int XORASSIGN = 122;
  int REMASSIGN = 123;
  int LSHIFTASSIGN = 124;
  int RSIGNEDSHIFTASSIGN = 125;
  int RUNSIGNEDSHIFTASSIGN = 126;

  int DEFAULT = 0;
  int IN_SINGLE_LINE_COMMENT = 1;
  int IN_FORMAL_COMMENT = 2;
  int IN_MULTI_LINE_COMMENT = 3;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"//\"",
    "<token of kind 7>",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "\"*/\"",
    "<token of kind 12>",
    "\"abstract\"",
    "\"at\"",
    "\"behavior\"",
    "\"boolean\"",
    "\"break\"",
    "\"byte\"",
    "\"case\"",
    "\"catch\"",
    "\"char\"",
    "\"const\"",
    "\"continue\"",
    "\"currentContinuation\"",
    "\"default\"",
    "\"do\"",
    "\"double\"",
    "\"else\"",
    "\"extends\"",
    "\"false\"",
    "\"final\"",
    "\"finally\"",
    "\"float\"",
    "\"for\"",
    "\"goto\"",
    "\"if\"",
    "\"implements\"",
    "\"import\"",
    "\"instanceof\"",
    "\"int\"",
    "\"interface\"",
    "\"join\"",
    "\"long\"",
    "\"module\"",
    "\"native\"",
    "\"new\"",
    "\"null\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"return\"",
    "\"short\"",
    "\"static\"",
    "\"super\"",
    "\"switch\"",
    "\"synchronized\"",
    "\"this\"",
    "\"throw\"",
    "\"throws\"",
    "\"token\"",
    "\"transient\"",
    "\"true\"",
    "\"try\"",
    "\"void\"",
    "\"volatile\"",
    "\"while\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"<-\"",
    "\"@\"",
    "\"->\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
  };

}
