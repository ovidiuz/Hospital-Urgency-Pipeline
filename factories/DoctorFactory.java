package factories;

import com.fasterxml.jackson.databind.JsonNode;
import entities.Cardiologist;
import entities.Doctor;
import entities.ERPhysician;
import entities.Gastroenterologist;
import entities.GeneralSurgeon;
import entities.Internist;
import entities.Neurologist;

public final class DoctorFactory {

    private static DoctorFactory instance = null;
    private static int currentId;

    private DoctorFactory() {
        currentId = 0;
    }

    public static DoctorFactory getInstance() {

        if (instance == null) {
            instance = new DoctorFactory();
        }

        return instance;

    }

    public Doctor createDoctor(JsonNode jsonNode) {

        String type = jsonNode.get("type").asText();
        boolean isSurgeon = jsonNode.get("isSurgeon").asBoolean();
        int maxForTreatment = jsonNode.get("maxForTreatment").asInt();

        Doctor doctor;

        switch (type) {

            case "CARDIOLOGIST":
                doctor = new Cardiologist(currentId, isSurgeon, maxForTreatment);
                break;

            case "ER_PHYSICIAN":
                doctor = new ERPhysician(currentId, isSurgeon, maxForTreatment);
                break;

            case "GASTROENTEROLOGIST":
                doctor = new Gastroenterologist(currentId, isSurgeon, maxForTreatment);
                break;

            case "GENERAL_SURGEON":
                doctor = new GeneralSurgeon(currentId, isSurgeon, maxForTreatment);
                break;

            case "INTERNIST":
                doctor = new Internist(currentId, isSurgeon, maxForTreatment);
                break;

            case "NEUROLOGIST":
                doctor = new Neurologist(currentId, isSurgeon, maxForTreatment);
                break;

            default:
                doctor = null;
                break;

        }

        currentId++;

        return doctor;
    }

}
