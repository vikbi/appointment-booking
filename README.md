## Appointment Booking
***
#### Develop an optimised approach to get the availability of the doctors from their working schedule. Logic can accommodate N number of doctors and existing bookings in the system.
***
* ###  Pseudocode
  * Assumption : parameterised function will be called with list of doctors working schedule, available appointments and number of days. It will return the availability list for given number of days. 
  * create day wise list of doctors from their working schedule availability
  * create day wise list of available appointments
  * Loop for each day from current time and check for availability from above two lists.
  * Conditionally produce a list of availability for each doctor <i> (a separate function can be called recursively) </i> and return this list as expected result.
***

* ####  Execution Overview
  * Java main method is called from <i> BookingApplication Class </i>
    * To create dummy data for doctors and appointments, two function called <i>generateDoctorsData() & generateAppointmentsData() </i>.
      ```
      List<DoctorScheduleDto> doctorSchedules = generateDoctorsData();
      List<AppointmentsDto> appointments = generateAppointmentsData(); 
      ```
      * created Dr. Alex works for five days a week and Dr. Peter works on Monday only with standard working schedule from 9am to 6pm.
        * Booked 6 appointments for two users for following date time for both doctors
          ```
          Doctor Alex
            2023-11-24 11:00
            2023-11-24 14:00
          Doctor Peter 
            2023-11-27 10:00
            2023-11-27 13:00
          Doctor Alex
            2023-11-27 13:00
            2023-12-01 11:00

          ```
          * Function will be called with dummy data and expected number of days availability of each doctor we are looking for, expecting for 10 days.
            ```
              List<AvailabilityDto> availabilities = getAvailability(appointments, 
                                                                      doctorSchedules, 
                                                                        10);
            ```
            * Printing the results as follows, produce the results by considering current date as <i>2023-11-24 </i>
              ```
                Dr. Alex available on 2023-11-24 09:00
                Dr. Alex available on 2023-11-24 10:00
                Dr. Alex available on 2023-11-24 12:00
                Dr. Alex available on 2023-11-24 13:00
                Dr. Alex available on 2023-11-24 15:00
                Dr. Alex available on 2023-11-24 16:00
                Dr. Alex available on 2023-11-24 17:00
                Dr. Alex available on 2023-11-27 09:00
                Dr. Alex available on 2023-11-27 10:00
                Dr. Alex available on 2023-11-27 11:00
                Dr. Alex available on 2023-11-27 12:00
                Dr. Alex available on 2023-11-27 14:00
                Dr. Alex available on 2023-11-27 15:00
                Dr. Alex available on 2023-11-27 16:00
                Dr. Alex available on 2023-11-27 17:00
                Dr. Peter available on 2023-11-27 09:00
                Dr. Peter available on 2023-11-27 11:00
                Dr. Peter available on 2023-11-27 12:00
                Dr. Peter available on 2023-11-27 14:00
                Dr. Peter available on 2023-11-27 15:00
                Dr. Peter available on 2023-11-27 16:00
                Dr. Peter available on 2023-11-27 17:00
                Dr. Alex available on 2023-11-28 09:00
                Dr. Alex available on 2023-11-28 10:00
                Dr. Alex available on 2023-11-28 11:00
                Dr. Alex available on 2023-11-28 12:00
                Dr. Alex available on 2023-11-28 13:00
                Dr. Alex available on 2023-11-28 14:00
                Dr. Alex available on 2023-11-28 15:00
                Dr. Alex available on 2023-11-28 16:00
                Dr. Alex available on 2023-11-28 17:00
                Dr. Alex available on 2023-11-29 09:00
                Dr. Alex available on 2023-11-29 10:00
                Dr. Alex available on 2023-11-29 11:00
                Dr. Alex available on 2023-11-29 12:00
                Dr. Alex available on 2023-11-29 13:00
                Dr. Alex available on 2023-11-29 14:00
                Dr. Alex available on 2023-11-29 15:00
                Dr. Alex available on 2023-11-29 16:00
                Dr. Alex available on 2023-11-29 17:00
                Dr. Alex available on 2023-11-30 09:00
                Dr. Alex available on 2023-11-30 10:00
                Dr. Alex available on 2023-11-30 11:00
                Dr. Alex available on 2023-11-30 12:00
                Dr. Alex available on 2023-11-30 13:00
                Dr. Alex available on 2023-11-30 14:00
                Dr. Alex available on 2023-11-30 15:00
                Dr. Alex available on 2023-11-30 16:00
                Dr. Alex available on 2023-11-30 17:00
                Dr. Alex available on 2023-12-01 09:00
                Dr. Alex available on 2023-12-01 10:00
                Dr. Alex available on 2023-12-01 12:00
                Dr. Alex available on 2023-12-01 13:00
                Dr. Alex available on 2023-12-01 14:00
                Dr. Alex available on 2023-12-01 15:00
                Dr. Alex available on 2023-12-01 16:00
                Dr. Alex available on 2023-12-01 17:00
```