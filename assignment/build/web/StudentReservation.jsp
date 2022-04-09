<%@page import="ict.bean.EquipmentList"%>
<%@page import="ict.bean.CheckOutRecord"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Student Reservation</title>
    <link type="text/css" rel="stylesheet" href="menucss.css">
    <script>
        function approve(rId,judgement) {
            if(confirm("sure "+judgement+" ?")){
                location="handleT?action=approve&id=" +rId+"&judgement="+judgement;
            }
        }
</script>
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
    <jsp:include page="TechnicianMenu.jsp" />
    <div style="padding:20px;">
        <h1>Reservation List </h1>
        <jsp:useBean id="cor" scope="request" class="java.util.ArrayList<ict.bean.CheckOutRecord>" />
        <jsp:useBean id="el" scope="request" class="java.util.ArrayList<ict.bean.EquipmentList>" />
        <%
            out.println("<div class='split left'>");
            out.println("<table id='reservationList'>");
            out.println("<tr><th>Reservation Id</th><th>Student Id</th><th>Equipment List</th><th>Reservation Date</th><th>Approve</th></tr>");
            for (int count = 0; count < cor.size(); count++) {
                CheckOutRecord checkR = cor.get(count);
                out.println(" <form method=post action=handleT>");
                out.println("<input type=hidden name=action value=reservationList>");
                out.println("<input type=hidden name=rId value="+checkR.getRId()+">");
                out.println("<tr>");
                out.println("<td>" +checkR.getRId() + "</td>");
                out.println("<td>" +checkR.getSId() + "</td>");
                out.println("<td><input type=submit value=viewDetail class='greenButton'/></td>");
                out.println("<td>" +checkR.getResDate() + "</td>");
                out.println("</form>");
                 out.println("<td><button onClick=approve('" + checkR.getRId() + "','borrowed')>accept</button>"
                         + "<button class=deactivate onClick=approve('" + checkR.getRId() + "','rejected')>reject</button></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</div>");
            out.println("<div class='split right'>");
            if(el.size()>=1){
            out.println("<table id='equipmentList'>");
            out.println("<tr><th>EquipmentList Id</th><th>Equipment</th><th>Count</th></tr>");
            for (int count = 0; count < el.size(); count++) {
                EquipmentList eList = el.get(count);
                out.println(" <form method=post action=handleT>");
                out.println("<tr>");
                out.println("<td>" +eList.getRId() + "</td>");
                out.println("<td>" +eList.getEquipment() + "</td>");
                out.println("<td>" +eList.getCount() + "</td>");
                out.println("</tr>");
                out.println("</form>");
            }
            out.println("</table>");
            }
            out.println("</div>");
        %>
    </div>

</body>
</html>
