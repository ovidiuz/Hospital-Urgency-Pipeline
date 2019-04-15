package entities;

import skel.IllnessType;
import skel.InvestigationResult;
import skel.MedicalDB;
import skel.State;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Observable;

public abstract class Doctor implements Observer {

    protected int id;
    protected String type;
    protected double c1;
    protected double c2;
    protected boolean isSurgeon;
    protected static final int T = 22;
    protected int maxForTreatment;
    protected EnumSet<IllnessType> affections;
    public final EnumSet<IllnessType> getAffections() {
        return affections;
    }

    public Doctor(int id, boolean isSurgeon, int maxForTreatment) {
        this.id = id;
        this.isSurgeon = isSurgeon;
        this.maxForTreatment = maxForTreatment;
    }

    public final void removeFromLists(Map<IllnessType, List<Doctor>> lists) {

        lists.forEach(

                (key, value) -> {

                    if (value.contains(this)) {
                        value.remove(this);
                    }

                }
        );

    }

    public final void addInLists(Map<IllnessType, List<Doctor>> lists) {

        lists.forEach(

                (key, value) -> {

                    if (this.affections.contains(key)) {
                        value.add(this);
                    }

                }
        );

    }

    @Override
    public final void update(Observable o, Object arg) {

        if (!(o instanceof Patient)) {
            return;
        }

        Patient patient = (Patient) o;
        int password = (int) arg;

        if (password != 2) {
            return;
        }

        if (patient.getRemainingRounds() <= 0 || patient.getSeverity() <= 0) {
            goHome(patient);
            patient.deleteObservers();
        } else {
            remainInHospital(patient);
        }

    }

    public final void diagnose(Patient patient) {

        if (patient.getInvestigationResult() == InvestigationResult.OPERATE && this.isSurgeon) {
            operate(patient);
            return;
        }

        if (patient.getInvestigationResult() == InvestigationResult.HOSPITALIZE) {
            hospitalize(patient);
            return;
        }

        if (patient.getInvestigationResult() == InvestigationResult.TREATMENT) {
            sendHome(patient);
            return;
        }

        if (patient.getSeverity() <= this.maxForTreatment) {
            this.sendHome(patient);
        } else {

            MedicalDB medicalDB = MedicalDB.getInstance();
            medicalDB.getInvestigationsList().add(patient);
            patient.setState(State.INVESTIGATIONSQUEUE);

        }

    }

    public final void operate(Patient patient) {

        int severity = patient.getSeverity();
        severity = severity - (int) Math.round(severity * this.c2);
        int rounds = (int) Math.round(severity * this.c1);
        patient.setSeverity(severity);
        patient.setRemainingRounds(rounds);

        setOperateStatus(patient);
        patient.addObserver(this);

    }

    public final void hospitalize(Patient patient) {

        int severity = patient.getSeverity();
        int rounds = (int) Math.round(severity * this.c1);

        if (rounds < 3) {
            rounds = 3;
        }

        patient.setRemainingRounds(rounds);
        setHospitalizeStatus(patient);
        patient.addObserver(this);
    }

    public final boolean isSurgeon() {
        return isSurgeon;
    }

    public final void sendHomeAfterTreatment(Patient patient) {
        patient.setState(State.HOME_DONE_TREATMENT);
    }

    public abstract void sendHome(Patient patient);

    public abstract void goHome(Patient patient);

    public abstract void setOperateStatus(Patient patient);

    public abstract void setHospitalizeStatus(Patient patient);

    public abstract void remainInHospital(Patient patient);
}
