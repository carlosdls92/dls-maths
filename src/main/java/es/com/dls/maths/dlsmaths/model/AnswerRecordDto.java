package es.com.dls.maths.dlsmaths.model;

import es.com.dls.maths.dlsmaths.enums.OperationEnum;

public class AnswerRecordDto {
    private final int a;
    private final int b;
    private final String operation;
    private final int givenAnswer;
    private int correctAnswer;
    private final boolean correct;
    private final int timeToAnswer;

    public AnswerRecordDto(int a, int b, OperationEnum operation, int givenAnswer, boolean correct, int timeToAnswer) {
        this.a = a;
        this.b = b;
        this.operation = operation.name();
        this.givenAnswer = givenAnswer;
        this.timeToAnswer = timeToAnswer;
        this.correctAnswer = this.a * this.b;
        this.correct = correct;
    }

    public int getA() { return a; }
    public int getB() { return b; }
    public int getGivenAnswer() { return givenAnswer; }
    public int getCorrectAnswer() { return correctAnswer; }
    public boolean isCorrect() { return correct; }
    public int getTimeToAnswer() { return timeToAnswer; }
}
