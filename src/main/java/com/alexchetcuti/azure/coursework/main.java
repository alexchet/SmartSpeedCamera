package com.alexchetcuti.azure.coursework;
import java.util.Date;
import java.util.Random;

public class Main {

	public static void main(String[] args){
		if (args.length > 0) {
			Camera currentCamera = new Camera();
			currentCamera.setU_id(Integer.parseInt(args[0]));
			currentCamera.setStreet_name(args[1]);
			currentCamera.setTown(args[2]);
			currentCamera.setSpeed_limit(Integer.parseInt(args[3]));
			Date d = new Date();
			currentCamera.setStart_time(d.toString());
			
			System.out.println(currentCamera.toString());
			
			//Common.startCamera(currentCamera.toString());
			//System.out.println("Camera sent message to Topic successfully!");
			System.out.println("Camera not sending message to Topic.");
			
			int trafficRate = Integer.parseInt(args[4]);
			int ratePerSecond = 60000/trafficRate;
			
			Random rand = new Random();
			
			while(true) {
				int min_velocity = currentCamera.getSpeed_limit() / 2;
				int max_velocity = currentCamera.getSpeed_limit() * 2;
				
				int current_velocity = rand.nextInt(max_velocity - min_velocity) + min_velocity;
				
				Vehicle v = new Vehicle(VehicleType.VALUES.get(rand.nextInt(VehicleType.SIZE)), Common.genRegPlate(), current_velocity, currentCamera.getU_id());
				Common.carSighting(v);
				System.out.println(v.toString());
				
	            try {
					Thread.sleep(ratePerSecond);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
