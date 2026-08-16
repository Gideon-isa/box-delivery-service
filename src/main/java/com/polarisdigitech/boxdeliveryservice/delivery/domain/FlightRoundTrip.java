package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

import java.time.Instant;

public final class FlightRoundTrip implements ValueObject {
    private Instant departureTime;
    private Instant locationArrivalTime;

   private FlightRoundTrip(Instant locationArrivalTime, Instant departureTime) {
       this.locationArrivalTime = locationArrivalTime;
       this.departureTime = departureTime;
   }
   public static Result<FlightRoundTrip, DomainError> build(Instant locationArrivalTime,  Instant departureTime) {
       if (locationArrivalTime == null) {
           return Result.failure(ValidationError.of("locationArrivalTime", "locationArrivalTime can not be null"));
       }

       if (departureTime == null) {
           return Result.failure(ValidationError.of("departure", "departure can not be null"));
       }
       return Result.success(new FlightRoundTrip(locationArrivalTime, departureTime));
   }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getLocationArrivalTime() {
        return locationArrivalTime;
    }

    public void setLocationArrivalTime(Instant locationArrivalTime) {
        this.locationArrivalTime = locationArrivalTime;
    }
}
