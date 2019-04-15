package entities;

import skel.MedicalDB;
import skel.State;
import skel.Urgency;
import skel.UrgencyEstimator;

import java.util.Observable;
import java.util.Observer;

public final class Nurse implements Observer {

    private int id;
    private boolean busyThisRound;
    private int nextNurse;

    public void setNextNurse(int nextNurse) {
        this.nextNurse = nextNurse;
    }

    public Nurse(int id) {
        this.id = id;
    }

    public void consult(Patient patient) {

        MedicalDB medicalDB = MedicalDB.getInstance();
        UrgencyEstimator urgencyEstimator = UrgencyEstimator.getInstance();
        Urgency patientUrgency = urgencyEstimator.estimateUrgency(
                patient.getIllnessName(),
                patient.getSeverity()
        );

        patient.setUrgency(patientUrgency);
        medicalDB.getExaminationList().add(patient);
        patient.setState(State.EXAMINATIONSQUEUE);

    }

    public void treat(Patient patient) {

        int severity = patient.getSeverity();
        patient.setSeverity(severity - Doctor.T);
        int rounds = patient.getRemainingRounds();
        patient.setRemainingRounds(--rounds);
        MedicalDB medicalDB = MedicalDB.getInstance();

    }

    public void setBusyThisRound(boolean busyThisRound) {
        this.busyThisRound = busyThisRound;
    }

    @Override
    public void update(Observable o, Object arg) {

        if (!(o instanceof Patient)) {
            return;
        }

        Patient patient = (Patient) o;
        int password = (int) arg;

        if (password != 1) {
            return;
        }

        if (this.busyThisRound) {
            MedicalDB medicalDB = MedicalDB.getInstance();
            medicalDB.getNursesList().get(nextNurse).update(o, arg);
        }

        if (patient.getState() == State.HOSPITALIZED_CARDIO
                || patient.getState() == State.HOSPITALIZED_ERPHYSICIAN
                || patient.getState() == State.HOSPITALIZED_GASTRO
                || patient.getState() == State.HOSPITALIZED_INTERNIST
                || patient.getState() == State.HOSPITALIZED_NEURO
                || patient.getState() == State.HOSPITALIZED_SURGEON
                || patient.getState() == State.OPERATED_CARDIO
                || patient.getState() == State.OPERATED_ERPHYSICIAN
                || patient.getState() == State.OPERATED_NEURO
                || patient.getState() == State.OPERATED_SURGEON
        ) {

            if (patient.isTreatedThisRound()) {
                return;
            }

            if (patient.getSeverity() <= 0 || patient.getRemainingRounds() <= 0) {
                return;
            }

            int severity = patient.getSeverity();
            patient.setSeverity(severity - Doctor.T);
            int rounds = patient.getRemainingRounds();
            patient.setRemainingRounds(--rounds);

            MedicalDB medicalDB = MedicalDB.getInstance();
            String log = medicalDB.getNurseLog();
            log += "Nurse " + id + " treated " + patient.getName()
                    + " and patient has " + rounds;

            if (rounds == 1) {
                log += " more round\n";
            } else {
                log += " more rounds\n";
            }

            medicalDB.setNurseLog(log);
            patient.setTreatedThisRound(true);
            this.busyThisRound = true;

            if (this.id + 1 == medicalDB.getNurses()) {

                for (Nurse nurse:medicalDB.getNursesList()) {
                    nurse.setBusyThisRound(false);
                }

            }

        }


    }
}

