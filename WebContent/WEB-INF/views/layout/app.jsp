<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
    <title><c:out value="勤怠管理システム" /></title>
    <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">

    <style>
    @CHARSET "UTF-8";
    /*
    YUI 3.18.1 (build f7e7bcb)
    Copyright 2014 Yahoo! Inc. All rights reserved.
    Licensed under the BSD License.
    http://yuilibrary.com/license/
    */
    html{color:#000;background:#FFF}body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td{margin:0;padding:0}table{border-collapse:collapse;border-spacing:0}fieldset,img{border:0}address,caption,cite,code,dfn,em,strong,th,var{font-style:normal;font-weight:normal}ol,ul{list-style:none}caption,th{text-align:left}h1,h2,h3,h4,h5,h6{font-size:100%;font-weight:normal}q:before,q:after{content:''}abbr,acronym{border:0;font-variant:normal}sup{vertical-align:text-top}sub{vertical-align:text-bottom}input,textarea,select{font-family:inherit;font-size:inherit;font-weight:inherit;*font-size:100%}legend{color:#000}#yui3-css-stamp.cssreset{display:none}


    body {
        color: #333333;
        font-family: "Hiragino Kaku Gothic Pro",Meiryo,"MS PGothic",Helvetica,Arial,sans-serif;
    }

    #header {
        width: 100%;
        height: 70px;
        background-color: #333333;
    }

    #content {
        width: 94%;
        margin-top: 15px;
        padding-left: 3%;
    }

    h1 {
        font-size: 24px;
        display: inline;
    }


    h2 {
        font-size: 36px;
        margin-bottom: 15px;
    }

    li {
        margin-top: 10px;
        margin-bottom: 10px;
    }

    p {
        margin-top: 15px;
        margin-bottom: 15px;
    }

    a {
        text-decoration: none;
        color: #24738e;
    }

    table, tr, th, td {
        border: 1px solid #cccccc;
    }

    table {
        width: 100%;
        table-layout: fixed;
    }

    th {
        width: 26%;
        padding: 10px 2%;
    }

    td {
        width: 66%;
        padding: 10px 2%;
    }

    button {
        font-size: 14px;
        padding: 5px 10px;
    }

    #footer {
        text-align: center;
    }

    #flush_success {
        width: 100%;
        padding-top: 28px;
        padding-left: 2%;
        padding-bottom: 28px;
        margin-bottom: 15px;
        color: #155724;
        background-color: #d4edda;
    }

    #flush_error {
        width: 100%;
        padding-top: 28px;
        padding-left: 2%;
        padding-bottom: 28px;
        margin-bottom: 15px;
        color: #721c24;
        background-color: #f8d7da;
    }

    table#employee_list th {
        width: 30%;
        padding: 10px 2%;
    }

    table#employee_list td {
        width: 29%;
        padding: 10px 2%;
    }

    tr.row1 {
            background-color: #f2f2f2;
    }
    tr.row0 {
            background-color: #ffffff;
    }

    select {
        height: 30px;
    }

    #header_menu {
        width: 57%;
        padding-top: 17px;
        padding-left: 3%;
        display: inline-block;
    }

    #employee_name {
        color: #cccccc;
        width: 36%;
        padding-right: 3%;
        text-align: right;
        display: inline-block;
    }

    #header a {
        color: #eeeeee;
    }

    h1 a {
        color: #eeeeee;
    }

    table#report_list th {
        font-weight: bold;
        padding: 10px 2%;
    }

    table#report_list .report_name {
        width: 20%;
        padding: 10px 2%;
    }
    table#report_list .report_date {
        width: 20%;
    }

    table#report_list .report_title {
        width: 37%;
    }

    table#report_list .report_action {
        width: 13%;
    }

    pre {
        font-family: "Hiragino Kaku Gothic Pro",Meiryo,"MS PGothic",Helvetica,Arial,sans-serif;
    }

    h3 {
        font-size: larger;
    }

    .clock{
        width: 100%;
        padding-top: 28px;
        padding-left: 2%;
        padding-bottom: 28px;
        margin-bottom: 15px;
        background-color: silver;
    }

    </style>
</head>
<body>
    <div id="wrapper">
        <div id="header">
            <div id="header_menu">
                <h1><a href="<c:url value='/?action=Attendance&command=index' />">勤怠管理システム</a></h1>&nbsp;&nbsp;&nbsp;
                <c:if test="${sessionScope.login_employee != null}">
                    <c:if test="${sessionScope.login_employee.adminFlag == 1}">
                        <a href="<c:url value='?action=Employee&command=index' />">従業員管理</a>&nbsp;
                    </c:if>
                </c:if>
            </div>
            <c:if test="${sessionScope.login_employee != null}">
                <div id="employee_name">
                    <c:out value="${sessionScope.login_employee.name}" />
                    &nbsp;さん&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value='?action=Auth&command=logout' />">ログアウト</a>
                </div>
            </c:if>
        </div>
        <div id="content">${param.content}</div>
        <div id="footer">by Kazuma Koike.</div>
    </div>

</body>
</html>