<%@page import="ict.bean.Equipment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.db.EquipmentDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Equipment Reservation</title>
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
    function AddtoList(type) {
        var count = document.getElementById(type).value;
        location="handleS?action=addToList&type="+type+"&number="+count;
    }
</script>
</head>
<body>
    <jsp:include page="StudentMenu.jsp" />
    <div id="bodyContent">
        <h1>Please select:(*each Equipment can only borrow up to 5)</h1>
        <jsp:useBean id="types" scope="request" class="java.util.ArrayList<ict.bean.Equipment>" />
        <table border="1" id="equipmentTable" class="display" style="width:100%">
            <thead>
            <tr class="trbg" >
                <th >Equipmnet Type</th>
                <th>Available Count</th>
                <th>Count</th>
                <th>Add</th>
            </tr>
            </thead>
            <tbody>
        <%
            for (int count = 0; count < types.size(); count++) {
                Equipment e = types.get(count);
                out.println("<tr>");
                out.println("<td>" +e.getEType() + "</td>");
                out.println("<td>" +e.getCount() + "</td>");
                out.println("<td><select name=number class='selectChoice' id=" + e.getEType() + ">");
                for(int number=1;number<=e.getCount();number++){
                    out.println("<option value="+ number + ">"+number+"</option>");
                }
                out.println("</select></td>");
                out.println("<td><button onClick=AddtoList('" + e.getEType() + "')>add to list</button></td>");
                out.println("</tr>");
            }
        %>
        </tbody>
       </table>
        </div>
</body>
</html>
