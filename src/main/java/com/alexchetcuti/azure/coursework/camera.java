package com.alexchetcuti.azure.coursework;

import java.util.Date;

public class camera {

	public camera() {
		
	}
	
	public camera(int u_id, String street_name, String town, int speed_limit, String start_time) {
		this.u_id = u_id;
		this.street_name = street_name;
		this.town = town;
		this.speed_limit = speed_limit;
		this.start_time = start_time;
	}
	
	public String toString()
	{
		return "Unique ID: " + this.u_id + " | " +
				"Street Name: " + this.street_name + " | " +
				"Town: " + this.town + " | " +
				"Speed Limit: " + this.speed_limit + " | " +
				"Start Time: " + this.start_time;
	}
	
	private int u_id = 0;
	private String street_name = "";
	private String town = "";
	private int speed_limit = 0;
	private String start_time = null;
	
	/**
	 * @return the u_id
	 */
	public int getU_id() {
		return u_id;
	}
	/**
	 * @param u_id the u_id to set
	 */
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	/**
	 * @return the street_name
	 */
	public String getStreet_name() {
		return street_name;
	}
	/**
	 * @param street_name the street_name to set
	 */
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}
	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}
	/**
	 * @return the speed_limit
	 */
	public int getSpeed_limit() {
		return speed_limit;
	}
	/**
	 * @param speed_limit the speed_limit to set
	 */
	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}
	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
}
