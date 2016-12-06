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
import com.microsoft.windowsazure.services.servicebus.models.CreateTopicResult;
import com.microsoft.windowsazure.services.servicebus.models.RuleInfo;
import com.microsoft.windowsazure.services.servicebus.models.SubscriptionInfo;
import com.microsoft.windowsazure.services.servicebus.models.TopicInfo;

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
	
	public static void createTopic()
	{
		ServiceBusContract service = serviceConnect();
		TopicInfo topicInfo = new TopicInfo("TestTopic");
		try  
		{
		    CreateTopicResult result = service.createTopic(topicInfo);
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}
	
	public static void createSubscription()
	{
		ServiceBusContract service = serviceConnect();
		
		//Create a subscription with the default (MatchAll) filter
		SubscriptionInfo subInfo = new SubscriptionInfo("AllMessages");
		try {
			CreateSubscriptionResult result =
			    service.createSubscription("TestTopic", subInfo);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void createFilterHighSubscription()
	{
		// Create a "HighMessages" filtered subscription
		ServiceBusContract service = serviceConnect();
		
		try {
			SubscriptionInfo subInfo1 = new SubscriptionInfo("HighMessages");
			CreateSubscriptionResult result1 = service.createSubscription("TestTopic", subInfo1);
			RuleInfo ruleInfo1 = new RuleInfo("myRuleGT3");
			ruleInfo1 = ruleInfo1.withSqlExpressionFilter("MessageNumber > 3");
			CreateRuleResult ruleResult1 = service.createRule("TestTopic", "HighMessages", ruleInfo1);
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("TestTopic", "HighMessages", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void createFilterLowSubscription()
	{
		// Create a "LowMessages" filtered subscription
		ServiceBusContract service = serviceConnect();
		
		try {
			SubscriptionInfo subInfo2 = new SubscriptionInfo("LowMessages");
			CreateSubscriptionResult result2 = service.createSubscription("TestTopic", subInfo2);
			RuleInfo ruleInfo2 = new RuleInfo("myRuleLE3");
			ruleInfo2 = ruleInfo2.withSqlExpressionFilter("MessageNumber <= 3");
			CreateRuleResult ruleResult2 = service.createRule("TestTopic", "LowMessages", ruleInfo2);
			// Delete the default rule, otherwise the new rule won't be invoked.
			service.deleteRule("TestTopic", "LowMessages", "$Default");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		message.setProperty("camera_u_id", vehicle.getCamera_u_id());
			
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
