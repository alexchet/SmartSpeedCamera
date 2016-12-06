package com.alexchetcuti.azure.coursework;
import java.util.Date;

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
			
			Common.startCamera(currentCamera.toString());
			System.out.println("Camera sent message to Topic successfully!");
		}
	}
}
