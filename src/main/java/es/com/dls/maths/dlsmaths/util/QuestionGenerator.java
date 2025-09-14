package es.com.dls.maths.dlsmaths.util;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.enums.MultiplicationMode;
import es.com.dls.maths.dlsmaths.enums.OperationEnum;
import es.com.dls.maths.dlsmaths.model.Question;
import es.com.dls.maths.dlsmaths.model.QuestionAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestionGenerator {
    private static Random rand = new Random();

    public static QuestionAbstract generateAdditionQuestion(int maxDigits, GameConfig config) {
        Random rand = new Random();

        int max = (int) Math.pow(10, maxDigits) - 1;
        int min = (int) Math.pow(10, maxDigits - 1);

        int a = rand.nextInt((max - min) + 1) + min;
        int b = rand.nextInt((max - min) + 1) + min;
        return new QuestionAbstract(a, b, OperationEnum.ADDITION, config);

    }

    public static List<Question> generateListAdditionQuestions(int numberOfQuestions, int maxDigits) {
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

    public static QuestionAbstract generateMultiplicationQuestion(
            MultiplicationMode mode,
            int table, // solo si es modo SPECIFIC
            OperationEnum operation,
            GameConfig config) {
        int a = getMultiplicand(mode, table, listMultiplicandValids(config));
        int b = rand.nextInt(12) + 1;
        return new QuestionAbstract(a, b, operation, config);
    }

    private static int getMultiplicand(
            MultiplicationMode mode,
            int table, // solo si es modo SPECIFIC
            List<Integer> listMultiplicandValids){

        Random rand = new Random();
        if(MultiplicationMode.SPECIFIC == mode){
            return table;
        }
        int a = 0;
        do{
            a = rand.nextInt(12) + 1;
        }while(!listMultiplicandValids.contains(a));
        return a;
    }

    private static List<Integer> listMultiplicandValids(GameConfig config){
        return Arrays.stream(config.getRangeTables()
                .split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
