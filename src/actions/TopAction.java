package actions;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * トップページに関する処理を行うActionクラス
 *
 */
public class TopAction extends ActionBase {

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        //メソッドを実行
        invoke();

    }

    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope("flush");
        if (flush != null) {
            putRequestScope("flush", flush);
            removeSessionScope("flush");
        }

        //一覧画面を表示
        forward("topPage/index");
    }

}