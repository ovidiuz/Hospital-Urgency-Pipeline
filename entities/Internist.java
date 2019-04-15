package entities;

import skel.IllnessType;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class Internist extends Doctor {

    public Internist(int id, boolean isSurgeon, int maxForTreatment) {
        super(id, isSurgeon, maxForTreatment);
        c1 = 0.01;
        //this.isSurgeon = false;
        type = "internist";
        affections = EnumSet.of(
                IllnessType.ABDOMINAL_PAIN,
                IllnessType.ALLERGIC_REACTION,
                IllnessType.FOOD_POISONING,
                IllnessType.HEART_DISEASE,
                IllnessType.HIGH_FEVER,
                IllnessType.PNEUMONIA
        );
    }

    @Override
    public void sendHome(Patient patient) {
        patient.setState(State.HOME_INTERNIST);
    }

    @Override
    public void goHome(Patient patient) {
        sendHomeAfterTreatment(patient);
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Internist sent " + patient.getName() + " home\n");
    }

    @Override
    public void setOperateStatus(Patient patient) { }

    @Override
    public void setHospitalizeStatus(Patient patient) {
        patient.setState(State.HOSPITALIZED_INTERNIST);
    }

    @Override
    public void remainInHospital(Patient patient) {
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Internist says that " + patient.getName()
                + " should remain in hospital\n");
    }
}
