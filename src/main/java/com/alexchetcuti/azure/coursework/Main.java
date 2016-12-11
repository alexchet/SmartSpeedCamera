package com.alexchetcuti.azure.coursework;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		OfflineModule offlineModule = new OfflineModule();
		
		if (args.length > 0) {
			Camera currentCamera = new Camera();
			currentCamera.setUniqueID(Integer.parseInt(args[0]));
			currentCamera.setStreetName(args[1]);
			currentCamera.setTown(args[2]);
			currentCamera.setSpeedLimit(Integer.parseInt(args[3]));
			Date d = new Date();
			currentCamera.setStartTime(d.toString());
			
			System.out.println(currentCamera.toString());
			
			Common.startCamera(currentCamera, offlineModule);
			System.out.println("Camera started successfully!");
			
			int trafficRate = Integer.parseInt(args[4]);
			int ratePerSecond = 60000/trafficRate;
			
			Random rand = new Random();
			
			while(true) {
				int min_velocity = currentCamera.getSpeedLimit() - (currentCamera.getSpeedLimit() / 3);
				int max_velocity = currentCamera.getSpeedLimit() + (currentCamera.getSpeedLimit() / 5);
				
				int current_velocity = rand.nextInt(max_velocity - min_velocity) + min_velocity;
				
				Vehicle v = new Vehicle(VehicleType.VALUES.get(rand.nextInt(VehicleType.SIZE)), Common.genRegPlate(), current_velocity, currentCamera.getUniqueID());
				Common.carSighting(v, currentCamera.getSpeedLimit(), offlineModule);
				System.out.println(v.toString());
				
	            try {
					Thread.sleep(ratePerSecond);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	    		//Means we have Internet here so we try again
	    		if (Common.checkInternet()) {
	    			if(offlineModule.countOfflineCameras() > 0) {
		    			List<Camera> offlineCameraList = offlineModule.getOfflineCameraList();
		    			for (Camera offlineCamera : offlineCameraList) {
		    				Common.startCamera(offlineCamera, offlineModule);
		    				System.out.println("From offline store :" + offlineCamera.toString());
		    			}
	
		    			offlineModule.setOfflineCameraList(new ArrayList<Camera>());
		    		}

		    		if (offlineModule.countOfflineVehicles() > 0) {
		    			List<Vehicle> offlineVehicleList = offlineModule.getOfflineVehicleList();
		    			for (Vehicle offlineVehicle : offlineVehicleList) {
		    				Common.carSighting(offlineVehicle, currentCamera.getSpeedLimit(), offlineModule);
		    				System.out.println("From offline store :" + offlineVehicle.toString());
		    			}
		    			
		    			offlineModule.setOfflineVehicleList(new ArrayList<Vehicle>());
		    		}
	    		}
			}
		}
	}
}
