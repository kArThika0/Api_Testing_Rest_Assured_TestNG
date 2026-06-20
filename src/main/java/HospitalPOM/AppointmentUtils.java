package HospitalPOM;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class AppointmentUtils {

    // Simulated booked slots (doctorId|date|time)
    private static final Set<String> bookedSlots = new HashSet<>();

    // Simulated insurance coverage map
    private static final Map<String, List<String>> insuranceCoverage = new HashMap<>();

    static {
        // preset a booked slot (not the sample 10:30 so sample booking can succeed)
        bookedSlots.add("DOC-1042|2026-07-10|10:00");

        // insurance coverage mapping
        insuranceCoverage.put("ICICI-HLT-90213", Arrays.asList("cardiology", "orthopedics"));
        insuranceCoverage.put("MEDICARE-001", Arrays.asList("dermatology"));
    }

    // Check if the doctor works on the given date/slot. For demo: doctor is unavailable on Sundays.
    public static boolean checkDoctorAvailability(String doctorId, String date, String slot) {
        LocalDate d = LocalDate.parse(date);
        if (d.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }
        // Further business rules can be added; assume available otherwise
        return true;
    }

    // Validate that the patient record exists in the system
    public static boolean validatePatientRecord(String patientId) {
        if (patientId == null || !patientId.startsWith("PAT-")) return false;
        // Simulate a missing patient
        if ("PAT-404".equals(patientId) || "PAT-NOTFOUND".equals(patientId)) return false;
        return true;
    }

    // Check if the time slot is already taken for the doctor on that date
    public static boolean checkSlotConflict(String doctorId, String date, String timeSlot) {
        String key = String.join("|", doctorId, date, timeSlot);
        return bookedSlots.contains(key);
    }

    // Verify whether the insurance covers the specialty
    public static boolean verifyInsurance(String insuranceId, String specialty) {
        if (insuranceId == null || specialty == null) return false;
        List<String> covered = insuranceCoverage.get(insuranceId);
        return covered != null && covered.contains(specialty.toLowerCase()) || (covered != null && covered.contains(specialty));
    }

    // Generate a simulated confirmation object (map) for successful booking
    public static Map<String, Object> generateAppointmentConfirmation(String doctorId, String patientId, String date, String timeSlot) {
        Map<String, Object> map = new HashMap<>();
        // appointment id like APT-YYYYMMDD-XXX
        String appointmentId = "APT-" + date.replaceAll("-", "") + "-" + new Random().nextInt(900) + 100;
        map.put("appointmentId", appointmentId);
        map.put("status", "BOOKED");
        map.put("doctorName", "Dr. Ramesh Iyer");
        map.put("hospital", "Apollo Chennai");
        map.put("scheduledAt", date + "T" + timeSlot + ":00+05:30");
        // token number for demo: use random between 1 and 10
        map.put("tokenNumber", 3);
        map.put("instructions", "Arrive 15 mins early. Bring previous reports.");
        map.put("errors", Collections.emptyList());

        // add to booked slots to prevent future clashes
        bookedSlots.add(String.join("|", doctorId, date, timeSlot));

        return map;
    }
}

