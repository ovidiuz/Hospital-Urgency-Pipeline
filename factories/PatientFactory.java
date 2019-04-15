package factories;

import com.fasterxml.jackson.databind.JsonNode;
import entities.Patient;
import skel.IllnessType;

public final class PatientFactory {

    private static PatientFactory instance = null;

    private PatientFactory() { }

    public static PatientFactory getInstance() {

        if (instance == null) {
            instance = new PatientFactory();
        }

        return instance;

    }

    public Patient createPatient(JsonNode jsonNode) {

        int id = jsonNode.get("id").asInt();
        String name = jsonNode.get("name").asText();
        int age = jsonNode.get("age").asInt();
        int time = jsonNode.get("time").asInt();
        String illnessName = jsonNode.get("state").get("illnessName").asText();
        int severity = jsonNode.get("state").get("severity").asInt();

        return new Patient(id, name, age, time, IllnessType.valueOf(illnessName), severity);

    }
}
