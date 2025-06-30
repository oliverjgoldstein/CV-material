/******************************************************************************
Check the style of a Java program.  It is assumed that the program compiles OK.
See the usage message for a list of points that are checked.  The program is
designed as a single class, using only basic Java features as far as possible.

Ian Holyer, January 2008
******************************************************************************/

import java.io.*;
import java.util.*;

class Style
{
   // The program being checked is stored in a global array of lines.  Each
   // line is a character array ending with '\n'.  Error messages are stored in
   // an array indexed by line number.  The checker keeps track of the
   // program's indent amount and indent style.

   String filename;
   char [] [] lines;
   String [] errors;
   int indentAmount;
   int indentStyle;
   static final int Allman=0, Gnu=1, Horstmann=2, KandR=3, Whitesmith=4;

   // Get the filename from the command line.  If there isn't one, each check
   // method prints out its own one-line description to form a usage message.

   public static void main (String[] args)
   {
      Style program = new Style();
      if (args.length != 1)
      {
         System.out.println ("Usage: java Style filename");
         System.out.println ("Checks Java programs for these style problems,");
         System.out.println ("producing no output if there are no problems:");
         program.filename = null;
         program.runChecks();
      }
      else
      {
         program.readFile(args[0]);
         program.runChecks();
         program.printErrors();
      }
   }

   // Run the checks in the right order.  Each check is a self-contained
   // method, which may fix up the text to improve subsequent checks, and which
   // uses support methods e.g. describe(), report(), gather() and summarise().

   void runChecks ()
   {
      checkLastLine ();
      checkTrailingBlankLines ();
      checkTrailingSpaces ();
      checkLongLines ();
      checkHardTabs ();
      checkCharacters ();
      checkQuotes ();
      checkCommentsExist ();
      checkCommentsWithCode ();
      checkNestedComments ();
      checkComments ();
      checkIndentStyle ();
      checkIndentAmount ();
      checkDoubleBraces ();
      checkIndents ();
      checkLongMethods ();
   }

   // Check for a missing newline at the end of the last line of the file.
   // This can't be detected using readLine(), which removes line endings, so
   // the file is re-opened using random access, and the last byte is examined.
   // There shouldn't be any I/O errors, so arrange to ignore them.

   void checkLastLine ()
   {
      if (describe ("last line not ending in newline")) return;
      try
      {
         RandomAccessFile file = new RandomAccessFile(filename, "r");
         long n = file.length();
         if (n == 0) return;
         file.seek(n-1);
         char c = (char) file.read();
         if (c == '\r' || c == '\n') return;
      }
      catch (IOException err) { }
      report (lines.length-1, "There is no newline at the end of the file.");
   }

   // Check for trailing blank lines.

   void checkTrailingBlankLines()
   {
      if (describe ("trailing blank lines at the end of the file")) return;
      int n = 0;
      for (int i = lines.length - 1; i >= 0; i--)
      {
         if (! (lines[i].length == 1)) break;
         n++;
      }
      int ln = lines.length - n;
      if (n == 0) return;
      if (n == 1) report (ln, "There is a blank line at the end of the file");
      else report (ln, "There are blank lines at the end of the file");
   }

   // Check whether any line has trailing whitespace, then remove it to avoid
   // any unnecessary error message from checkLongLines().

   void checkTrailingSpaces()
   {
      if (describe ("trailing white space at the end of a line")) return;
      int[] errorLines = new int[0];
      for (int i = 0; i < lines.length; i++)
      {
         char [] line = lines[i];
         int n = line.length - 1;
         while (n >= 1 && Character.isWhitespace(line[n-1])) n--;
         if (n < line.length - 1)
         {
            errorLines = gather (errorLines, i);
            char [] newline = new char [n+1];
            for (int c = 0; c < n; c++) newline[c] = line[c];
            newline[n] = '\n';
            lines[i] = newline;
         }
      }
      String error = "Line % contains trailing white space";
      if (errorLines.length > 1)
      {
         error = "There is trailing white space on lines %.";
      }
      summarise (errorLines, error);
   }

   // Check for lines longer than 80 characters (including the newline),
   // assuming that trailing white space has been removed.

   void checkLongLines()
   {
      if (describe ("lines over 80 characters long")) return;
      int[] errorLines = new int[0];
      for (int i = 0; i < lines.length; i++)
      {
         if (lines[i].length > 80)
         {
            errorLines = gather (errorLines, i);
         }
      }
      String error = "Line % is over 80 characters long.";
      if (errorLines.length > 1)
      {
         error = "There are over 80 characters on lines %.";
      }
      summarise (errorLines, error);
   }

