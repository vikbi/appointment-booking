package org.coder;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityDto {
    private int doctorId;
    private String doctorName;
    private LocalTime time;
    private LocalDate date;


    public AvailabilityDto(int doctorId, String doctorName, LocalTime time, LocalDate date) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
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

    public String getDoctorName() {
        return doctorName;
    }
}
