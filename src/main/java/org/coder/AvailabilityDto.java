package org.coder;

import java.time.LocalDateTime;

public class AvailabilityDto {
    private int doctorId;
    private String doctorName;
    private LocalDateTime date;

    public AvailabilityDto(int doctorId, String doctorName, LocalDateTime date) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
