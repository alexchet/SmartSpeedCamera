package com.alexchetcuti.azure.coursework;

public class Vehicle {
	
	public Vehicle() {
		
	}
	
	public Vehicle(VehicleType vehicle_type, String reg_plate, int velocity, int camera_u_id) {
		this.vehicle_type = vehicle_type;
		this.reg_plate = reg_plate;
		this.velocity = velocity;
		this.camera_u_id = camera_u_id;
	}
	
	public String toString()
	{
		return "Vehicle Type: " + this.vehicle_type.toString() + " | " +
				"Registration Plate: " + this.reg_plate + " | " +
				"Velocity: " + this.velocity + " | " +
				"Camera Unique ID: " + this.camera_u_id;
	}
	
	private VehicleType vehicle_type;
	private String reg_plate;
	private int velocity;
	private int camera_u_id;
	/**
	 * @return the vehicle_type
	 */
	public VehicleType getVehicle_type() {
		return vehicle_type;
	}
	/**
	 * @param vehicle_type the vehicle_type to set
	 */
	public void setVehicle_type(VehicleType vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	/**
	 * @return the reg_plate
	 */
	public String getReg_plate() {
		return reg_plate;
	}
	/**
	 * @param reg_plate the reg_plate to set
	 */
	public void setReg_plate(String reg_plate) {
		this.reg_plate = reg_plate;
	}
	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return velocity;
	}
	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	/**
	 * @return the camera_u_id
	 */
	public int getCamera_u_id() {
		return camera_u_id;
	}
	/**
	 * @param camera_u_id the camera_u_id to set
	 */
	public void setCamera_u_id(int camera_u_id) {
		this.camera_u_id = camera_u_id;
	} 
}
