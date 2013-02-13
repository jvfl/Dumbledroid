package io.leocad.dumbledroid.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	
	public static ArrayList<String> arrayNames = new ArrayList<String>();
	public static ArrayList<String> originalNames = new ArrayList<String>();
	
	public static void preParseJSONObject(JSONObject jsonObject) throws JSONException{
		Iterator keys = jsonObject.keys();
		
		while(keys.hasNext()){
			
			String keyString = keys.next().toString();
			Object attribute = jsonObject.get(keyString);
			
			if(attribute instanceof JSONArray){
				
				Object first = ((JSONArray) attribute).get(0);
				
				if(first instanceof JSONObject){
					
					String name = keyString.substring(0, 1).toUpperCase() + keyString.substring(1,(keyString.length()-1)).toLowerCase();			
					
					arrayNames.add(name);
					originalNames.add(keyString);
				}
			}
			
		}
	}
	
	public static void preParseJSONArray(JSONArray jsonArray) throws JSONException {
		
		Object first = jsonArray.get(0);
		
		if(first instanceof JSONObject){
			preParseJSONObject((JSONObject) first);
		}
		
	}
	
		
	public static void parseJSONObject(JSONObject jsonObject, String rootClass, String rootPackage) throws JSONException, IOException{
		Iterator keys = jsonObject.keys();
		
		ClassGenerator gen = new ClassGenerator(rootClass, rootPackage);
		
		while(keys.hasNext()){
			
			String keyString = keys.next().toString();
			Object attribute = jsonObject.get(keyString);
			String attributeString = attribute.toString();
							
			if(attribute instanceof JSONObject){
				
				attributeString = keyString.substring(0, 1).toUpperCase() + keyString.substring(1).toLowerCase();
				
				parseJSONObject((JSONObject) attribute, attributeString , rootPackage );
				
			}else if(attribute instanceof JSONArray){
				
				String arrayType = parseJSONArray((JSONArray) attribute,null, null, rootPackage);
				
				StringBuffer propertyBuffer = new StringBuffer();
				propertyBuffer.append("List<");
				propertyBuffer.append(arrayType);
				propertyBuffer.append(">");
				
				attributeString = propertyBuffer.toString();
				
			}else{
				attributeString = inferPrimitiveType(attributeString);
			}
			
			gen.addPublicAttribute(attributeString,keyString);
			
		}
		
		gen.writeToFile(rootClass);
	}
	
	public static String parseJSONArray(JSONArray jsonArray, String thisClass, String rootClass, String rootPackage) throws JSONException, IOException{
		
		Object first = jsonArray.get(0);
		String arrayType = "";
		
		if(first instanceof JSONObject){
			
			if(rootClass != null){						
				arrayType = rootClass;	
			}else{
				arrayType = arrayNames.remove(0);
			}
			
			parseJSONObject((JSONObject) first, arrayType , rootPackage);
			
		}else if(first instanceof JSONArray){
			
			
			
		}else{
						
			String type = inferPrimitiveType(first.toString());
			
			if(type.equals("int")){
				arrayType = "Integer";
			}else{
				arrayType = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
			}
			
		}
		
		if(rootClass != null){
			ClassGenerator gen = new ClassGenerator(thisClass,rootPackage);
			
			StringBuffer propertyBuffer = new StringBuffer();
			propertyBuffer.append("List<");
			propertyBuffer.append(arrayType);
			propertyBuffer.append(">");
			
			gen.addPublicAttribute(propertyBuffer.toString(), rootClass.toLowerCase());
			
			gen.writeToFile(thisClass);
		}
		
		return arrayType;
		
	}
	
	private static String inferPrimitiveType(String attribute){
		
		String doublePattern = "\\d+.\\d+";
		String intPattern = "\\d+";
		String booleanPattern = "true|false";
		
		if(attribute.matches(doublePattern)){
			
			return "double";
			
		}else if(attribute.matches(intPattern)){
			
			return "int";
			
		}else if(attribute.matches(booleanPattern)){
			
			return "boolean";
			
		}else{
			
			return "String";
			
		}
		
	}

}
