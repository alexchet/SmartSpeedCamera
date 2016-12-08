package com.alexchetcuti.azure.coursework;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateRuleResult;
import com.microsoft.windowsazure.services.servicebus.models.CreateSubscriptionResult;
import com.microsoft.windowsazure.services.servicebus.models.RuleInfo;
import com.microsoft.windowsazure.services.servicebus.models.SubscriptionInfo;

public class Common {
	
	public static ServiceBusContract serviceConnect()
	{
		Configuration config =
			    ServiceBusConfiguration.configureWithSASAuthentication(
			      "smartspeedcamera",
			      "RootManageSharedAccessKey",
			      "e9eZDy/Vj+gzxhTsM2ZMzNlliqpO305GT/KB+GPEvas=",
			      ".servicebus.windows.net"
			      );

		ServiceBusContract service = ServiceBusService.create(config);
		return service;
	}
	
	public static void startCamera(Camera camera, OfflineModule offlineModule)
	{
		boolean tryOffline = false;
		ServiceBusContract service = serviceConnect();
		
		// Create message, passing a string message for the body
		BrokeredMessage message = new BrokeredMessage(camera.toString());
		message.setProperty("MessageType", "CAMERA");
		message.setProperty("uniqueID", camera.getUniqueID());
		message.setProperty("streetName", camera.getStreetName());
		message.setProperty("town", camera.getTown());
		message.setProperty("speedLimit", camera.getSpeedLimit());
		message.setProperty("startTime", camera.getStartTime());
			
		try {
			service.sendTopicMessage("MainTopic", message);
		} catch (Exception e) {
			offlineModule.addOfflineCamera(camera);
		}
		
		//Means we have Internet here so we try again
		if (tryOffline && offlineModule.countOfflineCameras() > 0) {
			List<Camera> offlineCameraList = offlineModule.getOfflineCameraList();
			for (Camera offlineCamera : offlineCameraList) {
				startCamera(offlineCamera, offlineModule);
			}
		}
		
		offlineModule.setOfflineCameraList(new ArrayList<Camera>());;
	}
	
	public static void carSighting(Vehicle vehicle, OfflineModule offlineModule)
	{
		boolean tryOffline = false;
		ServiceBusContract service = serviceConnect();
		
		// Create message, passing a string message for the body
		BrokeredMessage message = new BrokeredMessage(vehicle.toString());
		// Set some additional custom app-specific property
		message.setProperty("MessageType", "VEHICLE");
		message.setProperty("vehicleType", vehicle.getVehicleType());
		message.setProperty("regPlate", vehicle.getRegPlate());
		message.setProperty("velocity", vehicle.getVelocity());
		message.setProperty("cameraUniqueID", vehicle.getCameraUniqueID());
			
		try {
			service.sendTopicMessage("MainTopic", message);
			tryOffline = true;
		} catch (Exception e) {
			offlineModule.addOfflineVehicle(vehicle);
		}
		
		//Means we have Internet here so we try again
		if (tryOffline && offlineModule.countOfflineVehicles() > 0) {
			List<Vehicle> offlineVehicleList = offlineModule.getOfflineVehicleList();
			for (Vehicle offlineVehicle : offlineVehicleList) {
				carSighting(offlineVehicle, offlineModule);
			}
			
			offlineModule.setOfflineVehicleList(new ArrayList<Vehicle>());
		}
	}
	
	public static String genRegPlate()
	{
		String possChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String possNumbs = "1234567890";
		
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
        for (int i=0; i<2; i++) {
        	int randInt = rand.nextInt(25);
        	builder.append(possChars.charAt(randInt));
        }
        for (int i=0; i<2; i++) {
        	int randInt = rand.nextInt(9);
        	builder.append(possNumbs.charAt(randInt));
        }
    	builder.append(" ");

        for (int i=0; i<3; i++) {
        	int randInt = rand.nextInt(25);
        	builder.append(possChars.charAt(randInt));
        }
        
        return builder.toString();
	}

	public static void createCamerasSubscription()
	{ 
		ServiceBusContract service = serviceConnect();
		
		SubscriptionInfo subInfo = new SubscriptionInfo("SpeedCameras");
		try {
			CreateSubscriptionResult result = service.createSubscription("MainTopic", subInfo);
			RuleInfo ruleInfo = new RuleInfo("ruleCamera");
			ruleInfo = ruleInfo.withSqlExpressionFilter("MessageType = 'CAMERA'");
			CreateRuleResult ruleResult = service.createRule("MainTopic", "SpeedCameras", ruleInfo);
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("MainTopic", "SpeedCameras", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createVehiclesSubscription()
	{ 
		ServiceBusContract service = serviceConnect();
		
		SubscriptionInfo subInfo = new SubscriptionInfo("Vehicles");
		try {
			CreateSubscriptionResult result = service.createSubscription("MainTopic", subInfo);
			RuleInfo ruleInfo = new RuleInfo("ruleVehicles");
			ruleInfo = ruleInfo.withSqlExpressionFilter("MessageType = 'VEHICLE'");
			CreateRuleResult ruleResult = service.createRule("MainTopic", "Vehicles", ruleInfo);
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("MainTopic", "Vehicles", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
