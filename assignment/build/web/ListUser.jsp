<%@page import="ict.bean.User"%>
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
        $('#userTable').DataTable();
    });

</script>

<script>
    function changeStatus(id,status) {
        location="handleST?action=deactivate&id=" +id+"&status="+status;
    }
    function editUser(id) {
        location="handleST?action=editUser&id="+id;
    }
</script>
</head>
<body>
    <jsp:include page="SeniorTechnicianMenu.jsp" />
     <div class="blur" id="blur">   
    <div id="bodyContent">
        <jsp:useBean id="users" scope="request" class="java.util.ArrayList<ict.bean.User>" />
        <table border="1" id="userTable" class="display" style="width:100%">
            <thead>
            <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Email</th>
                <th>name</th>
                <th>Status</th>
                <th>modify</th>
            </tr>
            </thead>
            <tbody>
        <%
            String status="deactivate";
            for (int count = 0; count < users.size(); count++) {
                User u = users.get(count);
                out.println("<tr>");
                out.println("<td><a href='handleST?action=userDetail&id="+u.getUserId()+"'>" +u.getUserId() + "</a></td>");
                out.println("<td>" +u.getUserType() + "</td>");
                out.println("<td>" +u.getUserEmail() + "</td>");
                out.println("<td>" +u.getUsername() + "</td>");
                out.println("<td>" +u.getUserStatus() + "</td>");
                if(u.getUserStatus().equalsIgnoreCase("Available")){
                    out.println("<td><button class=deactivate onClick=changeStatus('" + u.getUserId() + "','" + status + "')>deactivate</button>");
                }
                else{
                    status="available";
                    out.println("<td><button onClick=changeStatus('" + u.getUserId() + "','" + status + "')>activate</button>");
                }
                out.println("<button class='reset' onClick=editUser('" + u.getUserId() + "')>edit</button></td>");
                out.println("</tr>");
                out.println("</form>");
            }
        %>
        </tbody>
       </table>
        </div>
     </div>
</body>
</html>
