package entities;

import skel.IllnessType;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class Neurologist extends Doctor {
    public Neurologist(int id, boolean isSurgeon, int maxForTreatment) {
        super(id, isSurgeon, maxForTreatment);
        c1 = 0.5;
        c2 = 0.1;
        type = "neurologist";
        affections = EnumSet.of(
                IllnessType.STROKE
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
        medicalDB.addToDoctorLog(this.id, "Neurologist sent " + patient.getName() + " home\n");
        //medicalDB.setDoctorLog(log);
    }

    @Override
    public void setOperateStatus(Patient patient) {
        patient.setState(State.OPERATED_NEURO);
    }

    @Override
    public void setHospitalizeStatus(Patient patient) {
        patient.setState(State.HOSPITALIZED_NEURO);
    }

    @Override
    public void remainInHospital(Patient patient) {
        MedicalDB medicalDB = MedicalDB.getInstance();
        Map<Integer, List<String>> log = medicalDB.getDoctorLog();
        medicalDB.addToDoctorLog(id, "Neurologist says that " + patient.getName()
                + " should remain in hospital\n");
        //medicalDB.setDoctorLog(log);
    }
}
