package es.com.dls.maths.dlsmaths.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameConfig {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private int timeLimitSeconds = 5;
    private boolean enableTimer = true;
    private int rangeIni=2;
    private int rangeFin=12;
    private String rangeTables;
    @Column(name = "PERCENTAGE_TO_POINTS", columnDefinition = "NUMERIC(38, 2) DEFAULT 0")
    private BigDecimal percentageToPoints;
    @Column(columnDefinition = "BOOLEAN")
    private boolean enableAllTables = true;
    @Column(name = "NUMBER_QUESTIONS", columnDefinition = "INTEGER DEFAULT 10")
    private int numberQuestions;
    @Column(name = "NUMBER_ANSWERS", columnDefinition = "INTEGER DEFAULT 10")
    private int numberAnswers;
}
