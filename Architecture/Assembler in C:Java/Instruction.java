import java.util.*;

public class Instruction {
	int hex;

	Instruction(String line, Set<Label> labels) {
		String[] parsed = line.split(" ");
		String instr = parsed[0];
		ArrayList<String> args = new ArrayList<String>();
		if (parsed.length > 1) {
			if (parsed[1].contains(",")) {
				args.addAll(Arrays.asList(parsed[1].split(",")));
			} else {
				args.add(parsed[1]);
			}
		}
		switch (instr.toUpperCase()) {
		case ("INC"):
		case ("DEC"):
			try {
				String temp = "";
				if (instr.toUpperCase().equals("INC"))
					temp += "000";
				else
					temp += "001";
				if (args.get(0).equals("r0")) {
					temp += "00000";
				} else {
					temp += "10000";
				}
				hex = Integer.parseInt(temp, 2);
			} catch (NumberFormatException e) {
				System.err.println("ERROR OCCURRED!");
				System.exit(1);
			}
			break;
		case ("JNZ"):case ("JNEG"):
			try {
				if (args.size() == 0)
					throw new Error("NO ARGUMENTS PROVIDED!");
				String temp;
				if(instr.toUpperCase().equals("JNZ"))
					temp="01000000";
				else
					temp="01100000";
				hex = Integer.parseInt(temp, 2);
				if (args.get(0).startsWith(":")) {
					if (!labels.contains(new Label(args.get(0))))
						throw new Error("LABEL DOES NOT EXIST");
					for (Label label : labels) {
						if (args.get(0).equals(label.getLabel())) {
							hex += label.getAddress();
							break;
						}
					}
				} else {
					int address = Integer.parseInt(args.get(0), 16);
					if (address > 15)
						throw new Error("ADDRESS GREATER THAN 15");
					hex += address;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (Error e) {
				e.printStackTrace();
				System.exit(1);
			}
			break;
		case ("STR"):case ("LDR"):
			try {
				if (args.size() < 2)
					throw new Error("NOT ENOUGH ARGUMENTS PROVIDED!");
				String temp;
				if(instr.toUpperCase().equals("STR"))
					temp="100";
				else
					temp="101";
				if(args.get(0).equals("r0"))
					temp+="00000";
				else
					temp+="10000";
				hex = Integer.parseInt(temp, 2);
				if (args.get(1).startsWith(":")) {
					if (!labels.contains(new Label(args.get(1))))
						throw new Error("LABEL DOES NOT EXIST");
					for (Label label : labels) {
						if (args.get(1).equals(label.getLabel())) {
							hex += label.getAddress();
							break;
						}
					}
				} else {
					int address = Integer.parseInt(args.get(1), 16);
					if (address > 15)
						throw new Error("ADDRESS GREATER THAN 15");
					hex += address;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (Error e) {
				e.printStackTrace();
				System.exit(1);
			}
			break;
//		case ("HALT"):
//			hex = Integer.parseInt("01100000", 2);
//			break;
		default:
			throw new Error("WRONG INSTRUCTION");
		}
	}

	public String getHex() {
		return Integer.toHexString(hex);
	}

}
