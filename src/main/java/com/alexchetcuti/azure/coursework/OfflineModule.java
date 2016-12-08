package com.alexchetcuti.azure.coursework;

import java.util.ArrayList;
import java.util.List;

public class OfflineModule {

	public OfflineModule() {
		// TODO Auto-generated constructor stub
	}

	private List<Camera> offlineCameraList = new ArrayList<Camera>();
	private List<Vehicle> offlineVehicleList =  new ArrayList<Vehicle>();

	public void addOfflineCamera(Camera camera) 
	{
		offlineCameraList.add(camera);
	}
	
	public void addOfflineVehicle(Vehicle vehicle) 
	{
		offlineVehicleList.add(vehicle);
	}
	
	public int countOfflineCameras()
	{
		return offlineCameraList.size();
	}
	
	public int countOfflineVehicles()
	{
		return offlineVehicleList.size();
	}
	
	/**
	 * @return the offlineCameraList
	 */
	public List<Camera> getOfflineCameraList() {
		return offlineCameraList;
	}
	/**
	 * @param offlineCameraList the offlineCameraList to set
	 */
	public void setOfflineCameraList(List<Camera> offlineCameraList) {
		this.offlineCameraList = offlineCameraList;
	}

	/**
	 * @return the offlineVehicleList
	 */
	public List<Vehicle> getOfflineVehicleList() {
		return offlineVehicleList;
	}

	/**
	 * @param offlineVehicleList the offlineVehicleList to set
	 */
	public void setOfflineVehicleList(List<Vehicle> offlineVehicleList) {
		this.offlineVehicleList = offlineVehicleList;
	}
}
