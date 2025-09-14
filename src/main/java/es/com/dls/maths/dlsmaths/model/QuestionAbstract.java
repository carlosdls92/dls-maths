package es.com.dls.maths.dlsmaths.model;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.enums.OperationEnum;
import es.com.dls.maths.dlsmaths.util.AnswerGenerator;

import java.util.*;

public class QuestionAbstract {
    private final int a;
    private final int b;
    private final OperationEnum operation;
    private int correctAnswer;
    private final List<Integer> possibleAnswers;

    public QuestionAbstract(int a, int b, OperationEnum operation, GameConfig config) {
        this.a = a;
        this.b = b;
        this.operation = operation;

        switch (operation){
            case MULTIPLICATION:{
                this.correctAnswer = a * b;
                break;
            }
            case ADDITION: {
                this.correctAnswer = a + b;
                break;
            }
            case DIVISION: {
                this.correctAnswer = a / b;
                break;
            }
            case SUBSTRACTION: {
                this.correctAnswer = a - b;
                break;
            }
        }
        this.possibleAnswers = AnswerGenerator.getAnswersFromQuestion(this, config);
    }

    public int getA() { return a; }
    public int getB() { return b; }
    public OperationEnum getOperation() { return operation; }
    public int getCorrectAnswer() { return correctAnswer; }
    public List<Integer> getPossibleAnswers() { return possibleAnswers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionAbstract)) return false;
        QuestionAbstract that = (QuestionAbstract) o;
        return a == that.a && b == that.b;
    }

}
