grammar Mxstar;

compilationUnit
    :   definition* EOF
    ;

definition
    : variableDeclaration
    | functionDefinition
    | classDefinition
    ;

variableDeclaration
    :   typeSpecifier name=Identifier ( '=' expression)? ';'
    ;

baseTypeSpecifier
    :   primitiveType
    |   Identifier
    ;

primitiveType
    :   Int
    |   Bool
    |   String
    |   Void
    ;

arrayTypeSpecifier
    :   baseTypeSpecifier ('[' ']')+
    ;

typeSpecifier
    :   arrayTypeSpecifier
    |   baseTypeSpecifier
    ;

functionDefinition
    :   returnType=typeSpecifier functionName=Identifier
        '(' parameter? ')' block
    ;

parameter
    :   typeSpecifier Identifier (',' typeSpecifier Identifier)*
    ;

block
    :   '{' statement* '}'
    ;

statement
    :   block
    |   ';'
    |   variableDeclaration
    |   expression ';'
    |   selectionStatement
    |   iterationStatement
    |   jumpStatement
    ;

selectionStatement
    :   If '(' cond=expression ')' thenBody=statement
        (Else elseBody=statement)?
    ;

iterationStatement
    :   While '(' cond=expression ')' statement
    |   For '(' initialization=expression? ';'
                cond=expression? ';'
                step=expression? ')' statement
    ;

jumpStatement
    :   Continue ';'
    |   Break ';'
    |   Return expression? ';'
    ;

classDefinition
    :   Class name=Identifier '{' (constructor | variableDeclaration | functionDefinition)* '}'
    ;

constructor
    :   name=Identifier '(' ')' block
    ;

expressionList
    :   expression (',' expression)*
    ;

expression
    :   expression op=('++' | '--')                  # PostfixExpr    // Precedence 1
    |   expression '(' expressionList? ')'           # FuncallExpr
    |   expression '[' expression ']'                # ArefExpr
    |   expression '.' Identifier                    # MemberExpr

    |   <assoc=right> op=('++'|'--') expression      # UnaryExpr        // Precedence 2
    |   <assoc=right> op=('+' | '-') expression      # UnaryExpr
    |   <assoc=right> op=('!' | '~') expression      # UnaryExpr
    |   <assoc=right> 'new' creator                  # New

    |   expression op=('*' | '/' | '%') expression   # BinaryExpr       // Precedence 3
    |   expression op=('+' | '-') expression         # BinaryExpr       // Precedence 4
    |   expression op=('<<'|'>>') expression         # BinaryExpr       // Precedence 5
    |   expression op=('<' | '>') expression         # BinaryExpr       // Precedence 6
    |   expression op=('<='|'>=') expression         # BinaryExpr
    |   expression op=('=='|'!=') expression         # BinaryExpr       // Precedence 7
    |   expression op='&' expression                 # BinaryExpr       // Precedence 8
    |   expression op='^' expression                 # BinaryExpr       // Precedence 9
    |   expression op='|' expression                 # BinaryExpr       // Precedence 10
    |   expression op='&&' expression                # BinaryExpr       // Precedence 11
    |   expression op='||' expression                # BinaryExpr       // Precedence 12

    |   <assoc=right> expression op='=' expression   # BinaryExpr       // Precedence 14

    |   'this'                                       # SelfPointer
    |   literal                                      # LiteralExpr
    |   Identifier                                   # Var
    |   '(' expression ')'                           # SubExpression
    ;

creator
    :   type=('int' | 'bool' | 'string' | Identifier)
                    ('[' expression ']')+
                    ('[' ']')+
                    ('[' expression ']')+            # ErrorCreator
    |   type=('int' | 'bool' | 'string' | Identifier)
                    ('[' expression ']')+
                    ('[' ']')?                       # ArrayCreator
    |   type=Identifier                              # nonArrayCreator
    ;


literal
    :   'null'                    # Null
    |   type = ('true' | 'false') # Bool
    |   IntegerConstant           # Integer
    |   StringLiteral             # String
    ;


// Reserved words
Auto : 'auto';
Bool : 'bool';
Break : 'break';
Class : 'class';
Continue : 'continue';
Else : 'else';
For : 'for';
If : 'if';
Int : 'int';
Long : 'long';
New : 'new';
fragment True : 'true';
fragment False : 'false';
Return : 'return';
String: 'string';
This : 'this';
Void : 'void';
While : 'while';

LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Less : '<';
LessEqual : '<=';
Greater : '>';
GreaterEqual : '>=';
Equal : '==';
NotEqual : '!=';

Plus : '+';
PlusPlus : '++';
Minus : '-';
MinusMinus : '--';
Star : '*';
Div : '/';
Mod : '%';

AndAnd : '&&';
OrOr : '||';
Not : '!';

LeftShift : '<<';
RightShift : '>>';
Tilde : '~';
Or : '|';
Caret : '^';
And : '&';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';

Arrow : '->';
Dot : '.';

Identifier
    :   IdentifierNondigit
        (   IdentifierNondigit
        |   Digit
        )*
    ;


fragment
IdentifierNondigit
    :   Nondigit
    //|   // other implementation-defined characters...
    ;

fragment
Nondigit
    :   [a-zA-Z_]
    ;

fragment
Digit
    :   [0-9]
    ;

IntegerConstant
    :   [0]
    |   [1-9] [0-9]*
    ;

fragment
DecimalConstant
    :   NonzeroDigit Digit*
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;

fragment
Sign
    :   '+' | '-'
    ;

DigitSequence
    :   Digit+
    ;

StringLiteral
    :   '"' SChar* '"'
    ;
  
fragment
SChar
    :   ~["\\\n\r]
    |   '\\' ["n\\]
    ;

// ignore the lines generated by c preprocessor                                   
// sample line : '#line 1 "/home/dm/files/dk1.h" 1'                           
LineAfterPreprocessing
    :   '#line' Whitespace* ~[\r\n]*
        -> skip
    ;  

LineDirective
    :   '#' Whitespace? DecimalConstant Whitespace? StringLiteral ~[\r\n]*
        -> skip
    ;

PragmaDirective
    :   '#' Whitespace? 'pragma' Whitespace ~[\r\n]*
        -> skip
    ;

Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;