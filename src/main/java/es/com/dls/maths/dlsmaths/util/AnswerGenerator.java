package es.com.dls.maths.dlsmaths.util;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.model.QuestionAbstract;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AnswerGenerator {
    public static List<Integer> getAnswersFromQuestion(QuestionAbstract question, GameConfig config){
        switch (question.getOperation()){
            case MULTIPLICATION: {
                return getMultiplicationAnswersFromQuestion(question, config);
            }
            case ADDITION: {
                return getAdditionAnswersFromQuestion(question, config);
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    public static List<Integer> getMultiplicationAnswersFromQuestion(QuestionAbstract question, GameConfig config) {
        int a = question.getA();
        int b = question.getB();
        int correct = a * b;

        Set<Integer> options = new LinkedHashSet<>();
        options.add(correct);

        Random random = new Random();

        while (options.size() < config.getNumberAnswers()) {
            int deltaA = random.nextInt(5) - 2; // -2 a +2
            int deltaB = random.nextInt(5) - 2;

            int newA = Math.max(1, a + deltaA); // evitar 0 o negativos
            int newB = Math.max(1, b + deltaB);

            int distractor = newA * newB;

            // Agregar solo si no es correcto ni 0
            if (distractor != correct && distractor != 0) {
                options.add(distractor);
            }
        }

        List<Integer> listOptions = new ArrayList<>(options);
        Collections.shuffle(listOptions);
        return listOptions;
    }

    public static List<Integer> getAdditionAnswersFromQuestion(QuestionAbstract question, GameConfig config) {
        int a = question.getA();
        int b = question.getB();
        int correct = a + b;

        Set<Integer> options = new LinkedHashSet<>();
        options.add(correct);

        Random random = new Random();

        while (options.size() < config.getNumberAnswers()) {
            int deltaA = random.nextInt(5) - 2; // -2 a +2
            int deltaB = random.nextInt(5) - 2;

            int newA = Math.max(1, a + deltaA); // evitar 0 o negativos
            int newB = Math.max(1, b + deltaB);

            int distractor = newA + newB;

            // Agregar solo si no es correcto ni 0
            if (distractor != correct && distractor != 0) {
                options.add(distractor);
            }
        }

        List<Integer> listOptions = new ArrayList<>(options);
        Collections.shuffle(listOptions);
        return listOptions;
    }

}
