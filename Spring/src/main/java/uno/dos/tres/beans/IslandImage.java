package uno.dos.tres.beans;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class IslandImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimages")
    private int id;
    @Column(name = "img_path")
    private String imagePath;
    @OneToOne
    @JoinColumn(name = "islands_idislands")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Island island;

}
