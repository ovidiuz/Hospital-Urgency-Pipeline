package entities;

import skel.IllnessType;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class Gastroenterologist extends Doctor {

    public Gastroenterologist(int id, boolean isSurgeon, int maxForTreatment) {
        super(id, isSurgeon, maxForTreatment);
        c1 = 0.5;
        //this.isSurgeon = false;
        type = "gastroenterologist";
        affections = EnumSet.of(
                IllnessType.ABDOMINAL_PAIN,
                IllnessType.ALLERGIC_REACTION,
                IllnessType.FOOD_POISONING
        );
    }

    @Override
    public void sendHome(Patient patient) {
        patient.setState(State.HOME_GASTRO);
    }

    @Override
    public void goHome(Patient patient) {
        sendHomeAfterTreatment(patient);
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Gastroenterologist sent " + patient.getName() + " home\n");
        //medicalDB.setDoctorLog(log);
    }

    @Override
    public void setOperateStatus(Patient patient) { }

    @Override
    public void setHospitalizeStatus(Patient patient) {
        patient.setState(State.HOSPITALIZED_GASTRO);
    }

    @Override
    public void remainInHospital(Patient patient) {
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Gastroenterologist says that " + patient.getName()
                + " should remain in hospital\n");
        //medicalDB.setDoctorLog(log);
    }
}
