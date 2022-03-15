package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Attendance;

/**
 * 勤怠情報のDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class AttendanceConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param av AttendanceViewのインスタンス
     * @return Attendanceのインスタンス
     */
    public static Attendance toModel(AttendanceView av) {

        return new Attendance(
                av.getId(),
                av.getYear(),
                av.getMonth(),
                av.getDate(),
                av.getStarting_time(),
                av.getQuitting_time(),
                av.getContent(),
                av.getEmployee_id());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param a Attendanceのインスタンス
     * @return AttendanceViewのインスタンス
     */
    public static AttendanceView toView(Attendance a) {

        if(a == null) {
            return null;
        }

        return new AttendanceView(
                a.getId(),
                a.getYear(),
                a.getMonth(),
                a.getDate(),
                a.getStarting_time(),
                a.getQuitting_time(),
                a.getContent(),
                a.getEmployee_id());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<AttendanceView> toViewList(List<Attendance> list) {
        List<AttendanceView> avs = new ArrayList<>();

        for (Attendance a : list) {
            avs.add(toView(a));
        }

        return avs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param a DTOモデル(コピー先)
     * @param av Viewモデル(コピー元)
     */
    public static void copyViewToModel(Attendance a, AttendanceView av) {
        a.setId(av.getId());
        a.setYear(av.getYear());
        a.setMonth(av.getMonth());
        a.setDate(av.getDate());
        a.setStarting_time(av.getStarting_time());
        a.setQuitting_time(av.getQuitting_time());
        a.setContent(av.getContent());
        a.setEmployee_id(av.getEmployee_id());

    }

}