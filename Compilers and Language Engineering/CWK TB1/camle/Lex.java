// $ANTLR 3.5.2 Lex.g 2016-04-29 14:45:48

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class Lex extends Lexer {
	public static final int EOF=-1;
	public static final int ASSIGN=4;
	public static final int CLOSEPAREN=5;
	public static final int COMMA=6;
	public static final int COMMENT=7;
	public static final int DIVIDE=8;
	public static final int DO=9;
	public static final int ELSE=10;
	public static final int EQUAL=11;
	public static final int FALSE=12;
	public static final int FLOAT=13;
	public static final int GREATER=14;
	public static final int GREATEREQUAL=15;
	public static final int IDENT=16;
	public static final int IF=17;
	public static final int INTNUM=18;
	public static final int LOGAND=19;
	public static final int MINUS=20;
	public static final int MULTIPLY=21;
	public static final int NOT=22;
	public static final int NOTEQUAL=23;
	public static final int OPENPAREN=24;
	public static final int OR=25;
	public static final int PLUS=26;
	public static final int READ=27;
	public static final int SEMICOLON=28;
	public static final int SKIP=29;
	public static final int SMALLEQUAL=30;
	public static final int SMALLER=31;
	public static final int STRING=32;
	public static final int THEN=33;
	public static final int TRUE=34;
	public static final int WHILE=35;
	public static final int WRITE=36;
	public static final int WRITELN=37;
	public static final int WS=38;
	public static final int XOR=39;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public Lex() {} 
	public Lex(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public Lex(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "Lex.g"; }

	// $ANTLR start "READ"
	public final void mREAD() throws RecognitionException {
		try {
			int _type = READ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:4:12: ( 'read' )
			// Lex.g:4:14: 'read'
			{
			match("read"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "READ"

	// $ANTLR start "ELSE"
	public final void mELSE() throws RecognitionException {
		try {
			int _type = ELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:5:12: ( 'else' )
			// Lex.g:5:14: 'else'
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELSE"

	// $ANTLR start "THEN"
	public final void mTHEN() throws RecognitionException {
		try {
			int _type = THEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:6:12: ( 'then' )
			// Lex.g:6:14: 'then'
			{
			match("then"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THEN"

	// $ANTLR start "DO"
	public final void mDO() throws RecognitionException {
		try {
			int _type = DO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:7:12: ( 'do' )
			// Lex.g:7:14: 'do'
			{
			match("do"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DO"

	// $ANTLR start "WHILE"
	public final void mWHILE() throws RecognitionException {
		try {
			int _type = WHILE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:8:12: ( 'while' )
			// Lex.g:8:14: 'while'
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHILE"

	// $ANTLR start "IF"
	public final void mIF() throws RecognitionException {
		try {
			int _type = IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:9:12: ( 'if' )
			// Lex.g:9:14: 'if'
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IF"

	// $ANTLR start "TRUE"
	public final void mTRUE() throws RecognitionException {
		try {
			int _type = TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:10:12: ( 'true' )
			// Lex.g:10:14: 'true'
			{
			match("true"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRUE"

	// $ANTLR start "FALSE"
	public final void mFALSE() throws RecognitionException {
		try {
			int _type = FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:11:12: ( 'false' )
			// Lex.g:11:14: 'false'
			{
			match("false"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FALSE"

	// $ANTLR start "WRITE"
	public final void mWRITE() throws RecognitionException {
		try {
			int _type = WRITE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:12:12: ( 'write' )
			// Lex.g:12:14: 'write'
			{
			match("write"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WRITE"

	// $ANTLR start "WRITELN"
	public final void mWRITELN() throws RecognitionException {
		try {
			int _type = WRITELN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:13:12: ( 'writeln' )
			// Lex.g:13:14: 'writeln'
			{
			match("writeln"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WRITELN"

	// $ANTLR start "SKIP"
	public final void mSKIP() throws RecognitionException {
		try {
			int _type = SKIP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:14:12: ( 'skip' )
			// Lex.g:14:14: 'skip'
			{
			match("skip"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SKIP"

	// $ANTLR start "SEMICOLON"
	public final void mSEMICOLON() throws RecognitionException {
		try {
			int _type = SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:16:14: ( ';' )
			// Lex.g:16:16: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMICOLON"

	// $ANTLR start "OPENPAREN"
	public final void mOPENPAREN() throws RecognitionException {
		try {
			int _type = OPENPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:17:14: ( '(' )
			// Lex.g:17:16: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPENPAREN"

	// $ANTLR start "CLOSEPAREN"
	public final void mCLOSEPAREN() throws RecognitionException {
		try {
			int _type = CLOSEPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:18:14: ( ')' )
			// Lex.g:18:16: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSEPAREN"

	// $ANTLR start "EQUAL"
	public final void mEQUAL() throws RecognitionException {
		try {
			int _type = EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:20:14: ( '=' )
			// Lex.g:20:16: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUAL"

	// $ANTLR start "SMALLEQUAL"
	public final void mSMALLEQUAL() throws RecognitionException {
		try {
			int _type = SMALLEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:21:14: ( '<=' )
			// Lex.g:21:16: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SMALLEQUAL"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:22:14: ( '!' )
			// Lex.g:22:16: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "ASSIGN"
	public final void mASSIGN() throws RecognitionException {
		try {
			int _type = ASSIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:23:14: ( ':=' )
			// Lex.g:23:16: ':='
			{
			match(":="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSIGN"

	// $ANTLR start "MULTIPLY"
	public final void mMULTIPLY() throws RecognitionException {
		try {
			int _type = MULTIPLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:24:14: ( '*' )
			// Lex.g:24:16: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MULTIPLY"

	// $ANTLR start "LOGAND"
	public final void mLOGAND() throws RecognitionException {
		try {
			int _type = LOGAND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:25:14: ( '&' )
			// Lex.g:25:16: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LOGAND"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:26:14: ( ',' )
			// Lex.g:26:16: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:27:14: ( '+' )
			// Lex.g:27:16: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "MINUS"
	public final void mMINUS() throws RecognitionException {
		try {
			int _type = MINUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:28:14: ( '-' )
			// Lex.g:28:16: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUS"

	// $ANTLR start "FLOAT"
	public final void mFLOAT() throws RecognitionException {
		try {
			int _type = FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:30:14: ( ( '0' .. '9' )+ ( '.' ) ( '0' .. '9' )+ )
			// Lex.g:30:16: ( '0' .. '9' )+ ( '.' ) ( '0' .. '9' )+
			{
			// Lex.g:30:16: ( '0' .. '9' )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			// Lex.g:30:27: ( '.' )
			// Lex.g:30:28: '.'
			{
			match('.'); 
			}

			// Lex.g:30:32: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FLOAT"

	// $ANTLR start "INTNUM"
	public final void mINTNUM() throws RecognitionException {
		try {
			int _type = INTNUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:31:14: ( ( '0' .. '9' )+ )
			// Lex.g:31:16: ( '0' .. '9' )+
			{
			// Lex.g:31:16: ( '0' .. '9' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INTNUM"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:32:14: ( '\\'' ( '\\'' '\\'' |~ '\\'' )* '\\'' )
			// Lex.g:32:16: '\\'' ( '\\'' '\\'' |~ '\\'' )* '\\''
			{
			match('\''); 
			// Lex.g:32:21: ( '\\'' '\\'' |~ '\\'' )*
			loop4:
			while (true) {
				int alt4=3;
				int LA4_0 = input.LA(1);
				if ( (LA4_0=='\'') ) {
					int LA4_1 = input.LA(2);
					if ( (LA4_1=='\'') ) {
						alt4=1;
					}

				}
				else if ( ((LA4_0 >= '\u0000' && LA4_0 <= '&')||(LA4_0 >= '(' && LA4_0 <= '\uFFFF')) ) {
					alt4=2;
				}

				switch (alt4) {
				case 1 :
					// Lex.g:32:22: '\\'' '\\''
					{
					match('\''); 
					match('\''); 
					}
					break;
				case 2 :
					// Lex.g:32:34: ~ '\\''
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop4;
				}
			}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:33:14: ( '{' (~ '}' )* '}' )
			// Lex.g:33:16: '{' (~ '}' )* '}'
			{
			match('{'); 
			// Lex.g:33:20: (~ '}' )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '\u0000' && LA5_0 <= '|')||(LA5_0 >= '~' && LA5_0 <= '\uFFFF')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '|')||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop5;
				}
			}

			match('}'); 
			skip();
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:34:14: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
			// Lex.g:34:16: ( ' ' | '\\t' | '\\r' | '\\n' )+
			{
			// Lex.g:34:16: ( ' ' | '\\t' | '\\r' | '\\n' )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||LA6_0=='\r'||LA6_0==' ') ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
			}

			skip();
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "IDENT"
	public final void mIDENT() throws RecognitionException {
		try {
			int _type = IDENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:35:14: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
			// Lex.g:35:16: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Lex.g:35:43: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( ((LA7_0 >= '0' && LA7_0 <= '9')||(LA7_0 >= 'A' && LA7_0 <= 'Z')||(LA7_0 >= 'a' && LA7_0 <= 'z')) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop7;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IDENT"

	// $ANTLR start "SMALLER"
	public final void mSMALLER() throws RecognitionException {
		try {
			int _type = SMALLER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:39:14: ( '<' )
			// Lex.g:39:16: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SMALLER"

	// $ANTLR start "GREATER"
	public final void mGREATER() throws RecognitionException {
		try {
			int _type = GREATER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:40:14: ( '>' )
			// Lex.g:40:16: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATER"

	// $ANTLR start "GREATEREQUAL"
	public final void mGREATEREQUAL() throws RecognitionException {
		try {
			int _type = GREATEREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:41:14: ( '>=' )
			// Lex.g:41:16: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATEREQUAL"

	// $ANTLR start "NOTEQUAL"
	public final void mNOTEQUAL() throws RecognitionException {
		try {
			int _type = NOTEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:42:14: ( '!=' )
			// Lex.g:42:16: '!='
			{
			match("!="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOTEQUAL"

	// $ANTLR start "XOR"
	public final void mXOR() throws RecognitionException {
		try {
			int _type = XOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:43:14: ( '^' )
			// Lex.g:43:16: '^'
			{
			match('^'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "XOR"

	// $ANTLR start "DIVIDE"
	public final void mDIVIDE() throws RecognitionException {
		try {
			int _type = DIVIDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:44:14: ( '/' )
			// Lex.g:44:16: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIVIDE"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:45:14: ( '||' )
			// Lex.g:45:16: '||'
			{
			match("||"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	@Override
	public void mTokens() throws RecognitionException {
		// Lex.g:1:8: ( READ | ELSE | THEN | DO | WHILE | IF | TRUE | FALSE | WRITE | WRITELN | SKIP | SEMICOLON | OPENPAREN | CLOSEPAREN | EQUAL | SMALLEQUAL | NOT | ASSIGN | MULTIPLY | LOGAND | COMMA | PLUS | MINUS | FLOAT | INTNUM | STRING | COMMENT | WS | IDENT | SMALLER | GREATER | GREATEREQUAL | NOTEQUAL | XOR | DIVIDE | OR )
		int alt8=36;
		alt8 = dfa8.predict(input);
		switch (alt8) {
			case 1 :
				// Lex.g:1:10: READ
				{
				mREAD(); 

				}
				break;
			case 2 :
				// Lex.g:1:15: ELSE
				{
				mELSE(); 

				}
				break;
			case 3 :
				// Lex.g:1:20: THEN
				{
				mTHEN(); 

				}
				break;
			case 4 :
				// Lex.g:1:25: DO
				{
				mDO(); 

				}
				break;
			case 5 :
				// Lex.g:1:28: WHILE
				{
				mWHILE(); 

				}
				break;
			case 6 :
				// Lex.g:1:34: IF
				{
				mIF(); 

				}
				break;
			case 7 :
				// Lex.g:1:37: TRUE
				{
				mTRUE(); 

				}
				break;
			case 8 :
				// Lex.g:1:42: FALSE
				{
				mFALSE(); 

				}
				break;
			case 9 :
				// Lex.g:1:48: WRITE
				{
				mWRITE(); 

				}
				break;
			case 10 :
				// Lex.g:1:54: WRITELN
				{
				mWRITELN(); 

				}
				break;
			case 11 :
				// Lex.g:1:62: SKIP
				{
				mSKIP(); 

				}
				break;
			case 12 :
				// Lex.g:1:67: SEMICOLON
				{
				mSEMICOLON(); 

				}
				break;
			case 13 :
				// Lex.g:1:77: OPENPAREN
				{
				mOPENPAREN(); 

				}
				break;
			case 14 :
				// Lex.g:1:87: CLOSEPAREN
				{
				mCLOSEPAREN(); 

				}
				break;
			case 15 :
				// Lex.g:1:98: EQUAL
				{
				mEQUAL(); 

				}
				break;
			case 16 :
				// Lex.g:1:104: SMALLEQUAL
				{
				mSMALLEQUAL(); 

				}
				break;
			case 17 :
				// Lex.g:1:115: NOT
				{
				mNOT(); 

				}
				break;
			case 18 :
				// Lex.g:1:119: ASSIGN
				{
				mASSIGN(); 

				}
				break;
			case 19 :
				// Lex.g:1:126: MULTIPLY
				{
				mMULTIPLY(); 

				}
				break;
			case 20 :
				// Lex.g:1:135: LOGAND
				{
				mLOGAND(); 

				}
				break;
			case 21 :
				// Lex.g:1:142: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 22 :
				// Lex.g:1:148: PLUS
				{
				mPLUS(); 

				}
				break;
			case 23 :
				// Lex.g:1:153: MINUS
				{
				mMINUS(); 

				}
				break;
			case 24 :
				// Lex.g:1:159: FLOAT
				{
				mFLOAT(); 

				}
				break;
			case 25 :
				// Lex.g:1:165: INTNUM
				{
				mINTNUM(); 

				}
				break;
			case 26 :
				// Lex.g:1:172: STRING
				{
				mSTRING(); 

				}
				break;
			case 27 :
				// Lex.g:1:179: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 28 :
				// Lex.g:1:187: WS
				{
				mWS(); 

				}
				break;
			case 29 :
				// Lex.g:1:190: IDENT
				{
				mIDENT(); 

				}
				break;
			case 30 :
				// Lex.g:1:196: SMALLER
				{
				mSMALLER(); 

				}
				break;
			case 31 :
				// Lex.g:1:204: GREATER
				{
				mGREATER(); 

				}
				break;
			case 32 :
				// Lex.g:1:212: GREATEREQUAL
				{
				mGREATEREQUAL(); 

				}
				break;
			case 33 :
				// Lex.g:1:225: NOTEQUAL
				{
				mNOTEQUAL(); 

				}
				break;
			case 34 :
				// Lex.g:1:234: XOR
				{
				mXOR(); 

				}
				break;
			case 35 :
				// Lex.g:1:238: DIVIDE
				{
				mDIVIDE(); 

				}
				break;
			case 36 :
				// Lex.g:1:245: OR
				{
				mOR(); 

				}
				break;

		}
	}


	protected DFA8 dfa8 = new DFA8(this);
	static final String DFA8_eotS =
		"\1\uffff\10\31\4\uffff\1\51\1\53\6\uffff\1\55\4\uffff\1\57\3\uffff\4\31"+
		"\1\64\2\31\1\67\2\31\10\uffff\4\31\1\uffff\2\31\1\uffff\2\31\1\102\1\103"+
		"\1\104\1\105\3\31\1\111\4\uffff\1\112\1\114\1\115\2\uffff\1\31\2\uffff"+
		"\1\117\1\uffff";
	static final String DFA8_eofS =
		"\120\uffff";
	static final String DFA8_minS =
		"\1\11\1\145\1\154\1\150\1\157\1\150\1\146\1\141\1\153\4\uffff\2\75\6\uffff"+
		"\1\56\4\uffff\1\75\3\uffff\1\141\1\163\1\145\1\165\1\60\2\151\1\60\1\154"+
		"\1\151\10\uffff\1\144\1\145\1\156\1\145\1\uffff\1\154\1\164\1\uffff\1"+
		"\163\1\160\4\60\3\145\1\60\4\uffff\3\60\2\uffff\1\156\2\uffff\1\60\1\uffff";
	static final String DFA8_maxS =
		"\1\174\1\145\1\154\1\162\1\157\1\162\1\146\1\141\1\153\4\uffff\2\75\6"+
		"\uffff\1\71\4\uffff\1\75\3\uffff\1\141\1\163\1\145\1\165\1\172\2\151\1"+
		"\172\1\154\1\151\10\uffff\1\144\1\145\1\156\1\145\1\uffff\1\154\1\164"+
		"\1\uffff\1\163\1\160\4\172\3\145\1\172\4\uffff\3\172\2\uffff\1\156\2\uffff"+
		"\1\172\1\uffff";
	static final String DFA8_acceptS =
		"\11\uffff\1\14\1\15\1\16\1\17\2\uffff\1\22\1\23\1\24\1\25\1\26\1\27\1"+
		"\uffff\1\32\1\33\1\34\1\35\1\uffff\1\42\1\43\1\44\12\uffff\1\20\1\36\1"+
		"\41\1\21\1\30\1\31\1\40\1\37\4\uffff\1\4\2\uffff\1\6\12\uffff\1\1\1\2"+
		"\1\3\1\7\3\uffff\1\13\1\5\1\uffff\1\11\1\10\1\uffff\1\12";
	static final String DFA8_specialS =
		"\120\uffff}>";
	static final String[] DFA8_transitionS = {
			"\2\30\2\uffff\1\30\22\uffff\1\30\1\16\4\uffff\1\21\1\26\1\12\1\13\1\20"+
			"\1\23\1\22\1\24\1\uffff\1\34\12\25\1\17\1\11\1\15\1\14\1\32\2\uffff\32"+
			"\31\3\uffff\1\33\1\31\1\uffff\3\31\1\4\1\2\1\7\2\31\1\6\10\31\1\1\1\10"+
			"\1\3\2\31\1\5\3\31\1\27\1\35",
			"\1\36",
			"\1\37",
			"\1\40\11\uffff\1\41",
			"\1\42",
			"\1\43\11\uffff\1\44",
			"\1\45",
			"\1\46",
			"\1\47",
			"",
			"",
			"",
			"",
			"\1\50",
			"\1\52",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\54\1\uffff\12\25",
			"",
			"",
			"",
			"",
			"\1\56",
			"",
			"",
			"",
			"\1\60",
			"\1\61",
			"\1\62",
			"\1\63",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\1\65",
			"\1\66",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\1\70",
			"\1\71",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\72",
			"\1\73",
			"\1\74",
			"\1\75",
			"",
			"\1\76",
			"\1\77",
			"",
			"\1\100",
			"\1\101",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\1\106",
			"\1\107",
			"\1\110",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"",
			"",
			"",
			"",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"\12\31\7\uffff\32\31\6\uffff\13\31\1\113\16\31",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			"",
			"",
			"\1\116",
			"",
			"",
			"\12\31\7\uffff\32\31\6\uffff\32\31",
			""
	};

	static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
	static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
	static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
	static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
	static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
	static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
	static final short[][] DFA8_transition;

	static {
		int numStates = DFA8_transitionS.length;
		DFA8_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
		}
	}

	protected class DFA8 extends DFA {

		public DFA8(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 8;
			this.eot = DFA8_eot;
			this.eof = DFA8_eof;
			this.min = DFA8_min;
			this.max = DFA8_max;
			this.accept = DFA8_accept;
			this.special = DFA8_special;
			this.transition = DFA8_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( READ | ELSE | THEN | DO | WHILE | IF | TRUE | FALSE | WRITE | WRITELN | SKIP | SEMICOLON | OPENPAREN | CLOSEPAREN | EQUAL | SMALLEQUAL | NOT | ASSIGN | MULTIPLY | LOGAND | COMMA | PLUS | MINUS | FLOAT | INTNUM | STRING | COMMENT | WS | IDENT | SMALLER | GREATER | GREATEREQUAL | NOTEQUAL | XOR | DIVIDE | OR );";
		}
	}

}
