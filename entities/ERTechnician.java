package entities;

import skel.InvestigationResult;
import skel.MedicalDB;
import skel.State;

public final class ERTechnician {

    private static final int C1 = 75;
    private static final int C2 = 40;

    public void examine(Patient patient) {

        if (patient.getSeverity() > C1) {
            patient.setInvestigationResult(InvestigationResult.OPERATE);
        }

        if (patient.getSeverity() > C2 && patient.getSeverity() <= C1) {
            patient.setInvestigationResult(InvestigationResult.HOSPITALIZE);
        }

        if (patient.getSeverity() <= C2) {
            patient.setInvestigationResult(InvestigationResult.TREATMENT);
        }

        MedicalDB medicalDB = MedicalDB.getInstance();
        medicalDB.getExaminationList().add(patient);
        patient.setState(State.EXAMINATIONSQUEUE);

    }

}
