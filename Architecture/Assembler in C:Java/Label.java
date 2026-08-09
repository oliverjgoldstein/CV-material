public class Label {

	private String label;
	private int address;

	public Label(String label, int address) {
		try {
			this.label = label;
			if (address > 15)
				throw new Error("NOT ENOUGH SPACE TO STORE ADDRESS");
			this.address = address;
		} catch (Error e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Label(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getAddress() {
		return address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Label))
			return false;
		Label other = (Label) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

}
