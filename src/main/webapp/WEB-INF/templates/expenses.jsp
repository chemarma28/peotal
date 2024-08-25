<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %> 
<%@ page import="java.time.Month" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.novorock.jexpenses.view.to.ExpenseCardsGroupedByDate" %>
<%@ page import="com.novorock.jexpenses.domain.MonthlyExpense" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="j" uri="/WEB-INF/tlds/icons.tld" %>

<html>
<head>
    <meta charset="UTF-8"></meta>
    <title>Expenses</title>

    <link rel="stylesheet" href="./stylesheets/expense.index.css"/>
</head>
    
<body>
    <div id="skeleton">
        <header>
            <div class="office">
                <span>${currentMonthlyExpense.keeper.office.name}</span>
            </div>

            <div class="balance">
                <span>Balance: <b>${cmCurrencyFormatter.format(currentMonthlyExpense.balance)}</b></span>
            </div>

            <div>
                <button id="new-income-btn" class="btn">Income</button>
                <button class="btn toggle-modal" data-target="#new-card-wizard">New Expense</button>
            </div>
        </header>

        <main>
            <div class="scrollable">
                <div id="accordion" class="accordion">
                    <c:forEach var="group" items="${expenseCardGroups}">
                        <section data-key="${group.key.format(cmDateTimeFormatter)}">
                            <div class="accordion-header">
                                <j:svgIcon size="<%= 16 %>" name="chevron" />
                                <span>${group.key.format(cmDateTimeFormatter)}</span>
                            </div>

                            <div class="accordion-body">
                                <table>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>DESCRIPTION</th>
                                            <th>AMOUNT</th>
                                            <th>ACTION</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:set var="SECTION_TOTAL_AMOUNT" value="${ZERO}"/>

                                        <c:forEach var="card" varStatus="status" items="${group.value}">
                                            <tr>
                                                <td>${status.count}</td>
                                                <td>${card.description}</td>
                                                <td>${cmCurrencyFormatter.format(card.amount)}</td>
                                                <td class="action"><a>Delete</a></td>
                                            </tr>

                                            <c:set var="SECTION_TOTAL_AMOUNT" value="${SECTION_TOTAL_AMOUNT.add(card.amount)}"/>
                                        </c:forEach>
                                    </tbody>

                                    <tfoot>
                                        <tr>
                                            <td></td>
                                            <td>Total:</td>
                                            <td>${cmCurrencyFormatter.format(SECTION_TOTAL_AMOUNT)}</td>
                                            <td></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </section>
                    </c:forEach>
                </div>
            </div>
        </main>

        <aside>
            <table>
                <thead>
                    <tr>
                        <th>MONTHS</th>
                        <th>Amount</th>
                        <th>Income</th>
                    </tr>
                </thead>

                <tbody>
                    <c:set var="TOTAL_SPENT_AMOUNT" value="${ZERO}"/>
                    <c:set var="TOTAL_BALANCE" value="${ZERO}"/>

                    <c:forEach var="monthlyExpense" items="${monthlyExpenses}">
                        <c:set var="active" value=""/>

                        <c:if test="${monthlyExpense.monthDate.getMonth().equals(currentMonth)}">
                            <c:set var="active" value="active"/>
                        </c:if>

                        <tr class="${active}">
                            <td>${cmMonths[monthlyExpense.monthDate.getMonthValue() - 1]}</td>

                            <c:set var="TOTAL_SPENT_AMOUNT" value="${TOTAL_SPENT_AMOUNT.add(monthlyExpense.spentAmount)}" />
                            
                            <td>
                                <span class="month-decimal">${cmNumberFormatter.format(monthlyExpense.spentAmount)}</span>
                            </td>

                            <c:set var="TOTAL_BALANCE" value="${TOTAL_BALANCE.add(monthlyExpense.balance)}" />
                            
                            <td>
                                <span class="month-decimal">${cmNumberFormatter.format(monthlyExpense.balance)}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>

                <tfoot>
                    <tr>
                        <td>Total:</td>
                        <td>${cmNumberFormatter.format(TOTAL_SPENT_AMOUNT)}</td>
                        <td>${cmNumberFormatter.format(TOTAL_BALANCE)}</td>
                    </tr>
                </tfoot>
            </table>

            <div id="years">
                <div>
                    <span>Years</span>
                </div>

                <div class="btn-group">
                    <button>2021</button>
                    <button>2022</button>
                    <button>2023</button>
                    <button class="enabled">2024</button>
                </div>
            </div>
        </aside>
            
        <div id="new-card-wizard" class="modal">
            <div class="modal-overlay toggle-modal">
                <div class="modal-dialog">
                    <div class="modal-header">
                        <h2>New Expense Card</h2>
                    </div>
                
                    <div class="modal-body">
                        <form id="new-card-form" name="new-card-form" action="${pageContext.request.contextPath}/expense.jsp" method="POST" autocomplete="false">
                            <label>
                                <span class="label-text">Amount</span>
                                <input name="amount" class="md" type="text" required="true" placeholder="Amount" value="15"/>
                            </label>

                            <label>
                                <span class="label-text">Date</span>
                                <input name="date" class="md" type="date" required="true" placeholder="Date" value="2024-10-12"/>
                            </label>

                            <label>
                                <span class="label-text">Description</span>
                                <textarea name="description" rows="15" style="width: 100%" placeholder="Description"></textarea>
                            </label>
                        </form>
                    </div>
                
                    <div class="modal-footer">
                        <button class="btn toggle-modal">Cancel</button>
                        <input type="submit" class="btn" value="Save" form="new-card-form"></input>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>


<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<script type="module" src="./js/expense.index.js"></script>

</html>
