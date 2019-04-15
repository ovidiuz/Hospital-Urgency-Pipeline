package stages;

import entities.ERTechnician;
import entities.Patient;
import skel.MedicalDB;

import java.util.Queue;

public class InvestigationQueue {

    public static void run() {


        MedicalDB medicalDB = MedicalDB.getInstance();
        Queue<Patient> investigationsList = medicalDB.getInvestigationsList();

        for (ERTechnician investigator: medicalDB.getInvestigatorsList()) {

            if (investigationsList.isEmpty()) {
                break;
            }

            Patient patient = investigationsList.poll();
            investigator.examine(patient);

        }

    }
}
