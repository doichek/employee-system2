<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:choose>
          <c:when test="${attendance != null}">

            <h2>id : ${attendance.id} の勤怠情報詳細ページ</h2>

            <p>年：<c:out value="${attendance.year}" /></p>
            <p>月：<c:out value="${attendance.month}" /></p>
            <p>日：<c:out value="${attendance.date}" /></p>
            <p>出勤時間：<c:out value="${attendance.starting_time}" /></p>
            <p>退勤時間：<c:out value="${attendance.quitting_time}" /></p>
            <p>備考欄：<c:out value="${attendance.content}" /></p>
          </c:when>
          <c:otherwise>
            <h2>お探しのデータは見つかりませんでした。</h2>
           </c:otherwise>
        </c:choose>
        <p><a href="<c:url value='?action=Attendance&command=index' />">一覧に戻る</a></p>
        <p><a href="<c:url value='?action=Attendance&command=edit&id=${attendance.id}&date=${attendance.date}&month=${attendance.month}&year=${attendance.year}' />">この勤怠情報を編集する</a><p>



    </c:param>
</c:import>