package com.alexchetcuti.azure.coursework;
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
	
	public static void startCamera(String cameraInfo)
	{
		ServiceBusContract service = serviceConnect();
		
		//Send Messages to a topic
		// Create message, passing a string message for the body
		BrokeredMessage message = new BrokeredMessage(cameraInfo);
		message.setProperty("MessageType", "CAMERA");
			
		// Send message to the topic
		try {
			service.sendTopicMessage("MainTopic", message);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void carSighting(Vehicle vehicle)
	{
		ServiceBusContract service = serviceConnect();
		
		//Send Messages to a topic
		// Create message, passing a string message for the body
		BrokeredMessage message = new BrokeredMessage(vehicle.toString());
		// Set some additional custom app-specific property
		message.setProperty("MessageType", "VEHICLE");
		message.setProperty("velocity", vehicle.getVelocity());
		message.setProperty("cameraUniqueID", vehicle.getCameraUniqueID());
			
		// Send message to the topic
		try {
			service.sendTopicMessage("MainTopic", message);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
