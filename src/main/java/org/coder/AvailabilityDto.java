package org.coder;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityDto {
    private int doctorId;
    private LocalTime time;
    private LocalDate date;

    public AvailabilityDto() {
    }

    public AvailabilityDto(int doctorId, LocalTime time, LocalDate date) {
        this.doctorId = doctorId;
        this.time = time;
        this.date = date;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }
}
