package org.coder;

import java.time.LocalDateTime;

public class AvailabilityDto {
    private DoctorDto doctor;
    private LocalDateTime date;

    public AvailabilityDto(DoctorDto doctor, LocalDateTime date) {
        this.doctor = doctor;
        this.date = date;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
