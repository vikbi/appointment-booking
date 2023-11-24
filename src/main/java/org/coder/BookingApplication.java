package org.coder;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingApplication {

    public static void main(String args[]){

        //to generate sample data about doctors and appointments
        List<DoctorScheduleDto> doctorSchedules = generateDoctorsData();
        List<AppointmentsDto> appointments = generateAppointmentsData();

        //main function to provide the list of doctors availability for given number of days
        List<AvailabilityDto> availabilities = getAvailability(appointments, doctorSchedules, 10);

        for(AvailabilityDto availability : availabilities) {
            System.out.println("Doctor: "+availability.getDoctorId() +" "+ availability.getDate().toString() +" "+ availability.getTime().toString());
        }
    }

    // to generate appointments for available doctor
    private static List<AppointmentsDto> generateAppointmentsData() {
        List<AppointmentsDto> appointments = new ArrayList<>();
        AppointmentsDto appointment1 = new AppointmentsDto(1,1,
                LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(11,00))
        );

        AppointmentsDto appointment2 = new AppointmentsDto(1,2,
                LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(12,00))
        );

        AppointmentsDto appointment3 = new AppointmentsDto(2,1,
                LocalDateTime.of(LocalDate.of(2023, 11,27), LocalTime.of(10,00))
        );

        AppointmentsDto appointment4 = new AppointmentsDto(2,2,
                LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(13,00))
        );

        appointments.add(appointment1);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);

        return appointments;
    }

    // to generate doctors details and their working schedule in a week
    private static List<DoctorScheduleDto> generateDoctorsData() {
        List<DoctorScheduleDto> doctorSchedules = new ArrayList<DoctorScheduleDto>();

        List<WorkHourDto> workingList1 = new ArrayList<>();
        workingList1.add(new WorkHourDto(DayOfWeek.MONDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        workingList1.add(new WorkHourDto(DayOfWeek.TUESDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        workingList1.add(new WorkHourDto(DayOfWeek.WEDNESDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        workingList1.add(new WorkHourDto(DayOfWeek.THURSDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        workingList1.add(new WorkHourDto(DayOfWeek.FRIDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));

        DoctorScheduleDto doctor1 = new DoctorScheduleDto(1, "DoctorOne", workingList1);
        doctorSchedules.add(doctor1);

        // add second doctor schedule
        List<WorkHourDto> workingList2 = new ArrayList<>();
        workingList2.add(new WorkHourDto(DayOfWeek.MONDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        DoctorScheduleDto doctor2 = new DoctorScheduleDto(2, "DoctorTwo", workingList2);
        doctorSchedules.add(doctor2);

        //... can create n number of doctors and their schedule
        return doctorSchedules;
    }

    // get the available doctors for given day of week
    static List<DoctorScheduleDto> getDoctorsScheduleByDay(List<DoctorScheduleDto> doctorSchedules, DayOfWeek day) {
            return doctorSchedules
                    .stream()
                    .filter(DoctorScheduleDto ->
                            DoctorScheduleDto.getSchedule().stream()
                            .filter(WorkHourDto -> WorkHourDto.getDay() == day).count() > 0
                ).collect(Collectors.toList());
    }

    // get the appointments for given datetime
    // expecting day of month, edge case: expecting current month values
    static List<AppointmentsDto> getAppointmentByDay(List<AppointmentsDto> appointments,
                                                     MonthDay day){
        List<AppointmentsDto> appointmentsForDay = new ArrayList<>();
        for(AppointmentsDto appointment : appointments) {
            if(appointment.getDate().getDayOfMonth() == day.getDayOfMonth()) {
                appointmentsForDay.add(appointment);
            }
        }
        return appointmentsForDay;
    }

    // check if appointment exist for particular doctor for given date and time
    private static boolean checkAppointmentForDoctor(List<AppointmentsDto> currentAppointments,
                                             LocalTime currentTime,
                                             LocalDateTime currentDate,
                                             int doctorId) {
        return currentAppointments.stream()
                .filter(AppointmentsDto->
                                (AppointmentsDto.getDoctorId() == doctorId)
                                && (AppointmentsDto.getDate().toLocalTime() == currentTime)
                                && (AppointmentsDto.getDate().getDayOfMonth() == currentDate.getDayOfMonth())
                ).count() > 0;

    }

    // main function to provide doctors availability for given number of days
    // this is one of the approach to find the solution
    // day-wise creating list of doctors and appointments first, then checking if appointment for doctor is exist or not
    // conditionally adding record for availability of each doctor

    // Note: can handle n number of doctors and n number of appointments
    private static List<AvailabilityDto> getAvailability(List<AppointmentsDto> appointments, List<DoctorScheduleDto> doctorSchedules, int numberOfDays) {

        List<AvailabilityDto> availabilityList = new ArrayList<>();

        // current date can be provided as parameter or can be taken from current time
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(9,00));

       for(int i = numberOfDays; i > 0; i--) {
            //get the list of doctors for current day
            List<DoctorScheduleDto> currentDayDoctors = getDoctorsScheduleByDay(doctorSchedules, currentDate.getDayOfWeek());

            //get the list of appointments for current day
            List<AppointmentsDto> currentDayAppointments = getAppointmentByDay(appointments, MonthDay.of(currentDate.getMonth(), currentDate.getDayOfMonth()));

            //temporarily storing current date into another variable, could be managed from separate function
            LocalDateTime finalCurrentDate = currentDate;

            currentDayDoctors.stream().forEach(DoctorScheduleDto -> {
                // get the working schedule for current day for particular doctor
                WorkHourDto schedule = DoctorScheduleDto.getSchedule().stream()
                        .filter(WorkHourDto -> WorkHourDto.getDay() == finalCurrentDate.getDayOfWeek())
                        .findAny().get();

                LocalTime startTime = schedule.getStartTime();
                LocalTime endTime = schedule.getEndTime();
                LocalTime currentTime = startTime;

                // loop to create availability between start and end time of doctors working hours
                while ((currentTime.isAfter(startTime) && currentTime.isBefore(endTime))
                        || (currentTime == endTime)
                        || (currentTime == startTime)) {

                    // check if appointment exists for given time for given doctor
                    boolean exist = checkAppointmentForDoctor(currentDayAppointments, currentTime, finalCurrentDate, DoctorScheduleDto.getDoctorId());
                    if (!exist) {
                        // create availability for doctor for current date time
                        availabilityList.add(new AvailabilityDto(DoctorScheduleDto.getDoctorId(), currentTime, finalCurrentDate.toLocalDate()));
                    }

                    // increment time to check for next hour availability for current doctor
                    currentTime = currentTime.plusHours(1);
                }
            });

            // increment date by one to get the next date time value
            currentDate = currentDate.plusDays(1);
        }

        return availabilityList;
    }
}
