package com.alexchetcuti.azure.coursework;
import java.util.Random;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;

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
	
	public static void sendMessages()
	{
		ServiceBusContract service = serviceConnect();
		
		//Send Messages to a topic
		for (int i=0; i<5; i++)  {
			// Create message, passing a string message for the body
			BrokeredMessage message = new BrokeredMessage("Test message " + i);
			// Set some additional custom app-specific property
			message.setProperty("MessageNumber", i);
			// Send message to the topic
			try {
				service.sendTopicMessage("TestTopic", message);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void startCamera(String cameraInfo)
	{
		ServiceBusContract service = serviceConnect();
		
		//Send Messages to a topic
		// Create message, passing a string message for the body
		BrokeredMessage message = new BrokeredMessage(cameraInfo);
			
		// Send message to the topic
		try {
			service.sendTopicMessage("cameras", message);
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
		message.setProperty("velocity", vehicle.getVelocity());
		message.setProperty("cameraUniqueID", vehicle.getCameraUniqueID());
			
		// Send message to the topic
		try {
			service.sendTopicMessage("vehicles", message);
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
}
