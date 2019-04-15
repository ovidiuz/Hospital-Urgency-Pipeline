package entities;

import skel.IllnessType;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class Cardiologist extends Doctor {

    public Cardiologist(int id, boolean isSurgeon, int maxForTreatment) {
        super(id, isSurgeon, maxForTreatment);
        c1 = 0.4;
        c2 = 0.1;
        type = "cardiologist";
        affections = EnumSet.of(
                IllnessType.HEART_ATTACK,
                IllnessType.HEART_DISEASE
        );
    }

    @Override
    public void sendHome(Patient patient) {
        patient.setState(State.HOME_CARDIO);
    }

    @Override
    public void goHome(Patient patient) {
        sendHomeAfterTreatment(patient);
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Cardiologist sent " + patient.getName() + " home\n");
        //medicalDB.setDoctorLog(log);
    }

    @Override
    public void setOperateStatus(Patient patient) {
        patient.setState(State.OPERATED_CARDIO);
    }

    @Override
    public void setHospitalizeStatus(Patient patient) {
        patient.setState(State.HOSPITALIZED_CARDIO);
    }

    @Override
    public void remainInHospital(Patient patient) {
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Cardiologist says that " + patient.getName()
                + " should remain in hospital\n");
        //medicalDB.setDoctorLog(log);
    }
}

