package HospitalPOM;

import java.util.Arrays;

public class AppointmentData {

    public AppointmentRequest createSampleRequest() {
        AppointmentRequest req = new AppointmentRequest();
        req.setDoctorId("DOC-1042");
        req.setPatientId("PAT-8821");
        req.setSpecialty("cardiology");
        req.setAppointmentDate("2026-07-10");
        req.setTimeSlot("10:30");
        req.setAppointmentType("consultation");
        req.setSymptoms(Arrays.asList("chest pain", "fatigue"));
        req.setInsuranceId("ICICI-HLT-90213");
        return req;
    }
}

