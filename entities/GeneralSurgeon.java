package entities;

import skel.IllnessType;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class GeneralSurgeon extends Doctor {

    public GeneralSurgeon(int id, boolean isSurgeon, int maxForTreatment) {
        super(id, isSurgeon, maxForTreatment);
        c1 = 0.2;
        c2 = 0.2;
        this.isSurgeon = true;
        type = "general surgeon";
        affections = EnumSet.of(
                IllnessType.ABDOMINAL_PAIN,
                IllnessType.BURNS,
                IllnessType.CAR_ACCIDENT,
                IllnessType.CUTS,
                IllnessType.SPORT_INJURIES
        );
    }

    @Override
    public void sendHome(Patient patient) {
        patient.setState(State.HOME_SURGEON);
    }

    @Override
    public void goHome(Patient patient) {
        sendHomeAfterTreatment(patient);
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "General Surgeon sent " + patient.getName() + " home\n");
        //medicalDB.setDoctorLog(log);
    }

    @Override
    public void setOperateStatus(Patient patient) {
        patient.setState(State.OPERATED_SURGEON);
    }

    @Override
    public void setHospitalizeStatus(Patient patient) {
        patient.setState(State.HOSPITALIZED_SURGEON);
    }

    @Override
    public void remainInHospital(Patient patient) {
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "General Surgeon says that " + patient.getName()
                + " should remain in hospital\n");
        //medicalDB.setDoctorLog(log);
    }
}
