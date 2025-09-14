package es.com.dls.maths.dlsmaths.model;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.enums.MultiplicationMode;
import es.com.dls.maths.dlsmaths.enums.OperationEnum;
import es.com.dls.maths.dlsmaths.util.QuestionGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSession {

    private MultiplicationMode mode;
    private int table; // solo si es modo SPECIFIC
//    private Queue<MultiplicationQuestion> questions;
    private List<QuestionAbstract> questions;
    private List<AnswerRecordDto> answers;
    private OperationEnum operation;
    private int totalQuestions = 20;
    private int currentIndex;
    private List<Integer> operands;
    private BigDecimal percent;
    private int maxDigits =2;
    private boolean secondChance = false;
    private BigDecimal percentageToPoints;

    public GameSession(MultiplicationMode mode, int table, GameConfig config) {
        this.mode = mode;
        this.table = table;
        this.answers = new ArrayList<>();
        this.questions = new LinkedList<>();
        this.operation = OperationEnum.MULTIPLICATION;
        this.totalQuestions = config.getNumberQuestions();
        this.percentageToPoints = config.getPercentageToPoints();
        this.questions.add(QuestionGenerator.generateMultiplicationQuestion(mode, table, OperationEnum.MULTIPLICATION, config));
//        generateQuestions(config.getRangeTables());
//        this.operands = generateRandomOperands(config);
    }

    public GameSession(int maxDigits, GameConfig config) {
        this.answers = new ArrayList<>();
        this.questions = new LinkedList<>();
        this.operation = OperationEnum.ADDITION;
        this.maxDigits = maxDigits;
        this.totalQuestions = config.getNumberQuestions();
        this.percentageToPoints = config.getPercentageToPoints();
        this.questions.add(QuestionGenerator.generateAdditionQuestion(maxDigits, config));
    }

    public BigDecimal getPercent() {
        int totalCorrect = answers.stream()
                .filter(AnswerRecordDto::isCorrect)
                .collect(Collectors.toList())
                .size();
        return BigDecimal.valueOf(totalCorrect)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf((answers.size())), 2, RoundingMode.CEILING);
    }

    public BigDecimal getPoints() {
        int totalCorrect = answers.stream()
                .filter(AnswerRecordDto::isCorrect)
                .collect(Collectors.toList())
                .size();
        return BigDecimal.valueOf(totalCorrect)
                .divide(BigDecimal.valueOf((answers.size())), 2, RoundingMode.CEILING)
                .multiply(percentageToPoints)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
    }

    public int getTotalCorrect() {
        return answers.stream()
                .filter(AnswerRecordDto::isCorrect)
                .collect(Collectors.toList())
                .size();
    }

    public QuestionAbstract getNextQuestion(int currentIndex) {
        return questions.get(currentIndex-1);
    }

    public void submitAnswer(int a, int b, int givenAnswer, int timeToAnswer, OperationEnum operation, GameConfig config) {
        QuestionAbstract q = new QuestionAbstract(a, b, operation, config);
        boolean correct = q.getCorrectAnswer() == givenAnswer;
        answers.add(new AnswerRecordDto(q.getA(), q.getB(), operation, givenAnswer, correct, timeToAnswer));

        if (!correct && !secondChance) {
            secondChance = true;
            // lógica de repetición
            if (mode == MultiplicationMode.ALL) {
                questions.add(new QuestionAbstract(q.getB(), q.getA(), operation, config));
                questions.add(new QuestionAbstract(q.getA(), q.getB(), operation, config));
                return;
            } else {
                questions.add(new QuestionAbstract(q.getB(), q.getA(), operation, config));
                return;
            }
        }
        secondChance=false;
        QuestionAbstract question = null;
        do{
            switch (operation) {
                case MULTIPLICATION:
                    question = QuestionGenerator.generateMultiplicationQuestion(mode, table, operation, config);
                    break;
                case ADDITION:
                    question = QuestionGenerator.generateAdditionQuestion(maxDigits, config);
                    break;
            }
//            question = QuestionGenerator.generateMultiplicationQuestion(mode, table, operation, config);
        }while(questions.get(answers.size()-1).equals(question));
        questions.add(question);
    }

    public int getCurrentQuestionNumber() {
        return answers.size() + 1;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public boolean isFinished() {
//        return answers.size() >= totalQuestions && questions.isEmpty();
        return answers.size() >= totalQuestions;
    }

    public List<AnswerRecordDto> getAnswers() {
        return answers;
    }

    public MultiplicationMode getMode() {
        return mode;
    }

    public int getTable() {
        return table;
    }

    public int getCurrentOperand() {
        return operands.get(currentIndex);
    }
}
