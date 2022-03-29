<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<script>
    const clock = () => {
      // 現在の日時・時刻の情報を取得
      const d = new Date();

      // 年を取得
      let year = d.getFullYear();
      // 月を取得
      let month = d.getMonth() + 1;
      // 日を取得
      let date = d.getDate();
      // 曜日を取得
      let dayNum = d.getDay();
      const weekday = ["日", "月", "火", "水", "木", "金", "土"];
      let day = weekday[dayNum];
      // 時を取得
      let hour = d.getHours();
      // 分を取得
      let min = d.getMinutes();
      // 秒を取得
      let sec = d.getSeconds();

      // 1桁の場合は0を足して2桁に
      month = month < 10 ? "0" + month : month;
      date = date < 10 ? "0" + date : date;
      hour = hour < 10 ? "0" + hour : hour;
      min = min < 10 ? "0" + min : min;
      sec = sec < 10 ? "0" + sec : sec;

      // 日付・時刻の文字列を作成
      //let today = `${year}.${month}.${date} ${day}`;
      //let time = `${hour}:${min}:${sec}`;

      // 文字列を出力
      // document.querySelector(".clock-date").innerText = today;
      //document.querySelector(".clock-time").innerText = time;

      document.querySelector(".clock-year").innerText = year;
      document.querySelector(".clock-month").innerText = month;
      document.querySelector(".clock-date").innerText = date;
      document.querySelector(".clock-day").innerText = day;
      document.querySelector(".clock-hour").innerText = hour;
      document.querySelector(".clock-min").innerText = min;
      document.querySelector(".clock-sec").innerText = sec;

};
        // 1秒ごとにclock関数を呼び出す
        setInterval(clock, 1000);
</script>



<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <c:if test="${flush_error != null}">
            <div id="flush_error">
                <c:out value="${flush_error}"></c:out>
            </div>
        </c:if>

        <div class="clock">
            <p>
               <span class="clock-year"></span>年<span class="clock-month"></span>月<span class="clock-date"></span>日<span class="clock-day"></span>曜日
            </p>
            <p>
                <span class="clock-hour"></span>:<span class="clock-min"></span>:<span class="clock-sec"></span>
            </p>


            <form method="POST" action="<c:url value='?action=Attendance&command=${createOrUpdate}' />">
                <input type="hidden" name="_token" value="${_token}" />
                <button type="submit" name="shukkinn" value=0>出勤</button>
                <button type="submit" name="shukkinn" value=1>退勤</button>
            </form>
        </div>


        <h2>勤怠情報一覧</h2>

        <h1><c:out value="${mc.getYear()}" />年<c:out value="${mc.getMonth()}" />月のカレンダー</h1>

            <p>
               <a href="<c:url value='?action=Attendance&command=index&year=${mc.getYear()}&month=${mc.getMonth()-1}' />">前月</a>
               <a href="<c:url value='?action=Attendance&command=index&year=${mc.getYear()}&month=${mc.getMonth()+1}' />">翌月</a>
            </p>

        <table id="employee_list">
            <tbody>
                <tr>
                    <th>日にち</th>
                    <th>出勤時刻</th>
                    <th>退勤時刻</th>
                    <th>備考</th>
                </tr>

                <c:forEach var="date" items="${mc.getDate()}" varStatus="status">
                <tr class="row${status.count % 2}">
                    <c:set var="getDateMatch" value="0" />
                    <c:forEach var="attendance" items="${attendances}">
                      <c:if test="${date == attendance.date && mc.getYear() == attendance.year && mc.getMonth() == attendance.month}">
                            <td>
                                <a href="<c:url value='?action=Attendance&command=show&id=${attendance.id}' />">
                                    <c:out value="${date}" />日
                                </a>
                            </td>
                            <td>
                                <c:out value="${attendance.starting_time}" />
                            </td>
                            <td>
                                <c:out value="${attendance.quitting_time}" />
                            </td>
                            <td>
                                <c:out value="${attendance.content}" />
                            </td>
                            <c:set var="getDateMatch" value="1" />
                      </c:if>
                    </c:forEach>

                    <c:if test="${getDateMatch == 0}">
                        <td>
                            <a href="<c:url value='?action=Attendance&command=entryNew&date=${date}&month=${mc.getMonth()}&year=${mc.getYear()}' />">
                            <c:out value="${date}" />日
                            </a>
                            </td>
                            <td>
                            </td>
                            <td>
                            </td>
                            <td>
                            </td>
                    </c:if>
                </tr>
                </c:forEach>
            </tbody>
        </table>

    </c:param>
</c:import>