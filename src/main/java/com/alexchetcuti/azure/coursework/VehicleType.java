package com.alexchetcuti.azure.coursework;

public enum VehicleType {
	CAR(1),
    TRUCK(2),
    MOTORCYLE(3);
    
	private int value;
    private VehicleType(int value) {
    	this.value = value;
    }
    
    public int getValue() {
    	return value;
    }
}
