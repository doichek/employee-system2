package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import services.EmployeeService;

/**
 * 認証に関する処理を行うActionクラス
 *
 */
public class AuthAction extends ActionBase {

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
     * ログイン画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void showLogin() throws ServletException, IOException {

        //CSRF対策用トークンを設定
        putRequestScope("_token", getTokenId());

        //セッションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
        String flush = getSessionScope("flush");
        if (flush != null) {
            putRequestScope("flush",flush);
            removeSessionScope("flush");
        }

        //ログイン画面を表示
        forward("login/login");
    }

    /**
     * ログイン処理を行う
     * @throws ServletException
     * @throws IOException
     */
    public void login() throws ServletException, IOException {

        String code = getRequestParam("code");
        String plainPass = getRequestParam("password");
        String pepper = getContextScope("pepper");

        //有効な従業員か認証する
        Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

        if (isValidEmployee) {
            //認証成功の場合

            //CSRF対策 tokenのチェック
            if (checkToken()) {

                //ログインした従業員のDBデータを取得
                EmployeeView ev = service.findOne(code, plainPass, pepper);
                //セッションにログインした従業員を設定
                putSessionScope("login_employee", ev);
                //セッションにログイン完了のフラッシュメッセージを設定
                putSessionScope("flush", "ログインしました");
//                //トップページへリダイレクト
//                redirect("Top","index" );
                //勤怠管理画面へリダイレクト
                redirect("Attendance","index" );
            }
        } else {
            //認証失敗の場合

            //CSRF対策用トークンを設定
            putRequestScope("_token", getTokenId());
            //認証失敗エラーメッセージ表示フラグをたてる
            putRequestScope("loginError", true);
            //入力された従業員コードを設定
            putRequestScope("code", code);

            //ログイン画面を表示
            forward("login/login");
        }
    }

    /**
     * ログアウト処理を行う
     * @throws ServletException
     * @throws IOException
     */
    public void logout() throws ServletException, IOException {

        //セッションからログイン従業員のパラメータを削除
        removeSessionScope("login_employee");

        //セッションにログアウト時のフラッシュメッセージを追加
        putSessionScope("flush", "ログアウトしました。");

        //ログイン画面にリダイレクト
        redirect("Auth", "showLogin");

    }


}
