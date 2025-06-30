// $ANTLR 3.5.2 Syn.g 2016-05-05 08:17:18

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
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "ASSIGN", "CLOSEPAREN", 
		"COLON", "COMMENT", "DO", "ELSE", "EQUAL", "FALSE", "GREQ", "GRTR", "ID", 
		"IF", "INTNUM", "LESS", "LSEQ", "MINUS", "MULT", "NOT", "OPENPAREN", "PLUS", 
		"READ", "SEMICOLON", "SKIP", "STRING", "THEN", "TRUE", "WHILE", "WRITE", 
		"WRITELN", "WS"
	};
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


	public static class program_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "program"
	// Syn.g:22:1: program : statements ;
	public final Syn.program_return program() throws RecognitionException {
		Syn.program_return retval = new Syn.program_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope statements1 =null;


		try {
			// Syn.g:22:9: ( statements )
			// Syn.g:23:5: statements
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_statements_in_program56);
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
	// Syn.g:26:1: statements : statement ( SEMICOLON ^ statement )* ;
	public final Syn.statements_return statements() throws RecognitionException {
		Syn.statements_return retval = new Syn.statements_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SEMICOLON3=null;
		ParserRuleReturnScope statement2 =null;
		ParserRuleReturnScope statement4 =null;

		Object SEMICOLON3_tree=null;

		try {
			// Syn.g:26:12: ( statement ( SEMICOLON ^ statement )* )
			// Syn.g:27:5: statement ( SEMICOLON ^ statement )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_statement_in_statements71);
			statement2=statement();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, statement2.getTree());

			// Syn.g:27:15: ( SEMICOLON ^ statement )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==SEMICOLON) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Syn.g:27:17: SEMICOLON ^ statement
					{
					SEMICOLON3=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_statements75); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SEMICOLON3_tree = (Object)adaptor.create(SEMICOLON3);
					root_0 = (Object)adaptor.becomeRoot(SEMICOLON3_tree, root_0);
					}

					pushFollow(FOLLOW_statement_in_statements78);
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
	// Syn.g:30:1: statement : ( WRITE ^ OPENPAREN ! ( INTNUM | string ) CLOSEPAREN !| ID ASSIGN ^ exp | WRITELN ^| SKIP | IF ^ boolexp THEN statement ELSE statement | WHILE ^ boolexp DO ^ statement | READ ^ OPENPAREN ! ID CLOSEPAREN !| WRITE ^ OPENPAREN ! ( exp ) CLOSEPAREN !| WRITE ^ OPENPAREN ! ( boolexp ) CLOSEPAREN !| WRITE ^ OPENPAREN ! ( string ) CLOSEPAREN !| OPENPAREN ! ( statements ) CLOSEPAREN !);
	public final Syn.statement_return statement() throws RecognitionException {
		Syn.statement_return retval = new Syn.statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WRITE5=null;
		Token OPENPAREN6=null;
		Token INTNUM7=null;
		Token CLOSEPAREN9=null;
		Token ID10=null;
		Token ASSIGN11=null;
		Token WRITELN13=null;
		Token SKIP14=null;
		Token IF15=null;
		Token THEN17=null;
		Token ELSE19=null;
		Token WHILE21=null;
		Token DO23=null;
		Token READ25=null;
		Token OPENPAREN26=null;
		Token ID27=null;
		Token CLOSEPAREN28=null;
		Token WRITE29=null;
		Token OPENPAREN30=null;
		Token CLOSEPAREN32=null;
		Token WRITE33=null;
		Token OPENPAREN34=null;
		Token CLOSEPAREN36=null;
		Token WRITE37=null;
		Token OPENPAREN38=null;
		Token CLOSEPAREN40=null;
		Token OPENPAREN41=null;
		Token CLOSEPAREN43=null;
		ParserRuleReturnScope string8 =null;
		ParserRuleReturnScope exp12 =null;
		ParserRuleReturnScope boolexp16 =null;
		ParserRuleReturnScope statement18 =null;
		ParserRuleReturnScope statement20 =null;
		ParserRuleReturnScope boolexp22 =null;
		ParserRuleReturnScope statement24 =null;
		ParserRuleReturnScope exp31 =null;
		ParserRuleReturnScope boolexp35 =null;
		ParserRuleReturnScope string39 =null;
		ParserRuleReturnScope statements42 =null;

		Object WRITE5_tree=null;
		Object OPENPAREN6_tree=null;
		Object INTNUM7_tree=null;
		Object CLOSEPAREN9_tree=null;
		Object ID10_tree=null;
		Object ASSIGN11_tree=null;
		Object WRITELN13_tree=null;
		Object SKIP14_tree=null;
		Object IF15_tree=null;
		Object THEN17_tree=null;
		Object ELSE19_tree=null;
		Object WHILE21_tree=null;
		Object DO23_tree=null;
		Object READ25_tree=null;
		Object OPENPAREN26_tree=null;
		Object ID27_tree=null;
		Object CLOSEPAREN28_tree=null;
		Object WRITE29_tree=null;
		Object OPENPAREN30_tree=null;
		Object CLOSEPAREN32_tree=null;
		Object WRITE33_tree=null;
		Object OPENPAREN34_tree=null;
		Object CLOSEPAREN36_tree=null;
		Object WRITE37_tree=null;
		Object OPENPAREN38_tree=null;
		Object CLOSEPAREN40_tree=null;
		Object OPENPAREN41_tree=null;
		Object CLOSEPAREN43_tree=null;

		try {
			// Syn.g:30:11: ( WRITE ^ OPENPAREN ! ( INTNUM | string ) CLOSEPAREN !| ID ASSIGN ^ exp | WRITELN ^| SKIP | IF ^ boolexp THEN statement ELSE statement | WHILE ^ boolexp DO ^ statement | READ ^ OPENPAREN ! ID CLOSEPAREN !| WRITE ^ OPENPAREN ! ( exp ) CLOSEPAREN !| WRITE ^ OPENPAREN ! ( boolexp ) CLOSEPAREN !| WRITE ^ OPENPAREN ! ( string ) CLOSEPAREN !| OPENPAREN ! ( statements ) CLOSEPAREN !)
			int alt3=11;
			switch ( input.LA(1) ) {
			case WRITE:
				{
				int LA3_1 = input.LA(2);
				if ( (synpred3_Syn()) ) {
					alt3=1;
				}
				else if ( (synpred10_Syn()) ) {
					alt3=8;
				}
				else if ( (synpred11_Syn()) ) {
					alt3=9;
				}
				else if ( (synpred12_Syn()) ) {
					alt3=10;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case ID:
				{
				alt3=2;
				}
				break;
			case WRITELN:
				{
				alt3=3;
				}
				break;
			case SKIP:
				{
				alt3=4;
				}
				break;
			case IF:
				{
				alt3=5;
				}
				break;
			case WHILE:
				{
				alt3=6;
				}
				break;
			case READ:
				{
				alt3=7;
				}
				break;
			case OPENPAREN:
				{
				alt3=11;
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
					// Syn.g:31:5: WRITE ^ OPENPAREN ! ( INTNUM | string ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					WRITE5=(Token)match(input,WRITE,FOLLOW_WRITE_in_statement96); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITE5_tree = (Object)adaptor.create(WRITE5);
					root_0 = (Object)adaptor.becomeRoot(WRITE5_tree, root_0);
					}

					OPENPAREN6=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement99); if (state.failed) return retval;
					// Syn.g:31:23: ( INTNUM | string )
					int alt2=2;
					int LA2_0 = input.LA(1);
					if ( (LA2_0==INTNUM) ) {
						alt2=1;
					}
					else if ( (LA2_0==STRING) ) {
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
							// Syn.g:31:25: INTNUM
							{
							INTNUM7=(Token)match(input,INTNUM,FOLLOW_INTNUM_in_statement104); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INTNUM7_tree = (Object)adaptor.create(INTNUM7);
							adaptor.addChild(root_0, INTNUM7_tree);
							}

							}
							break;
						case 2 :
							// Syn.g:31:34: string
							{
							pushFollow(FOLLOW_string_in_statement108);
							string8=string();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string8.getTree());

							}
							break;

					}

					CLOSEPAREN9=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement112); if (state.failed) return retval;
					}
					break;
				case 2 :
					// Syn.g:32:5: ID ASSIGN ^ exp
					{
					root_0 = (Object)adaptor.nil();


					ID10=(Token)match(input,ID,FOLLOW_ID_in_statement119); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ID10_tree = (Object)adaptor.create(ID10);
					adaptor.addChild(root_0, ID10_tree);
					}

					ASSIGN11=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_statement121); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ASSIGN11_tree = (Object)adaptor.create(ASSIGN11);
					root_0 = (Object)adaptor.becomeRoot(ASSIGN11_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_statement124);
					exp12=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp12.getTree());

					}
					break;
				case 3 :
					// Syn.g:33:5: WRITELN ^
					{
					root_0 = (Object)adaptor.nil();


					WRITELN13=(Token)match(input,WRITELN,FOLLOW_WRITELN_in_statement130); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITELN13_tree = (Object)adaptor.create(WRITELN13);
					root_0 = (Object)adaptor.becomeRoot(WRITELN13_tree, root_0);
					}

					}
					break;
				case 4 :
					// Syn.g:34:5: SKIP
					{
					root_0 = (Object)adaptor.nil();


					SKIP14=(Token)match(input,SKIP,FOLLOW_SKIP_in_statement137); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SKIP14_tree = (Object)adaptor.create(SKIP14);
					adaptor.addChild(root_0, SKIP14_tree);
					}

					}
					break;
				case 5 :
					// Syn.g:35:5: IF ^ boolexp THEN statement ELSE statement
					{
					root_0 = (Object)adaptor.nil();


					IF15=(Token)match(input,IF,FOLLOW_IF_in_statement143); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IF15_tree = (Object)adaptor.create(IF15);
					root_0 = (Object)adaptor.becomeRoot(IF15_tree, root_0);
					}

					pushFollow(FOLLOW_boolexp_in_statement146);
					boolexp16=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp16.getTree());

					THEN17=(Token)match(input,THEN,FOLLOW_THEN_in_statement152); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					THEN17_tree = (Object)adaptor.create(THEN17);
					adaptor.addChild(root_0, THEN17_tree);
					}

					pushFollow(FOLLOW_statement_in_statement154);
					statement18=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement18.getTree());

					ELSE19=(Token)match(input,ELSE,FOLLOW_ELSE_in_statement160); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ELSE19_tree = (Object)adaptor.create(ELSE19);
					adaptor.addChild(root_0, ELSE19_tree);
					}

					pushFollow(FOLLOW_statement_in_statement162);
					statement20=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement20.getTree());

					}
					break;
				case 6 :
					// Syn.g:38:5: WHILE ^ boolexp DO ^ statement
					{
					root_0 = (Object)adaptor.nil();


					WHILE21=(Token)match(input,WHILE,FOLLOW_WHILE_in_statement168); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WHILE21_tree = (Object)adaptor.create(WHILE21);
					root_0 = (Object)adaptor.becomeRoot(WHILE21_tree, root_0);
					}

					pushFollow(FOLLOW_boolexp_in_statement171);
					boolexp22=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp22.getTree());

					DO23=(Token)match(input,DO,FOLLOW_DO_in_statement177); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DO23_tree = (Object)adaptor.create(DO23);
					root_0 = (Object)adaptor.becomeRoot(DO23_tree, root_0);
					}

					pushFollow(FOLLOW_statement_in_statement180);
					statement24=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement24.getTree());

					}
					break;
				case 7 :
					// Syn.g:40:5: READ ^ OPENPAREN ! ID CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					READ25=(Token)match(input,READ,FOLLOW_READ_in_statement186); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					READ25_tree = (Object)adaptor.create(READ25);
					root_0 = (Object)adaptor.becomeRoot(READ25_tree, root_0);
					}

					OPENPAREN26=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement189); if (state.failed) return retval;
					ID27=(Token)match(input,ID,FOLLOW_ID_in_statement192); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ID27_tree = (Object)adaptor.create(ID27);
					adaptor.addChild(root_0, ID27_tree);
					}

					CLOSEPAREN28=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement194); if (state.failed) return retval;
					}
					break;
				case 8 :
					// Syn.g:41:5: WRITE ^ OPENPAREN ! ( exp ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					WRITE29=(Token)match(input,WRITE,FOLLOW_WRITE_in_statement201); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITE29_tree = (Object)adaptor.create(WRITE29);
					root_0 = (Object)adaptor.becomeRoot(WRITE29_tree, root_0);
					}

					OPENPAREN30=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement204); if (state.failed) return retval;
					// Syn.g:41:23: ( exp )
					// Syn.g:41:25: exp
					{
					pushFollow(FOLLOW_exp_in_statement209);
					exp31=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp31.getTree());

					}

					CLOSEPAREN32=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement213); if (state.failed) return retval;
					}
					break;
				case 9 :
					// Syn.g:42:5: WRITE ^ OPENPAREN ! ( boolexp ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					WRITE33=(Token)match(input,WRITE,FOLLOW_WRITE_in_statement220); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITE33_tree = (Object)adaptor.create(WRITE33);
					root_0 = (Object)adaptor.becomeRoot(WRITE33_tree, root_0);
					}

					OPENPAREN34=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement223); if (state.failed) return retval;
					// Syn.g:42:23: ( boolexp )
					// Syn.g:42:25: boolexp
					{
					pushFollow(FOLLOW_boolexp_in_statement228);
					boolexp35=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp35.getTree());

					}

					CLOSEPAREN36=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement232); if (state.failed) return retval;
					}
					break;
				case 10 :
					// Syn.g:43:5: WRITE ^ OPENPAREN ! ( string ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					WRITE37=(Token)match(input,WRITE,FOLLOW_WRITE_in_statement239); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WRITE37_tree = (Object)adaptor.create(WRITE37);
					root_0 = (Object)adaptor.becomeRoot(WRITE37_tree, root_0);
					}

					OPENPAREN38=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement242); if (state.failed) return retval;
					// Syn.g:43:23: ( string )
					// Syn.g:43:25: string
					{
					pushFollow(FOLLOW_string_in_statement247);
					string39=string();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string39.getTree());

					}

					CLOSEPAREN40=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement251); if (state.failed) return retval;
					}
					break;
				case 11 :
					// Syn.g:44:5: OPENPAREN ! ( statements ) CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN41=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_statement258); if (state.failed) return retval;
					// Syn.g:44:16: ( statements )
					// Syn.g:44:18: statements
					{
					pushFollow(FOLLOW_statements_in_statement263);
					statements42=statements();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statements42.getTree());

					}

					CLOSEPAREN43=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_statement267); if (state.failed) return retval;
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


	public static class boolexp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolexp"
	// Syn.g:47:1: boolexp : (| boolterm ( AND ^ boolterm )* );
	public final Syn.boolexp_return boolexp() throws RecognitionException {
		Syn.boolexp_return retval = new Syn.boolexp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND45=null;
		ParserRuleReturnScope boolterm44 =null;
		ParserRuleReturnScope boolterm46 =null;

		Object AND45_tree=null;

		try {
			// Syn.g:47:9: (| boolterm ( AND ^ boolterm )* )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==CLOSEPAREN||LA5_0==DO||LA5_0==THEN) ) {
				alt5=1;
			}
			else if ( (LA5_0==FALSE||LA5_0==ID||LA5_0==INTNUM||(LA5_0 >= NOT && LA5_0 <= OPENPAREN)||LA5_0==TRUE) ) {
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
					// Syn.g:48:3: 
					{
					root_0 = (Object)adaptor.nil();


					}
					break;
				case 2 :
					// Syn.g:48:5: boolterm ( AND ^ boolterm )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolterm_in_boolexp282);
					boolterm44=boolterm();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm44.getTree());

					// Syn.g:48:14: ( AND ^ boolterm )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==AND) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// Syn.g:48:16: AND ^ boolterm
							{
							AND45=(Token)match(input,AND,FOLLOW_AND_in_boolexp286); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							AND45_tree = (Object)adaptor.create(AND45);
							root_0 = (Object)adaptor.becomeRoot(AND45_tree, root_0);
							}

							pushFollow(FOLLOW_boolterm_in_boolexp289);
							boolterm46=boolterm();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm46.getTree());

							}
							break;

						default :
							break loop4;
						}
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
	// $ANTLR end "boolexp"


	public static class boolterm_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolterm"
	// Syn.g:52:1: boolterm : ( NOT ^ bool | bool );
	public final Syn.boolterm_return boolterm() throws RecognitionException {
		Syn.boolterm_return retval = new Syn.boolterm_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT47=null;
		ParserRuleReturnScope bool48 =null;
		ParserRuleReturnScope bool49 =null;

		Object NOT47_tree=null;

		try {
			// Syn.g:52:10: ( NOT ^ bool | bool )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==NOT) ) {
				alt6=1;
			}
			else if ( (LA6_0==FALSE||LA6_0==ID||LA6_0==INTNUM||LA6_0==OPENPAREN||LA6_0==TRUE) ) {
				alt6=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// Syn.g:53:5: NOT ^ bool
					{
					root_0 = (Object)adaptor.nil();


					NOT47=(Token)match(input,NOT,FOLLOW_NOT_in_boolterm307); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT47_tree = (Object)adaptor.create(NOT47);
					root_0 = (Object)adaptor.becomeRoot(NOT47_tree, root_0);
					}

					pushFollow(FOLLOW_bool_in_boolterm310);
					bool48=bool();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, bool48.getTree());

					}
					break;
				case 2 :
					// Syn.g:54:5: bool
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_bool_in_boolterm316);
					bool49=bool();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, bool49.getTree());

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
	// Syn.g:57:1: bool : ( TRUE ^| FALSE ^| exp EQUAL ^ exp | exp LSEQ ^ exp | OPENPAREN ! boolexp CLOSEPAREN !);
	public final Syn.bool_return bool() throws RecognitionException {
		Syn.bool_return retval = new Syn.bool_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRUE50=null;
		Token FALSE51=null;
		Token EQUAL53=null;
		Token LSEQ56=null;
		Token OPENPAREN58=null;
		Token CLOSEPAREN60=null;
		ParserRuleReturnScope exp52 =null;
		ParserRuleReturnScope exp54 =null;
		ParserRuleReturnScope exp55 =null;
		ParserRuleReturnScope exp57 =null;
		ParserRuleReturnScope boolexp59 =null;

		Object TRUE50_tree=null;
		Object FALSE51_tree=null;
		Object EQUAL53_tree=null;
		Object LSEQ56_tree=null;
		Object OPENPAREN58_tree=null;
		Object CLOSEPAREN60_tree=null;

		try {
			// Syn.g:57:6: ( TRUE ^| FALSE ^| exp EQUAL ^ exp | exp LSEQ ^ exp | OPENPAREN ! boolexp CLOSEPAREN !)
			int alt7=5;
			switch ( input.LA(1) ) {
			case TRUE:
				{
				alt7=1;
				}
				break;
			case FALSE:
				{
				alt7=2;
				}
				break;
			case ID:
				{
				int LA7_3 = input.LA(2);
				if ( (synpred18_Syn()) ) {
					alt7=3;
				}
				else if ( (synpred19_Syn()) ) {
					alt7=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INTNUM:
				{
				int LA7_4 = input.LA(2);
				if ( (synpred18_Syn()) ) {
					alt7=3;
				}
				else if ( (synpred19_Syn()) ) {
					alt7=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case OPENPAREN:
				{
				int LA7_5 = input.LA(2);
				if ( (synpred18_Syn()) ) {
					alt7=3;
				}
				else if ( (synpred19_Syn()) ) {
					alt7=4;
				}
				else if ( (true) ) {
					alt7=5;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}
			switch (alt7) {
				case 1 :
					// Syn.g:58:4: TRUE ^
					{
					root_0 = (Object)adaptor.nil();


					TRUE50=(Token)match(input,TRUE,FOLLOW_TRUE_in_bool329); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					TRUE50_tree = (Object)adaptor.create(TRUE50);
					root_0 = (Object)adaptor.becomeRoot(TRUE50_tree, root_0);
					}

					}
					break;
				case 2 :
					// Syn.g:59:5: FALSE ^
					{
					root_0 = (Object)adaptor.nil();


					FALSE51=(Token)match(input,FALSE,FOLLOW_FALSE_in_bool336); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FALSE51_tree = (Object)adaptor.create(FALSE51);
					root_0 = (Object)adaptor.becomeRoot(FALSE51_tree, root_0);
					}

					}
					break;
				case 3 :
					// Syn.g:60:5: exp EQUAL ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool343);
					exp52=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp52.getTree());

					EQUAL53=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_bool345); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EQUAL53_tree = (Object)adaptor.create(EQUAL53);
					root_0 = (Object)adaptor.becomeRoot(EQUAL53_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool348);
					exp54=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp54.getTree());

					}
					break;
				case 4 :
					// Syn.g:61:5: exp LSEQ ^ exp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exp_in_bool354);
					exp55=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp55.getTree());

					LSEQ56=(Token)match(input,LSEQ,FOLLOW_LSEQ_in_bool356); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LSEQ56_tree = (Object)adaptor.create(LSEQ56);
					root_0 = (Object)adaptor.becomeRoot(LSEQ56_tree, root_0);
					}

					pushFollow(FOLLOW_exp_in_bool359);
					exp57=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp57.getTree());

					}
					break;
				case 5 :
					// Syn.g:62:5: OPENPAREN ! boolexp CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN58=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_bool365); if (state.failed) return retval;
					pushFollow(FOLLOW_boolexp_in_bool368);
					boolexp59=boolexp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp59.getTree());

					CLOSEPAREN60=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_bool370); if (state.failed) return retval;
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
	// Syn.g:65:1: exp : term ( ( PLUS ^| MINUS ^) term )* ;
	public final Syn.exp_return exp() throws RecognitionException {
		Syn.exp_return retval = new Syn.exp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PLUS62=null;
		Token MINUS63=null;
		ParserRuleReturnScope term61 =null;
		ParserRuleReturnScope term64 =null;

		Object PLUS62_tree=null;
		Object MINUS63_tree=null;

		try {
			// Syn.g:65:5: ( term ( ( PLUS ^| MINUS ^) term )* )
			// Syn.g:66:4: term ( ( PLUS ^| MINUS ^) term )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_term_in_exp384);
			term61=term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, term61.getTree());

			// Syn.g:66:9: ( ( PLUS ^| MINUS ^) term )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// Syn.g:66:11: ( PLUS ^| MINUS ^) term
					{
					// Syn.g:66:11: ( PLUS ^| MINUS ^)
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==PLUS) ) {
						alt8=1;
					}
					else if ( (LA8_0==MINUS) ) {
						alt8=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 8, 0, input);
						throw nvae;
					}

					switch (alt8) {
						case 1 :
							// Syn.g:66:13: PLUS ^
							{
							PLUS62=(Token)match(input,PLUS,FOLLOW_PLUS_in_exp390); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							PLUS62_tree = (Object)adaptor.create(PLUS62);
							root_0 = (Object)adaptor.becomeRoot(PLUS62_tree, root_0);
							}

							}
							break;
						case 2 :
							// Syn.g:66:21: MINUS ^
							{
							MINUS63=(Token)match(input,MINUS,FOLLOW_MINUS_in_exp395); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							MINUS63_tree = (Object)adaptor.create(MINUS63);
							root_0 = (Object)adaptor.becomeRoot(MINUS63_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_term_in_exp400);
					term64=term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, term64.getTree());

					}
					break;

				default :
					break loop9;
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
	// Syn.g:69:1: term : factor ( MULT ^ factor )* ;
	public final Syn.term_return term() throws RecognitionException {
		Syn.term_return retval = new Syn.term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token MULT66=null;
		ParserRuleReturnScope factor65 =null;
		ParserRuleReturnScope factor67 =null;

		Object MULT66_tree=null;

		try {
			// Syn.g:69:6: ( factor ( MULT ^ factor )* )
			// Syn.g:70:4: factor ( MULT ^ factor )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_factor_in_term416);
			factor65=factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, factor65.getTree());

			// Syn.g:70:11: ( MULT ^ factor )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==MULT) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// Syn.g:70:13: MULT ^ factor
					{
					MULT66=(Token)match(input,MULT,FOLLOW_MULT_in_term420); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					MULT66_tree = (Object)adaptor.create(MULT66);
					root_0 = (Object)adaptor.becomeRoot(MULT66_tree, root_0);
					}

					pushFollow(FOLLOW_factor_in_term423);
					factor67=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, factor67.getTree());

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
	// $ANTLR end "term"


	public static class factor_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "factor"
	// Syn.g:73:1: factor : ( ID | INTNUM | OPENPAREN ! exp CLOSEPAREN !);
	public final Syn.factor_return factor() throws RecognitionException {
		Syn.factor_return retval = new Syn.factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ID68=null;
		Token INTNUM69=null;
		Token OPENPAREN70=null;
		Token CLOSEPAREN72=null;
		ParserRuleReturnScope exp71 =null;

		Object ID68_tree=null;
		Object INTNUM69_tree=null;
		Object OPENPAREN70_tree=null;
		Object CLOSEPAREN72_tree=null;

		try {
			// Syn.g:73:8: ( ID | INTNUM | OPENPAREN ! exp CLOSEPAREN !)
			int alt11=3;
			switch ( input.LA(1) ) {
			case ID:
				{
				alt11=1;
				}
				break;
			case INTNUM:
				{
				alt11=2;
				}
				break;
			case OPENPAREN:
				{
				alt11=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}
			switch (alt11) {
				case 1 :
					// Syn.g:74:5: ID
					{
					root_0 = (Object)adaptor.nil();


					ID68=(Token)match(input,ID,FOLLOW_ID_in_factor440); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ID68_tree = (Object)adaptor.create(ID68);
					adaptor.addChild(root_0, ID68_tree);
					}

					}
					break;
				case 2 :
					// Syn.g:75:5: INTNUM
					{
					root_0 = (Object)adaptor.nil();


					INTNUM69=(Token)match(input,INTNUM,FOLLOW_INTNUM_in_factor446); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INTNUM69_tree = (Object)adaptor.create(INTNUM69);
					adaptor.addChild(root_0, INTNUM69_tree);
					}

					}
					break;
				case 3 :
					// Syn.g:76:5: OPENPAREN ! exp CLOSEPAREN !
					{
					root_0 = (Object)adaptor.nil();


					OPENPAREN70=(Token)match(input,OPENPAREN,FOLLOW_OPENPAREN_in_factor452); if (state.failed) return retval;
					pushFollow(FOLLOW_exp_in_factor455);
					exp71=exp();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exp71.getTree());

					CLOSEPAREN72=(Token)match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_factor457); if (state.failed) return retval;
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
	// Syn.g:82:1: string : s= STRING -> STRING[$string::tmp] ;
	public final Syn.string_return string() throws RecognitionException {
		string_stack.push(new string_scope());
		Syn.string_return retval = new Syn.string_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token s=null;

		Object s_tree=null;
		RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

		try {
			// Syn.g:84:5: (s= STRING -> STRING[$string::tmp] )
			// Syn.g:85:5: s= STRING
			{
			s=(Token)match(input,STRING,FOLLOW_STRING_in_string489); if (state.failed) return retval; 
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
			// 85:54: -> STRING[$string::tmp]
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

	// $ANTLR start synpred3_Syn
	public final void synpred3_Syn_fragment() throws RecognitionException {
		// Syn.g:31:5: ( WRITE OPENPAREN ( INTNUM | string ) CLOSEPAREN )
		// Syn.g:31:5: WRITE OPENPAREN ( INTNUM | string ) CLOSEPAREN
		{
		match(input,WRITE,FOLLOW_WRITE_in_synpred3_Syn96); if (state.failed) return;

		match(input,OPENPAREN,FOLLOW_OPENPAREN_in_synpred3_Syn99); if (state.failed) return;

		// Syn.g:31:23: ( INTNUM | string )
		int alt12=2;
		int LA12_0 = input.LA(1);
		if ( (LA12_0==INTNUM) ) {
			alt12=1;
		}
		else if ( (LA12_0==STRING) ) {
			alt12=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 12, 0, input);
			throw nvae;
		}

		switch (alt12) {
			case 1 :
				// Syn.g:31:25: INTNUM
				{
				match(input,INTNUM,FOLLOW_INTNUM_in_synpred3_Syn104); if (state.failed) return;

				}
				break;
			case 2 :
				// Syn.g:31:34: string
				{
				pushFollow(FOLLOW_string_in_synpred3_Syn108);
				string();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_synpred3_Syn112); if (state.failed) return;

		}

	}
	// $ANTLR end synpred3_Syn

	// $ANTLR start synpred10_Syn
	public final void synpred10_Syn_fragment() throws RecognitionException {
		// Syn.g:41:5: ( WRITE OPENPAREN ( exp ) CLOSEPAREN )
		// Syn.g:41:5: WRITE OPENPAREN ( exp ) CLOSEPAREN
		{
		match(input,WRITE,FOLLOW_WRITE_in_synpred10_Syn201); if (state.failed) return;

		match(input,OPENPAREN,FOLLOW_OPENPAREN_in_synpred10_Syn204); if (state.failed) return;

		// Syn.g:41:23: ( exp )
		// Syn.g:41:25: exp
		{
		pushFollow(FOLLOW_exp_in_synpred10_Syn209);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

		match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_synpred10_Syn213); if (state.failed) return;

		}

	}
	// $ANTLR end synpred10_Syn

	// $ANTLR start synpred11_Syn
	public final void synpred11_Syn_fragment() throws RecognitionException {
		// Syn.g:42:5: ( WRITE OPENPAREN ( boolexp ) CLOSEPAREN )
		// Syn.g:42:5: WRITE OPENPAREN ( boolexp ) CLOSEPAREN
		{
		match(input,WRITE,FOLLOW_WRITE_in_synpred11_Syn220); if (state.failed) return;

		match(input,OPENPAREN,FOLLOW_OPENPAREN_in_synpred11_Syn223); if (state.failed) return;

		// Syn.g:42:23: ( boolexp )
		// Syn.g:42:25: boolexp
		{
		pushFollow(FOLLOW_boolexp_in_synpred11_Syn228);
		boolexp();
		state._fsp--;
		if (state.failed) return;

		}

		match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_synpred11_Syn232); if (state.failed) return;

		}

	}
	// $ANTLR end synpred11_Syn

	// $ANTLR start synpred12_Syn
	public final void synpred12_Syn_fragment() throws RecognitionException {
		// Syn.g:43:5: ( WRITE OPENPAREN ( string ) CLOSEPAREN )
		// Syn.g:43:5: WRITE OPENPAREN ( string ) CLOSEPAREN
		{
		match(input,WRITE,FOLLOW_WRITE_in_synpred12_Syn239); if (state.failed) return;

		match(input,OPENPAREN,FOLLOW_OPENPAREN_in_synpred12_Syn242); if (state.failed) return;

		// Syn.g:43:23: ( string )
		// Syn.g:43:25: string
		{
		pushFollow(FOLLOW_string_in_synpred12_Syn247);
		string();
		state._fsp--;
		if (state.failed) return;

		}

		match(input,CLOSEPAREN,FOLLOW_CLOSEPAREN_in_synpred12_Syn251); if (state.failed) return;

		}

	}
	// $ANTLR end synpred12_Syn

	// $ANTLR start synpred18_Syn
	public final void synpred18_Syn_fragment() throws RecognitionException {
		// Syn.g:60:5: ( exp EQUAL exp )
		// Syn.g:60:5: exp EQUAL exp
		{
		pushFollow(FOLLOW_exp_in_synpred18_Syn343);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,EQUAL,FOLLOW_EQUAL_in_synpred18_Syn345); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred18_Syn348);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred18_Syn

	// $ANTLR start synpred19_Syn
	public final void synpred19_Syn_fragment() throws RecognitionException {
		// Syn.g:61:5: ( exp LSEQ exp )
		// Syn.g:61:5: exp LSEQ exp
		{
		pushFollow(FOLLOW_exp_in_synpred19_Syn354);
		exp();
		state._fsp--;
		if (state.failed) return;

		match(input,LSEQ,FOLLOW_LSEQ_in_synpred19_Syn356); if (state.failed) return;

		pushFollow(FOLLOW_exp_in_synpred19_Syn359);
		exp();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred19_Syn

	// Delegated rules

	public final boolean synpred12_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred12_Syn_fragment(); // can never throw exception
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
	public final boolean synpred10_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred10_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred11_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred11_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred18_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred18_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred19_Syn() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred19_Syn_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}



	public static final BitSet FOLLOW_statements_in_program56 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_statements71 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_SEMICOLON_in_statements75 = new BitSet(new long[]{0x000000038A818000L});
	public static final BitSet FOLLOW_statement_in_statements78 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_WRITE_in_statement96 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement99 = new BitSet(new long[]{0x0000000010020000L});
	public static final BitSet FOLLOW_INTNUM_in_statement104 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_string_in_statement108 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement112 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_statement119 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_ASSIGN_in_statement121 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_statement124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITELN_in_statement130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SKIP_in_statement137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_statement143 = new BitSet(new long[]{0x0000000060C29000L});
	public static final BitSet FOLLOW_boolexp_in_statement146 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_THEN_in_statement152 = new BitSet(new long[]{0x000000038A818000L});
	public static final BitSet FOLLOW_statement_in_statement154 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_ELSE_in_statement160 = new BitSet(new long[]{0x000000038A818000L});
	public static final BitSet FOLLOW_statement_in_statement162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHILE_in_statement168 = new BitSet(new long[]{0x0000000040C29200L});
	public static final BitSet FOLLOW_boolexp_in_statement171 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_DO_in_statement177 = new BitSet(new long[]{0x000000038A818000L});
	public static final BitSet FOLLOW_statement_in_statement180 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_READ_in_statement186 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement189 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_ID_in_statement192 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement194 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_statement201 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement204 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_statement209 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_statement220 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement223 = new BitSet(new long[]{0x0000000040C29040L});
	public static final BitSet FOLLOW_boolexp_in_statement228 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_statement239 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement242 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_string_in_statement247 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_statement258 = new BitSet(new long[]{0x000000038A818000L});
	public static final BitSet FOLLOW_statements_in_statement263 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_statement267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolterm_in_boolexp282 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_boolexp286 = new BitSet(new long[]{0x0000000040C29000L});
	public static final BitSet FOLLOW_boolterm_in_boolexp289 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_boolterm307 = new BitSet(new long[]{0x0000000040829000L});
	public static final BitSet FOLLOW_bool_in_boolterm310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_bool_in_boolterm316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_bool329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FALSE_in_bool336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool343 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_EQUAL_in_bool345 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_bool348 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_bool354 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LSEQ_in_bool356 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_bool359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_bool365 = new BitSet(new long[]{0x0000000040C29040L});
	public static final BitSet FOLLOW_boolexp_in_bool368 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_bool370 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_term_in_exp384 = new BitSet(new long[]{0x0000000001100002L});
	public static final BitSet FOLLOW_PLUS_in_exp390 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_MINUS_in_exp395 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_term_in_exp400 = new BitSet(new long[]{0x0000000001100002L});
	public static final BitSet FOLLOW_factor_in_term416 = new BitSet(new long[]{0x0000000000200002L});
	public static final BitSet FOLLOW_MULT_in_term420 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_factor_in_term423 = new BitSet(new long[]{0x0000000000200002L});
	public static final BitSet FOLLOW_ID_in_factor440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTNUM_in_factor446 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENPAREN_in_factor452 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_factor455 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_factor457 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_string489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_synpred3_Syn96 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_synpred3_Syn99 = new BitSet(new long[]{0x0000000010020000L});
	public static final BitSet FOLLOW_INTNUM_in_synpred3_Syn104 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_string_in_synpred3_Syn108 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_synpred3_Syn112 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_synpred10_Syn201 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_synpred10_Syn204 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_synpred10_Syn209 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_synpred10_Syn213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_synpred11_Syn220 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_synpred11_Syn223 = new BitSet(new long[]{0x0000000040C29040L});
	public static final BitSet FOLLOW_boolexp_in_synpred11_Syn228 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_synpred11_Syn232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WRITE_in_synpred12_Syn239 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_OPENPAREN_in_synpred12_Syn242 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_string_in_synpred12_Syn247 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CLOSEPAREN_in_synpred12_Syn251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred18_Syn343 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_EQUAL_in_synpred18_Syn345 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_synpred18_Syn348 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exp_in_synpred19_Syn354 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LSEQ_in_synpred19_Syn356 = new BitSet(new long[]{0x0000000000828000L});
	public static final BitSet FOLLOW_exp_in_synpred19_Syn359 = new BitSet(new long[]{0x0000000000000002L});
}
