package org.coder;

public class DoctorDto {

    private int id;
    private String name;
    private String description;

    private WorkHourDto schedule;

    protected DoctorDto(int id, String name, String description, WorkHourDto schedule) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.schedule = schedule;
    }

    public DoctorDto(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkHourDto getSchedule() {
        return schedule;
    }
}
