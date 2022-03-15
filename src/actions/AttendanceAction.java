package actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.AttendanceView;
import actions.views.EmployeeView;
import models.Attendance;
import models.MyCalendar;
import models.MyCalendarLogic;
import services.AttendanceService;

/**
 * 従業員に関わる処理を行うActionクラス
 *
 */
public class AttendanceAction extends ActionBase {

    private AttendanceService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new AttendanceService();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

            putRequestScope("_token",getTokenId()); //CSRF対策用トークン

            //ログインしているアカウントに紐づく勤怠情報レコードをすべて取得
            EmployeeView ev = getSessionScope( "login_employee" );
            List<AttendanceView> attendances = service.getPerPage(ev.getId());

            Attendance a  = service.findToday();

            if (a != null) {
                String update = "update";
                putRequestScope("createOrUpdate" ,update);
            }else {
                String create = "create";
                putRequestScope("createOrUpdate", create);
            }

            putRequestScope("attendances", attendances); //取得した勤怠情報レコード

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope("flush");
            if (flush != null) {
                putRequestScope("flush", flush);
                removeSessionScope("flush");
            }


            //////カレンダー表示///////////////
            String s_year=request.getParameter("year");
            String s_month=request.getParameter("month");

            MyCalendarLogic logic=new MyCalendarLogic();
            MyCalendar mc=null;

            if(s_year != null && s_month != null) {
                int year =Integer.parseInt(s_year);
                int month=Integer.parseInt(s_month);
                if(month==0) {
                    month=12;
                    year--;
                }
                if(month==13) {
                    month=1;
                    year++;
                }
                //年と月のクエリパラメーターが来ている場合にはその年月でカレンダーを生成する
                mc=logic.createMyCalendar(year,month);
            }else {
                //クエリパラメータが来ていないときは実行日時のカレンダーを生成する。

                mc=logic.createMyCalendar();
            }

            request.setAttribute("mc", mc);
            ////カレンダー表示終わり/////////////////////////


