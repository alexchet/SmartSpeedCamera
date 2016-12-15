package com.alexchetcuti.azure.coursework;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
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
import com.sun.jersey.api.client.ClientHandlerException;

public class Common {

	private static final String SSCStorageConnectionString = 
			"DefaultEndpointsProtocol=http;" +
			"AccountName=sscdatastorage;" +
			"AccountKey=9OcI4KkZf6FyD/CSOLBQIzbjxiSIcjKVy0QtO6U1z0Ydb/juV4k49MTLWoRMWl124/GyfOwFMSDGN3htKTnq3Q==";

	public static CloudStorageAccount storageConnect()
	{
	    try {
		    // Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount =
			    CloudStorageAccount.parse(SSCStorageConnectionString);
			
			return storageAccount;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
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
		if (checkInternet()) {
			try{
				ServiceBusContract service = serviceConnect();
				// Create message, passing a string message for the body
				BrokeredMessage message = new BrokeredMessage(camera.toString());
				message.setProperty("uniqueID", camera.getUniqueID());
				message.setProperty("streetName", camera.getStreetName());
				message.setProperty("town", camera.getTown());
				message.setProperty("speedLimit", camera.getSpeedLimit());
				message.setProperty("startTime", camera.getStartTime());
				
				service.sendTopicMessage("MainTopic", message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.print("Offline: ");
			offlineModule.addOfflineCamera(camera);
		}
	}
	
	public static void carSighting(Vehicle vehicle, int speedLimit, OfflineModule offlineModule)
	{
		if (checkInternet()) {
			ServiceBusContract service = serviceConnect();
			
			// Create message, passing a string message for the body
			BrokeredMessage message = new BrokeredMessage(vehicle.toString());
			// Set some additional custom app-specific property
			message.setProperty("vehicleType", vehicle.getVehicleType());
			message.setProperty("regPlate", vehicle.getRegPlate());
			message.setProperty("velocity", vehicle.getVelocity());
			message.setProperty("speedLimit", speedLimit);
			message.setProperty("cameraUniqueID", vehicle.getCameraUniqueID());
				
			try {
				service.sendTopicMessage("MainTopic", message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.print("Offline: ");
			offlineModule.addOfflineVehicle(vehicle);;
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
			ruleInfo = ruleInfo.withSqlExpressionFilter("uniqueID IS NOT NULL");
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
			ruleInfo = ruleInfo.withSqlExpressionFilter("regPlate IS NOT NULL");
			CreateRuleResult ruleResult = service.createRule("MainTopic", "Vehicles", ruleInfo);
			
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("MainTopic", "Vehicles", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createVehiclesSpeedingSubscription()
	{ 
		ServiceBusContract service = serviceConnect();
		
		SubscriptionInfo subInfo = new SubscriptionInfo("VehiclesSpeeding");
		try {
			CreateSubscriptionResult result = service.createSubscription("MainTopic", subInfo);
			
			RuleInfo ruleInfo1 = new RuleInfo("ruleSpeeding");
			ruleInfo1 = ruleInfo1.withSqlExpressionFilter("velocity > speedLimit");
			CreateRuleResult ruleResult1 = service.createRule("MainTopic", "VehiclesSpeeding", ruleInfo1);
			
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("MainTopic", "VehiclesSpeeding", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkInternet()
	{
		boolean isConnected = false;
		try {
			if ( InetAddress.getByName("www.google.com").isReachable(10000)) isConnected = true;
		} catch (Exception e) {
			isConnected = false;
		}
		
		return isConnected;
	}
	
	public static void addMessageToQueue(String regPlate)
	{
		try
		{
			ServiceBusContract service = serviceConnect();
		    BrokeredMessage message = new BrokeredMessage(regPlate);
		    service.sendQueueMessage("vehiclespeedingqueue", message);
		}
		catch (ServiceException e)
		{
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}
}
