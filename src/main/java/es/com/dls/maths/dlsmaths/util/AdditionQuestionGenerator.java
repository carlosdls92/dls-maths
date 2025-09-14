package es.com.dls.maths.dlsmaths.util;

import es.com.dls.maths.dlsmaths.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdditionQuestionGenerator {
    public static List<Question> generate(int numberOfQuestions, int maxDigits) {
        Random rand = new Random();
        List<Question> questions = new ArrayList<>();

        int max = (int) Math.pow(10, maxDigits) - 1;
        int min = (int) Math.pow(10, maxDigits - 1);

        for (int i = 0; i < numberOfQuestions; i++) {
            int a = rand.nextInt((max - min) + 1) + min;
            int b = rand.nextInt((max - min) + 1) + min;
            questions.add(new Question(a + " + " + b, a + b));
        }

        return questions;
    }
}
