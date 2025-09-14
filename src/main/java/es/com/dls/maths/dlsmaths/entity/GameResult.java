package es.com.dls.maths.dlsmaths.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // Relación con el usuario que jugó

    private String mode;

    private String operation;

    private String tableName; // si fue modo SPECIFIC

    @Column(name = "TOTAL_QUESTIONS", columnDefinition = "INTEGER DEFAULT 0")
    private int totalQuestions;

    @Column(name = "TOTAL_CORRECT", columnDefinition = "INTEGER DEFAULT 0")
    private int totalCorrect;

    private BigDecimal percent;

    private BigDecimal points;

    private BigDecimal average;

    private LocalDateTime playedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_result_id")
    private List<AnswerRecord> answers;
}
