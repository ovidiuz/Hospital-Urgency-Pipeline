package stages;

import entities.Nurse;
import entities.Patient;
import skel.MedicalDB;
import skel.State;

public class TriageQueue {

    public static void run() {

        MedicalDB medicalDB = MedicalDB.getInstance();

        for (int i = 0; i < medicalDB.getIncomingPatients().size(); i++) {

            Patient patient = medicalDB.getIncomingPatients().get(i);

            if (medicalDB.getCurrentRound() == patient.getTime()) {
                medicalDB.getTriageList().add(patient);
                patient.setState(State.TRIAGEQUEUE);
            }

        }

        for (Nurse nurse: medicalDB.getNursesList()) {

            Patient patient = medicalDB.getTriageList().poll();

            if (patient == null) {
                break;
            }

            nurse.consult(patient);
            patient.addObserver(medicalDB.getNursesList().get(0));

        }



    }

}
