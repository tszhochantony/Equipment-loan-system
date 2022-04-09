<%@page import="ict.bean.StudentList"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Reservation List</title>
    <link type="text/css" rel="stylesheet" href="menucss.css">
    <script>
        function confirmCorrect(theForm) {
            if (confirm("Please confirm all information is correct,\n You can't start next reservation before finish or br rejected this reservation")) {
                return true;
            } else {
                return false;
            }
        }
</script>
</head>
<body>
    <jsp:include page="StudentMenu.jsp" />
    <div style="padding:20px;">
        <h1>Your List </h1>
        <jsp:useBean id="list" scope="request" class="java.util.ArrayList<ict.bean.StudentList>" />
        <%
            if(list.size()>0){
                out.println("<table id='studentList'>");
                out.println("<tr><th>Equipmnet Type</th><th>available count</th><th>select</th></tr>");
                for (int count = 0; count < list.size(); count++) {
                    StudentList sc = list.get(count);
                    out.println(" <form method=post action=handleS>");
                    out.println("<input type=hidden name=action value=removeFromSlist>");
                    out.println("<input type=hidden name=type value="+sc.getEType()+">");
                    out.println("<input type=hidden name=count value="+sc.getCount()+">");
                    out.println("<tr>");
                    out.println("<td>" +sc.getEType() + "</td>");
                    out.println("<td>" +sc.getCount() + "</td>");
                     out.println("<td><input type=submit value=remove id='remove'/></td>");
                    out.println("</tr>");
                    out.println("</form>");
                }
                out.println("</table>");

                out.println("<form method='post' action='handleS' onsubmit='return confirmCorrect(this);'>");
                out.println("<input type='hidden' name='action' value='confirm' />");
                out.println("<input type='submit' id='sendReseration' value='Confirm and Send'/>");
            }
            else{
                out.println("<h1> Empty</h1>");
            }
        %>
    </form>
    </div>

</body>
</html>
