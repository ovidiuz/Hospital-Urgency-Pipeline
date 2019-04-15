package skel;

import entities.Doctor;
import entities.ERTechnician;
import entities.Nurse;
import entities.Patient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Comparator;


public final class MedicalDB {

    private int simulationLength;
    private int currentRound;
    private int nurses;
    private int investigators;
    private List<Doctor> doctors;

    public String getNurseLog() {
        return nurseLog;
    }

    public void setNurseLog(String nurseLog) {
        this.nurseLog = nurseLog;
    }

    private List<Patient> incomingPatients;
    private List<Nurse> nursesList;
    private Queue<Patient> triageList;
    private Queue<Patient> examinationList;
    private Queue<Patient> investigationsList;
    private List<ERTechnician> investigatorsList;
    private Map<IllnessType, List<Doctor>> diseaseDoctorList;
    private String nurseLog;
    private Map<Integer, List<String>> doctorLog;

    public Map<IllnessType, List<Doctor>> getDiseaseDoctorList() {
        return diseaseDoctorList;
    }

    private static MedicalDB instance = null;

    public void createDiseaseDoctorList() {

        this.diseaseDoctorList = new HashMap<>();

        for (Doctor doctor: this.getDoctors()) {

            for (IllnessType illness: doctor.getAffections()) {

                List<Doctor> doctorsList = diseaseDoctorList.get(illness);
                if (doctorsList == null) {
                    doctorsList = new ArrayList<>();
                    diseaseDoctorList.put(illness, doctorsList);
                }
                doctorsList.add(doctor);

            }

        }

    }

    public void nextRound() {
        currentRound++;
    }

    public List<ERTechnician> getInvestigatorsList() {
        return investigatorsList;
    }

    public void setInvestigatorsList(List<ERTechnician> investigatorsList) {
        this.investigatorsList = investigatorsList;
    }

    private MedicalDB() {
        doctors = new ArrayList<>();
        incomingPatients = new LinkedList<>();
        nursesList = new ArrayList<>();
        examinationList = new PriorityQueue<Patient>(Patient.patientComparator);
        investigationsList = new PriorityQueue<Patient>(Patient.patientComparator);
        investigatorsList = new LinkedList<>();
        triageList = new PriorityQueue<>(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                if (o1.getSeverity() > o2.getSeverity()) {
                    return -1;
                }

                if (o1.getSeverity() < o2.getSeverity()) {
                    return 1;
                }

                return o1.getName().compareTo(o2.getName());
            }
        });
        currentRound = 0;
        nurseLog = "";
        doctorLog = new TreeMap<>();
    }

    public void addToDoctorLog(int id, String message) {

        List<String> msgList = doctorLog.get(id);

        if (msgList == null) {
            msgList = new ArrayList<>();
        }

        msgList.add(message);
        doctorLog.put(id, msgList);

    }

    public void printDoctorLog() {

        doctorLog.forEach(
                (key, value) -> {

                    for (String message: value) {
                        System.out.print(message);
                    }
                }
        );

    }

    public Map<Integer, List<String>> getDoctorLog() {
        return doctorLog;
    }

    public void setDoctorLog(Map<Integer, List<String>> doctorLog) {
        this.doctorLog = doctorLog;
    }

    public static MedicalDB getInstance() {

        if (instance == null) {
            instance = new MedicalDB();
        }
        return instance;
    }

    public int getSimulationLength() {
        return simulationLength;
    }

    public void setSimulationLength(int simulationLength) {
        this.simulationLength = simulationLength;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getNurses() {
        return nurses;
    }

    public void setNurses(int nurses) {
        this.nurses = nurses;
    }

    public int getInvestigators() {
        return investigators;
    }

    public void setInvestigators(int investigators) {
        this.investigators = investigators;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Patient> getIncomingPatients() {
        return incomingPatients;
    }

    public void setIncomingPatients(List<Patient> incomingPatients) {
        this.incomingPatients = incomingPatients;
    }

    public List<Nurse> getNursesList() {
        return nursesList;
    }

    public Queue<Patient> getTriageList() {
        return triageList;
    }

    public Queue<Patient> getExaminationList() {
        return examinationList;
    }

    public Queue<Patient> getInvestigationsList() {
        return investigationsList;
    }

}
