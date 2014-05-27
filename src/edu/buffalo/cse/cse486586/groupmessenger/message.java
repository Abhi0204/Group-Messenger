package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.Serializable;


public class message implements Serializable {

	private String key;
	private String value;
	private static final long serialVersionUID = 1234L;	
	
	public message(String key,String value)
	{
		this.setKey(key);
		this.setValue(value);
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}





}
