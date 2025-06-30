// $ANTLR 3.5.2 Syn.g 2016-04-29 14:45:49

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class Syn extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSIGN", "CLOSEPAREN", "COMMA", 
		"COMMENT", "DIVIDE", "DO", "ELSE", "EQUAL", "FALSE", "FLOAT", "GREATER", 
		"GREATEREQUAL", "IDENT", "IF", "INTNUM", "LOGAND", "MINUS", "MULTIPLY", 
		"NOT", "NOTEQUAL", "OPENPAREN", "OR", "PLUS", "READ", "SEMICOLON", "SKIP", 
		"SMALLEQUAL", "SMALLER", "STRING", "THEN", "TRUE", "WHILE", "WRITE", "WRITELN", 
		"WS", "XOR"
	};
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public Syn(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public Syn(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return Syn.tokenNames; }
	@Override public String getGrammarFileName() { return "Syn.g"; }


		private String cleanString(String s){
			String tmp;
			tmp = s.replaceAll("^'", "");
			s = tmp.replaceAll("'$", "");
			tmp = s.replaceAll("''", "'");
			return tmp;
		}


	protected static class string_scope {
		String tmp;
	}
	protected Stack<string_scope> string_stack = new Stack<string_scope>();

	public static class string_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "string"
	// Syn.g:21:1: string : s= STRING -> STRING[$string::tmp] ;
	public final Syn.string_return string() throws RecognitionException {
		string_stack.push(new string_scope());
		Syn.string_return retval = new Syn.string_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token s=null;

		Object s_tree=null;
		RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

		try {
			// Syn.g:23:5: (s= STRING -> STRING[$string::tmp] )
			// Syn.g:24:5: s= STRING
			{
			s=(Token)match(input,STRING,FOLLOW_STRING_in_string61); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_STRING.add(s);

			if ( state.backtracking==0 ) { string_stack.peek().tmp = cleanString((s!=null?s.getText():null)); }
			// AST REWRITE
			// elements: STRING
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 24:54: -> STRING[$string::tmp]
			{
				adaptor.addChild(root_0, (Object)adaptor.create(STRING, string_stack.peek().tmp));
			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
			string_stack.pop();
		}
		return retval;
	}
	// $ANTLR end "string"


	public static class program_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "program"
	// Syn.g:28:1: program : statements ;
	public final Syn.program_return program() throws RecognitionException {
		Syn.program_return retval = new Syn.program_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope statements1 =null;


		try {
			// Syn.g:28:9: ( statements )
			// Syn.g:29:5: statements
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_statements_in_program81);
			statements1=statements();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, statements1.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "program"


	public static class statements_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "statements"
	// Syn.g:32:1: statements : statement ( SEMICOLON ^ statement )* ;
	public final Syn.statements_return statements() throws RecognitionException {
		Syn.statements_return retval = new Syn.statements_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SEMICOLON3=null;
		ParserRuleReturnScope statement2 =null;
		ParserRuleReturnScope statement4 =null;

		Object SEMICOLON3_tree=null;

		try {
			// Syn.g:32:12: ( statement ( SEMICOLON ^ statement )* )
			// Syn.g:33:5: statement ( SEMICOLON ^ statement )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_statement_in_statements96);
			statement2=statement();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, statement2.getTree());

			// Syn.g:33:15: ( SEMICOLON ^ statement )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==SEMICOLON) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Syn.g:33:17: SEMICOLON ^ statement
					{
					SEMICOLON3=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_statements100); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SEMICOLON3_tree = (Object)adaptor.create(SEMICOLON3);
					root_0 = (Object)adaptor.becomeRoot(SEMICOLON3_tree, root_0);
					}

					pushFollow(FOLLOW_statement_in_statements103);
					statement4=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement4.getTree());

					}
					break;

				default :
					break loop1;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statements"


	public static class statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "statement"
	// Syn.g:36:1: statement : ( variable ASSIGN ^ exp | SKIP | IF ^ boolexp THEN ! statement ELSE ! statement | WHILE ^ boolexp DO ! statement | READ ^ OPENPAREN ! variable CLOSEPAREN !| OPENPAREN ! statements CLOSEPAREN !| WRITE ^ OPENPAREN ! ( string | arg ) CLOSEPAREN !| WRITELN );
	public final Syn.statement_return statement() throws RecognitionException {
		Syn.statement_return retval = new Syn.statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ASSIGN6=null;
		Token SKIP8=null;
		Token IF9=null;
		Token THEN11=null;
		Token ELSE13=null;
		Token WHILE15=null;
		Token DO17=null;
		Token READ19=null;
		Token OPENPAREN20=null;
		Token CLOSEPAREN22=null;
		Token OPENPAREN23=null;
		Token CLOSEPAREN25=null;
		Token WRITE26=null;
		Token OPENPAREN27=null;
		Token CLOSEPAREN30=null;
		Token WRITELN31=null;
		ParserRuleReturnScope variable5 =null;
		ParserRuleReturnScope exp7 =null;
		ParserRuleReturnScope boolexp10 =null;
		ParserRuleReturnScope statement12 =null;
		ParserRuleReturnScope statement14 =null;
		ParserRuleReturnScope boolexp16 =null;
		ParserRuleReturnScope statement18 =null;
		ParserRuleReturnScope variable21 =null;
		ParserRuleReturnScope statements24 =null;
		ParserRuleReturnScope string28 =null;
		ParserRuleReturnScope arg29 =null;

		Object ASSIGN6_tree=null;
		Object SKIP8_tree=null;
		Object IF9_tree=null;
		Object THEN11_tree=null;
		Object ELSE13_tree=null;
		Object WHILE15_tree=null;
		Object DO17_tree=null;
		Object READ19_tree=null;
		Object OPENPAREN20_tree=null;
		Object CLOSEPAREN22_tree=null;
		Object OPENPAREN23_tree=null;
		Object CLOSEPAREN25_tree=null;
		Object WRITE26_tree=null;
		Object OPENPAREN27_tree=null;
		Object CLOSEPAREN30_tree=null;
		Object WRITELN31_tree=null;

		try {
			// Syn.g:36:11: ( variable ASSIGN ^ exp | SKIP | IF ^ boolexp THEN ! statement ELSE ! statement | WHILE ^ boolexp DO ! statement | READ ^ OPENPAREN ! variable CLOSEPAREN !| OPENPAREN ! statements CLOSEPAREN !| WRITE ^ OPENPAREN ! ( string | arg ) CLOSEPAREN !| WRITELN )
			int alt3=8;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt3=1;
				}
				break;
			case SKIP:
				{
				alt3=2;
				}
				break;
			case IF:
				{
				alt3=3;
				}
				break;
			case WHILE:
				{
				alt3=4;
				}
				break;
			case READ:
				{
				alt3=5;
				}
				break;
			case OPENPAREN:
				{
				alt3=6;
				}
				break;
			case WRITE:
				{
				alt3=7;
				}
				break;
			case WRITELN:
				{
				alt3=8;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// Syn.g:37:5: variable ASSIGN ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_variable_in_statement121);
					variable5=variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, variable5.getTree());

					ASSIGN6=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_statement123); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ASSIGN6_tree = (Object)adaptor.create(ASSIGN6);
					root_0 = (Object)adaptor.becomeRoot(ASSIGN6_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_statement126);
					exp7=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp7.getTree());

					}
					break;
				case 2 :
					// Syn.g:38:5: SKIP
					{
					root_0 = (Object)adaptor.nil();


					SKIP8=(Token)match(input,SKIP,FOLLOW_SKIP_in_statement132); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SKIP8_tree = (Object)adaptor.create(SKIP8);
					adaptor.addChild(root_0, SKIP8_tree);
					}

					}
					break;
				case 3 :
					// Syn.g:39:5: IF ^ boolexp THEN ! statement ELSE ! statement
					{
					root_0 = (Object)adaptor.nil();


					IF9=(Token)match(input,IF,FOLLOW_IF_in_statement138); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IF9_tree = (Object)adaptor.create(IF9);
					root_0 = (Object)adaptor.becomeRoot(IF9_tree, root_0);
					}

					pushFollow(FOLLOW_boolexp_in_statement141);
					boolexp10=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp10.getTree());

					THEN11=(Token)match(input,THEN,FOLLOW_THEN_in_statement143); if (state.failed) return retval;
					pushFollow(FOLLOW_statement_in_statement146);
					statement12=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement12.getTree());

					ELSE13=(Token)match(input,ELSE,FOLLOW_ELSE_in_statement148); if (state.failed) return retval;
					pushFollow(FOLLOW_statement_in_statement151);
					statement14=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement14.getTree());

					}
					break;
				case 4 :
					// Syn.g:40:5: WHILE ^ boolexp DO ! statement
					{
					root_0 = (Object)adaptor.nil();


					WHILE15=(Token)match(input,WHILE,FOLLOW_WHILE_in_statement157); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WHILE15_tree = (Object)adaptor.create(WHILE15);
					root_0 = (Object)adaptor.becomeRoot(WHILE15_tree, root_0);
					}

					pushFollow(FOLLOW_boolexp_in_statement160);
					boolexp16=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp16.getTree());

					DO17=(Token)match(input,DO,FOLLOW_DO_in_statement162); if (state.failed) return retval;
					pushFollow(FOLLOW_statement_in_statement165);
					statement18=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement18.getTree());

					}
					break;
				case 5 :
					// Syn.g:41:5: READ ^ OPENPAREN ! variable CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					READ19=(Token)match(input,READ,FOLLOW_READ_in_statement171); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					READ19_tree = (Object)adaptor.create(READ19);
					root_0 = (Object)adaptor.becomeRoot(READ19_tree, root_0);
					}

					OPENPAREN20=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement174); if (state.failed) return retval;
					pushFollow(FOLLOW_variable_in_statement177);
					variable21=variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, variable21.getTree());

					CLOSEPAREN22=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement179); if (state.failed) return retval;
					}
					break;
				case 6 :
					// Syn.g:42:5: OPENPAREN ! statements CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN23=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement186); if (state.failed) return retval;
					pushFollow(FOLLOW_statements_in_statement189);
					statements24=statements();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statements24.getTree());

					CLOSEPAREN25=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement191); if (state.failed) return retval;
					}
					break;
				case 7 :
					// Syn.g:43:5: WRITE ^ OPENPAREN ! ( string | arg ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					WRITE26=(Token)match(input,WRITE,FOLLOW_WRITE_in_statement198); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITE26_tree = (Object)adaptor.create(WRITE26);
					root_0 = (Object)adaptor.becomeRoot(WRITE26_tree, root_0);
					}

					OPENPAREN27=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement201); if (state.failed) return retval;
					// Syn.g:43:23: ( string | arg )
					int alt2=2;
					int LA2_0 = input.LA(1);
					if ( (LA2_0==STRING) ) {
						alt2=1;
					}
					else if ( ((LA2_0 >= FALSE && LA2_0 <= FLOAT)||LA2_0==IDENT||LA2_0==INTNUM||LA2_0==NOT||LA2_0==OPENPAREN||LA2_0==TRUE) ) {
						alt2=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 2, 0, input);
						throw nvae;
					}

					switch (alt2) {
						case 1 :
							// Syn.g:43:25: string
							{
							pushFollow(FOLLOW_string_in_statement206);
							string28=string();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string28.getTree());

							}
							break;
						case 2 :
							// Syn.g:43:34: arg
							{
							pushFollow(FOLLOW_arg_in_statement210);
							arg29=arg();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arg29.getTree());

							}
							break;

					}

					CLOSEPAREN30=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement214); if (state.failed) return retval;
					}
					break;
				case 8 :
					// Syn.g:44:5: WRITELN
					{
					root_0 = (Object)adaptor.nil();


					WRITELN31=(Token)match(input,WRITELN,FOLLOW_WRITELN_in_statement221); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITELN31_tree = (Object)adaptor.create(WRITELN31);
					adaptor.addChild(root_0, WRITELN31_tree);
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statement"


	public static class arg_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arg"
	// Syn.g:47:1: arg : ( ( boolexp )=> boolexp | ( exp )=> exp );
	public final Syn.arg_return arg() throws RecognitionException {
		Syn.arg_return retval = new Syn.arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope boolexp32 =null;
		ParserRuleReturnScope exp33 =null;


		try {
			// Syn.g:47:5: ( ( boolexp )=> boolexp | ( exp )=> exp )
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==NOT) && (synpred1_Syn())) {
				alt4=1;
			}
			else if ( (LA4_0==TRUE) && (synpred1_Syn())) {
				alt4=1;
			}
			else if ( (LA4_0==FALSE) && (synpred1_Syn())) {
				alt4=1;
			}
			else if ( (LA4_0==IDENT) ) {
				int LA4_4 = input.LA(2);
				if ( (synpred1_Syn()) ) {
					alt4=1;
				}
				else if ( (synpred2_Syn()) ) {
					alt4=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA4_0==INTNUM) ) {
				int LA4_5 = input.LA(2);
				if ( (synpred1_Syn()) ) {
					alt4=1;
				}
				else if ( (synpred2_Syn()) ) {
					alt4=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA4_0==FLOAT) ) {
				int LA4_6 = input.LA(2);
				if ( (synpred1_Syn()) ) {
					alt4=1;
				}
				else if ( (synpred2_Syn()) ) {
					alt4=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA4_0==OPENPAREN) ) {
				int LA4_7 = input.LA(2);
				if ( (synpred1_Syn()) ) {
					alt4=1;
				}
				else if ( (synpred2_Syn()) ) {
					alt4=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// Syn.g:48:5: ( boolexp )=> boolexp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolexp_in_arg240);
					boolexp32=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp32.getTree());

					}
					break;
				case 2 :
					// Syn.g:49:5: ( exp )=> exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_arg251);
					exp33=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp33.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arg"


	public static class boolexp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolexp"
	// Syn.g:52:1: boolexp : boolterm ( ( LOGAND ^| OR ^) boolterm )* ;
	public final Syn.boolexp_return boolexp() throws RecognitionException {
		Syn.boolexp_return retval = new Syn.boolexp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LOGAND35=null;
		Token OR36=null;
		ParserRuleReturnScope boolterm34 =null;
		ParserRuleReturnScope boolterm37 =null;

		Object LOGAND35_tree=null;
		Object OR36_tree=null;

		try {
			// Syn.g:52:9: ( boolterm ( ( LOGAND ^| OR ^) boolterm )* )
			// Syn.g:53:3: boolterm ( ( LOGAND ^| OR ^) boolterm )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_boolterm_in_boolexp264);
			boolterm34=boolterm();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm34.getTree());

			// Syn.g:53:12: ( ( LOGAND ^| OR ^) boolterm )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==LOGAND||LA6_0==OR) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// Syn.g:53:13: ( LOGAND ^| OR ^) boolterm
					{
					// Syn.g:53:13: ( LOGAND ^| OR ^)
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0==LOGAND) ) {
						alt5=1;
					}
					else if ( (LA5_0==OR) ) {
						alt5=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 5, 0, input);
						throw nvae;
					}

					switch (alt5) {
						case 1 :
							// Syn.g:53:14: LOGAND ^
							{
							LOGAND35=(Token)match(input,LOGAND,FOLLOW_LOGAND_in_boolexp268); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							LOGAND35_tree = (Object)adaptor.create(LOGAND35);
							root_0 = (Object)adaptor.becomeRoot(LOGAND35_tree, root_0);
							}

							}
							break;
						case 2 :
							// Syn.g:53:24: OR ^
							{
							OR36=(Token)match(input,OR,FOLLOW_OR_in_boolexp273); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							OR36_tree = (Object)adaptor.create(OR36);
							root_0 = (Object)adaptor.becomeRoot(OR36_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_boolterm_in_boolexp277);
					boolterm37=boolterm();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm37.getTree());

					}
					break;

				default :
					break loop6;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolexp"


	public static class boolterm_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolterm"
	// Syn.g:56:1: boolterm : ( NOT ^ bool | bool );
	public final Syn.boolterm_return boolterm() throws RecognitionException {
		Syn.boolterm_return retval = new Syn.boolterm_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT38=null;
		ParserRuleReturnScope bool39 =null;
		ParserRuleReturnScope bool40 =null;

		Object NOT38_tree=null;

		try {
			// Syn.g:56:10: ( NOT ^ bool | bool )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==NOT) ) {
				alt7=1;
			}
			else if ( ((LA7_0 >= FALSE && LA7_0 <= FLOAT)||LA7_0==IDENT||LA7_0==INTNUM||LA7_0==OPENPAREN||LA7_0==TRUE) ) {
				alt7=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// Syn.g:57:5: NOT ^ bool
					{
					root_0 = (Object)adaptor.nil();


					NOT38=(Token)match(input,NOT,FOLLOW_NOT_in_boolterm294); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT38_tree = (Object)adaptor.create(NOT38);
					root_0 = (Object)adaptor.becomeRoot(NOT38_tree, root_0);
					}

					pushFollow(FOLLOW_bool_in_boolterm297);
					bool39=bool();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, bool39.getTree());

					}
					break;
				case 2 :
					// Syn.g:58:5: bool
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_bool_in_boolterm303);
					bool40=bool();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, bool40.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolterm"


	public static class bool_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "bool"
	// Syn.g:61:1: bool : ( TRUE | FALSE | ( exp EQUAL exp )=> exp EQUAL ^ exp | ( exp SMALLEQUAL exp )=> exp SMALLEQUAL ^ exp | ( exp SMALLER exp )=> exp SMALLER ^ exp | ( exp GREATER exp )=> exp GREATER ^ exp | ( exp GREATEREQUAL exp )=> exp GREATEREQUAL ^ exp | ( exp NOTEQUAL exp )=> exp NOTEQUAL ^ exp | OPENPAREN ! boolexp CLOSEPAREN !);
	public final Syn.bool_return bool() throws RecognitionException {
		Syn.bool_return retval = new Syn.bool_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRUE41=null;
		Token FALSE42=null;
		Token EQUAL44=null;
		Token SMALLEQUAL47=null;
		Token SMALLER50=null;
		Token GREATER53=null;
		Token GREATEREQUAL56=null;
		Token NOTEQUAL59=null;
		Token OPENPAREN61=null;
		Token CLOSEPAREN63=null;
		ParserRuleReturnScope exp43 =null;
		ParserRuleReturnScope exp45 =null;
		ParserRuleReturnScope exp46 =null;
		ParserRuleReturnScope exp48 =null;
		ParserRuleReturnScope exp49 =null;
		ParserRuleReturnScope exp51 =null;
		ParserRuleReturnScope exp52 =null;
		ParserRuleReturnScope exp54 =null;
		ParserRuleReturnScope exp55 =null;
		ParserRuleReturnScope exp57 =null;
		ParserRuleReturnScope exp58 =null;
		ParserRuleReturnScope exp60 =null;
		ParserRuleReturnScope boolexp62 =null;

		Object TRUE41_tree=null;
		Object FALSE42_tree=null;
		Object EQUAL44_tree=null;
		Object SMALLEQUAL47_tree=null;
		Object SMALLER50_tree=null;
		Object GREATER53_tree=null;
		Object GREATEREQUAL56_tree=null;
		Object NOTEQUAL59_tree=null;
		Object OPENPAREN61_tree=null;
		Object CLOSEPAREN63_tree=null;

		try {
			// Syn.g:61:6: ( TRUE | FALSE | ( exp EQUAL exp )=> exp EQUAL ^ exp | ( exp SMALLEQUAL exp )=> exp SMALLEQUAL ^ exp | ( exp SMALLER exp )=> exp SMALLER ^ exp | ( exp GREATER exp )=> exp GREATER ^ exp | ( exp GREATEREQUAL exp )=> exp GREATEREQUAL ^ exp | ( exp NOTEQUAL exp )=> exp NOTEQUAL ^ exp | OPENPAREN ! boolexp CLOSEPAREN !)
			int alt8=9;
			switch ( input.LA(1) ) {
			case TRUE:
				{
				alt8=1;
				}
				break;
			case FALSE:
				{
				alt8=2;
				}
				break;
			case IDENT:
				{
				int LA8_3 = input.LA(2);
				if ( (synpred3_Syn()) ) {
					alt8=3;
				}
				else if ( (synpred4_Syn()) ) {
					alt8=4;
				}
				else if ( (synpred5_Syn()) ) {
					alt8=5;
				}
				else if ( (synpred6_Syn()) ) {
					alt8=6;
				}
				else if ( (synpred7_Syn()) ) {
					alt8=7;
				}
				else if ( (synpred8_Syn()) ) {
					alt8=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 8, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INTNUM:
				{
				int LA8_4 = input.LA(2);
				if ( (synpred3_Syn()) ) {
					alt8=3;
				}
				else if ( (synpred4_Syn()) ) {
					alt8=4;
				}
				else if ( (synpred5_Syn()) ) {
					alt8=5;
				}
				else if ( (synpred6_Syn()) ) {
					alt8=6;
				}
				else if ( (synpred7_Syn()) ) {
					alt8=7;
				}
				else if ( (synpred8_Syn()) ) {
					alt8=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 8, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case FLOAT:
				{
				int LA8_5 = input.LA(2);
				if ( (synpred3_Syn()) ) {
					alt8=3;
				}
				else if ( (synpred4_Syn()) ) {
					alt8=4;
				}
				else if ( (synpred5_Syn()) ) {
					alt8=5;
				}
				else if ( (synpred6_Syn()) ) {
					alt8=6;
				}
				else if ( (synpred7_Syn()) ) {
					alt8=7;
				}
				else if ( (synpred8_Syn()) ) {
					alt8=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 8, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case OPENPAREN:
				{
				int LA8_6 = input.LA(2);
				if ( (synpred3_Syn()) ) {
					alt8=3;
				}
				else if ( (synpred4_Syn()) ) {
					alt8=4;
				}
				else if ( (synpred5_Syn()) ) {
					alt8=5;
				}
				else if ( (synpred6_Syn()) ) {
					alt8=6;
				}
				else if ( (synpred7_Syn()) ) {
					alt8=7;
				}
				else if ( (synpred8_Syn()) ) {
					alt8=8;
				}
				else if ( (true) ) {
					alt8=9;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}
			switch (alt8) {
				case 1 :
					// Syn.g:62:5: TRUE
					{
					root_0 = (Object)adaptor.nil();


					TRUE41=(Token)match(input,TRUE,FOLLOW_TRUE_in_bool318); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					TRUE41_tree = (Object)adaptor.create(TRUE41);
					adaptor.addChild(root_0, TRUE41_tree);
					}

					}
					break;
				case 2 :
					// Syn.g:63:5: FALSE
					{
					root_0 = (Object)adaptor.nil();


					FALSE42=(Token)match(input,FALSE,FOLLOW_FALSE_in_bool324); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FALSE42_tree = (Object)adaptor.create(FALSE42);
					adaptor.addChild(root_0, FALSE42_tree);
					}

					}
					break;
				case 3 :
					// Syn.g:64:5: ( exp EQUAL exp )=> exp EQUAL ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool339);
					exp43=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp43.getTree());

					EQUAL44=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_bool341); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EQUAL44_tree = (Object)adaptor.create(EQUAL44);
					root_0 = (Object)adaptor.becomeRoot(EQUAL44_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool344);
					exp45=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp45.getTree());

					}
					break;
				case 4 :
					// Syn.g:65:5: ( exp SMALLEQUAL exp )=> exp SMALLEQUAL ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool358);
					exp46=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp46.getTree());

					SMALLEQUAL47=(Token)match(input,SMALLEQUAL,FOLLOW_SMALLEQUAL_in_bool360); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SMALLEQUAL47_tree = (Object)adaptor.create(SMALLEQUAL47);
					root_0 = (Object)adaptor.becomeRoot(SMALLEQUAL47_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool363);
					exp48=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp48.getTree());

					}
					break;
				case 5 :
					// Syn.g:66:5: ( exp SMALLER exp )=> exp SMALLER ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool377);
					exp49=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp49.getTree());

					SMALLER50=(Token)match(input,SMALLER,FOLLOW_SMALLER_in_bool379); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SMALLER50_tree = (Object)adaptor.create(SMALLER50);
					root_0 = (Object)adaptor.becomeRoot(SMALLER50_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool382);
					exp51=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp51.getTree());

					}
					break;
				case 6 :
					// Syn.g:67:5: ( exp GREATER exp )=> exp GREATER ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool396);
					exp52=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp52.getTree());

					GREATER53=(Token)match(input,GREATER,FOLLOW_GREATER_in_bool398); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					GREATER53_tree = (Object)adaptor.create(GREATER53);
					root_0 = (Object)adaptor.becomeRoot(GREATER53_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool401);
					exp54=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp54.getTree());

					}
					break;
				case 7 :
					// Syn.g:68:5: ( exp GREATEREQUAL exp )=> exp GREATEREQUAL ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool415);
					exp55=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp55.getTree());

					GREATEREQUAL56=(Token)match(input,GREATEREQUAL,FOLLOW_GREATEREQUAL_in_bool417); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					GREATEREQUAL56_tree = (Object)adaptor.create(GREATEREQUAL56);
					root_0 = (Object)adaptor.becomeRoot(GREATEREQUAL56_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool420);
					exp57=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp57.getTree());

					}
					break;
				case 8 :
					// Syn.g:69:5: ( exp NOTEQUAL exp )=> exp NOTEQUAL ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool434);
					exp58=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp58.getTree());

					NOTEQUAL59=(Token)match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_bool436); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOTEQUAL59_tree = (Object)adaptor.create(NOTEQUAL59);
					root_0 = (Object)adaptor.becomeRoot(NOTEQUAL59_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool439);
					exp60=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp60.getTree());

					}
					break;
				case 9 :
					// Syn.g:70:5: OPENPAREN ! boolexp CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN61=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_bool445); if (state.failed) return retval;
					pushFollow(FOLLOW_boolexp_in_bool448);
					boolexp62=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp62.getTree());

					CLOSEPAREN63=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_bool450); if (state.failed) return retval;
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "bool"


	public static class exp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "exp"
	// Syn.g:73:1: exp : term ( ( PLUS ^| MINUS ^| XOR ^) term )* ;
	public final Syn.exp_return exp() throws RecognitionException {
		Syn.exp_return retval = new Syn.exp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PLUS65=null;
		Token MINUS66=null;
		Token XOR67=null;
		ParserRuleReturnScope term64 =null;
		ParserRuleReturnScope term68 =null;

		Object PLUS65_tree=null;
		Object MINUS66_tree=null;
		Object XOR67_tree=null;

		try {
			// Syn.g:73:5: ( term ( ( PLUS ^| MINUS ^| XOR ^) term )* )
			// Syn.g:74:3: term ( ( PLUS ^| MINUS ^| XOR ^) term )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_term_in_exp464);
			term64=term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, term64.getTree());

			// Syn.g:74:8: ( ( PLUS ^| MINUS ^| XOR ^) term )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==MINUS||LA10_0==PLUS||LA10_0==XOR) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// Syn.g:74:9: ( PLUS ^| MINUS ^| XOR ^) term
					{
					// Syn.g:74:9: ( PLUS ^| MINUS ^| XOR ^)
					int alt9=3;
					switch ( input.LA(1) ) {
					case PLUS:
						{
						alt9=1;
						}
						break;
					case MINUS:
						{
						alt9=2;
						}
						break;
					case XOR:
						{
						alt9=3;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 9, 0, input);
						throw nvae;
					}
					switch (alt9) {
						case 1 :
							// Syn.g:74:10: PLUS ^
							{
							PLUS65=(Token)match(input,PLUS,FOLLOW_PLUS_in_exp468); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							PLUS65_tree = (Object)adaptor.create(PLUS65);
							root_0 = (Object)adaptor.becomeRoot(PLUS65_tree, root_0);
							}

							}
							break;
						case 2 :
							// Syn.g:74:18: MINUS ^
							{
							MINUS66=(Token)match(input,MINUS,FOLLOW_MINUS_in_exp473); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							MINUS66_tree = (Object)adaptor.create(MINUS66);
							root_0 = (Object)adaptor.becomeRoot(MINUS66_tree, root_0);
							}

							}
							break;
						case 3 :
							// Syn.g:74:27: XOR ^
							{
							XOR67=(Token)match(input,XOR,FOLLOW_XOR_in_exp478); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							XOR67_tree = (Object)adaptor.create(XOR67);
							root_0 = (Object)adaptor.becomeRoot(XOR67_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_term_in_exp482);
					term68=term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, term68.getTree());

					}
					break;

				default :
					break loop10;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exp"


	public static class term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "term"
	// Syn.g:77:1: term : factor ( ( MULTIPLY ^| DIVIDE ^) factor )* ;
	public final Syn.term_return term() throws RecognitionException {
		Syn.term_return retval = new Syn.term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token MULTIPLY70=null;
		Token DIVIDE71=null;
		ParserRuleReturnScope factor69 =null;
		ParserRuleReturnScope factor72 =null;

		Object MULTIPLY70_tree=null;
		Object DIVIDE71_tree=null;

		try {
			// Syn.g:77:6: ( factor ( ( MULTIPLY ^| DIVIDE ^) factor )* )
			// Syn.g:78:3: factor ( ( MULTIPLY ^| DIVIDE ^) factor )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_factor_in_term497);
			factor69=factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, factor69.getTree());

			// Syn.g:78:10: ( ( MULTIPLY ^| DIVIDE ^) factor )*
			loop12:
			while (true) {
				int alt12=2;
				int LA12_0 = input.LA(1);
				if ( (LA12_0==DIVIDE||LA12_0==MULTIPLY) ) {
					alt12=1;
				}

				switch (alt12) {
				case 1 :
					// Syn.g:78:11: ( MULTIPLY ^| DIVIDE ^) factor
					{
					// Syn.g:78:11: ( MULTIPLY ^| DIVIDE ^)
					int alt11=2;
					int LA11_0 = input.LA(1);
					if ( (LA11_0==MULTIPLY) ) {
						alt11=1;
					}
					else if ( (LA11_0==DIVIDE) ) {
						alt11=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 11, 0, input);
						throw nvae;
					}

					switch (alt11) {
						case 1 :
							// Syn.g:78:12: MULTIPLY ^
							{
							MULTIPLY70=(Token)match(input,MULTIPLY,FOLLOW_MULTIPLY_in_term501); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							MULTIPLY70_tree = (Object)adaptor.create(MULTIPLY70);
							root_0 = (Object)adaptor.becomeRoot(MULTIPLY70_tree, root_0);
							}

							}
							break;
						case 2 :
							// Syn.g:78:24: DIVIDE ^
							{
							DIVIDE71=(Token)match(input,DIVIDE,FOLLOW_DIVIDE_in_term506); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							DIVIDE71_tree = (Object)adaptor.create(DIVIDE71);
							root_0 = (Object)adaptor.becomeRoot(DIVIDE71_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_factor_in_term511);
					factor72=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, factor72.getTree());

					}
					break;

				default :
					break loop12;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "term"


	public static class factor_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "factor"
	// Syn.g:81:1: factor : ( variable | INTNUM | FLOAT | OPENPAREN ! exp CLOSEPAREN !);
	public final Syn.factor_return factor() throws RecognitionException {
		Syn.factor_return retval = new Syn.factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token INTNUM74=null;
		Token FLOAT75=null;
		Token OPENPAREN76=null;
		Token CLOSEPAREN78=null;
		ParserRuleReturnScope variable73 =null;
		ParserRuleReturnScope exp77 =null;

		Object INTNUM74_tree=null;
		Object FLOAT75_tree=null;
		Object OPENPAREN76_tree=null;
		Object CLOSEPAREN78_tree=null;

		try {
			// Syn.g:81:8: ( variable | INTNUM | FLOAT | OPENPAREN ! exp CLOSEPAREN !)
			int alt13=4;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt13=1;
				}
				break;
			case INTNUM:
				{
				alt13=2;
				}
				break;
			case FLOAT:
				{
				alt13=3;
				}
				break;
			case OPENPAREN:
				{
				alt13=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}
			switch (alt13) {
				case 1 :
					// Syn.g:82:5: variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_variable_in_factor528);
					variable73=variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, variable73.getTree());

					}
					break;
				case 2 :
					// Syn.g:83:5: INTNUM
					{
					root_0 = (Object)adaptor.nil();


					INTNUM74=(Token)match(input,INTNUM,FOLLOW_INTNUM_in_factor534); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INTNUM74_tree = (Object)adaptor.create(INTNUM74);
					adaptor.addChild(root_0, INTNUM74_tree);
					}

					}
					break;
				case 3 :
					// Syn.g:84:5: FLOAT
					{
					root_0 = (Object)adaptor.nil();


					FLOAT75=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_factor540); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FLOAT75_tree = (Object)adaptor.create(FLOAT75);
					adaptor.addChild(root_0, FLOAT75_tree);
					}

					}
					break;
				case 4 :
					// Syn.g:85:5: OPENPAREN ! exp CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN76=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_factor546); if (state.failed) return retval;
					pushFollow(FOLLOW_exp_in_factor549);
					exp77=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp77.getTree());

					CLOSEPAREN78=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_factor551); if (state.failed) return retval;
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "factor"


	public static class variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "variable"
	// Syn.g:88:1: variable : IDENT ;
	public final Syn.variable_return variable() throws RecognitionException {
		Syn.variable_return retval = new Syn.variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT79=null;

		Object IDENT79_tree=null;

		try {
			// Syn.g:88:10: ( IDENT )
			// Syn.g:89:3: IDENT
			{
			root_0 = (Object)adaptor.nil();


			IDENT79=(Token)match(input,IDENT,FOLLOW_IDENT_in_variable565); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IDENT79_tree = (Object)adaptor.create(IDENT79);
			adaptor.addChild(root_0, IDENT79_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "variable"

	// $ANTLR start synpred1_Syn
	public final void synpred1_Syn_fragment() throws RecognitionException {
		// Syn.g:48:5: ( boolexp )
		// Syn.g:48:6: boolexp
		{
		pushFollow(FOLLOW_boolexp_in_synpred1_Syn237);
		boolexp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred1_Syn

	// $ANTLR start synpred2_Syn
	public final void synpred2_Syn_fragment() throws RecognitionException {
		// Syn.g:49:5: ( exp )
		// Syn.g:49:6: exp
		{
		pushFollow(FOLLOW_exp_in_synpred2_Syn247);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred2_Syn

	// $ANTLR start synpred3_Syn
	public final void synpred3_Syn_fragment() throws RecognitionException {
		// Syn.g:64:5: ( exp EQUAL exp )
		// Syn.g:64:6: exp EQUAL exp
		{
		pushFollow(FOLLOW_exp_in_synpred3_Syn331);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,EQUAL,FOLLOW_EQUAL_in_synpred3_Syn334); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred3_Syn336);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred3_Syn

	// $ANTLR start synpred4_Syn
	public final void synpred4_Syn_fragment() throws RecognitionException {
		// Syn.g:65:5: ( exp SMALLEQUAL exp )
		// Syn.g:65:6: exp SMALLEQUAL exp
		{
		pushFollow(FOLLOW_exp_in_synpred4_Syn351);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,SMALLEQUAL,FOLLOW_SMALLEQUAL_in_synpred4_Syn353); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred4_Syn355);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred4_Syn

	// $ANTLR start synpred5_Syn
	public final void synpred5_Syn_fragment() throws RecognitionException {
		// Syn.g:66:5: ( exp SMALLER exp )
		// Syn.g:66:6: exp SMALLER exp
		{
		pushFollow(FOLLOW_exp_in_synpred5_Syn370);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,SMALLER,FOLLOW_SMALLER_in_synpred5_Syn372); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred5_Syn374);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred5_Syn

	// $ANTLR start synpred6_Syn
	public final void synpred6_Syn_fragment() throws RecognitionException {
		// Syn.g:67:5: ( exp GREATER exp )
		// Syn.g:67:6: exp GREATER exp
		{
		pushFollow(FOLLOW_exp_in_synpred6_Syn389);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,GREATER,FOLLOW_GREATER_in_synpred6_Syn391); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred6_Syn393);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred6_Syn

	// $ANTLR start synpred7_Syn
	public final void synpred7_Syn_fragment() throws RecognitionException {
		// Syn.g:68:5: ( exp GREATEREQUAL exp )
		// Syn.g:68:6: exp GREATEREQUAL exp
		{
		pushFollow(FOLLOW_exp_in_synpred7_Syn408);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,GREATEREQUAL,FOLLOW_GREATEREQUAL_in_synpred7_Syn410); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred7_Syn412);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred7_Syn

	// $ANTLR start synpred8_Syn
	public final void synpred8_Syn_fragment() throws RecognitionException {
		// Syn.g:69:5: ( exp NOTEQUAL exp )
		// Syn.g:69:6: exp NOTEQUAL exp
		{
		pushFollow(FOLLOW_exp_in_synpred8_Syn427);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_synpred8_Syn429); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred8_Syn431);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred8_Syn

	// Delegated rules

	public final boolean synpred1_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred1_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred2_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred2_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred3_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred3_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred5_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred5_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred4_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred4_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred6_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred6_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred8_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred8_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred7_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred7_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}



	public static final BitSet FOLLOW_STRING_in_string61 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statements_in_program81 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_statements96 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_SEMICOLON_in_statements100 = new BitSet(new long[]{0x0000003829030000L});
	public static final BitSet FOLLOW_statement_in_statements103 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_variable_in_statement121 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_ASSIGN_in_statement123 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_statement126 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SKIP_in_statement132 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_statement138 = new BitSet(new long[]{0x0000000401453000L});
	public static final BitSet FOLLOW_boolexp_in_statement141 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_THEN_in_statement143 = new BitSet(new long[]{0x0000003829030000L});
	public static final BitSet FOLLOW_statement_in_statement146 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_ELSE_in_statement148 = new BitSet(new long[]{0x0000003829030000L});
	public static final BitSet FOLLOW_statement_in_statement151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHILE_in_statement157 = new BitSet(new long[]{0x0000000401453000L});
	public static final BitSet FOLLOW_boolexp_in_statement160 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_DO_in_statement162 = new BitSet(new long[]{0x0000003829030000L});
	public static final BitSet FOLLOW_statement_in_statement165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_READ_in_statement171 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement174 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_variable_in_statement177 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement186 = new BitSet(new long[]{0x0000003829030000L});
	public static final BitSet FOLLOW_statements_in_statement189 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_statement198 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement201 = new BitSet(new long[]{0x0000000501453000L});
	public static final BitSet FOLLOW_string_in_statement206 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_arg_in_statement210 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement214 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITELN_in_statement221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolexp_in_arg240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_arg251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolterm_in_boolexp264 = new BitSet(new long[]{0x0000000002080002L});
	public static final BitSet FOLLOW_LOGAND_in_boolexp268 = new BitSet(new long[]{0x0000000401453000L});
	public static final BitSet FOLLOW_OR_in_boolexp273 = new BitSet(new long[]{0x0000000401453000L});
	public static final BitSet FOLLOW_boolterm_in_boolexp277 = new BitSet(new long[]{0x0000000002080002L});
	public static final BitSet FOLLOW_NOT_in_boolterm294 = new BitSet(new long[]{0x0000000401053000L});
	public static final BitSet FOLLOW_bool_in_boolterm297 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_bool_in_boolterm303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_bool318 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FALSE_in_bool324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool339 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_EQUAL_in_bool341 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool358 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_SMALLEQUAL_in_bool360 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool377 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_SMALLER_in_bool379 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool396 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_GREATER_in_bool398 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool415 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_GREATEREQUAL_in_bool417 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool420 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool434 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_NOTEQUAL_in_bool436 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_bool439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_bool445 = new BitSet(new long[]{0x0000000401453000L});
	public static final BitSet FOLLOW_boolexp_in_bool448 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_bool450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_term_in_exp464 = new BitSet(new long[]{0x0000008004100002L});
	public static final BitSet FOLLOW_PLUS_in_exp468 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_MINUS_in_exp473 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_XOR_in_exp478 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_term_in_exp482 = new BitSet(new long[]{0x0000008004100002L});
	public static final BitSet FOLLOW_factor_in_term497 = new BitSet(new long[]{0x0000000000200102L});
	public static final BitSet FOLLOW_MULTIPLY_in_term501 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_DIVIDE_in_term506 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_factor_in_term511 = new BitSet(new long[]{0x0000000000200102L});
	public static final BitSet FOLLOW_variable_in_factor528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTNUM_in_factor534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FLOAT_in_factor540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_factor546 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_factor549 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_factor551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_variable565 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolexp_in_synpred1_Syn237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred2_Syn247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred3_Syn331 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_EQUAL_in_synpred3_Syn334 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred3_Syn336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred4_Syn351 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_SMALLEQUAL_in_synpred4_Syn353 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred4_Syn355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred5_Syn370 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_SMALLER_in_synpred5_Syn372 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred5_Syn374 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred6_Syn389 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_GREATER_in_synpred6_Syn391 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred6_Syn393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred7_Syn408 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_GREATEREQUAL_in_synpred7_Syn410 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred7_Syn412 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred8_Syn427 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_NOTEQUAL_in_synpred8_Syn429 = new BitSet(new long[]{0x0000000001052000L});
	public static final BitSet FOLLOW_exp_in_synpred8_Syn431 = new BitSet(new long[]{0x0000000000000002L});
}
