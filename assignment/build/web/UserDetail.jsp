<%@page import="ict.bean.EquipmentList"%>
<%@page import="ict.bean.CheckOutRecord"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Reservation List</title>
    <link type="text/css" rel="stylesheet" href="js/datatables.min.css">
<link type="text/css" rel="stylesheet" href="menucss.css">
<script type="text/javascript" src="js/jquery-3.5.1.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        $('#reservationTable').DataTable();
    });

</script>
    <script>
        function showE(rId,id) {
                location="handleST?action=userDetail&rId="+rId+"&id="+id;
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
    <jsp:include page="SeniorTechnicianMenu.jsp" />
    <jsp:useBean id="user" scope="request" class="ict.bean.User" />
    <div style="padding:20px;">
        <h1>Student Detail</h1>
        <jsp:useBean id="cor" scope="request" class="java.util.ArrayList<ict.bean.CheckOutRecord>" />
        <jsp:useBean id="el" scope="request" class="java.util.ArrayList<ict.bean.EquipmentList>" />
        <div class='split left'>
            <h1>ID: <%=user.getUserId()%></h1><br>  <h1>Name:<%=user.getUsername()%></h1><br><h1>  Email: <%=user.getUserEmail()%></h1>
            <table border="1" id="reservationTable" class="display">
            <thead>
            <tr>
                <th>Reservation Id</th>
                <th>Student Id</th>
                <th>Equipment List</th>
                <th>Reservation Date</th>
                <th>Status</th>
                <th>Technician ID</th>
                <th>Handle Date</th>
                <th>Return Date</th>
            </tr>
            </thead>
            <tbody>
        <%
            for (int count = 0; count < cor.size(); count++) {
                CheckOutRecord checkR = cor.get(count);
                out.println("<tr>");
                out.println("<td>" +checkR.getRId() + "</td>");
                out.println("<td>" +checkR.getSId() + "</td>");
                out.println("<td><button onclick=showE('"+checkR.getRId()+"','"+checkR.getSId()+"')>view detail</button></td>");
                out.println("<td>" +checkR.getResDate() + "</td>");
                out.println("<td>" +checkR.getStatus() + "</td>");
                out.println("<td>" +checkR.getTId() + "</td>");
                out.println("<td>" +checkR.getHanDate() + "</td>");
                out.println("<td>" +checkR.getRetDate() + "</td>");
                out.println("</tr>");
            }
        %>
            </tbody>
            </table>
            </div>
        <div class="split right">
        <%
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
        %>
         </div>      
    </div>

</body>
</html>
