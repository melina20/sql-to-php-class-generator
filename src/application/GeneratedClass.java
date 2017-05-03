package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneratedClass {
	private String name;
	private List<ClassParameter> parameters;
	private String output;
	
	public GeneratedClass(String _name, List<ClassParameter> _parameters){
		this.name = _name.substring(0, 1).toUpperCase() + _name.substring(1);
		this.parameters = _parameters;
	}
	
	public String export() {		
		this.output = "<?php \n";
		this.output += "class "+ this.name + " {\n";
		
		for (int i = 0; i < this.parameters.size(); i++){
			String t = this.parameters.get(i).getType();
			Integer l = this.parameters.get(i).getLength();
						
			this.output += "/** \n";
			this.output += " * @var "+ t + "\n";
			if (l > 0) {
				this.output += " * @length " + this.parameters.get(i).getLength() + "\n";
			}
			
			this.output += " */\n";
			this.output += "private $" + this.parameters.get(i).getName() + ";\n";
			this.output += "\n";
		}
		
		for (int i = 0; i < this.parameters.size(); i++){
			this.output += this.createGetter(this.parameters.get(i).getName());
			this.output += this.createSetter(this.parameters.get(i).getName());
		}
		this.output += "}\n";
		return this.output;
	
	}
	
	private String createGetter(String name){
		String ret = "";
		ret += "public function get" + name.substring(0, 1).toUpperCase() + name.substring(1) + "(){\n";
		ret += "    return $this->"+name + ";\n";
		ret += "}\n";
		
		return ret;
	}
	
	private String createSetter(String name){
		String ret = "";
		ret += "public function set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "($" + name + "){\n";
		ret += "    $this->"+name + " = "+ name + ";\n"; 
		ret += "}\n";
		
		return ret;
	}
}
