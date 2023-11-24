package org.coder;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentsDto {
    private int doctorId;
    private int userId;
    private LocalDateTime date;

    public AppointmentsDto(int doctorId, int userId, LocalDateTime date) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.date = date;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
