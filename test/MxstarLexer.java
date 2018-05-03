// Generated from .\Mxstar.g4 by ANTLR 4.7.1
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
		Auto=1, Bool=2, Break=3, Class=4, Continue=5, Else=6, For=7, If=8, Int=9, 
		Long=10, New=11, Return=12, String=13, This=14, Void=15, While=16, LeftParen=17, 
		RightParen=18, LeftBracket=19, RightBracket=20, LeftBrace=21, RightBrace=22, 
		Less=23, LessEqual=24, Greater=25, GreaterEqual=26, Equal=27, NotEqual=28, 
		Plus=29, PlusPlus=30, Minus=31, MinusMinus=32, Star=33, Div=34, Mod=35, 
		AndAnd=36, OrOr=37, Not=38, LeftShift=39, RightShift=40, Tilde=41, Or=42, 
		Caret=43, And=44, Question=45, Colon=46, Semi=47, Comma=48, Assign=49, 
		Arrow=50, Dot=51, Identifier=52, NullLiteral=53, BoolConstant=54, IntegerConstant=55, 
		DigitSequence=56, StringLiteral=57, LineAfterPreprocessing=58, LineDirective=59, 
		PragmaDirective=60, Whitespace=61, Newline=62, BlockComment=63, LineComment=64;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"Auto", "Bool", "Break", "Class", "Continue", "Else", "For", "If", "Int", 
		"Long", "New", "Null", "True", "False", "Return", "String", "This", "Void", 
		"While", "LeftParen", "RightParen", "LeftBracket", "RightBracket", "LeftBrace", 
		"RightBrace", "Less", "LessEqual", "Greater", "GreaterEqual", "Equal", 
		"NotEqual", "Plus", "PlusPlus", "Minus", "MinusMinus", "Star", "Div", 
		"Mod", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", "Tilde", "Or", 
		"Caret", "And", "Question", "Colon", "Semi", "Comma", "Assign", "Arrow", 
		"Dot", "Identifier", "IdentifierNondigit", "Nondigit", "Digit", "NullLiteral", 
		"BoolConstant", "IntegerConstant", "DecimalConstant", "NonzeroDigit", 
		"Sign", "DigitSequence", "StringLiteral", "SChar", "LineAfterPreprocessing", 
		"LineDirective", "PragmaDirective", "Whitespace", "Newline", "BlockComment", 
		"LineComment"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'auto'", "'bool'", "'break'", "'class'", "'continue'", "'else'", 
		"'for'", "'if'", "'int'", "'long'", "'new'", "'return'", "'string'", "'this'", 
		"'void'", "'while'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'<'", 
		"'<='", "'>'", "'>='", "'=='", "'!='", "'+'", "'++'", "'-'", "'--'", "'*'", 
		"'/'", "'%'", "'&&'", "'||'", "'!'", "'<<'", "'>>'", "'~'", "'|'", "'^'", 
		"'&'", "'?'", "':'", "';'", "','", "'='", "'->'", "'.'", null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "Auto", "Bool", "Break", "Class", "Continue", "Else", "For", "If", 
		"Int", "Long", "New", "Return", "String", "This", "Void", "While", "LeftParen", 
		"RightParen", "LeftBracket", "RightBracket", "LeftBrace", "RightBrace", 
		"Less", "LessEqual", "Greater", "GreaterEqual", "Equal", "NotEqual", "Plus", 
		"PlusPlus", "Minus", "MinusMinus", "Star", "Div", "Mod", "AndAnd", "OrOr", 
		"Not", "LeftShift", "RightShift", "Tilde", "Or", "Caret", "And", "Question", 
		"Colon", "Semi", "Comma", "Assign", "Arrow", "Dot", "Identifier", "NullLiteral", 
		"BoolConstant", "IntegerConstant", "DigitSequence", "StringLiteral", "LineAfterPreprocessing", 
		"LineDirective", "PragmaDirective", "Whitespace", "Newline", "BlockComment", 
		"LineComment"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2B\u01f6\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26"+
		"\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35"+
		"\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3"+
		"$\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3+\3+\3+\3,\3,\3,"+
		"\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3"+
		"\65\3\65\3\66\3\66\3\66\3\67\3\67\38\38\38\78\u0152\n8\f8\168\u0155\13"+
		"8\39\39\3:\3:\3;\3;\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\3=\3=\3=\3=\5=\u016b"+
		"\n=\3>\3>\3>\7>\u0170\n>\f>\16>\u0173\13>\5>\u0175\n>\3?\3?\7?\u0179\n"+
		"?\f?\16?\u017c\13?\3@\3@\3A\3A\3B\6B\u0183\nB\rB\16B\u0184\3C\3C\7C\u0189"+
		"\nC\fC\16C\u018c\13C\3C\3C\3D\3D\3D\5D\u0193\nD\3E\3E\3E\3E\3E\3E\3E\7"+
		"E\u019c\nE\fE\16E\u019f\13E\3E\7E\u01a2\nE\fE\16E\u01a5\13E\3E\3E\3F\3"+
		"F\5F\u01ab\nF\3F\3F\5F\u01af\nF\3F\3F\7F\u01b3\nF\fF\16F\u01b6\13F\3F"+
		"\3F\3G\3G\5G\u01bc\nG\3G\3G\3G\3G\3G\3G\3G\3G\3G\7G\u01c7\nG\fG\16G\u01ca"+
		"\13G\3G\3G\3H\6H\u01cf\nH\rH\16H\u01d0\3H\3H\3I\3I\5I\u01d7\nI\3I\5I\u01da"+
		"\nI\3I\3I\3J\3J\3J\3J\7J\u01e2\nJ\fJ\16J\u01e5\13J\3J\3J\3J\3J\3J\3K\3"+
		"K\3K\3K\7K\u01f0\nK\fK\16K\u01f3\13K\3K\3K\3\u01e3\2L\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\2\33\2\35\2\37\16!\17#\20%\21\'"+
		"\22)\23+\24-\25/\26\61\27\63\30\65\31\67\329\33;\34=\35?\36A\37C E!G\""+
		"I#K$M%O&Q\'S(U)W*Y+[,]-_.a/c\60e\61g\62i\63k\64m\65o\66q\2s\2u\2w\67y"+
		"8{9}\2\177\2\u0081\2\u0083:\u0085;\u0087\2\u0089<\u008b=\u008d>\u008f"+
		"?\u0091@\u0093A\u0095B\3\2\13\5\2C\\aac|\3\2\62;\3\2\62\62\3\2\63;\4\2"+
		"--//\6\2\f\f\17\17$$^^\5\2$$^^pp\4\2\f\f\17\17\4\2\13\13\"\"\2\u0200\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2"+
		"i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3"+
		"\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2"+
		"\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"+
		"\3\2\2\2\3\u0097\3\2\2\2\5\u009c\3\2\2\2\7\u00a1\3\2\2\2\t\u00a7\3\2\2"+
		"\2\13\u00ad\3\2\2\2\r\u00b6\3\2\2\2\17\u00bb\3\2\2\2\21\u00bf\3\2\2\2"+
		"\23\u00c2\3\2\2\2\25\u00c6\3\2\2\2\27\u00cb\3\2\2\2\31\u00cf\3\2\2\2\33"+
		"\u00d4\3\2\2\2\35\u00d9\3\2\2\2\37\u00df\3\2\2\2!\u00e6\3\2\2\2#\u00ed"+
		"\3\2\2\2%\u00f2\3\2\2\2\'\u00f7\3\2\2\2)\u00fd\3\2\2\2+\u00ff\3\2\2\2"+
		"-\u0101\3\2\2\2/\u0103\3\2\2\2\61\u0105\3\2\2\2\63\u0107\3\2\2\2\65\u0109"+
		"\3\2\2\2\67\u010b\3\2\2\29\u010e\3\2\2\2;\u0110\3\2\2\2=\u0113\3\2\2\2"+
		"?\u0116\3\2\2\2A\u0119\3\2\2\2C\u011b\3\2\2\2E\u011e\3\2\2\2G\u0120\3"+
		"\2\2\2I\u0123\3\2\2\2K\u0125\3\2\2\2M\u0127\3\2\2\2O\u0129\3\2\2\2Q\u012c"+
		"\3\2\2\2S\u012f\3\2\2\2U\u0131\3\2\2\2W\u0134\3\2\2\2Y\u0137\3\2\2\2["+
		"\u0139\3\2\2\2]\u013b\3\2\2\2_\u013d\3\2\2\2a\u013f\3\2\2\2c\u0141\3\2"+
		"\2\2e\u0143\3\2\2\2g\u0145\3\2\2\2i\u0147\3\2\2\2k\u0149\3\2\2\2m\u014c"+
		"\3\2\2\2o\u014e\3\2\2\2q\u0156\3\2\2\2s\u0158\3\2\2\2u\u015a\3\2\2\2w"+
		"\u015c\3\2\2\2y\u016a\3\2\2\2{\u0174\3\2\2\2}\u0176\3\2\2\2\177\u017d"+
		"\3\2\2\2\u0081\u017f\3\2\2\2\u0083\u0182\3\2\2\2\u0085\u0186\3\2\2\2\u0087"+
		"\u0192\3\2\2\2\u0089\u0194\3\2\2\2\u008b\u01a8\3\2\2\2\u008d\u01b9\3\2"+
		"\2\2\u008f\u01ce\3\2\2\2\u0091\u01d9\3\2\2\2\u0093\u01dd\3\2\2\2\u0095"+
		"\u01eb\3\2\2\2\u0097\u0098\7c\2\2\u0098\u0099\7w\2\2\u0099\u009a\7v\2"+
		"\2\u009a\u009b\7q\2\2\u009b\4\3\2\2\2\u009c\u009d\7d\2\2\u009d\u009e\7"+
		"q\2\2\u009e\u009f\7q\2\2\u009f\u00a0\7n\2\2\u00a0\6\3\2\2\2\u00a1\u00a2"+
		"\7d\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4\7g\2\2\u00a4\u00a5\7c\2\2\u00a5"+
		"\u00a6\7m\2\2\u00a6\b\3\2\2\2\u00a7\u00a8\7e\2\2\u00a8\u00a9\7n\2\2\u00a9"+
		"\u00aa\7c\2\2\u00aa\u00ab\7u\2\2\u00ab\u00ac\7u\2\2\u00ac\n\3\2\2\2\u00ad"+
		"\u00ae\7e\2\2\u00ae\u00af\7q\2\2\u00af\u00b0\7p\2\2\u00b0\u00b1\7v\2\2"+
		"\u00b1\u00b2\7k\2\2\u00b2\u00b3\7p\2\2\u00b3\u00b4\7w\2\2\u00b4\u00b5"+
		"\7g\2\2\u00b5\f\3\2\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7n\2\2\u00b8\u00b9"+
		"\7u\2\2\u00b9\u00ba\7g\2\2\u00ba\16\3\2\2\2\u00bb\u00bc\7h\2\2\u00bc\u00bd"+
		"\7q\2\2\u00bd\u00be\7t\2\2\u00be\20\3\2\2\2\u00bf\u00c0\7k\2\2\u00c0\u00c1"+
		"\7h\2\2\u00c1\22\3\2\2\2\u00c2\u00c3\7k\2\2\u00c3\u00c4\7p\2\2\u00c4\u00c5"+
		"\7v\2\2\u00c5\24\3\2\2\2\u00c6\u00c7\7n\2\2\u00c7\u00c8\7q\2\2\u00c8\u00c9"+
		"\7p\2\2\u00c9\u00ca\7i\2\2\u00ca\26\3\2\2\2\u00cb\u00cc\7p\2\2\u00cc\u00cd"+
		"\7g\2\2\u00cd\u00ce\7y\2\2\u00ce\30\3\2\2\2\u00cf\u00d0\7p\2\2\u00d0\u00d1"+
		"\7w\2\2\u00d1\u00d2\7n\2\2\u00d2\u00d3\7n\2\2\u00d3\32\3\2\2\2\u00d4\u00d5"+
		"\7v\2\2\u00d5\u00d6\7t\2\2\u00d6\u00d7\7w\2\2\u00d7\u00d8\7g\2\2\u00d8"+
		"\34\3\2\2\2\u00d9\u00da\7h\2\2\u00da\u00db\7c\2\2\u00db\u00dc\7n\2\2\u00dc"+
		"\u00dd\7u\2\2\u00dd\u00de\7g\2\2\u00de\36\3\2\2\2\u00df\u00e0\7t\2\2\u00e0"+
		"\u00e1\7g\2\2\u00e1\u00e2\7v\2\2\u00e2\u00e3\7w\2\2\u00e3\u00e4\7t\2\2"+
		"\u00e4\u00e5\7p\2\2\u00e5 \3\2\2\2\u00e6\u00e7\7u\2\2\u00e7\u00e8\7v\2"+
		"\2\u00e8\u00e9\7t\2\2\u00e9\u00ea\7k\2\2\u00ea\u00eb\7p\2\2\u00eb\u00ec"+
		"\7i\2\2\u00ec\"\3\2\2\2\u00ed\u00ee\7v\2\2\u00ee\u00ef\7j\2\2\u00ef\u00f0"+
		"\7k\2\2\u00f0\u00f1\7u\2\2\u00f1$\3\2\2\2\u00f2\u00f3\7x\2\2\u00f3\u00f4"+
		"\7q\2\2\u00f4\u00f5\7k\2\2\u00f5\u00f6\7f\2\2\u00f6&\3\2\2\2\u00f7\u00f8"+
		"\7y\2\2\u00f8\u00f9\7j\2\2\u00f9\u00fa\7k\2\2\u00fa\u00fb\7n\2\2\u00fb"+
		"\u00fc\7g\2\2\u00fc(\3\2\2\2\u00fd\u00fe\7*\2\2\u00fe*\3\2\2\2\u00ff\u0100"+
		"\7+\2\2\u0100,\3\2\2\2\u0101\u0102\7]\2\2\u0102.\3\2\2\2\u0103\u0104\7"+
		"_\2\2\u0104\60\3\2\2\2\u0105\u0106\7}\2\2\u0106\62\3\2\2\2\u0107\u0108"+
		"\7\177\2\2\u0108\64\3\2\2\2\u0109\u010a\7>\2\2\u010a\66\3\2\2\2\u010b"+
		"\u010c\7>\2\2\u010c\u010d\7?\2\2\u010d8\3\2\2\2\u010e\u010f\7@\2\2\u010f"+
		":\3\2\2\2\u0110\u0111\7@\2\2\u0111\u0112\7?\2\2\u0112<\3\2\2\2\u0113\u0114"+
		"\7?\2\2\u0114\u0115\7?\2\2\u0115>\3\2\2\2\u0116\u0117\7#\2\2\u0117\u0118"+
		"\7?\2\2\u0118@\3\2\2\2\u0119\u011a\7-\2\2\u011aB\3\2\2\2\u011b\u011c\7"+
		"-\2\2\u011c\u011d\7-\2\2\u011dD\3\2\2\2\u011e\u011f\7/\2\2\u011fF\3\2"+
		"\2\2\u0120\u0121\7/\2\2\u0121\u0122\7/\2\2\u0122H\3\2\2\2\u0123\u0124"+
		"\7,\2\2\u0124J\3\2\2\2\u0125\u0126\7\61\2\2\u0126L\3\2\2\2\u0127\u0128"+
		"\7\'\2\2\u0128N\3\2\2\2\u0129\u012a\7(\2\2\u012a\u012b\7(\2\2\u012bP\3"+
		"\2\2\2\u012c\u012d\7~\2\2\u012d\u012e\7~\2\2\u012eR\3\2\2\2\u012f\u0130"+
		"\7#\2\2\u0130T\3\2\2\2\u0131\u0132\7>\2\2\u0132\u0133\7>\2\2\u0133V\3"+
		"\2\2\2\u0134\u0135\7@\2\2\u0135\u0136\7@\2\2\u0136X\3\2\2\2\u0137\u0138"+
		"\7\u0080\2\2\u0138Z\3\2\2\2\u0139\u013a\7~\2\2\u013a\\\3\2\2\2\u013b\u013c"+
		"\7`\2\2\u013c^\3\2\2\2\u013d\u013e\7(\2\2\u013e`\3\2\2\2\u013f\u0140\7"+
		"A\2\2\u0140b\3\2\2\2\u0141\u0142\7<\2\2\u0142d\3\2\2\2\u0143\u0144\7="+
		"\2\2\u0144f\3\2\2\2\u0145\u0146\7.\2\2\u0146h\3\2\2\2\u0147\u0148\7?\2"+
		"\2\u0148j\3\2\2\2\u0149\u014a\7/\2\2\u014a\u014b\7@\2\2\u014bl\3\2\2\2"+
		"\u014c\u014d\7\60\2\2\u014dn\3\2\2\2\u014e\u0153\5q9\2\u014f\u0152\5q"+
		"9\2\u0150\u0152\5u;\2\u0151\u014f\3\2\2\2\u0151\u0150\3\2\2\2\u0152\u0155"+
		"\3\2\2\2\u0153\u0151\3\2\2\2\u0153\u0154\3\2\2\2\u0154p\3\2\2\2\u0155"+
		"\u0153\3\2\2\2\u0156\u0157\5s:\2\u0157r\3\2\2\2\u0158\u0159\t\2\2\2\u0159"+
		"t\3\2\2\2\u015a\u015b\t\3\2\2\u015bv\3\2\2\2\u015c\u015d\7p\2\2\u015d"+
		"\u015e\7w\2\2\u015e\u015f\7n\2\2\u015f\u0160\7n\2\2\u0160x\3\2\2\2\u0161"+
		"\u0162\7v\2\2\u0162\u0163\7t\2\2\u0163\u0164\7w\2\2\u0164\u016b\7g\2\2"+
		"\u0165\u0166\7h\2\2\u0166\u0167\7c\2\2\u0167\u0168\7n\2\2\u0168\u0169"+
		"\7u\2\2\u0169\u016b\7g\2\2\u016a\u0161\3\2\2\2\u016a\u0165\3\2\2\2\u016b"+
		"z\3\2\2\2\u016c\u0175\t\4\2\2\u016d\u0171\t\5\2\2\u016e\u0170\t\3\2\2"+
		"\u016f\u016e\3\2\2\2\u0170\u0173\3\2\2\2\u0171\u016f\3\2\2\2\u0171\u0172"+
		"\3\2\2\2\u0172\u0175\3\2\2\2\u0173\u0171\3\2\2\2\u0174\u016c\3\2\2\2\u0174"+
		"\u016d\3\2\2\2\u0175|\3\2\2\2\u0176\u017a\5\177@\2\u0177\u0179\5u;\2\u0178"+
		"\u0177\3\2\2\2\u0179\u017c\3\2\2\2\u017a\u0178\3\2\2\2\u017a\u017b\3\2"+
		"\2\2\u017b~\3\2\2\2\u017c\u017a\3\2\2\2\u017d\u017e\t\5\2\2\u017e\u0080"+
		"\3\2\2\2\u017f\u0180\t\6\2\2\u0180\u0082\3\2\2\2\u0181\u0183\5u;\2\u0182"+
		"\u0181\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0182\3\2\2\2\u0184\u0185\3\2"+
		"\2\2\u0185\u0084\3\2\2\2\u0186\u018a\7$\2\2\u0187\u0189\5\u0087D\2\u0188"+
		"\u0187\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2\2\2\u018a\u018b\3\2"+
		"\2\2\u018b\u018d\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018e\7$\2\2\u018e"+
		"\u0086\3\2\2\2\u018f\u0193\n\7\2\2\u0190\u0191\7^\2\2\u0191\u0193\t\b"+
		"\2\2\u0192\u018f\3\2\2\2\u0192\u0190\3\2\2\2\u0193\u0088\3\2\2\2\u0194"+
		"\u0195\7%\2\2\u0195\u0196\7n\2\2\u0196\u0197\7k\2\2\u0197\u0198\7p\2\2"+
		"\u0198\u0199\7g\2\2\u0199\u019d\3\2\2\2\u019a\u019c\5\u008fH\2\u019b\u019a"+
		"\3\2\2\2\u019c\u019f\3\2\2\2\u019d\u019b\3\2\2\2\u019d\u019e\3\2\2\2\u019e"+
		"\u01a3\3\2\2\2\u019f\u019d\3\2\2\2\u01a0\u01a2\n\t\2\2\u01a1\u01a0\3\2"+
		"\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4"+
		"\u01a6\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6\u01a7\bE\2\2\u01a7\u008a\3\2"+
		"\2\2\u01a8\u01aa\7%\2\2\u01a9\u01ab\5\u008fH\2\u01aa\u01a9\3\2\2\2\u01aa"+
		"\u01ab\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ae\5}?\2\u01ad\u01af\5\u008f"+
		"H\2\u01ae\u01ad\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0"+
		"\u01b4\5\u0085C\2\u01b1\u01b3\n\t\2\2\u01b2\u01b1\3\2\2\2\u01b3\u01b6"+
		"\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b7\3\2\2\2\u01b6"+
		"\u01b4\3\2\2\2\u01b7\u01b8\bF\2\2\u01b8\u008c\3\2\2\2\u01b9\u01bb\7%\2"+
		"\2\u01ba\u01bc\5\u008fH\2\u01bb\u01ba\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc"+
		"\u01bd\3\2\2\2\u01bd\u01be\7r\2\2\u01be\u01bf\7t\2\2\u01bf\u01c0\7c\2"+
		"\2\u01c0\u01c1\7i\2\2\u01c1\u01c2\7o\2\2\u01c2\u01c3\7c\2\2\u01c3\u01c4"+
		"\3\2\2\2\u01c4\u01c8\5\u008fH\2\u01c5\u01c7\n\t\2\2\u01c6\u01c5\3\2\2"+
		"\2\u01c7\u01ca\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01cb"+
		"\3\2\2\2\u01ca\u01c8\3\2\2\2\u01cb\u01cc\bG\2\2\u01cc\u008e\3\2\2\2\u01cd"+
		"\u01cf\t\n\2\2\u01ce\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01ce\3\2"+
		"\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d3\bH\2\2\u01d3"+
		"\u0090\3\2\2\2\u01d4\u01d6\7\17\2\2\u01d5\u01d7\7\f\2\2\u01d6\u01d5\3"+
		"\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01da\3\2\2\2\u01d8\u01da\7\f\2\2\u01d9"+
		"\u01d4\3\2\2\2\u01d9\u01d8\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u01dc\bI"+
		"\2\2\u01dc\u0092\3\2\2\2\u01dd\u01de\7\61\2\2\u01de\u01df\7,\2\2\u01df"+
		"\u01e3\3\2\2\2\u01e0\u01e2\13\2\2\2\u01e1\u01e0\3\2\2\2\u01e2\u01e5\3"+
		"\2\2\2\u01e3\u01e4\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4\u01e6\3\2\2\2\u01e5"+
		"\u01e3\3\2\2\2\u01e6\u01e7\7,\2\2\u01e7\u01e8\7\61\2\2\u01e8\u01e9\3\2"+
		"\2\2\u01e9\u01ea\bJ\2\2\u01ea\u0094\3\2\2\2\u01eb\u01ec\7\61\2\2\u01ec"+
		"\u01ed\7\61\2\2\u01ed\u01f1\3\2\2\2\u01ee\u01f0\n\t\2\2\u01ef\u01ee\3"+
		"\2\2\2\u01f0\u01f3\3\2\2\2\u01f1\u01ef\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2"+
		"\u01f4\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f4\u01f5\bK\2\2\u01f5\u0096\3\2"+
		"\2\2\30\2\u0151\u0153\u016a\u0171\u0174\u017a\u0184\u018a\u0192\u019d"+
		"\u01a3\u01aa\u01ae\u01b4\u01bb\u01c8\u01d0\u01d6\u01d9\u01e3\u01f1\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}