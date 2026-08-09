// $ANTLR 3.5.2 Lex.g 2016-05-05 08:17:18

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class Lex extends Lexer {
	public static final int EOF=-1;
	public static final int AND=4;
	public static final int ASSIGN=5;
	public static final int CLOSEPAREN=6;
	public static final int COLON=7;
	public static final int COMMENT=8;
	public static final int DO=9;
	public static final int ELSE=10;
	public static final int EQUAL=11;
	public static final int FALSE=12;
	public static final int GREQ=13;
	public static final int GRTR=14;
	public static final int ID=15;
	public static final int IF=16;
	public static final int INTNUM=17;
	public static final int LESS=18;
	public static final int LSEQ=19;
	public static final int MINUS=20;
	public static final int MULT=21;
	public static final int NOT=22;
	public static final int OPENPAREN=23;
	public static final int PLUS=24;
	public static final int READ=25;
	public static final int SEMICOLON=26;
	public static final int SKIP=27;
	public static final int STRING=28;
	public static final int THEN=29;
	public static final int TRUE=30;
	public static final int WHILE=31;
	public static final int WRITE=32;
	public static final int WRITELN=33;
	public static final int WS=34;

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

	// $ANTLR start "WRITE"
	public final void mWRITE() throws RecognitionException {
		try {
			int _type = WRITE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:8:12: ( 'write' )
			// Lex.g:8:14: 'write'
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
			// Lex.g:9:12: ( 'writeln' )
			// Lex.g:9:14: 'writeln'
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

	// $ANTLR start "READ"
	public final void mREAD() throws RecognitionException {
		try {
			int _type = READ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:10:12: ( 'read' )
			// Lex.g:10:14: 'read'
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

	// $ANTLR start "DO"
	public final void mDO() throws RecognitionException {
		try {
			int _type = DO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:11:12: ( 'do' )
			// Lex.g:11:14: 'do'
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

	// $ANTLR start "ELSE"
	public final void mELSE() throws RecognitionException {
		try {
			int _type = ELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:12:12: ( 'else' )
			// Lex.g:12:14: 'else'
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

	// $ANTLR start "FALSE"
	public final void mFALSE() throws RecognitionException {
		try {
			int _type = FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:13:12: ( 'false' )
			// Lex.g:13:14: 'false'
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

	// $ANTLR start "IF"
	public final void mIF() throws RecognitionException {
		try {
			int _type = IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:14:12: ( 'if' )
			// Lex.g:14:14: 'if'
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

	// $ANTLR start "SKIP"
	public final void mSKIP() throws RecognitionException {
		try {
			int _type = SKIP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:15:12: ( 'skip' )
			// Lex.g:15:14: 'skip'
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

	// $ANTLR start "THEN"
	public final void mTHEN() throws RecognitionException {
		try {
			int _type = THEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:16:12: ( 'then' )
			// Lex.g:16:14: 'then'
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

	// $ANTLR start "TRUE"
	public final void mTRUE() throws RecognitionException {
		try {
			int _type = TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:17:12: ( 'true' )
			// Lex.g:17:14: 'true'
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

	// $ANTLR start "WHILE"
	public final void mWHILE() throws RecognitionException {
		try {
			int _type = WHILE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:18:12: ( 'while' )
			// Lex.g:18:14: 'while'
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

	// $ANTLR start "SEMICOLON"
	public final void mSEMICOLON() throws RecognitionException {
		try {
			int _type = SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:25:14: ( ';' )
			// Lex.g:25:16: ';'
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
			// Lex.g:26:14: ( '(' )
			// Lex.g:26:16: '('
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
			// Lex.g:27:14: ( ')' )
			// Lex.g:27:16: ')'
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

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:28:14: ( ':' )
			// Lex.g:28:16: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "INTNUM"
	public final void mINTNUM() throws RecognitionException {
		try {
			int _type = INTNUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:30:14: ( ( '0' .. '9' )+ )
			// Lex.g:30:16: ( '0' .. '9' )+
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
			loop2:
			while (true) {
				int alt2=3;
				int LA2_0 = input.LA(1);
				if ( (LA2_0=='\'') ) {
					int LA2_1 = input.LA(2);
					if ( (LA2_1=='\'') ) {
						alt2=1;
					}

				}
				else if ( ((LA2_0 >= '\u0000' && LA2_0 <= '&')||(LA2_0 >= '(' && LA2_0 <= '\uFFFF')) ) {
					alt2=2;
				}

				switch (alt2) {
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
					break loop2;
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
			// Lex.g:34:14: ( '{' (~ '}' )* '}' )
			// Lex.g:34:16: '{' (~ '}' )* '}'
			{
			match('{'); 
			// Lex.g:34:20: (~ '}' )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '\u0000' && LA3_0 <= '|')||(LA3_0 >= '~' && LA3_0 <= '\uFFFF')) ) {
					alt3=1;
				}

				switch (alt3) {
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
					break loop3;
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
			// Lex.g:36:14: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
			// Lex.g:36:16: ( ' ' | '\\t' | '\\r' | '\\n' )+
			{
			// Lex.g:36:16: ( ' ' | '\\t' | '\\r' | '\\n' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0=='\r'||LA4_0==' ') ) {
					alt4=1;
				}

				switch (alt4) {
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
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

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:40:14: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
			// Lex.g:40:16: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Lex.g:40:39: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// Lex.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "EQUAL"
	public final void mEQUAL() throws RecognitionException {
		try {
			int _type = EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:42:14: ( '=' )
			// Lex.g:42:16: '='
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

	// $ANTLR start "ASSIGN"
	public final void mASSIGN() throws RecognitionException {
		try {
			int _type = ASSIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:43:11: ( ':=' )
			// Lex.g:43:13: ':='
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

	// $ANTLR start "MULT"
	public final void mMULT() throws RecognitionException {
		try {
			int _type = MULT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:44:14: ( '*' )
			// Lex.g:44:16: '*'
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
	// $ANTLR end "MULT"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:45:14: ( '+' )
			// Lex.g:45:16: '+'
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
			// Lex.g:46:14: ( '-' )
			// Lex.g:46:16: '-'
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

	// $ANTLR start "LESS"
	public final void mLESS() throws RecognitionException {
		try {
			int _type = LESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:47:14: ( '<' )
			// Lex.g:47:16: '<'
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
	// $ANTLR end "LESS"

	// $ANTLR start "LSEQ"
	public final void mLSEQ() throws RecognitionException {
		try {
			int _type = LSEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:48:14: ( '<=' )
			// Lex.g:48:16: '<='
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
	// $ANTLR end "LSEQ"

	// $ANTLR start "GRTR"
	public final void mGRTR() throws RecognitionException {
		try {
			int _type = GRTR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:49:14: ( '>' )
			// Lex.g:49:16: '>'
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
	// $ANTLR end "GRTR"

	// $ANTLR start "GREQ"
	public final void mGREQ() throws RecognitionException {
		try {
			int _type = GREQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:50:14: ( '=>' )
			// Lex.g:50:16: '=>'
			{
			match("=>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREQ"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:51:14: ( '&' )
			// Lex.g:51:16: '&'
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
	// $ANTLR end "AND"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Lex.g:52:14: ( '!' )
			// Lex.g:52:16: '!'
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

	@Override
	public void mTokens() throws RecognitionException {
		// Lex.g:1:8: ( WRITE | WRITELN | READ | DO | ELSE | FALSE | IF | SKIP | THEN | TRUE | WHILE | SEMICOLON | OPENPAREN | CLOSEPAREN | COLON | INTNUM | STRING | COMMENT | WS | ID | EQUAL | ASSIGN | MULT | PLUS | MINUS | LESS | LSEQ | GRTR | GREQ | AND | NOT )
		int alt6=31;
		alt6 = dfa6.predict(input);
		switch (alt6) {
			case 1 :
				// Lex.g:1:10: WRITE
				{
				mWRITE(); 

				}
				break;
			case 2 :
				// Lex.g:1:16: WRITELN
				{
				mWRITELN(); 

				}
				break;
			case 3 :
				// Lex.g:1:24: READ
				{
				mREAD(); 

				}
				break;
			case 4 :
				// Lex.g:1:29: DO
				{
				mDO(); 

				}
				break;
			case 5 :
				// Lex.g:1:32: ELSE
				{
				mELSE(); 

				}
				break;
			case 6 :
				// Lex.g:1:37: FALSE
				{
				mFALSE(); 

				}
				break;
			case 7 :
				// Lex.g:1:43: IF
				{
				mIF(); 

				}
				break;
			case 8 :
				// Lex.g:1:46: SKIP
				{
				mSKIP(); 

				}
				break;
			case 9 :
				// Lex.g:1:51: THEN
				{
				mTHEN(); 

				}
				break;
			case 10 :
				// Lex.g:1:56: TRUE
				{
				mTRUE(); 

				}
				break;
			case 11 :
				// Lex.g:1:61: WHILE
				{
				mWHILE(); 

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
				// Lex.g:1:98: COLON
				{
				mCOLON(); 

				}
				break;
			case 16 :
				// Lex.g:1:104: INTNUM
				{
				mINTNUM(); 

				}
				break;
			case 17 :
				// Lex.g:1:111: STRING
				{
				mSTRING(); 

				}
				break;
			case 18 :
				// Lex.g:1:118: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 19 :
				// Lex.g:1:126: WS
				{
				mWS(); 

				}
				break;
			case 20 :
				// Lex.g:1:129: ID
				{
				mID(); 

				}
				break;
			case 21 :
				// Lex.g:1:132: EQUAL
				{
				mEQUAL(); 

				}
				break;
			case 22 :
				// Lex.g:1:138: ASSIGN
				{
				mASSIGN(); 

				}
				break;
			case 23 :
				// Lex.g:1:145: MULT
				{
				mMULT(); 

				}
				break;
			case 24 :
				// Lex.g:1:150: PLUS
				{
				mPLUS(); 

				}
				break;
			case 25 :
				// Lex.g:1:155: MINUS
				{
				mMINUS(); 

				}
				break;
			case 26 :
				// Lex.g:1:161: LESS
				{
				mLESS(); 

				}
				break;
			case 27 :
				// Lex.g:1:166: LSEQ
				{
				mLSEQ(); 

				}
				break;
			case 28 :
				// Lex.g:1:171: GRTR
				{
				mGRTR(); 

				}
				break;
			case 29 :
				// Lex.g:1:176: GREQ
				{
				mGREQ(); 

				}
				break;
			case 30 :
				// Lex.g:1:181: AND
				{
				mAND(); 

				}
				break;
			case 31 :
				// Lex.g:1:185: NOT
				{
				mNOT(); 

				}
				break;

		}
	}


	protected DFA6 dfa6 = new DFA6(this);
	static final String DFA6_eotS =
		"\1\uffff\10\21\3\uffff\1\45\5\uffff\1\47\3\uffff\1\51\3\uffff\3\21\1\55"+
		"\2\21\1\60\3\21\6\uffff\3\21\1\uffff\2\21\1\uffff\5\21\1\76\1\77\1\21"+
		"\1\101\1\102\1\103\1\105\1\106\2\uffff\1\107\3\uffff\1\21\3\uffff\1\111"+
		"\1\uffff";
	static final String DFA6_eofS =
		"\112\uffff";
	static final String DFA6_minS =
		"\1\11\1\150\1\145\1\157\1\154\1\141\1\146\1\153\1\150\3\uffff\1\75\5\uffff"+
		"\1\76\3\uffff\1\75\3\uffff\2\151\1\141\1\60\1\163\1\154\1\60\1\151\1\145"+
		"\1\165\6\uffff\1\164\1\154\1\144\1\uffff\1\145\1\163\1\uffff\1\160\1\156"+
		"\3\145\2\60\1\145\5\60\2\uffff\1\60\3\uffff\1\156\3\uffff\1\60\1\uffff";
	static final String DFA6_maxS =
		"\1\173\1\162\1\145\1\157\1\154\1\141\1\146\1\153\1\162\3\uffff\1\75\5"+
		"\uffff\1\76\3\uffff\1\75\3\uffff\2\151\1\141\1\172\1\163\1\154\1\172\1"+
		"\151\1\145\1\165\6\uffff\1\164\1\154\1\144\1\uffff\1\145\1\163\1\uffff"+
		"\1\160\1\156\3\145\2\172\1\145\5\172\2\uffff\1\172\3\uffff\1\156\3\uffff"+
		"\1\172\1\uffff";
	static final String DFA6_acceptS =
		"\11\uffff\1\14\1\15\1\16\1\uffff\1\20\1\21\1\22\1\23\1\24\1\uffff\1\27"+
		"\1\30\1\31\1\uffff\1\34\1\36\1\37\12\uffff\1\26\1\17\1\35\1\25\1\33\1"+
		"\32\3\uffff\1\4\2\uffff\1\7\15\uffff\1\3\1\5\1\uffff\1\10\1\11\1\12\1"+
		"\uffff\1\1\1\13\1\6\1\uffff\1\2";
	static final String DFA6_specialS =
		"\112\uffff}>";
	static final String[] DFA6_transitionS = {
			"\2\20\2\uffff\1\20\22\uffff\1\20\1\31\4\uffff\1\30\1\16\1\12\1\13\1\23"+
			"\1\24\1\uffff\1\25\2\uffff\12\15\1\14\1\11\1\26\1\22\1\27\2\uffff\32"+
			"\21\4\uffff\1\21\1\uffff\3\21\1\3\1\4\1\5\2\21\1\6\10\21\1\2\1\7\1\10"+
			"\2\21\1\1\3\21\1\17",
			"\1\33\11\uffff\1\32",
			"\1\34",
			"\1\35",
			"\1\36",
			"\1\37",
			"\1\40",
			"\1\41",
			"\1\42\11\uffff\1\43",
			"",
			"",
			"",
			"\1\44",
			"",
			"",
			"",
			"",
			"",
			"\1\46",
			"",
			"",
			"",
			"\1\50",
			"",
			"",
			"",
			"\1\52",
			"\1\53",
			"\1\54",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\1\56",
			"\1\57",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\1\61",
			"\1\62",
			"\1\63",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\64",
			"\1\65",
			"\1\66",
			"",
			"\1\67",
			"\1\70",
			"",
			"\1\71",
			"\1\72",
			"\1\73",
			"\1\74",
			"\1\75",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\1\100",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\13\21\1\104\16\21",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"",
			"",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			"",
			"",
			"",
			"\1\110",
			"",
			"",
			"",
			"\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
			""
	};

	static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
	static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
	static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
	static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
	static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
	static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
	static final short[][] DFA6_transition;

	static {
		int numStates = DFA6_transitionS.length;
		DFA6_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
		}
	}

	protected class DFA6 extends DFA {

		public DFA6(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 6;
			this.eot = DFA6_eot;
			this.eof = DFA6_eof;
			this.min = DFA6_min;
			this.max = DFA6_max;
			this.accept = DFA6_accept;
			this.special = DFA6_special;
			this.transition = DFA6_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( WRITE | WRITELN | READ | DO | ELSE | FALSE | IF | SKIP | THEN | TRUE | WHILE | SEMICOLON | OPENPAREN | CLOSEPAREN | COLON | INTNUM | STRING | COMMENT | WS | ID | EQUAL | ASSIGN | MULT | PLUS | MINUS | LESS | LSEQ | GRTR | GREQ | AND | NOT );";
		}
	}

}
