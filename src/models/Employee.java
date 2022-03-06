package models;

import java.time.LocalDateTime;

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

/**
 * 従業員データのDTOモデル
 *
 */
@Table(name = "employees")
@NamedQueries({
    @NamedQuery(
            name = "employee.getAll",
            query = "SELECT e FROM Employee AS e ORDER BY e.id DESC"),
    @NamedQuery(
            name = "employee.count",
            query = "SELECT COUNT(e) FROM Employee AS e"),
    @NamedQuery(
            name = "employee.countRegisteredByCode",
            query = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :code"),
    @NamedQuery(
            name = "employee.getByCodeAndPass",
            query = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :code AND e.password = :password")
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Employee {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 社員番号
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * 氏名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * パスワード
     */
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    /**
     * 管理者権限があるかどうか（一般：0、管理者：1）
     */
    @Column(name = "admin_flag", nullable = false)
    private Integer adminFlag;

    /**
     *登録日時
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除された従業員かどうか（現役：0、削除済み：1）
     */
    @Column(name = "delete_flag", nullable = false)
    private Integer deleteFlag;

}