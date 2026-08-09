import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.*;

public class Reader implements ActionListener {
	ArrayList<Instruction> instructions;
	Set<Label> labels;

	public Reader() {
		clearTable(this);
	}

	public static void main(String[] args) {
		Reader reader = new Reader();
		SwingUtilities.invokeLater(reader::run);
	}
	
	static void clearTable(Reader reader){
		reader.instructions=new ArrayList<Instruction>();
		reader.labels = new HashSet<Label>();
	}
	
	private void run(){
		JFrame frame = new JFrame();
		JButton button = new JButton("Assemble!");
		button.addActionListener(this);
		frame.add(button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setTitle("Counter Machine Assembler");
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	void execute() {
		String filename = "input.txt";
		Writer writer = null;
		try {
			this.readLabels(filename);
			this.readInstructions(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		int i=0;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), "utf-8"));
			writer.write("v2.0 raw\n");
			for (Instruction inst : this.instructions) {
				writer.write(inst.getHex() + " ");
				System.out.print(Integer.toHexString(i) + " : ");
				System.out.println(inst.getHex() + " (" + Integer.toBinaryString(inst.hex) + ")");
				i++;
			}

		} catch (IOException ex) {
			System.err.println("AN ERROR OCCURED!");
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				System.err.println("AN ERROR OCCURED!");
				System.exit(1);
			}
		}
	}

	void readLabels(String filename) throws FileNotFoundException {
		int i = 0;
		File file = new File(filename);
		Scanner in = new Scanner(file);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.startsWith(":"))
				labels.add(new Label(line.trim(), i));
			if (!(line.startsWith(":") || line.startsWith(";")))
				i++;
		}
		in.close();
		System.out.println("First pass");
	}

	void readInstructions(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner in = new Scanner(file);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (!(line.startsWith(":") || line.startsWith(";"))) {
				instructions.add(new Instruction(line, labels));
			}
		}
		in.close();
		System.out.println("Second pass");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		clearTable(this);
		this.execute();
	}

}
