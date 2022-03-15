package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.AttendanceConverter;
import actions.views.AttendanceView;
import models.Attendance;
import models.validators.AttendanceValidator;

/**
 * 勤怠情報テーブルの操作に関わる処理を行うクラス
 */
public class AttendanceService extends ServiceBase {

    /**
     * ログイン中のアカウントに紐づく、勤怠情報レコードすべて取得し、AttendanceViewのリストで返却する
     * @param id ログイン中のアカウントのid
     * @return 表示するデータのリスト
     */
    public List<AttendanceView> getPerPage(int id) {
        List<Attendance> attendances = em.createNamedQuery("getAttendances", Attendance.class).setParameter("id", id).getResultList();

        return AttendanceConverter.toViewList(attendances);
    }

    /**
     * idを条件に取得したデータをAttendanceViewのインスタンスで返却する
     * @param id 勤怠情報のid
     * @return 取得データのインスタンス
     */
    public AttendanceView findOne(int id) {
        Attendance a = findOneInternal(id);
        return AttendanceConverter.toView(a);
    }


    /**
     * indexページやnewページから入力された勤怠情報を元にデータを1件作成し、勤怠情報テーブルに登録する
     * @param av 入力された勤怠情報の登録内容
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
    public List<String> create(AttendanceView av) {

        //登録内容のバリデーションを行う
        List<String> errors = AttendanceValidator.validate(av);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {

            em.getTransaction().begin();
            em.persist(AttendanceConverter.toModel(av));
            em.getTransaction().commit();

        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * indexページやnewページから入力された勤怠情報を元にデータを1件作成し、勤怠情報テーブルに登録する
     * @param ev 入力された勤怠情報の登録内容
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(AttendanceView av) {

        //idを条件に登録済みの勤怠情報を取得する
        AttendanceView savedEmp = findOne(av.getId());

            //変更後の勤怠情報を設定する
            savedEmp.setYear(av.getYear());
            savedEmp.setMonth(av.getMonth());
            savedEmp.setDate(av.getDate());
            savedEmp.setStarting_time(av.getStarting_time());
            savedEmp.setQuitting_time(av.getQuitting_time());
            savedEmp.setContent(av.getContent());

        //更新内容についてバリデーションを行う
        List<String> errors = AttendanceValidator.validate(savedEmp);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {

                em.getTransaction().begin();
                Attendance a = findOneInternal(av.getId());
                AttendanceConverter.copyViewToModel(a, av);
                em.getTransaction().commit();

        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを削除
     * @param id 勤怠情報のid
     */
    public void destroy(Integer id) {

        //idを条件に登録済みの勤怠情報を取得する
        Attendance savedEmp = findOneInternal(id);

        em.getTransaction().begin();
        em.remove(savedEmp);       // データ削除
        em.getTransaction().commit();

        return;
    }

    /**
     * idを条件にデータを1件取得し、Attendanceのインスタンスで返却する
     * @param id 勤怠情報のid
     * @return 取得データのインスタンス
     */
    private Attendance findOneInternal(int id) {
        Attendance a = em.find(Attendance.class, id);

        return a;
    }


    /**
     * 今日の日付を条件にデータを1件取得し、Attendanceのインスタンスで返却する
     * @return 取得データのインスタンス
     */
    public Attendance findToday() {
        Attendance a = null;

        try {
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd");
        //<!-- 日時情報を指定フォーマットの文字列で取得 -->
        Integer y = Integer.parseInt(nowDateTime.format( year ));
        Integer m = Integer.parseInt(nowDateTime.format( month ));
        Integer d = Integer.parseInt(nowDateTime.format( date ));


        a = em.createNamedQuery("getAttendanceToday", Attendance.class)
              .setParameter("year", y)
              .setParameter("month", m)
              .setParameter("date", d)
              .getSingleResult();

        } catch (NoResultException ex) {
        }

        return a;
    }



}
