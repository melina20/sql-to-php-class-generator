package application;

public class ClassParameter {
	private String name;
	private String type;
	private int length;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;

		switch (type) {
		case "varchar":
			this.type="string";
			break;
		case "int":
			this.type="integer";
			break;
		case "bool":
			this.type="boolean";
			break;
		case "bigint":
			this.type="integer";
			break;
		case "dec":
			this.type="float";
			break;
		}
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
}
