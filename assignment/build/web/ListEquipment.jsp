<%@page import="ict.bean.Equipment"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Equipment List</title>
    <style>
div{
    margin:10px;
}
#bodyContent{
    width:90%;
    margin:auto;
}

</style>
<link type="text/css" rel="stylesheet" href="js/datatables.min.css">
<link type="text/css" rel="stylesheet" href="menucss.css">
<script type="text/javascript" src="js/jquery-3.5.1.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        $('#equipmentTable').DataTable();
    });

</script>

<script>
    function able(eId,status) {
        location="handleT?action=changeStatus&eId=" +eId+"&status="+status;
    }
</script>
</head>
<body>
    <jsp:include page="TechnicianMenu.jsp" />
    <div id="bodyContent">
        <jsp:useBean id="equipments" scope="request" class="java.util.ArrayList<ict.bean.Equipment>" />
        <table border="1" id="equipmentTable" class="display" style="width:100%">
            <thead>
            <tr>
                <th>Equipmnet ID</th>
                <th>Equipmnet Type</th>
                <th>Status</th>
                <th>Change</th>
            </tr>
            </thead>
            <tbody>
        <%
            for (int count = 0; count < equipments.size(); count++) {
                Equipment sc = equipments.get(count);
                out.println("<tr>");
                out.println("<td>" +sc.getEId() + "</td>");
                out.println("<td>" +sc.getEType() + "</td>");
                out.println("<td>" +sc.getEStatus() + "</td>");
                if(sc.getEStatus().equalsIgnoreCase("available")){
                    out.println("<td><button class='deactivate' onClick=able('" + sc.getEId() + "','deactivate')>deactivate</button></td>");
                }
                else if(sc.getEStatus().equalsIgnoreCase("deactivate")){
                    out.println("<td><button onClick=able('" + sc.getEId() + "','available')>activate</button></td>");
                }
                    
                else{out.println("<td></td>");}
                out.println("</tr>");
                out.println("</form>");
            }
        %>
        </tbody>
       </table>
        </div>
</body>
</html>
