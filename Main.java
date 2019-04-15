import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Doctor;
import factories.DoctorFactory;
import skel.MedicalDB;
import entities.Patient;
import factories.PatientFactory;
import entities.Nurse;
import entities.ERTechnician;
import stages.TriageQueue;
import stages.ExaminationQueue;
import stages.InvestigationQueue;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = mapper.readTree(new FileReader(args[0]));
        } catch (IOException e) {
            System.out.println("HAHAHA");
        }

        MedicalDB medicalDB = MedicalDB.getInstance();

        medicalDB.setInvestigators(jsonNode.get("investigators").asInt());
        medicalDB.setNurses(jsonNode.get("nurses").asInt());
        medicalDB.setSimulationLength(jsonNode.get("simulationLength").asInt());

        DoctorFactory doctorFactory = DoctorFactory.getInstance();

        for (int i = 0; i < jsonNode.get("doctors").size(); i++) {

            Doctor newDoctor =
                    doctorFactory.createDoctor(jsonNode.get("doctors").get(i));
            medicalDB.getDoctors().add(newDoctor);

        }

        PatientFactory patientFactory = PatientFactory.getInstance();

        for (int i = 0; i < jsonNode.get("incomingPatients").size(); i++) {

            Patient newPatient =
                    patientFactory.createPatient(jsonNode.get("incomingPatients").get(i));
            medicalDB.getIncomingPatients().add(newPatient);

        }

        for (int i = 0; i < medicalDB.getNurses(); i++) {
            Nurse newNurse = new Nurse(i);
            if (i < medicalDB.getNurses() - 1) {
                newNurse.setNextNurse(i + 1);
            } else {
                newNurse.setNextNurse(0);
            }
            medicalDB.getNursesList().add(newNurse);
        }

        for (int i = 0; i < medicalDB.getInvestigators(); i++) {
            medicalDB.getInvestigatorsList().add(new ERTechnician());
        }

        medicalDB.getIncomingPatients().sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        medicalDB.createDiseaseDoctorList();

        while (medicalDB.getCurrentRound() < medicalDB.getSimulationLength()) {

            TriageQueue.run();
            ExaminationQueue.run();
            InvestigationQueue.run();

            System.out.println("~~~~ Patients in round "
                    + (medicalDB.getCurrentRound() + 1) + " ~~~~");

            for (Patient patient: medicalDB.getIncomingPatients()) {
                if (patient.getState() != null) {
                    System.out.print(patient.getName()
                            + " is " + patient.getState().getValue() + "\n");
                }
            }

            System.out.println();

            for (Patient patient: medicalDB.getIncomingPatients()) {
                patient.update();
                patient.setTreatedThisRound(false);
            }

            for (Nurse nurse: medicalDB.getNursesList()) {
                nurse.setBusyThisRound(false);
            }

            System.out.println("~~~~ Nurses treat patients ~~~~");
            System.out.println(medicalDB.getNurseLog());

            System.out.println("~~~~ Doctors check their "
                    + "hospitalized patients and give verdicts ~~~~");
            medicalDB.printDoctorLog();
            System.out.println();

            medicalDB.nextRound();
            medicalDB.setNurseLog("");
            medicalDB.setDoctorLog(new TreeMap<>());

        }

    }



}
