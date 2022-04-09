<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Home Page</title>
    <link type="text/css" rel="stylesheet" href="menucss.css">
    <script>
        function finish(rId) {
            location="handleT?action=finish&rId=" + rId;
        }
    </script>
    </head>
    <body>
        <jsp:include page="TechnicianMenu.jsp" />
        <div style="padding:20px;margin-top:30px;">
            <h1>Return Equipment</h1>
        <jsp:useBean id="cor" scope="request" class="ict.bean.CheckOutRecord" />
        <%
            if(cor.getRId()!=null){
                out.println("<form method='post' action='handleT'>");
                out.println("Reservation Id: "+ cor.getRId());
                out.println("<br><input type='hidden' name='action' value='returnEquipmentID' />");
                out.println("<br><input type='hidden' name='rId' value='"+cor.getRId()+"' />");
                out.println("<br><input type='hidden' name='sId' value='"+cor.getSId()+"' />");
                out.println("<label for='eId'>Equipment Id: </label>");
                out.println("<input type='text' name='eId' id='eId'/>");             
                out.println("<input type='submit' class='greenButton' value='return'/>");
                out.println("</form>");
                out.println("<button onclick=finish('"+cor.getRId()+"');>finish</button>");
            }else{
                out.println("<form method='post' action='handleT'>");
                out.println("<input type='hidden' name='action' value='returnEquipment' />");
                out.println("<label for='rId'>Student Id: </label>");
                out.println("<input type='text' name='sId' id='sId'/>");             
                out.println("<input type='submit' class='greenButton' value='confirm'/>");
                out.println("</form>");
            }     
        %>
    </div>
    </body>
</html>
