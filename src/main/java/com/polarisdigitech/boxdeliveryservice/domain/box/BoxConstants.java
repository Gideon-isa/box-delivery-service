package com.polarisdigitech.boxdeliveryservice.domain.box;

public final class BoxConstants {
    private BoxConstants() {}

    public final static double BOX_WEIGHT = 1.2; //kg
    public final static double MAX_PAYLOAD = 0.5; //kg ~ 500grams
    public final static double MAX_SPEED_REDUCTION = 0.4; //40% on maximum load 500grams
    public final static double MIN_SPEED = 10.0; // km/h
    public final static double MAX_SPEED = 50.0; // km/h
}
