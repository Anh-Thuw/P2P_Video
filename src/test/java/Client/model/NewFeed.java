package com.model;

import java.util.ArrayList;

public class NewFeed {
	public		static		ArrayList<NewFeed>		listNotAvailable	= 	new ArrayList<>();
	public		static		ArrayList<NewFeed> 		listAvailable		= 	new ArrayList<>();
	
	private 				String 					username ; 
	private 				String 					title ; 
	private 				String 					path ; 
	private 				String 					status ;
	
	public NewFeed() {	
	}
	
	public NewFeed(String username, String title, String path, String status) {
		this.username 	= 	username;
		this.title 		= 	title;
		this.path 		= 	path;
		this.status 	= 	status;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}