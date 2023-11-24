package org.coder;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookingApplication {
    public static void main(String args[]){

        //to generate sample data about doctors and appointments
        List<DoctorScheduleDto> doctorSchedules = generateDoctorsData();
        List<AppointmentsDto> appointments = generateAppointmentsData();

        //main function to provide the list of doctors availability for given number of days
        List<AvailabilityDto> availabilities = getAvailability(appointments, doctorSchedules, 10);

        //To show the output of the function
        availabilities
                .stream()
                .map(a -> "Dr. "+a.getDoctorName()+ " available on "+ a.getDate().toLocalDate().toString() + " "+ a.getDate().toLocalTime())
                .forEach(System.out::println);
    }

    /*
     Parameterised function to provide doctors availability for given number of days

     This is one of the approach to find the solution
     - create day-wise list of doctors working availability and booked appointments,
     - then for particular day, check if appointment for doctor is exist or not
     - conditionally adding record for availability of each doctor

     Note: can handle N number of doctors and N number of appointments
     */
    private static List<AvailabilityDto> getAvailability(List<AppointmentsDto> appointments,
                                                         List<DoctorScheduleDto> doctorSchedules,
                                                         int numberOfDays) {

        List<AvailabilityDto> availabilityList = new ArrayList<>();

        // current date can be provided as parameter or can be taken from current time
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.now());

        // Loop for given number of days starting from 24-11-2023
        for(int num = numberOfDays; num > 0; num--) {
            //get the list of doctors working on current day
            List<DoctorScheduleDto> currentDayDoctors = getDoctorsScheduleByDay(doctorSchedules, currentDate.getDayOfWeek());

            //get the list of appointments for current day
            List<AppointmentsDto> currentDayAppointments = getAppointmentByDay(appointments, MonthDay.of(currentDate.getMonth(), currentDate.getDayOfMonth()));

            //temporarily storing current date into another variable, could be managed from separate function
            LocalDateTime finalCurrentDate = currentDate;

            currentDayDoctors
                    .stream()
                    .map(doctor-> getAvailabilityOfDoctorByDay(doctor, finalCurrentDate, currentDayAppointments))
                    .forEach(list -> list.stream().forEach(a-> availabilityList.add(a))) ;

            // increment date to next day to get the further doctors availability
            currentDate = currentDate.plusDays(1);
        }

        return availabilityList;
    }

    // get availability list for given doctor for given date
    private static List<AvailabilityDto> getAvailabilityOfDoctorByDay(DoctorScheduleDto doctorSchedule,
                                                                      LocalDateTime currentDate,
                                                                      List<AppointmentsDto> currentDayAppointments){

            List<AvailabilityDto> availabilities = new ArrayList<>();

            // get the working schedule for current day for particular doctor
            WorkHourDto schedule = doctorSchedule
                    .getSchedule()
                    .stream()
                    .filter(WorkHourDto -> WorkHourDto.getDay() == currentDate.getDayOfWeek())
                    .findAny().get();

            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();
            LocalTime currentTime = startTime;

            // loop to create availability between start and end time
            while ((currentTime.isAfter(startTime) && currentTime.isBefore(endTime))
                    || (currentTime == endTime)
                    || (currentTime == startTime)) {

                // check if appointment exists for given time for given doctor
                if (!checkAppointmentForDoctor(currentDayAppointments,
                        currentTime,
                        currentDate,
                        doctorSchedule.getDoctorId())) {

                    // create availability for doctor for current date time
                    availabilities.add(new AvailabilityDto(
                                            doctorSchedule.getDoctorId(),
                                            doctorSchedule.getName(),
                                            LocalDateTime.of(currentDate.toLocalDate(), currentTime)
                                    ));
                }
                // increment time to next hour to check for further availability for current doctor
                currentTime = currentTime.plusHours(1);
            }

            return availabilities;
    }

    // To generate appointments dummy data for available doctor
    private static List<AppointmentsDto> generateAppointmentsData() {
        List<AppointmentsDto> appointments = new ArrayList<>();
        AppointmentsDto appointment1 = new AppointmentsDto(1,1,
                LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(11,00))
        );

        AppointmentsDto appointment2 = new AppointmentsDto(1,2,
                LocalDateTime.of(LocalDate.of(2023, 11,24), LocalTime.of(14,00))
        );

        AppointmentsDto appointment3 = new AppointmentsDto(2,1,
                LocalDateTime.of(LocalDate.of(2023, 11,27), LocalTime.of(10,00))
        );

        AppointmentsDto appointment4 = new AppointmentsDto(2,2,
                LocalDateTime.of(LocalDate.of(2023, 11,27), LocalTime.of(13,00))
        );

        AppointmentsDto appointment5 = new AppointmentsDto(1,1,
                LocalDateTime.of(LocalDate.of(2023, 11,27), LocalTime.of(13,00))
        );

        AppointmentsDto appointment6 = new AppointmentsDto(1,2,
                LocalDateTime.of(LocalDate.of(2023, 12,1), LocalTime.of(11,00))
        );

        appointments.add(appointment1);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);
        appointments.add(appointment5);
        appointments.add(appointment6);

        // can add N number of appointments for doctor
        // Additionally can introduce validation for booking appointments according to doctors working available schedule
        //
        return appointments;
    }

    // To generate doctors details and their working schedule in a week
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

        DoctorScheduleDto doctor1 = new DoctorScheduleDto(1, "Alex", workingList1);
        doctorSchedules.add(doctor1);

        // add second doctor working schedule
        List<WorkHourDto> workingList2 = new ArrayList<>();
        workingList2.add(new WorkHourDto(DayOfWeek.MONDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        DoctorScheduleDto doctor2 = new DoctorScheduleDto(2, "Peter", workingList2);
        doctorSchedules.add(doctor2);

        //... can create N number of doctors and their schedule
        return doctorSchedules;
    }

    // get the available doctors for given day of week
    private static List<DoctorScheduleDto> getDoctorsScheduleByDay(List<DoctorScheduleDto> doctorSchedules, DayOfWeek day) {
            return doctorSchedules
                    .stream()
                    .filter(DoctorScheduleDto ->
                            DoctorScheduleDto.getSchedule().stream()
                            .filter(w -> w.getDay() == day).count() > 0
                    ).collect(Collectors.toList());
    }

    // get the appointments for given datetime
    // expecting day of month, edge case: expecting current month values
    private static List<AppointmentsDto> getAppointmentByDay(List<AppointmentsDto> appointments, MonthDay day){
        return appointments
                .stream()
                .filter(a -> a.getDate().getDayOfMonth() == day.getDayOfMonth())
                .collect(Collectors.toList());
    }

    // check if appointment exist for particular doctor for given date and time
    private static boolean checkAppointmentForDoctor(List<AppointmentsDto> currentAppointments,
                                             LocalTime currentTime,
                                             LocalDateTime currentDate,
                                             int doctorId) {

        return currentAppointments
                .stream()
                .filter(a-> a.getDoctorId() == doctorId)
                .filter(a-> a.getDate().toLocalTime() == currentTime)
                .filter(a-> a.getDate().getDayOfMonth() == currentDate.getDayOfMonth())
                .count() > 0;

    }
}
