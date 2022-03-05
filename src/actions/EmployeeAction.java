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

}
