package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

public final class Coordinates implements ValueObject {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final double latitude;
    private final double longitude;

    private Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Result<Coordinates, DomainError> of(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            return Result.failure(ValidationError.of("latitude", "latitude must be between -90 and 90"));
        }

        if (longitude < -180 || longitude > 180) {
            return Result.failure(ValidationError.of("longitude", "Longitude must be between -180 and 180"));
        }
        return Result.success(new Coordinates(latitude, longitude));
    }

    public double distanceToKm(Coordinates other) {
        double latitude1Rad = Math.toRadians(this.latitude);
        double latitude2Rad = Math.toRadians(other.latitude);

        double deltaLatitude = Math.toRadians(other.latitude - this.latitude);
        double deltaLongitude = Math.toRadians(other.longitude - this.longitude);
        double coreOfHaversine  = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.cos(latitude1Rad) * Math.cos(latitude2Rad)
                * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);

        double angularDistance = 2 * Math.atan2(Math.sqrt(coreOfHaversine), Math.sqrt(1 - coreOfHaversine));
        return EARTH_RADIUS_KM * angularDistance;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
