package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import services.EmployeeService;

/**
 * 従業員に関わる処理を行うActionクラス
 *
 */
public class EmployeeAction extends ActionBase {

    private EmployeeService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

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

        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<EmployeeView> employees = service.getPerPage(page);

        //全ての従業員データの件数を取得
        long employeeCount = service.countAll();

        putRequestScope("employees", employees); //取得した従業員データ
        putRequestScope("employees_count", employeeCount); //全ての従業員データの件数
//        putRequestScope("employeesCount", employeeCount); //全ての従業員データの件数
        putRequestScope("page", page); //ページ数
        putRequestScope("maxRow", 15); //1ページに表示するレコードの数
//        putRequestScope("maxROW", 15); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope("flush");
        if (flush != null) {
            putRequestScope("flush", flush);
            removeSessionScope("flush");
        }

        //一覧画面を表示
        forward("employees/index");

    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope("_token",getTokenId()); //CSRF対策用トークン
        putRequestScope("employee", new EmployeeView()); //空の従業員インスタンス

        //新規登録画面を表示
        forward("employees/new");
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam("code"),
                    getRequestParam("name"),
                    getRequestParam("password"),
                    toNumber(getRequestParam("admin_flag")),
                    null,
                    null,
                    0);

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope("pepper");

            //従業員情報登録
            List<String> errors = service.create(ev, pepper);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope("_token", getTokenId()); //CSRF対策用トークン
                putRequestScope("employee", ev); //入力された従業員情報
                putRequestScope("errors", errors); //エラーのリスト

                //新規登録画面を再表示
                forward("employees/new");

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope("flush", "登録が完了しました。");

                //一覧画面にリダイレクト
                redirect("Employee", "index");
            }

        }
    }


    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に従業員データを取得する
        EmployeeView ev = service.findOne(toNumber(getRequestParam("id")));

        if (ev == null || ev.getDeleteFlag() == 1) {

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward("error/unknown");
            return;
        }

        putRequestScope("employee", ev); //取得した従業員情報

        //詳細画面を表示
        forward("employees/show");
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に従業員データを取得する
        EmployeeView ev = service.findOne(toNumber(getRequestParam("id")));

        if (ev == null || ev.getDeleteFlag() == 1) {

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward("error/unknown");
            return;
        }

        putRequestScope("_token", getTokenId()); //CSRF対策用トークン
        putRequestScope("employee", ev); //取得した従業員情報

        //編集画面を表示する
        forward("employees/edit");

    }


    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    toNumber(getRequestParam("id")),
                    getRequestParam("code"),
                    getRequestParam("name"),
                    getRequestParam("password"),
                    toNumber(getRequestParam("adminFlag")),
                    null,
                    null,
                    0);

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope("pepper");

            //従業員情報更新
            List<String> errors = service.update(ev, pepper);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope("_token", getTokenId()); //CSRF対策用トークン
                putRequestScope("employee", ev); //入力された従業員情報
                putRequestScope("errors", errors); //エラーのリスト

                //編集画面を再表示
                forward("employees/edit");
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope("flush", "更新が完了しました。");

                //一覧画面にリダイレクト
                redirect("Employee", "index");
            }
        }
    }



}
