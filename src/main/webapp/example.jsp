<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags" %>

<% String s = "hello world!"; %>

<template:generic>
    <jsp:attribute name="head">
        <title>Example</title>
        <link rel="stylesheet" href="./static/css/expense.css"/>
        <script type="text/javascript" src="./static/js/expense.js"></script>
    </jsp:attribute>

    <jsp:body>
        <h1>Hello world!</h1>
    </jsp:body>
</template:generic>
