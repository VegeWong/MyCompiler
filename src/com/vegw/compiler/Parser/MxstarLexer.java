package com.vegw.compiler.Parser;// Generated from .\Mxstar.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxstarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, Auto=4, Bool=5, Break=6, Class=7, Continue=8, 
		Else=9, For=10, If=11, Int=12, Long=13, New=14, Return=15, String=16, 
		This=17, Void=18, While=19, LeftParen=20, RightParen=21, LeftBracket=22, 
		RightBracket=23, LeftBrace=24, RightBrace=25, Less=26, LessEqual=27, Greater=28, 
		GreaterEqual=29, Equal=30, NotEqual=31, Plus=32, PlusPlus=33, Minus=34, 
		MinusMinus=35, Star=36, Div=37, Mod=38, AndAnd=39, OrOr=40, Not=41, LeftShift=42, 
		RightShift=43, Tilde=44, Or=45, Caret=46, And=47, Question=48, Colon=49, 
		Semi=50, Comma=51, Assign=52, Arrow=53, Dot=54, Identifier=55, IntegerConstant=56, 
		DigitSequence=57, StringLiteral=58, LineAfterPreprocessing=59, LineDirective=60, 
		PragmaDirective=61, Whitespace=62, Newline=63, BlockComment=64, LineComment=65;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "Auto", "Bool", "Break", "Class", "Continue", 
		"Else", "For", "If", "Int", "Long", "New", "True", "False", "Return", 
		"String", "This", "Void", "While", "LeftParen", "RightParen", "LeftBracket", 
		"RightBracket", "LeftBrace", "RightBrace", "Less", "LessEqual", "Greater", 
		"GreaterEqual", "Equal", "NotEqual", "Plus", "PlusPlus", "Minus", "MinusMinus", 
		"Star", "Div", "Mod", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", 
		"Tilde", "Or", "Caret", "And", "Question", "Colon", "Semi", "Comma", "Assign", 
		"Arrow", "Dot", "Identifier", "IdentifierNondigit", "Nondigit", "Digit", 
		"IntegerConstant", "DecimalConstant", "NonzeroDigit", "Sign", "DigitSequence", 
		"StringLiteral", "SChar", "LineAfterPreprocessing", "LineDirective", "PragmaDirective", 
		"Whitespace", "Newline", "BlockComment", "LineComment"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'null'", "'true'", "'false'", "'auto'", "'bool'", "'break'", "'class'", 
		"'continue'", "'else'", "'for'", "'if'", "'int'", "'long'", "'new'", "'return'", 
		"'string'", "'this'", "'void'", "'while'", "'('", "')'", "'['", "']'", 
		"'{'", "'}'", "'<'", "'<='", "'>'", "'>='", "'=='", "'!='", "'+'", "'++'", 
		"'-'", "'--'", "'*'", "'/'", "'%'", "'&&'", "'||'", "'!'", "'<<'", "'>>'", 
		"'~'", "'|'", "'^'", "'&'", "'?'", "':'", "';'", "','", "'='", "'->'", 
		"'.'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "Auto", "Bool", "Break", "Class", "Continue", 
		"Else", "For", "If", "Int", "Long", "New", "Return", "String", "This", 
		"Void", "While", "LeftParen", "RightParen", "LeftBracket", "RightBracket", 
		"LeftBrace", "RightBrace", "Less", "LessEqual", "Greater", "GreaterEqual", 
		"Equal", "NotEqual", "Plus", "PlusPlus", "Minus", "MinusMinus", "Star", 
		"Div", "Mod", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", "Tilde", 
		"Or", "Caret", "And", "Question", "Colon", "Semi", "Comma", "Assign", 
		"Arrow", "Dot", "Identifier", "IntegerConstant", "DigitSequence", "StringLiteral", 
		"LineAfterPreprocessing", "LineDirective", "PragmaDirective", "Whitespace", 
		"Newline", "BlockComment", "LineComment"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxstarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mxstar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2C\u01f1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3"+
		"\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3"+
		"\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3 \3 \3 \3"+
		"!\3!\3!\3\"\3\"\3\"\3#\3#\3$\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3(\3)\3)"+
		"\3*\3*\3*\3+\3+\3+\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3"+
		"\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\38\3"+
		"9\39\3:\3:\3:\7:\u015d\n:\f:\16:\u0160\13:\3;\3;\3<\3<\3=\3=\3>\3>\3>"+
		"\7>\u016b\n>\f>\16>\u016e\13>\5>\u0170\n>\3?\3?\7?\u0174\n?\f?\16?\u0177"+
		"\13?\3@\3@\3A\3A\3B\6B\u017e\nB\rB\16B\u017f\3C\3C\7C\u0184\nC\fC\16C"+
		"\u0187\13C\3C\3C\3D\3D\3D\5D\u018e\nD\3E\3E\3E\3E\3E\3E\3E\7E\u0197\n"+
		"E\fE\16E\u019a\13E\3E\7E\u019d\nE\fE\16E\u01a0\13E\3E\3E\3F\3F\5F\u01a6"+
		"\nF\3F\3F\5F\u01aa\nF\3F\3F\7F\u01ae\nF\fF\16F\u01b1\13F\3F\3F\3G\3G\5"+
		"G\u01b7\nG\3G\3G\3G\3G\3G\3G\3G\3G\3G\7G\u01c2\nG\fG\16G\u01c5\13G\3G"+
		"\3G\3H\6H\u01ca\nH\rH\16H\u01cb\3H\3H\3I\3I\5I\u01d2\nI\3I\5I\u01d5\n"+
		"I\3I\3I\3J\3J\3J\3J\7J\u01dd\nJ\fJ\16J\u01e0\13J\3J\3J\3J\3J\3J\3K\3K"+
		"\3K\3K\7K\u01eb\nK\fK\16K\u01ee\13K\3K\3K\3\u01de\2L\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\2!\2#\21%\22\'\23"+
		")\24+\25-\26/\27\61\30\63\31\65\32\67\339\34;\35=\36?\37A C!E\"G#I$K%"+
		"M&O\'Q(S)U*W+Y,[-]._/a\60c\61e\62g\63i\64k\65m\66o\67q8s9u\2w\2y\2{:}"+
		"\2\177\2\u0081\2\u0083;\u0085<\u0087\2\u0089=\u008b>\u008d?\u008f@\u0091"+
		"A\u0093B\u0095C\3\2\13\5\2C\\aac|\3\2\62;\3\2\62\62\3\2\63;\4\2--//\6"+
		"\2\f\f\17\17$$^^\5\2$$^^pp\4\2\f\f\17\17\4\2\13\13\"\"\2\u01fb\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2"+
		"[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3"+
		"\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2"+
		"\2\2{\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0089\3\2\2\2\2\u008b"+
		"\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2"+
		"\2\2\u0095\3\2\2\2\3\u0097\3\2\2\2\5\u009c\3\2\2\2\7\u00a1\3\2\2\2\t\u00a7"+
		"\3\2\2\2\13\u00ac\3\2\2\2\r\u00b1\3\2\2\2\17\u00b7\3\2\2\2\21\u00bd\3"+
		"\2\2\2\23\u00c6\3\2\2\2\25\u00cb\3\2\2\2\27\u00cf\3\2\2\2\31\u00d2\3\2"+
		"\2\2\33\u00d6\3\2\2\2\35\u00db\3\2\2\2\37\u00df\3\2\2\2!\u00e4\3\2\2\2"+
		"#\u00ea\3\2\2\2%\u00f1\3\2\2\2\'\u00f8\3\2\2\2)\u00fd\3\2\2\2+\u0102\3"+
		"\2\2\2-\u0108\3\2\2\2/\u010a\3\2\2\2\61\u010c\3\2\2\2\63\u010e\3\2\2\2"+
		"\65\u0110\3\2\2\2\67\u0112\3\2\2\29\u0114\3\2\2\2;\u0116\3\2\2\2=\u0119"+
		"\3\2\2\2?\u011b\3\2\2\2A\u011e\3\2\2\2C\u0121\3\2\2\2E\u0124\3\2\2\2G"+
		"\u0126\3\2\2\2I\u0129\3\2\2\2K\u012b\3\2\2\2M\u012e\3\2\2\2O\u0130\3\2"+
		"\2\2Q\u0132\3\2\2\2S\u0134\3\2\2\2U\u0137\3\2\2\2W\u013a\3\2\2\2Y\u013c"+
		"\3\2\2\2[\u013f\3\2\2\2]\u0142\3\2\2\2_\u0144\3\2\2\2a\u0146\3\2\2\2c"+
		"\u0148\3\2\2\2e\u014a\3\2\2\2g\u014c\3\2\2\2i\u014e\3\2\2\2k\u0150\3\2"+
		"\2\2m\u0152\3\2\2\2o\u0154\3\2\2\2q\u0157\3\2\2\2s\u0159\3\2\2\2u\u0161"+
		"\3\2\2\2w\u0163\3\2\2\2y\u0165\3\2\2\2{\u016f\3\2\2\2}\u0171\3\2\2\2\177"+
		"\u0178\3\2\2\2\u0081\u017a\3\2\2\2\u0083\u017d\3\2\2\2\u0085\u0181\3\2"+
		"\2\2\u0087\u018d\3\2\2\2\u0089\u018f\3\2\2\2\u008b\u01a3\3\2\2\2\u008d"+
		"\u01b4\3\2\2\2\u008f\u01c9\3\2\2\2\u0091\u01d4\3\2\2\2\u0093\u01d8\3\2"+
		"\2\2\u0095\u01e6\3\2\2\2\u0097\u0098\7p\2\2\u0098\u0099\7w\2\2\u0099\u009a"+
		"\7n\2\2\u009a\u009b\7n\2\2\u009b\4\3\2\2\2\u009c\u009d\7v\2\2\u009d\u009e"+
		"\7t\2\2\u009e\u009f\7w\2\2\u009f\u00a0\7g\2\2\u00a0\6\3\2\2\2\u00a1\u00a2"+
		"\7h\2\2\u00a2\u00a3\7c\2\2\u00a3\u00a4\7n\2\2\u00a4\u00a5\7u\2\2\u00a5"+
		"\u00a6\7g\2\2\u00a6\b\3\2\2\2\u00a7\u00a8\7c\2\2\u00a8\u00a9\7w\2\2\u00a9"+
		"\u00aa\7v\2\2\u00aa\u00ab\7q\2\2\u00ab\n\3\2\2\2\u00ac\u00ad\7d\2\2\u00ad"+
		"\u00ae\7q\2\2\u00ae\u00af\7q\2\2\u00af\u00b0\7n\2\2\u00b0\f\3\2\2\2\u00b1"+
		"\u00b2\7d\2\2\u00b2\u00b3\7t\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7c\2\2"+
		"\u00b5\u00b6\7m\2\2\u00b6\16\3\2\2\2\u00b7\u00b8\7e\2\2\u00b8\u00b9\7"+
		"n\2\2\u00b9\u00ba\7c\2\2\u00ba\u00bb\7u\2\2\u00bb\u00bc\7u\2\2\u00bc\20"+
		"\3\2\2\2\u00bd\u00be\7e\2\2\u00be\u00bf\7q\2\2\u00bf\u00c0\7p\2\2\u00c0"+
		"\u00c1\7v\2\2\u00c1\u00c2\7k\2\2\u00c2\u00c3\7p\2\2\u00c3\u00c4\7w\2\2"+
		"\u00c4\u00c5\7g\2\2\u00c5\22\3\2\2\2\u00c6\u00c7\7g\2\2\u00c7\u00c8\7"+
		"n\2\2\u00c8\u00c9\7u\2\2\u00c9\u00ca\7g\2\2\u00ca\24\3\2\2\2\u00cb\u00cc"+
		"\7h\2\2\u00cc\u00cd\7q\2\2\u00cd\u00ce\7t\2\2\u00ce\26\3\2\2\2\u00cf\u00d0"+
		"\7k\2\2\u00d0\u00d1\7h\2\2\u00d1\30\3\2\2\2\u00d2\u00d3\7k\2\2\u00d3\u00d4"+
		"\7p\2\2\u00d4\u00d5\7v\2\2\u00d5\32\3\2\2\2\u00d6\u00d7\7n\2\2\u00d7\u00d8"+
		"\7q\2\2\u00d8\u00d9\7p\2\2\u00d9\u00da\7i\2\2\u00da\34\3\2\2\2\u00db\u00dc"+
		"\7p\2\2\u00dc\u00dd\7g\2\2\u00dd\u00de\7y\2\2\u00de\36\3\2\2\2\u00df\u00e0"+
		"\7v\2\2\u00e0\u00e1\7t\2\2\u00e1\u00e2\7w\2\2\u00e2\u00e3\7g\2\2\u00e3"+
		" \3\2\2\2\u00e4\u00e5\7h\2\2\u00e5\u00e6\7c\2\2\u00e6\u00e7\7n\2\2\u00e7"+
		"\u00e8\7u\2\2\u00e8\u00e9\7g\2\2\u00e9\"\3\2\2\2\u00ea\u00eb\7t\2\2\u00eb"+
		"\u00ec\7g\2\2\u00ec\u00ed\7v\2\2\u00ed\u00ee\7w\2\2\u00ee\u00ef\7t\2\2"+
		"\u00ef\u00f0\7p\2\2\u00f0$\3\2\2\2\u00f1\u00f2\7u\2\2\u00f2\u00f3\7v\2"+
		"\2\u00f3\u00f4\7t\2\2\u00f4\u00f5\7k\2\2\u00f5\u00f6\7p\2\2\u00f6\u00f7"+
		"\7i\2\2\u00f7&\3\2\2\2\u00f8\u00f9\7v\2\2\u00f9\u00fa\7j\2\2\u00fa\u00fb"+
		"\7k\2\2\u00fb\u00fc\7u\2\2\u00fc(\3\2\2\2\u00fd\u00fe\7x\2\2\u00fe\u00ff"+
		"\7q\2\2\u00ff\u0100\7k\2\2\u0100\u0101\7f\2\2\u0101*\3\2\2\2\u0102\u0103"+
		"\7y\2\2\u0103\u0104\7j\2\2\u0104\u0105\7k\2\2\u0105\u0106\7n\2\2\u0106"+
		"\u0107\7g\2\2\u0107,\3\2\2\2\u0108\u0109\7*\2\2\u0109.\3\2\2\2\u010a\u010b"+
		"\7+\2\2\u010b\60\3\2\2\2\u010c\u010d\7]\2\2\u010d\62\3\2\2\2\u010e\u010f"+
		"\7_\2\2\u010f\64\3\2\2\2\u0110\u0111\7}\2\2\u0111\66\3\2\2\2\u0112\u0113"+
		"\7\177\2\2\u01138\3\2\2\2\u0114\u0115\7>\2\2\u0115:\3\2\2\2\u0116\u0117"+
		"\7>\2\2\u0117\u0118\7?\2\2\u0118<\3\2\2\2\u0119\u011a\7@\2\2\u011a>\3"+
		"\2\2\2\u011b\u011c\7@\2\2\u011c\u011d\7?\2\2\u011d@\3\2\2\2\u011e\u011f"+
		"\7?\2\2\u011f\u0120\7?\2\2\u0120B\3\2\2\2\u0121\u0122\7#\2\2\u0122\u0123"+
		"\7?\2\2\u0123D\3\2\2\2\u0124\u0125\7-\2\2\u0125F\3\2\2\2\u0126\u0127\7"+
		"-\2\2\u0127\u0128\7-\2\2\u0128H\3\2\2\2\u0129\u012a\7/\2\2\u012aJ\3\2"+
		"\2\2\u012b\u012c\7/\2\2\u012c\u012d\7/\2\2\u012dL\3\2\2\2\u012e\u012f"+
		"\7,\2\2\u012fN\3\2\2\2\u0130\u0131\7\61\2\2\u0131P\3\2\2\2\u0132\u0133"+
		"\7\'\2\2\u0133R\3\2\2\2\u0134\u0135\7(\2\2\u0135\u0136\7(\2\2\u0136T\3"+
		"\2\2\2\u0137\u0138\7~\2\2\u0138\u0139\7~\2\2\u0139V\3\2\2\2\u013a\u013b"+
		"\7#\2\2\u013bX\3\2\2\2\u013c\u013d\7>\2\2\u013d\u013e\7>\2\2\u013eZ\3"+
		"\2\2\2\u013f\u0140\7@\2\2\u0140\u0141\7@\2\2\u0141\\\3\2\2\2\u0142\u0143"+
		"\7\u0080\2\2\u0143^\3\2\2\2\u0144\u0145\7~\2\2\u0145`\3\2\2\2\u0146\u0147"+
		"\7`\2\2\u0147b\3\2\2\2\u0148\u0149\7(\2\2\u0149d\3\2\2\2\u014a\u014b\7"+
		"A\2\2\u014bf\3\2\2\2\u014c\u014d\7<\2\2\u014dh\3\2\2\2\u014e\u014f\7="+
		"\2\2\u014fj\3\2\2\2\u0150\u0151\7.\2\2\u0151l\3\2\2\2\u0152\u0153\7?\2"+
		"\2\u0153n\3\2\2\2\u0154\u0155\7/\2\2\u0155\u0156\7@\2\2\u0156p\3\2\2\2"+
		"\u0157\u0158\7\60\2\2\u0158r\3\2\2\2\u0159\u015e\5u;\2\u015a\u015d\5u"+
		";\2\u015b\u015d\5y=\2\u015c\u015a\3\2\2\2\u015c\u015b\3\2\2\2\u015d\u0160"+
		"\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f\3\2\2\2\u015ft\3\2\2\2\u0160"+
		"\u015e\3\2\2\2\u0161\u0162\5w<\2\u0162v\3\2\2\2\u0163\u0164\t\2\2\2\u0164"+
		"x\3\2\2\2\u0165\u0166\t\3\2\2\u0166z\3\2\2\2\u0167\u0170\t\4\2\2\u0168"+
		"\u016c\t\5\2\2\u0169\u016b\t\3\2\2\u016a\u0169\3\2\2\2\u016b\u016e\3\2"+
		"\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u0170\3\2\2\2\u016e"+
		"\u016c\3\2\2\2\u016f\u0167\3\2\2\2\u016f\u0168\3\2\2\2\u0170|\3\2\2\2"+
		"\u0171\u0175\5\177@\2\u0172\u0174\5y=\2\u0173\u0172\3\2\2\2\u0174\u0177"+
		"\3\2\2\2\u0175\u0173\3\2\2\2\u0175\u0176\3\2\2\2\u0176~\3\2\2\2\u0177"+
		"\u0175\3\2\2\2\u0178\u0179\t\5\2\2\u0179\u0080\3\2\2\2\u017a\u017b\t\6"+
		"\2\2\u017b\u0082\3\2\2\2\u017c\u017e\5y=\2\u017d\u017c\3\2\2\2\u017e\u017f"+
		"\3\2\2\2\u017f\u017d\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u0084\3\2\2\2\u0181"+
		"\u0185\7$\2\2\u0182\u0184\5\u0087D\2\u0183\u0182\3\2\2\2\u0184\u0187\3"+
		"\2\2\2\u0185\u0183\3\2\2\2\u0185\u0186\3\2\2\2\u0186\u0188\3\2\2\2\u0187"+
		"\u0185\3\2\2\2\u0188\u0189\7$\2\2\u0189\u0086\3\2\2\2\u018a\u018e\n\7"+
		"\2\2\u018b\u018c\7^\2\2\u018c\u018e\t\b\2\2\u018d\u018a\3\2\2\2\u018d"+
		"\u018b\3\2\2\2\u018e\u0088\3\2\2\2\u018f\u0190\7%\2\2\u0190\u0191\7n\2"+
		"\2\u0191\u0192\7k\2\2\u0192\u0193\7p\2\2\u0193\u0194\7g\2\2\u0194\u0198"+
		"\3\2\2\2\u0195\u0197\5\u008fH\2\u0196\u0195\3\2\2\2\u0197\u019a\3\2\2"+
		"\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019e\3\2\2\2\u019a\u0198"+
		"\3\2\2\2\u019b\u019d\n\t\2\2\u019c\u019b\3\2\2\2\u019d\u01a0\3\2\2\2\u019e"+
		"\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a1\3\2\2\2\u01a0\u019e\3\2"+
		"\2\2\u01a1\u01a2\bE\2\2\u01a2\u008a\3\2\2\2\u01a3\u01a5\7%\2\2\u01a4\u01a6"+
		"\5\u008fH\2\u01a5\u01a4\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\3\2\2"+
		"\2\u01a7\u01a9\5}?\2\u01a8\u01aa\5\u008fH\2\u01a9\u01a8\3\2\2\2\u01a9"+
		"\u01aa\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01af\5\u0085C\2\u01ac\u01ae"+
		"\n\t\2\2\u01ad\u01ac\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af\u01ad\3\2\2\2\u01af"+
		"\u01b0\3\2\2\2\u01b0\u01b2\3\2\2\2\u01b1\u01af\3\2\2\2\u01b2\u01b3\bF"+
		"\2\2\u01b3\u008c\3\2\2\2\u01b4\u01b6\7%\2\2\u01b5\u01b7\5\u008fH\2\u01b6"+
		"\u01b5\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01b9\7r"+
		"\2\2\u01b9\u01ba\7t\2\2\u01ba\u01bb\7c\2\2\u01bb\u01bc\7i\2\2\u01bc\u01bd"+
		"\7o\2\2\u01bd\u01be\7c\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c3\5\u008fH\2"+
		"\u01c0\u01c2\n\t\2\2\u01c1\u01c0\3\2\2\2\u01c2\u01c5\3\2\2\2\u01c3\u01c1"+
		"\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c6\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c6"+
		"\u01c7\bG\2\2\u01c7\u008e\3\2\2\2\u01c8\u01ca\t\n\2\2\u01c9\u01c8\3\2"+
		"\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc"+
		"\u01cd\3\2\2\2\u01cd\u01ce\bH\2\2\u01ce\u0090\3\2\2\2\u01cf\u01d1\7\17"+
		"\2\2\u01d0\u01d2\7\f\2\2\u01d1\u01d0\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2"+
		"\u01d5\3\2\2\2\u01d3\u01d5\7\f\2\2\u01d4\u01cf\3\2\2\2\u01d4\u01d3\3\2"+
		"\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d7\bI\2\2\u01d7\u0092\3\2\2\2\u01d8"+
		"\u01d9\7\61\2\2\u01d9\u01da\7,\2\2\u01da\u01de\3\2\2\2\u01db\u01dd\13"+
		"\2\2\2\u01dc\u01db\3\2\2\2\u01dd\u01e0\3\2\2\2\u01de\u01df\3\2\2\2\u01de"+
		"\u01dc\3\2\2\2\u01df\u01e1\3\2\2\2\u01e0\u01de\3\2\2\2\u01e1\u01e2\7,"+
		"\2\2\u01e2\u01e3\7\61\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e5\bJ\2\2\u01e5"+
		"\u0094\3\2\2\2\u01e6\u01e7\7\61\2\2\u01e7\u01e8\7\61\2\2\u01e8\u01ec\3"+
		"\2\2\2\u01e9\u01eb\n\t\2\2\u01ea\u01e9\3\2\2\2\u01eb\u01ee\3\2\2\2\u01ec"+
		"\u01ea\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ef\3\2\2\2\u01ee\u01ec\3\2"+
		"\2\2\u01ef\u01f0\bK\2\2\u01f0\u0096\3\2\2\2\27\2\u015c\u015e\u016c\u016f"+
		"\u0175\u017f\u0185\u018d\u0198\u019e\u01a5\u01a9\u01af\u01b6\u01c3\u01cb"+
		"\u01d1\u01d4\u01de\u01ec\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}