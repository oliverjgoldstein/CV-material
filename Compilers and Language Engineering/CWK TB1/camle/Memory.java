// COMS22201: Memory allocation for strings

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class Memory {

  static ArrayList<Byte> memory = new ArrayList<Byte>();
  static HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();

  static int is_haskell = 0;

  static public void change_Haskell(int value) {
    is_haskell   = value;
  }

  static public int allocateString(String text)
  {
    int addr = memory.size();
    int size = text.length();
    for (int i=0; i<size; i++) {
      memory.add(new Byte("", text.charAt(i)));
    }
    memory.add(new Byte("", 0));
    return addr;
  }

static public int allocateInt(String variableName) {
  
  // This also has to act as our symbol table for variables. i.e. it will find a suitable memory location.

  if(symbolTable.containsKey(variableName)) {
    int addr = symbolTable.get(variableName);
    return addr;
  } else {
	  int addr = memory.size();
    while (addr%4 != 0) {
      memory.add(new Byte("", 0));
      addr = memory.size();
    }
    memory.add(new Byte(variableName, 0));
    memory.add(new Byte(variableName, 0));
    memory.add(new Byte(variableName, 0));
    memory.add(new Byte(variableName, 0));
    symbolTable.put(variableName, addr);
    return addr;
  }
}

  static public void dumpData(PrintStream o)
  {
    Byte b;
    String s;
    int c;
    int size = memory.size();
    for (int i=0; i<size; i++) {
      b = memory.get(i);
      c = b.getContents();
      if (c >= 32) {
        s = String.valueOf((char)c);
      }
      else {
        s = ""; // "\\"+String.valueOf(c);
      }

      // THIS VARIABLE NEEDS TO BE UNCOMMENTED IF FOR STEVE GREGORYS PART
      if(is_haskell == 0) {
        o.println("DATA "+c+" ; "+s+" "+b.getName());
      }
    }
  }
}

class Byte {
  String varname;
  int contents;

  Byte(String n, int c)
  {
    varname = n;
    contents = c;
  } 

  String getName()
  {
    return varname;
  }

  int getContents()
  {
    return contents;
  }
}
