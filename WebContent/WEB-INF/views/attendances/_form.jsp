<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>


<input type="hidden" name="year" value="${year}"/>
<input type="hidden" name="month" value="${month}"/>
<input type="hidden" name="date" value="${date}"/>


<label for="starting_time">出勤時刻</label><br />
<input type="text" name="starting_time" placeholder="hh:mm" value="${attendance.starting_time}" />
<br /><br />

<label for="quitting_time">退勤時刻</label><br />
<input type="text" name="quitting_time" placeholder="hh:mm" value="${attendance.quitting_time}" />
<br /><br />

<label for="content">備考</label><br />
<input type="text" name="content" value="${attendance.content}" />
<br /><br />

<input type="hidden" name="id" value="${attendance.id}" />
<input type="hidden" name="_token" value="${_token}" />

<button type="submit">登録</button>