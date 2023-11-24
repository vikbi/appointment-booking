package org.coder;

import java.util.List;

public class DoctorScheduleDto {
    private int doctorId;
    private String name;
    private List<WorkHourDto> schedule;

    public DoctorScheduleDto(int doctorId, String name, List<WorkHourDto> schedule) {
        this.doctorId = doctorId;
        this.name = name;
        this.schedule = schedule;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public List<WorkHourDto> getSchedule() {
        return schedule;
    }
}
