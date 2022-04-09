<%@page import="java.util.Calendar"%>
<%@page import="ict.bean.EquipmentList"%>
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
</head>
<body>
    <jsp:include page="StudentMenu.jsp" />
    <div style="padding:20px;margin-top:30px;">
        <jsp:useBean id="message" scope="request" class="ict.bean.User" />
        <jsp:useBean id="cor" scope="request" class="java.util.ArrayList<ict.bean.CheckOutRecord>" />
        <jsp:useBean id="el" scope="request" class="java.util.ArrayList<ict.bean.EquipmentList>" />
        <%
            String mes = "";
            String mes2="";
            if(cor.size()==0){
                mes = message.getMessage();
                mes2 = mes != "" ? "If you have any question,Please content Technicians." : "";
            }
            else{
                mes = "Your reservation was overdue.Please return as soon as possible";
                out.println("<script type=\"text/javascript\">");
                out.println("alert('"+mes+"');");
                out.println("</script>");          
            }
           
        %>
        <p id="message"><%=mes%><%=mes2%></p>
        <%
                if(cor.size()>0){
                out.println("<div class='split left'>");
                out.println("<table id='reservationList'>");
                out.println("<tr><th>Reservation Id</th><th>Handle Date</th><th>Excepted Return Date</th></tr>");
                CheckOutRecord checkR = cor.get(0);
                out.println("<tr>");
                out.println("<td>" +checkR.getRId() + "</td>");
                out.println("<td>" +checkR.getHanDate() + "</td>");
                Calendar c = Calendar.getInstance(); 
                c.setTime(checkR.getHanDate()); 
                c.add(Calendar.DATE, 14);
                java.sql.Date returnDate= new java.sql.Date(c.getTimeInMillis());
                out.println("<td>" +returnDate + "</td>");
                out.println("</tr>");
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
