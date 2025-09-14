package es.com.dls.maths.dlsmaths.entity;

import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users") // o "users"
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
//    private String role; // RoleEnum.ADMIN, "CHILD"
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Column(name = "POINTS", columnDefinition = "NUMERIC(38, 2) DEFAULT 0")
    private BigDecimal points;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean multiplicationEnabled = true;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean additionEnabled = true;

    @Column(name = "addition_max_digits")
    private Integer additionMaxDigits = 2; // 1, 2 o 3

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameResult> gameResults = new ArrayList<>();
}
