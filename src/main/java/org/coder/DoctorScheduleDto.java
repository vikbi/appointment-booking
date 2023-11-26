package org.coder;

import java.util.List;

public class DoctorScheduleDto {
    private DoctorDto doctor;
    private List<WorkHourDto> schedule;

    public DoctorScheduleDto(DoctorDto doctor, List<WorkHourDto> schedule) {
        this.doctor = doctor;
        this.schedule = schedule;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public List<WorkHourDto> getSchedule() {
        return schedule;
    }
}
