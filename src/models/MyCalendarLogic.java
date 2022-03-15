package models;
import java.util.Calendar;

public class MyCalendarLogic {
    //カレンダーインスタンスを生成するメソッド(int...は可変長引数)
    public MyCalendar createMyCalendar(int... args) {
        //マイカレンダーインスタンス生成
        MyCalendar mc=new MyCalendar();
        //現在日時でカレンダーインスタンス生成
        Calendar cal=Calendar.getInstance();
        //2つの引数が来ていたら
        if(args.length==2) {
            //最初の引数で年を設定
            cal.set(Calendar.YEAR, args[0]);
            //次の引数で月を設定
            cal.set(Calendar.MONTH, args[1]-1);
        }
        //マイカレンダーに年を設定
        mc.setYear(cal.get(Calendar.YEAR));

        //マイカレンダーに月の設定
        mc.setMonth(cal.get(Calendar.MONTH)+1);

        //カレンダーの日付の数
        int daysCount=cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        //その行数で配列を生成
        String[] date=new String[daysCount];


        for(int i=0;i<daysCount;i++) {
                    //カウンター変数と実際の日付の変換
                    int data= i+1;
                    //配列に日付を入れる
                    date[i]=String.valueOf(data);
        }

        //作成した2次元配列をマイカレンダーにセットする。
        mc.setDate(date);
        return mc;
    }
}
