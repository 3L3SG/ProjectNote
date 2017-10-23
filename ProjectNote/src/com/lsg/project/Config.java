package com.lsg.project;

public class Config {
	private String email;
	private String password;
	private String hrs;
	private String mins;
	
	public Config() {
		
	}

	public Config(String email, String password, String hrs, String mins) {
		super();
		this.email = email;
		this.password = password;
		this.hrs = hrs;
		this.mins = mins;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHrs() {
		return hrs;
	}

	public void setHrs(String hrs) {
		this.hrs = hrs;
	}

	public String getMins() {
		return mins;
	}

	public void setMins(String mins) {
		this.mins = mins;
	}
	
	@Override
	public String toString() {
		return "Config [email=" + email + ", password=" + password + ", hrs=" + hrs + ", mins=" + mins + "]";
	}
	
	
}
