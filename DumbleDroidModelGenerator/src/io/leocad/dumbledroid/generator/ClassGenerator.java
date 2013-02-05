package io.leocad.dumbledroid.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassGenerator {
	
	private StringBuffer classCode;
	public static String filePath;
	
	public ClassGenerator(String className, String packageName){
		classCode = new StringBuffer();
		
		newPackageDecl(packageName);
		
		newClassDecl(className);
	}
	
	public void addPublicAttribute(String type, String name){
		classCode.append("	public ");
		classCode.append(type);
		classCode.append(" ");
		classCode.append(name);
		classCode.append(";");
		newLine();
	}
	
	public void addPrivateAttribute(String type, String name){
		
	}
	
	public void writeToFile(String fileName) throws IOException{
		newLines();
		classCode.append("}");
		
		File file = new File(filePath+File.separator+fileName+".java");
		System.out.println(file.getAbsolutePath());
		if(!file.exists()){
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fw);
		
		writer.write(classCode.toString());
		
		writer.close();
		fw.close();
		
		
	}
	
	private void newPackageDecl(String packageName){
		classCode.append("package ");
		classCode.append(packageName);
		classCode.append(";");
		newLines();
	}
	
	private void newClassDecl(String className){
		classCode.append("public class ");
		classCode.append(className);
		classCode.append(" {");
		newLines();
	}
	
	private void newLines(){
		classCode.append("\n");
		classCode.append("\n");
	}
	
	private void newLine(){
		classCode.append("\n");
	}

}