   // Check for tab characters in the file, and replace by 8 spaces to make the
   // indenting checks less severe.  Gather the line numbers and produce a
   // single report, since a programmer will probably do a global replace.

   void checkHardTabs()
   {
      if (describe ("tab characters (hard tabs)")) return;
      int[] errorLines = new int[0];
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         StringBuffer newline = new StringBuffer();
         boolean hasTab = false;
         for (int n = 0; n < line.length; n++)
         {
            char c = line[n];
            if (c != '\t')
            {
               newline.append (c);
               continue;
            }
            newline.append ("        ");
            hasTab = true;
         }
         if (hasTab) errorLines = gather (errorLines, i);
         if (hasTab) lines[i] = newline.toString().toCharArray ();
      }
      String error = "Line % contains a tab character.";
      if (errorLines.length > 1)
      {
         error = "There are tab characters on lines %.";
      }
      summarise (errorLines, error);
   }

   // Check that there are no control characters or unicode characters.

   void checkCharacters ()
   {
      if (describe ("control or non-ascii characters")) return;
      int[] errorLines = new int[0];
      for (int i = 0; i < lines.length; i++)
      {
         boolean hasControl = false;
         for (int j = 0; j < lines[i].length - 1; j++)
         {
            char c = lines[i][j];
            if (c < ' ' || c > '~') hasControl = true;
         }
         if (hasControl)
         {
            errorLines = gather (errorLines, i);
         }
      }
      String error = "Line % contains a control or non-ascii character";
      if (errorLines.length > 1)
      {
         error = "There are control or non-ascii characters on lines %.";
      }
      summarise (errorLines, error);
   }

   // In string and character constants, blot out { } // /* */ without changing
   // line lengths etc, to simplify later checks for blocks and comments.
   // Quotes inside comments are ignored to avoid problems.  This check does
   // not produce errors, so no description is given.

   void checkQuotes()
   {
      if (describe ("")) return;
      boolean inLongComment = false;
      for (int n = 0; n < lines.length; n++)
      {
         char[] line = lines[n];
         boolean inString = false, inChar = false;

         for (int i = 0; i < line.length - 1; i++)
         {
            char c = line[i], c2 = line[i+1];
            if (inString || inChar)
            {
               if (inString && c == '"') inString = false;
               else if (inChar && c == '\'') inChar = false;
               else if (c == '\\') line[i+1] = '?';
               else if (! Character.isWhitespace(c)) line[i] = '?';
            }
            else if (inLongComment)
            {
               if (c == '*' && c2 == '/') { i++; inLongComment = false; }
            }
            else if (c == '/' && c2 == '*') { i++; inLongComment = true; }
            else if (c == '/' && c2 == '/') break;
            else if (c == '"') inString = true;
            else if (c == '\'') inChar = true;
         }
         lines[n] = line;
      }
   }

   // Check that a program has at least one comment of either kind.

   void checkCommentsExist ()
   {
      if (describe ("comments have been included")) return;
      boolean ok = false;
      for (int n = 0; n < lines.length; n++)
      {
         String line = new String(lines[n]);
         if (line.indexOf("//") >= 0) ok = true;
         if (line.indexOf("/*") >= 0) ok = true;
      }
      if (!ok) report (lines.length-1, "There are no comments");
   }

   // Check for a comment on the same line as other code.  Assume that
   // checkQuotes has already been called.

   void checkCommentsWithCode()
   {
      if (describe ("comments on the same line as other code")) return;
      int[] errorLines = new int[0];
      boolean inLong = false;
      boolean saveInLong = inLong;
      for (int n = 0; n < lines.length; n++)
      {
         boolean hasLong = inLong, hasShort = false, hasCode = false;
         char[] line = lines[n];
         for (int i = 0; i < line.length - 1; i++)
         {
            char c = line[i], c2 = line[i+1];
            if (inLong)
            {
               if (c == '*' && c2 == '/') { i++; inLong = false; }
            }
            else if (hasShort) continue;
            else if (c == '/' && c2 == '*')
            {
               i++;
               if (hasLong) hasCode = true;
               inLong = hasLong = true;
            }
            else if (c == '/' && c2 == '/') { i++; hasShort = true; }
            else if (! Character.isWhitespace(c)) hasCode = true;
         }
         if (hasLong && hasShort || hasLong && hasCode || hasShort && hasCode)
         {
            errorLines = gather (errorLines, n);
            lines[n] = tidy (line, saveInLong);
            saveInLong = inLong;
         }
      }
      String error = "Line % contains a comment and other code";
      if (errorLines.length > 1)
      {
         error = "There are comments mixed with code on lines %.";
      }
      summarise (errorLines, error);
   }

   // Where a line contains a comment and other code, tidy up the common cases
   // to get of this situation, to simplify later comments.

   char[] tidy (char[] line, boolean inLong)
   {
      int indent = findIndent (line);
      if (inLong || line[indent] == '/' && line[indent+1] == '*')
      {
         int n = inLong ? indent : indent+2;
         for (int i = n; i < line.length-1; i++)
         {
            char c = line[i], c2 = line[i+1];
            if (c == '*' && c2 == '/')
            {
               line = (new String (line, 0, i+2) + "\n").toCharArray();
               return line;
            }
         }
      }
      else
      {
         for (int i = indent; i < line.length-1; i++)
         {
            char c = line[i];
            if (c == ';')
            {
               line = (new String (line, 0, i+1) + "\n").toCharArray();
               return line;
            }
         }
      }
      return line;
   }

   // Check each multi-line comment to see if it contains /* which is a sign of
   // potential trouble.

   void checkNestedComments ()
   {
      if (describe ("multiline comments containing /*")) return;
      int[] errorLines = new int[0];
      boolean inLongComment = false;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         for (int n = 0; n < line.length - 1; n++)
         {
            char c = line[n], c2 = line[n+1];
            if (inLongComment)
            {
               if (c == '/' && c2 == '*')
               {
                  errorLines = gather (errorLines, i);
               }
               else if (c == '*' && c2 == '/') { n++; inLongComment = false; }
            }
            else
            {
               if (c == '/' && c2 == '*') { n++; inLongComment = true; }
               else if (c == '/' && c2 == '/') break;
            }
         }
      }
      String error = "Line % contains /* within a multiline comment.";
      if (errorLines.length > 1)
      {
         error = "There is a /* within multiline comments on lines %.";
      }
      summarise (errorLines, error);
   }

   // Check that each line of a multi-line comment is indented by at least as
   // much as the first line.  Remove all comments, to blot out { and } and
   // simplify later checks, but leave a semicolon in place to check
   // indenting.  This check causes no errors, so it has no description.

   void checkComments ()
   {
      if (describe ("")) return;
      boolean inLongComment = false;
      int firstIndent = 0;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         int indent = findIndent (line);
         if (inLongComment)
         {
            if (indent < firstIndent)
            {
               report (i, "Line % is not indented enough");
            }
            lines[i] = new char[1];
            lines[i][0] = '\n';
         }
         else if (line[indent] == '/' && line[indent+1] == '/')
         {
            lines[i] = (new String (line, 0, indent) + ";\n").toCharArray();
         }
         else if (line[indent] == '/' && line[indent+1] == '*')
         {
            inLongComment = true;
            firstIndent = indent;
            lines[i] = (new String (line, 0, indent) + ";\n").toCharArray();
         }
         for (int n = indent; n < line.length-1; n++)
         {
            if (line[n] == '*' && line[n+1] == '/') inLongComment = false;
            if (! inLongComment && line[n] == '/' && line[n+1] == '/') break;
            if (line[n] == '/' && line[n+1] == '*') inLongComment = true;
         }
      }
   }

   // Check which indenting style is being used.  This check produces no
   // errors, so it gives no description.

   void checkIndentStyle()
   {
      if (describe("")) return;
      int n;
      char firstChar = ' ';
      for (n = 0; n < lines.length - 2; n++)
      {
         firstChar = lines[n][0];
         if (firstChar == '\n') continue;
         if (firstChar == '{' || firstChar == ' ') break;
      }
      if (firstChar == '{' && lines[n][1] == '\n') indentStyle = Allman;
      else if (firstChar == '{') indentStyle = Horstmann;
      else
      {
         char[] line = lines[n];
         int indent = findIndent (line);
         if (line[indent] != '{') indentStyle = KandR;
         else
         {
            int nextIndent = findIndent (lines[n+1]);
            if (nextIndent > indent) indentStyle = Gnu;
            else indentStyle = Whitesmith;
         }
      }
   }

   // Check what the indent amount is, and make sure it is sensible.  For the
   // Gnu style, the double indent between an outer and inner block must also
   // be sensible, so indentAmount = 2 is the only possibility.

   void checkIndentAmount()
   {
      if (describe("indenting which is too narrow or too wide")) return;
      indentAmount = 0;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         if (line[0] == ' ')
         {
            indentAmount = findIndent (line);
            if (indentAmount < 2) report (i, "Indenting is too narrow");
            if (indentAmount > 4) report (i, "Indenting is too wide");
            else if (indentStyle == Gnu && indentAmount > 2)
            {
               report (i, "Indenting is too wide");
            }
            return;
         }
      }
   }

   // Look for two open or close braces in a line, report them, and remove one.
   // Look for "...{...}" which is allowed in all styles and remove the braces.
   // Also deal with "...{...};" which occurs with array initialisation.

   void checkDoubleBraces ()
   {
      if (describe ("two open or two close braces on the same line")) return;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         int open = -1, close = -1;
         for (int n = 0; n < line.length - 1; n++)
         {
            char c = line[n];
            if (c == '{' && open >= 0)
            {
               line[n] = '?';
               report (i, "Line % contains two open braces.");
            }
            else if (c == '}' && close >= 0)
            {
               line[n] = '?';
               report (i, "Line % contains two close braces.");
            }
            else if (c == '{') open = n;
            else if (c == '}') close = n;
         }
         if (open > findIndent (line) && (close == line.length-2 ||
            close == line.length - 3 && line[close+1] == ';'))
         {
            line[open] = '?';
            line[close] = ';';
         }
      }
   }

   // Check for consistent indenting.

   void checkIndents()
   {
      if (describe ("inconsistent indenting")) return;
      int expected = 0;
      boolean continuation = false;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         if (line.length == 1) continue;
         int open = -1, close = -1, indent = findIndent (line);
         for (int n = 0; n < line.length-1; n++)
         {
            if (line[n] == '{') open = n;
            else if (line[n] == '}') close = n;
         }
         if (open < 0 && close < 0)
         {
            String s = new String (line, indent, line.length - indent);
            if (s.startsWith("case ") || s.startsWith("default ") ||
               s.startsWith("default:"))
            {
               testIndent (i, indent, expected - indentAmount);
            }
            else if (continuation)
            {
               testIndent (i, indent, expected + indentAmount);
            }
            else expected = testIndent (i, indent, expected);
            continuation = line[line.length-2] != ';' &&
               line[line.length-2] != ':';
            continue;
         }
         continuation = false;
         if (close < 0) expected = doOpenBrace (expected, i, line, open);
         else if (open < 0) expected = doCloseBrace (expected, i, line, close);
         else expected = doTwoBraces (expected, i, line, open, close);
      }
   }

   // Deal with a line which contains just an open brace.

   int doOpenBrace (int expected, int i, char[] line, int open)
   {
      int indent = findIndent (line);
      int end = line.length - 2;
      if (indentStyle != KandR && open > indent)
      {
         report (i, "Line % contains code before '{'.");
      }
      if (open < end)
      {
         if (indentStyle == Horstmann)
         {
            int code = open + 1;
            while (line[code] == ' ') code++;
            expected = expected + indentAmount;
            expected = testIndent (i, code, expected);
            return expected;
         }
         else report (i, "Line % contains code after '{'.");
      }
      if (indentStyle == Gnu || indentStyle == Whitesmith)
      {
         expected = expected + indentAmount;
      }
      expected = testIndent (i, indent, expected);
      if (indentStyle != Whitesmith) expected = expected + indentAmount;
      return expected;
   }

   // Deal with a line which contains just a close brace, or a close brace
   // followed by semicolon (array initialisation).

   int doCloseBrace (int expected, int i, char[] line, int close)
   {
      int indent = findIndent (line);
      int end = line.length - 2;
      if (close > indent) report (i, "Line % contains code before '}'.");
      if (close == end-1 && line[end] == ';') close = end;
      if (indentStyle != Horstmann && close < end)
      {
         report (i, "Line % contains code after '}'.");
      }
      if (indentStyle != Whitesmith) expected = expected - indentAmount;
      expected = testIndent (i, indent, expected);
      if (indentStyle == Gnu || indentStyle == Whitesmith)
      {
         expected = expected - indentAmount;
      }
      return expected;
   }

   // Deal with a line which contains both an open and close brace.

   int doTwoBraces (int expected, int i, char[] line, int open, int close)
   {
      int indent = findIndent (line);
      int end = line.length - 2;
      if (indentStyle == KandR && close == indent && open == end)
      {
         expected = expected - indentAmount;
         expected = testIndent (i, indent, expected);
         expected = expected + indentAmount;
         return expected;
      }
      if (open < close) report (i, "Line % contains '{' and '}'.");
      else report (i, "Line % contains '}' and '{'.");
      return indent;
   }

   // Report an error if the indent is not as expected, and return the current
   // indent as the new expected indent to aid error recovery a little (so a
   // missing } produces only a couple of error reports, not a file-full).

   int testIndent (int i, int indent, int expected)
   {
      if (indent == expected) return expected;
      String error = "Line % is indented " + indent;
      if (indent == 1) error = error + " space instead of " + expected + ".";
      else error = error + " spaces instead of " + expected + ".";
      report (i, error);
      return indent;
   }

   // Check for over-long method definitions.

   void checkLongMethods()
   {
      if (describe ("long method definitions")) return;
      int openBraces = 0, methodLength = 0;
      for (int i = 0; i < lines.length; i++)
      {
         char[] line = lines[i];
         int len = line.length ;
         if (openBraces >= 2)
         {
            for (int n = 0; n < line.length - 1; n++)
            {
               if (line[n] == ' ') continue;
               if (line[n] == ';') continue;
               if (line[n] == '{') continue;
               if (line[n] == '}') continue;
               methodLength++;
               break;
            }
         }
         if (openBraces < 2)
         {
            if (methodLength > 30)
            {
               report (i-1, "Line % ends a method over 30 lines long");
            }
            methodLength = 0;
         }
         for (int n = 0; n < line.length - 1; n++)
         {
            if (line[n] == '{') openBraces++;
            else if (line[n] == '}') openBraces--;
         }
      }
   }

   // -------------------- Supporting methods ---------------------------------

   // Find the number of spaces at the beginning of a line.

   int findIndent (char[] line)
   {
      for (int i = 0; i < line.length; i++)
      {
         if (line[i] != ' ') return i;
      }
      return 0;
   }

   // Check whether a usage message is being produced.  If so, print out the
   // given one line description, with a 'bullet point', and return true.  If
   // an empty description is given, print nothing.

   boolean describe (String description)
   {
      if (filename == null && ! description.equals(""))
      {
         System.out.println ("- " + description);
      }
      return (filename == null);
   }

   // Report an error.  Replace % in the message with the line number (counted
   // from 1).  Save up errors to print them at the end in line number order.

   void report (int lineNumber, String error)
   {
      int pos = error.indexOf ('%');
      if (pos >= 0)
      {
         String insert = "" + (lineNumber + 1);
         error = error.substring(0,pos) + insert + error.substring(pos+1);
      }
      String oldError = errors[lineNumber];
      if (oldError == null) errors[lineNumber] = error;
      else errors[lineNumber] = oldError + "\n" + error;
   }

   // Add a line number to a given list of line numbers, ready to produce a
   // single combined report using summarise().

   int[] gather (int[] lineNumbers, int lineNumber)
   {
      int[] newLineNumbers = new int[lineNumbers.length + 1];
      for (int i = 0; i < lineNumbers.length; i++)
      {
         newLineNumbers[i] = lineNumbers[i];
      }
      newLineNumbers[lineNumbers.length] = lineNumber;
      return newLineNumbers;
   }

   // Produce a summary report, using a list of collected line numbers, with %
   // replaced by a list of line numbers affected.

   void summarise (int[] lineNumbers, String error)
   {
      if (lineNumbers.length == 0) return;
      int index = lineNumbers[0];
      int pos = error.indexOf ('%');
      if (pos >= 0)
      {
         String message = "";
         String line = error.substring(0,pos) + (lineNumbers[0]+1);
         for (int i = 1; i < lineNumbers.length; i++)
         {
            line = line + ",";
            if (line.length() > 70)
            {
               message = message + line + "\n";
               line = "";
            }
            line = line + " " + (lineNumbers[i]+1);
         }
         error = message + line + error.substring(pos+1);
      }
      report (index, error);
   }

   // Print saved error messages in line number order.

   void printErrors ()
   {
      for (int i = 0; i < errors.length; i++)
      {
         if (errors[i] != null)
         {
            System.out.println (errors[i]);
         }
      }
   }

   // Read the file into the global array of lines, and initialise the array of
   // error messages (with an extra entry for an end-of-file message).

   void readFile (String name)
   {
      try
      {
         filename = name;
         BufferedReader input;
         input = new BufferedReader (new FileReader (filename));
         List<char[]> tempLines = new ArrayList<char[]> ();
         String line = input.readLine ();
         while (line != null)
         {
            tempLines.add ((line + "\n").toCharArray ());
            line = input.readLine ();
         }
         lines = new char [tempLines.size ()][];
         tempLines.toArray (lines);
         errors = new String [lines.length + 1];
      }
      catch (IOException err)
      {
         System.out.println (err.getMessage());
         System.exit (1);
      }
   }
}
