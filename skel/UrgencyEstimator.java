package skel;

import java.util.HashMap;
import java.util.Map;


/**
 * Estimates the urgency based on the patient's illness and how severe the illness is manifested
 */
public final class UrgencyEstimator {

    private static final int NUMBER_10 = 10;
    private static final int NUMBER_20 = 20;
    private static final int NUMBER_30 = 30;
    private static final int NUMBER_40 = 40;
    private static final int NUMBER_50 = 50;
    private static final int NUMBER_60 = 60;
    private static final int NUMBER_70 = 70;
    private static final int NUMBER_80 = 80;


    private static UrgencyEstimator instance;
    private Map<Urgency, HashMap<IllnessType, Integer>> algorithm;

    private UrgencyEstimator() {
        algorithm = new HashMap<Urgency, HashMap<IllnessType, Integer>>() {
            {
                put(Urgency.IMMEDIATE,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, NUMBER_60);
                                put(IllnessType.ALLERGIC_REACTION, NUMBER_50);
                                put(IllnessType.BROKEN_BONES, NUMBER_80);
                                put(IllnessType.BURNS, NUMBER_40);
                                put(IllnessType.CAR_ACCIDENT, NUMBER_30);
                                put(IllnessType.CUTS, NUMBER_50);
                                put(IllnessType.FOOD_POISONING, NUMBER_50);
                                put(IllnessType.HEART_ATTACK, 0);
                                put(IllnessType.HEART_DISEASE, NUMBER_40);
                                put(IllnessType.HIGH_FEVER, NUMBER_70);
                                put(IllnessType.PNEUMONIA, NUMBER_80);
                                put(IllnessType.SPORT_INJURIES, NUMBER_70);
                                put(IllnessType.STROKE, 0);

                            }
                        });

                put(Urgency.URGENT,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, NUMBER_40);
                                put(IllnessType.ALLERGIC_REACTION, NUMBER_30);
                                put(IllnessType.BROKEN_BONES, NUMBER_50);
                                put(IllnessType.BURNS, NUMBER_20);
                                put(IllnessType.CAR_ACCIDENT, NUMBER_20);
                                put(IllnessType.CUTS, NUMBER_30);
                                put(IllnessType.HEART_ATTACK, 0);
                                put(IllnessType.FOOD_POISONING, NUMBER_20);
                                put(IllnessType.HEART_DISEASE, NUMBER_20);
                                put(IllnessType.HIGH_FEVER, NUMBER_40);
                                put(IllnessType.PNEUMONIA, NUMBER_50);
                                put(IllnessType.SPORT_INJURIES, NUMBER_50);
                                put(IllnessType.STROKE, 0);
                            }
                        });

                put(Urgency.LESS_URGENT,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, NUMBER_10);
                                put(IllnessType.ALLERGIC_REACTION, NUMBER_10);
                                put(IllnessType.BROKEN_BONES, NUMBER_20);
                                put(IllnessType.BURNS, NUMBER_10);
                                put(IllnessType.CAR_ACCIDENT, NUMBER_10);
                                put(IllnessType.CUTS, NUMBER_10);
                                put(IllnessType.FOOD_POISONING, 0);
                                put(IllnessType.HEART_ATTACK, 0);
                                put(IllnessType.HEART_DISEASE, NUMBER_10);
                                put(IllnessType.HIGH_FEVER, 0);
                                put(IllnessType.PNEUMONIA, NUMBER_10);
                                put(IllnessType.SPORT_INJURIES, NUMBER_20);
                                put(IllnessType.STROKE, 0);
                            }
                        });

            }
        };
    }

    public static UrgencyEstimator getInstance() {
        if (instance == null) {
            instance = new UrgencyEstimator();
        }
        return instance;
    }

    //called by doctors and nurses
    public Urgency estimateUrgency(IllnessType illnessType, int severity) {

        if (severity >= algorithm.get(Urgency.IMMEDIATE).get(illnessType)) {
            return Urgency.IMMEDIATE;
        }
        if (severity >= algorithm.get(Urgency.URGENT).get(illnessType)) {
            return Urgency.URGENT;
        }
        if (severity >= algorithm.get(Urgency.LESS_URGENT).get(illnessType)) {
            return Urgency.LESS_URGENT;
        }
        return Urgency.NON_URGENT;
    }
}
