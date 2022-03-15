package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
@NamedQueries({
    @NamedQuery(//主キー以外の項目などで検索し、複数件の結果を取得したい場合に定義する
        name = "getAllAttendances",
        query = "SELECT m FROM Attendance AS m ORDER BY m.id DESC"
    ),
    @NamedQuery(
            name = "getAttendances",
            query = "SELECT m FROM Attendance AS m WHERE m.employee_id = :id ORDER BY m.id DESC"
    ),
    @NamedQuery(
            name = "getAttendanceToday",
            query = "SELECT m FROM Attendance AS m WHERE m.year = :year AND m.month = :month AND m.date = :date"
    )
})
@Table(name = "attendances")
public class Attendance {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主キー値を自動採番する
    private Integer id;

    @Column(name = "year", length = 255, nullable = false)
    private Integer year;

    @Column(name = "month", length = 255, nullable = false)
    private Integer month;

    @Column(name = "date", length = 255, nullable = false)
    private Integer date;

    @Column(name = "starting_time", length = 255, nullable = true)
    private String starting_time;

    @Column(name = "quitting_time", length = 255, nullable = true)
    private String quitting_time;

    @Column(name = "content", length = 255, nullable = true)
    private String content;

    @Column(name = "employee_id", length = 255, nullable = false)
    private Integer employee_id;
}