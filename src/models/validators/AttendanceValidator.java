package models.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;        //SimpleDateFormatクラスをインポート
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import actions.views.AttendanceView;
public class AttendanceValidator {
    // バリデーションを実行する
    public static List<String> validate(AttendanceView av){
        List<String> errors = new ArrayList<String>();

        if (!av.getStarting_time().equals("")){//空欄でなければチェックを行う
            String starting_time_error = validateStarting_time(av.getStarting_time());
            if(!starting_time_error.equals("")) {
                errors.add(starting_time_error);
            }
        }

        if (!av.getQuitting_time().equals("")) {//空欄でなければチェックを行う
            String quitting_time_error = validateQuitting_time(av.getQuitting_time());
            if(!quitting_time_error.equals("")) {
                errors.add(quitting_time_error);
            }
        }

        Integer listCount = errors.size();
        if (   (!av.getQuitting_time().equals("")) && (!av.getStarting_time().equals("")) && (listCount == 0) ){//両方空欄でない、かつ上記エラーが発生していない場合チェックを行う
              String starting_quitting_time_error = validateStarting_quitting_time(av.getStarting_time(),av.getQuitting_time());
              if(!starting_quitting_time_error.equals("")) {
                  errors.add(starting_quitting_time_error);
              }
        }

        return errors;
    }

    // 出勤時刻の入力チェック
    private static String validateStarting_time(String starting_time) {

            Pattern pattern = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]$|^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

            boolean result = pattern.matcher(starting_time).matches();
            if (result != true) {
                return "正しい出勤時刻を入力してください(hh:mm)(半角数字)";
            }


        return "";
    }

    // 退勤時刻の入力チェック
    private static String validateQuitting_time(String quitting_time) {

            Pattern pattern = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]$|^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

            boolean result = pattern.matcher(quitting_time).matches();
            if (result != true) {
                return "正しい退勤時刻を入力してください(hh:mm)(半角数字)";
            }


        return "";
    }

    // 出勤退勤時刻の整合性チェック
    private static String validateStarting_quitting_time(String starting_time, String quitting_time ) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Date date1;
        try {
            date1 = sdf.parse(starting_time);
        } catch (ParseException e1) {
            // TODO 自動生成された catch ブロック
            return "a";
        }

        Date date2;
        try {
            date2 = sdf.parse(quitting_time);
        } catch (ParseException e) {
            // TODO 自動生成された catch ブロック
            return "b";
        }

        System.out.println(date1.before(date2));
        boolean result = date1.before(date2);
        if (result != true) {
            return "出勤時刻と退勤時刻の整合性がとれていません";
        }

        return "";

    }


}