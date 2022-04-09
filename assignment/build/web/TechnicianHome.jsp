<%@page import="ict.bean.EquipmentList"%>
<%@page import="ict.bean.User"%>
<%@page import="java.util.Calendar"%>
<%@page import="ict.bean.CheckOutRecord"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Home Page</title>
    <link type="text/css" rel="stylesheet" href="menucss.css">
        <style>
.split {
  top:200px;
  position: fixed;
  bottom: 0;
}

.left {
  left: 0;
  width: 60%;
  margin-left: 30px;
}

.right {
  right: 0;
  width: 30%;
  margin-right: 30px;
}
</style>
<script>
            function showE(rId) {
                location="handleT?action=Technician&rId="+rId;
        }
</script>
    </head>
    <body>
        <jsp:include page="TechnicianMenu.jsp" />
        <div style="padding:20px;margin-top:30px;">
        <jsp:useBean id="rMessage" scope="request" class="ict.bean.User" />
        <jsp:useBean id="cor" scope="request" class="java.util.ArrayList<ict.bean.CheckOutRecord>" />
        <jsp:useBean id="el" scope="request" class="java.util.ArrayList<ict.bean.EquipmentList>" />
        <jsp:useBean id="u" scope="request" class="java.util.ArrayList<ict.bean.User>" />
        <%
            String mes = "";
            String mes2="";
            if(rMessage.getMessage()!=null){
                    mes = rMessage.getMessage();
            }
            if(cor.size()!=0){
                mes2 = "Some reservation was overdue.Please check and content related student";
                out.println("<script type=\"text/javascript\">");
                out.println("alert('"+mes2+"');");
                out.println("</script>");          
            }
           
        %>
        <p id="message"><%=mes2%></p>
        <p id="message"><%=mes%></p>
        <%
                if(cor.size()>0){
                out.println("<div class='split left'>");
                out.println("<table id='reservationList'>");
                out.println("<tr><th>Reservation ID</th><th>Equipment List</th><th>Student ID</th><th>Student Email</th><th>Student Name</th><th>Handled Date</th><th>Excepted Return Date</th></tr>");
                for (int count = 0; count < cor.size(); count++) {
                    CheckOutRecord checkR = cor.get(count);
                    User user = u.get(count);
                    out.println("<tr>");
                    out.println("<td>" +checkR.getRId() + "</td>");
                    out.println("<td><button onclick=showE('"+checkR.getRId()+"')>view detail</button></td>");
                    out.println("<td>" +checkR.getSId() + "</td>");
                    out.println("<td>" +user.getUserEmail() + "</td>");
                    out.println("<td>" +user.getUsername() + "</td>");
                    out.println("<td>" +checkR.getHanDate() + "</td>");
                    Calendar c = Calendar.getInstance(); 
                    c.setTime(checkR.getHanDate()); 
                    c.add(Calendar.DATE, 14);
                    java.sql.Date returnDate= new java.sql.Date(c.getTimeInMillis());
                    out.println("<td>" +returnDate + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</div>");
                out.println("<div class='split right'>");
                if(el.size()>=1){
                out.println("<table id='equipmentList'>");
                out.println("<tr><th>Equipment</th><th>Count</th></tr>");
                for (int count = 0; count < el.size(); count++) {
                    EquipmentList eList = el.get(count);
                    out.println(" <form method=post action=handleT>");
                    out.println("<tr>");
                    out.println("<td>" +eList.getEquipment() + "</td>");
                    out.println("<td>" +eList.getCount() + "</td>");
                    out.println("</tr>");
                    out.println("</form>");
                }
                out.println("</table>");
                }
                out.println("</div>");
            }
        %>
    </div>
    </body>
</html>
