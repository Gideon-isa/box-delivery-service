package com.polarisdigitech.boxdeliveryservice.domain.Delivery;

import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValidationError;

import java.time.Instant;

public final class FlightRoundTrip {
    private Instant departureTime;
    private Instant locationArrivalTime;
    private Instant returnedTime;

   private FlightRoundTrip(Instant locationArrivalTime, Instant returnedTime, Instant departureTime) {
       this.locationArrivalTime = locationArrivalTime;
       this.returnedTime = returnedTime;
       this.departureTime = departureTime;
   }
   public static Result<FlightRoundTrip, DomainError> build(Instant locationArrivalTime, Instant returnedTime, Instant departureTime) {
       if (locationArrivalTime == null) {
           return Result.failure(ValidationError.of("locationArrivalTime", "locationArrivalTime can not be null"));
       }
       if (returnedTime == null) {
           return Result.failure(ValidationError.of("returnedTime", "returnedTime can not be null"));
       }
       if (departureTime == null) {
           return Result.failure(ValidationError.of("departure", "departure can not be null"));
       }
       return Result.success(new FlightRoundTrip(locationArrivalTime, returnedTime, departureTime));
   }
}
