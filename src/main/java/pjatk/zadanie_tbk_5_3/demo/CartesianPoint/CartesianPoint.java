package pjatk.zadanie_tbk_5_3.demo.CartesianPoint;

import lombok.*;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(name = "points")
@Getter
@Setter
@NoArgsConstructor
public class CartesianPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    public CartesianPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
