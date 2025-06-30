// COMS22201: Code generation

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class Cg
{
  static private Integer registerCount = 1;
  static private Map<Integer, Integer> registerMem = new HashMap<Integer, Integer>();
  static private Map<Integer, Integer> registerConst = new HashMap<Integer, Integer>();
  public static void program(IRTree irt, PrintStream o)
  {
    emit(o, "XOR R0,R0,R0");   // Initialize R0 to 0
    statement(irt, o);
    emit(o, "HALT");           // Program must end with HALT
    Memory.dumpData(o);        // Dump DATA lines: initial memory contents
  }

  private static int count_const(IRTree irt, int tail) {
    tail = 0;
      if(irt.getOp().equals("CONST")) {
        tail = tail + 1;
      } else if(irt.getOp().equals("MEM")) {
        tail = tail - 1;
      }

      for(int i = 0; i < irt.returnSubNumber(); i++) {
        if(irt.getSub(i) != null) {
          tail += count_const(irt.getSub(i), tail);
        }
      }

      return tail;
  }

  private static void statement(IRTree irt, PrintStream o)
  {
    if (irt.getOp().equals("SEQ")) {
      statement(irt.getSub(0), o);
      statement(irt.getSub(1), o);
    }
    
    else if (irt.getOp().equals("WRS") && irt.getSub(0).getOp().equals("MEM") && irt.getSub(0).getSub(0).getOp().equals("CONST")) {
      String a = irt.getSub(0).getSub(0).getSub(0).getOp();
      emit(o, "WRS "+a);
    }
    
    else if (irt.getOp().equals("WR")) {
      String e = expression(irt.getSub(0), o);
      emit(o, "WR "+e);
      registerCount = registerCount - count_const(irt, 0);
    }
    
    else if (irt.getOp().equals("MOVE")) {

      // MOVE - MEM - CONST - 43. e.g.

      String registerToLoadInto = irt.getSub(0).getSub(0).getSub(0).getOp();      
      String result = expression(irt.getSub(1), o);

      registerMem.put(Integer.parseInt(registerToLoadInto), Character.getNumericValue(result.charAt(1)));

      emit(o, "STORE " + result + ", R0," + registerToLoadInto);
    } 

    else if (irt.getOp().equals("RD")) {
      String registerToReadTo = irt.getSub(0).getSub(0).getSub(0).getOp();
      emit(o, "RD" + " R" + registerCount.toString());

      registerMem.put(Integer.parseInt(registerToReadTo), registerCount);

      emit(o, "STORE R" + registerCount.toString() + ", R0, " +  registerToReadTo);
    } 

    else if (irt.getOp().equals("CJUMP")) {

      // Let's get the op.

      String operation = irt.getSub(0).toString();
      String expressionOne = expression(irt.getSub(1), o);
      String expressionTwo = expression(irt.getSub(2), o);
      String nameOne = irt.getSub(3).toString();
      String nameTwo = irt.getSub(4).toString();

      /* We have the following operators:

        1.  = -> With this let's jump to first label if register is zero, second label if non zero.
        2. != -> With this let's jump to first label if non zero and second label if zero.
        3. <= -> With this let's  jump to first label if negative or zero else if positive go to second label.

      */

      emit(o, "SUB " + expressionOne + ", " + expressionOne + ", " + expressionTwo);

      // Now we have expressionOne storing 1 - 2.

      if(operation.equals("=")) {

        emit(o, "BEQZ " + expressionOne + ", " + nameOne);
        emit(o, "BNEZ " + expressionOne + ", " + nameTwo);

      }

      else if(operation.equals("<=")) {

        emit(o, "BLTZ " + expressionOne + ", " + nameOne);
        emit(o, "BEQZ " + expressionOne + ", " + nameOne);
        emit(o, "BGEZ " + expressionOne + ", " + nameTwo);

      } 

      else if(operation.equals("!=")) {

        emit(o, "BEQZ " + expressionOne + ", " + nameTwo);
        emit(o, "BNEZ " + expressionOne + ", " + nameOne);

      }

      else if(operation.equals("<")) {

        emit(o, "BLTZ " + expressionOne + ", " + nameOne);
        emit(o, "BGEZ " + expressionOne + ", " + nameTwo);

      }

      else if(operation.equals(">=")) {

        emit(o, "BGEZ " + expressionOne + ", " + nameOne);
        emit(o, "BLTZ " + expressionOne + ", " + nameTwo);

      }

      else if(operation.equals(">")) {

        emit(o, "BLTZ " + expressionOne + ", " + nameTwo);
        emit(o, "BEQZ " + expressionOne + ", " + nameTwo);
        emit(o, "BGEZ " + expressionOne + ", " + nameOne);

      }

      registerCount = registerCount - count_const(irt, 0);
    } 

    else if(irt.getOp().equals("JUMP")) {
      emit(o, "JMP " + irt.getSub(0));
    }

    else if (irt.getOp().equals("LABEL")) {      
      emit(o, irt.getSub(0).getOp() + ":");
    }

    else if (irt.getOp().equals("NOP")) {      
      emit(o, "NOP");
    }


    else {
      error(irt.getOp());
    }
  }

  private static String expression(IRTree irt, PrintStream o)
  {
    String result = "";
    if (irt.getOp().equals("CONST")) {
      String intVal = irt.getSub(0).getOp();
      if(registerConst.get(intVal) != null) {
        result = "R"+registerConst.get(intVal).toString();
      } else {
        registerConst.put(Integer.parseInt(intVal), registerCount);
        result = "R"+registerCount.toString();
      }
      emit(o, "ADDI "+result+",R0,"+intVal); // R0 is the zero register.
    } else if (irt.getOp().equals("MEM")) {

      // This is because it goes MEM - CONST - 43.

      String numberItself = irt.getSub(0).getSub(0).getOp();
      result = "R"+registerMem.get(Integer.parseInt(numberItself)).toString();
      emit(o, "LOAD "+result+",R0,"+numberItself);
    } else if (irt.getOp().equals("SUB") || (irt.getOp().equals("MUL")) || (irt.getOp().equals("ADD")) || (irt.getOp().equals("DIV")) || (irt.getOp().equals("XOR")) || (irt.getOp().equals("HALT"))) {

      // Gets CONST 43 and then just does 43.

      if((irt.getOp().equals("HALT"))) { // Ask about this.
        emit(o, "HALT");
      } else {

        result = "R" + registerCount.toString();
        String assemblyInstruction = irt.getOp();
        emit(o, assemblyInstruction + " " + result + 
          "," + expression(irt.getSub(0), o) + 
          "," + expression(irt.getSub(1), o));
      }
    } 
    else {
      error(irt.getOp());
    }
    registerCount++;
    return result;
  }

  private static void emit(PrintStream o, String s)
  {
    o.println(s);
  }

  private static void error(String op)
  {
    System.out.println("CG error: "+op);
    System.exit(1);
  }
}
