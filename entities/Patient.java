package entities;

import skel.IllnessType;
import skel.InvestigationResult;
import skel.State;
import skel.Urgency;

import java.util.Comparator;
import java.util.Observable;

public final class Patient extends Observable {

    public void update() {
        this.setChanged();
        this.notifyObservers(1);
        this.setChanged();
        this.notifyObservers(2);
    }

    private InvestigationResult investigationResult;
    private int id;
    private String name;
    private int remainingRounds;
    private Urgency urgency;

    public boolean isTreatedThisRound() {
        return treatedThisRound;
    }

    public void setTreatedThisRound(boolean treatedThisRound) {
        this.treatedThisRound = treatedThisRound;
    }

    private boolean treatedThisRound;

    public InvestigationResult getInvestigationResult() {
        return investigationResult;
    }

    public void setInvestigationResult(InvestigationResult investigationResult) {
        this.investigationResult = investigationResult;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public int getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(int remainingRounds) {
        this.remainingRounds = remainingRounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Patient(int id, String name, int age, int time, IllnessType illnessType, int severity) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.time = time;
        this.illnessName = illnessType;
        this.severity = severity;
        this.investigationResult = InvestigationResult.NOT_DIAGNOSED;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private int age;
    private int time;

    private State state;
    private IllnessType illnessName;
    private int severity;

    public IllnessType getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(IllnessType illnessName) {
        this.illnessName = illnessName;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public static Comparator<Patient> patientComparator =
            new Comparator<Patient>() {
                @Override
                public int compare(Patient o1, Patient o2) {

                    if (o1.getUrgency().ordinal() < o2.getUrgency().ordinal()) {
                        return -1;
                    }

                    if (o1.getUrgency().ordinal() > o2.getUrgency().ordinal()) {
                        return 1;
                    }

                    if (o1.getSeverity() > o2.getSeverity()) {
                        return -1;
                    }

                    if (o1.getSeverity() < o2.getSeverity()) {
                        return 1;
                    }

                    return o2.getName().compareTo(o1.getName());
                }
            };

}