            //一覧画面を表示
            forward("attendances/index");
    }


    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

            putRequestScope("_token",getTokenId()); //CSRF対策用トークン
            putRequestScope("attendance", new AttendanceView()); //空の従業員インスタンス
            putRequestScope("year", toNumber(getRequestParam("year")));
            putRequestScope("date", toNumber(getRequestParam("date")));
            putRequestScope("month",toNumber(getRequestParam("month")));

            //新規登録画面を表示
            forward("attendances/new");
    }


    /**
     * 新規登録を行う(退勤打刻ボタンでの時間入力は不可能※新規登録画面からの手入力はOK)
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

            //CSRF対策 tokenのチェック
            if (checkToken()) {
                EmployeeView ev = getSessionScope( "login_employee" );
                AttendanceView av = null;
                List<String> errors = new ArrayList<String>();


                //出勤打刻ボタンを押した場合
                if( 0 == toNumber(getRequestParam("shukkinn")) ){
                     LocalDateTime nowDateTime = LocalDateTime.now();
                     DateTimeFormatter ye = DateTimeFormatter.ofPattern("yyyy");
                     DateTimeFormatter mo = DateTimeFormatter.ofPattern("MM");
                     DateTimeFormatter da = DateTimeFormatter.ofPattern("dd");
                     DateTimeFormatter ti = DateTimeFormatter.ofPattern("HH:mm");
                     //<!-- 日時情報を指定フォーマットの文字列で取得 -->
                     String year = nowDateTime.format( ye );
                     String month = nowDateTime.format( mo );
                     String date = nowDateTime.format( da );
                     String time = nowDateTime.format( ti );

                    av = new AttendanceView(
                            null,//id
                            toNumber(year),//year
                            toNumber(month),//month
                            toNumber(date),//date
                            time,//starting_time
                            "",//quitting_time
                            getRequestParam("content"),//content
                            ev.getId()//employee_id
                            );

                    errors = service.create(av);


                //退勤打刻ボタンを押した場合
                } else if ( 1 == toNumber(getRequestParam("shukkinn")) ){
                    errors.add("出勤打刻が完了していません");


                //新規登録画面から勤怠情報を手入力した場合
                } else {
                    av = new AttendanceView(
                        null,
                        toNumber(getRequestParam("year")),
                        toNumber(getRequestParam("month")),
                        toNumber(getRequestParam("date")),
                        getRequestParam("starting_time"),
                        getRequestParam("quitting_time"),
                        getRequestParam("content"),
                        ev.getId()
                        );
                    errors = service.create(av);
                };


                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    if( 1 == toNumber(getRequestParam("shukkinn")) ) {//出勤打刻でのエラー
                        putSessionScope("flush", errors.get(0));
                        redirect("Attendance", "index");
                    }else {//新規登録画面の手入力でのエラー
                        putRequestScope("_token", getTokenId()); //CSRF対策用トークン
                        putRequestScope("attendance", av); //入力された従業員情報
                        putRequestScope("errors", errors); //エラーのリスト
                        putRequestScope("year", toNumber(getRequestParam("year")));
                        putRequestScope("date", toNumber(getRequestParam("date")));
                        putRequestScope("month",toNumber(getRequestParam("month")));
                        //新規登録画面を再表示
                        forward("attendances/new");
                    }

                } else {
                    //登録中にエラーがなかった場合
                    putSessionScope("flush", "登録が完了しました。");
                    redirect("Attendance", "index");
                }

            }

    }


    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

            //idを条件に勤怠情報レコードを1件取得する
            AttendanceView av = service.findOne(toNumber(getRequestParam("id")));

            if (av == null) {

                //データが取得できなかった、または削除されている場合はエラー画面を表示
                forward("error/unknown");
                return;
            }

            putRequestScope("attendance", av);
            //詳細画面を表示
            forward("attendances/show");

    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

            //idを条件に勤怠情報を1件取得する
            AttendanceView av = service.findOne(toNumber(getRequestParam("id")));

            if (av == null) {

                //データが取得できなかった、または削除されている場合はエラー画面を表示
                forward("error/unknown");
                return;
            }

            putRequestScope("_token", getTokenId()); //CSRF対策用トークン
            putRequestScope("attendance", av); //取得した従業員情報
            putRequestScope("year", toNumber(getRequestParam("year")));
            putRequestScope("date", toNumber(getRequestParam("date")));
            putRequestScope("month",toNumber(getRequestParam("month")));

            //編集画面を表示する
            forward("attendances/edit");


    }


    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {


            //CSRF対策 tokenのチェック
            if (checkToken()) {
                EmployeeView ev = getSessionScope( "login_employee" );
                AttendanceView av = null;
                List<String> errors = new ArrayList<String>();

               if( 0 == toNumber(getRequestParam("shukkinn")) ){
                    errors.add("すでに出勤打刻は完了しています");
               }
               else if ( 1 == toNumber(getRequestParam("shukkinn")) ){

                   LocalDateTime nowDateTime = LocalDateTime.now();
                   DateTimeFormatter ye = DateTimeFormatter.ofPattern("yyyy");
                   DateTimeFormatter mo = DateTimeFormatter.ofPattern("MM");
                   DateTimeFormatter da = DateTimeFormatter.ofPattern("dd");
                   DateTimeFormatter ti = DateTimeFormatter.ofPattern("HH:mm");
                   //<!-- 日時情報を指定フォーマットの文字列で取得 -->
                   String year = nowDateTime.format( ye );
                   String month = nowDateTime.format( mo );
                   String date = nowDateTime.format( da );
                   String time = nowDateTime.format( ti );

                   Attendance a  = service.findToday();
                   if ( a.getQuitting_time().equals("")  ) {
                       av = new AttendanceView(
                          a.getId(),
                          toNumber(year),
                          toNumber(month),
                          toNumber(date),
                          a.getStarting_time(),
                          time,
                          a.getContent(),
                          ev.getId()
                          );

                       errors = service.update(av);
                   } else {
                       errors.add("すでに退勤打刻は完了しています");
                   }


               }else {

                    av = new AttendanceView(
                            toNumber(getRequestParam("id")),
                            toNumber(getRequestParam("year")),
                            toNumber(getRequestParam("month")),
                            toNumber(getRequestParam("date")),
                            getRequestParam("starting_time"),
                            getRequestParam("quitting_time"),
                            getRequestParam("content"),
                            ev.getId()
                            );
                    errors = service.update(av);
               }


                if (errors.size() > 0) {
                    //更新中にエラーが発生した場合

                    if(  0 == toNumber(getRequestParam("shukkinn"))  ||  1 == toNumber(getRequestParam("shukkinn"))  ) {
                        putSessionScope("flush", errors.get(0));
                        redirect("Attendance", "index");
                    }else {

                        putRequestScope("_token", getTokenId()); //CSRF対策用トークン
                        putRequestScope("attendance", ev); //入力された従業員情報
                        putRequestScope("errors", errors); //エラーのリスト
                        putRequestScope("year", toNumber(getRequestParam("year")));
                        putRequestScope("date", toNumber(getRequestParam("date")));
                        putRequestScope("month",toNumber(getRequestParam("month")));

                        //編集画面を再表示
                        forward("attendances/edit");
                    }

                } else {
                    //更新中にエラーがなかった場合

                    putSessionScope("flush", "更新が完了しました。");
                    redirect("Attendance", "index");
                }
            }

    }

    /**
     * 削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

            //CSRF対策 tokenのチェック
            if (checkToken()) {

                //idを条件に勤怠情報を削除する
                service.destroy(toNumber(getRequestParam("id")));
                putSessionScope("flush", "削除が完了しました。");
                redirect("Attendance", "index");
            }

    }

}
