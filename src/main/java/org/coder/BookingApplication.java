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
                .map(a -> "Dr. "+a.getDoctor().getName()+ " available on "+ a.getDate().toLocalDate().toString() + " "+ a.getDate().toLocalTime())
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

        //day wise list of doctors working in a week, can be cashed for each call
        Map<String, List<DoctorDto>> dayDoctorsMap = getDoctorsMapByDay(doctorSchedules);

        // Loop for given number of days starting from 24-11-2023
        for(int num = numberOfDays; num > 0; num--) {
            // get the doctors working on current day
            List<DoctorDto> doctorsList = dayDoctorsMap.get(currentDate.getDayOfWeek().toString());
            //get the list of appointments for current day, same as doctors day wise mapping can be created for appointments
            List<AppointmentsDto> currentDayAppointments = getAppointmentByDay(appointments, MonthDay.of(currentDate.getMonth(), currentDate.getDayOfMonth()));

            try{
                for(DoctorDto doctor : doctorsList) {
                    availabilityList.addAll(getAvailabilityOfDoctorByDay(doctor, currentDate, currentDayAppointments));
                }
            }catch (NullPointerException e) {
                System.out.println("no doctors found for day: "+currentDate.getDayOfWeek());
            }
            // increment date to next day to get the further doctors availability
            currentDate = currentDate.plusDays(1);
        }

        return availabilityList;
    }

    // get availability list for given doctor for given date
    private static List<AvailabilityDto> getAvailabilityOfDoctorByDay(DoctorDto doctor,
                                                                      LocalDateTime currentDate,
                                                                      List<AppointmentsDto> currentDayAppointments){

            List<AvailabilityDto> availabilities = new ArrayList<>();

            LocalTime startTime = doctor.getSchedule().getStartTime();
            LocalTime endTime = doctor.getSchedule().getEndTime();
            LocalTime currentTime = startTime;

            // loop to create availability between start and end time
            while ((currentTime.isAfter(startTime) && currentTime.isBefore(endTime))
                    || (currentTime == endTime)
                    || (currentTime == startTime)) {

                // check if appointment exists for given time for given doctor
                if (!checkAppointmentForDoctor(currentDayAppointments,
                        currentTime,
                        currentDate,
                        doctor.getId())) {

                    // create availability for doctor for current date time
                    availabilities.add(new AvailabilityDto(
                                            doctor,
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
        List<DoctorScheduleDto> doctorSchedules = new ArrayList<>();

        DoctorDto doctor1 = new DoctorDto(1, "Alex", "Heart Specialist");
        DoctorDto doctor2 = new DoctorDto(2, "Peter", "Eye Specialist");

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

        doctorSchedules.add( new DoctorScheduleDto(doctor1, workingList1));

        // add second doctor working schedule
        List<WorkHourDto> workingList2 = new ArrayList<>();
        workingList2.add(new WorkHourDto(DayOfWeek.MONDAY,
                LocalTime.of(9,00),
                LocalTime.of(17,00)));
        doctorSchedules.add(new DoctorScheduleDto(doctor2, workingList2));

        //... can create N number of doctors and their schedule
        return doctorSchedules;
    }

    // get the available doctors for day of week
    private static Map<String, List<DoctorDto>> getDoctorsMapByDay(List<DoctorScheduleDto> doctorSchedules) {

        Map<String, List<DoctorDto>> doctorsList = new HashMap<String, List<DoctorDto>>();
        DayOfWeek day = DayOfWeek.MONDAY;

        for(int dayNum=6; dayNum>0; dayNum--){
            List<DoctorDto> dayList = new ArrayList<>();
            DayOfWeek currentDay = day;
            doctorSchedules
                    .stream()
                    .forEach(docSchedule -> {
                                // check if current day schedule for doctor exists
                                Optional<WorkHourDto> schedule = docSchedule
                                                                    .getSchedule()
                                                                    .stream()
                                                                    .filter(w-> w.getDay() == currentDay)
                                                                    .findFirst();
                                if(!schedule.isEmpty()) {
                                    DoctorDto doctor = new DoctorDto(
                                                            docSchedule.getDoctor().getId(),
                                                            docSchedule.getDoctor().getName(),
                                                            docSchedule.getDoctor().getDescription(),
                                                            schedule.get());
                                        dayList.add(doctor);
                                        doctorsList.put(currentDay.toString(), dayList);
                                }
                    });
            day = day.plus(1);
        }

        return doctorsList;
    }

    /* get the appointments for given datetime

        if number of appointments are huge,
        then creating a local map is better to filter out appointments for each day,
        will reduce iteration time like day wise list of doctors
    */
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
