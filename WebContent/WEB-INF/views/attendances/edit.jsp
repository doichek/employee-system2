<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">

        <c:choose>
            <c:when test="${attendance != null}">
                <h2><c:out value="${year}" />年<c:out value="${month}" />月<c:out value="${date}" />日の勤怠情報入力(編集)ページ</h2>

                <form method="POST" action="<c:url value='?action=Attendance&command=update' />">
                    <c:import url="_form.jsp" />
                </form>

                <p><a href="<c:url value='?action=Attendance&command=index' />">一覧に戻る</a></p>
                <p><a href="#" onclick="confirmDestroy();">この勤怠情報を削除する</a></p>
                <form method="POST" action="<c:url value='?action=Attendance&command=destroy' />">
                    <input type="hidden" name="id" value="${attendance.id}" />
                    <input type="hidden" name="_token" value="${_token}" />
                </form>
                <script>
                function confirmDestroy() {
                    if(confirm("本当に削除してよろしいですか？")) {
                        document.forms[1].submit();
                    }
                }
                </script>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

    </c:param>
</c:import>