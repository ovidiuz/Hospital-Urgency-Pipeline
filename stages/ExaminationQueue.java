package stages;

import entities.Doctor;
import entities.Patient;
import skel.InvestigationResult;
import skel.MedicalDB;
import skel.State;

import java.util.List;
import java.util.Queue;

public class ExaminationQueue {

    public static void run() {

        MedicalDB medicalDB = MedicalDB.getInstance();
        Queue<Patient> examinationList = medicalDB.getExaminationList();

        while (!examinationList.isEmpty()) {

            Patient patient = examinationList.poll();
            List<Doctor> doctorList
                    = medicalDB.getDiseaseDoctorList().get(patient.getIllnessName());

            if (doctorList == null || doctorList.isEmpty()) {
                patient.setState(State.OTHERHOSPITAL);
                continue;
            }

            Doctor doctor = null;

            if (patient.getInvestigationResult() == InvestigationResult.OPERATE) {

                for (Doctor doc : doctorList) {
                    if (doc.isSurgeon()) {
                        doctor = doc;
                        break;
                    }
                }
            } else {
                doctor = doctorList.get(0);
            }

            if (doctor == null) {
                patient.setState(State.OTHERHOSPITAL);
                continue;
            }



            doctor.removeFromLists(medicalDB.getDiseaseDoctorList());
            doctor.diagnose(patient);
            doctor.addInLists(medicalDB.getDiseaseDoctorList());

        }


    }

}
