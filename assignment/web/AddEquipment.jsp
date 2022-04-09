<%@page import="ict.bean.Equipment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.db.EquipmentDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>modifiy Equipment</title>
        <link type="text/css" rel="stylesheet" href="menucss.css">
        <style>
            #background {
                width: 100%;
                position: fixed;
                top:85px;
                bottom: 0;
                background-image: url("img/addEquip.jpg");
                background-size: 100% 100%;
            }
            #addEquiForm{
                left:0;
                margin-left: 50px;
                width:550px;
                color: rgb(49, 43, 45);
                font-size: 25px;
                background-color: #87C6F1;
                padding:10px;
            }
            h1{
                background-color: rgb(40, 204, 158);
                color:white;
                margin-left: 30px;
                width:600px;
            }
            #selectChoice{
    width: 150px;
    height:40px;
    font-size: 20px;
}
        </style>
        <script>
            function able() {
                var type = document.getElementById("selectChoice").value;
                if(type=="other"){document.getElementById("other").hidden = false;}
                else{document.getElementById("other").hidden = true;}
            }
        </script>
        <script>
        function myFunction() {
            var type = document.getElementById("selectChoice").value;
            if(type=="other"){type=document.getElementById("other").value;}
            var quantity = document.getElementById("quantity").value;
            var statusList = document.getElementsByName('status');
            var status;
            for (var i=0; i<statusList.length; i++) {
				if (statusList[i].checked) {
                                    status=statusList[i].value;
				  }
				}
            if(confirm("\
                        Plese confirm following information is correct:\n\
                            \n\
                        Equipment Type :"+type+"\n\
                        Initial Status :"+ status+"\n\
                        Quantity :"+quantity+"\n\
            ")){
                location="handleT?action=add&eType="+type+"&status="+status+"&quantity="+quantity;
            }else{
                
            }
        }
        </script>
    </head>
    <body>
        <jsp:include page="TechnicianMenu.jsp" />    
        <div id="background">
                     <h1>Add Equipment</h1>
        <div id="addEquiForm" >    
        Equipment Type:
        <select id="selectChoice" onchange="able()">
        <jsp:useBean id="equipmentType" scope="request" class="java.util.ArrayList<ict.bean.Equipment>" />
        <%@ taglib  uri="/WEB-INF/tlds/EquipmentBeanTag.tld" prefix="eb" %>
        <eb:showEquipment equipmentBean="<%=equipmentType%>"  />       
        <option name="type" value="other" >Other</option>
        </select>
        <input type="text" id="other" name="other" hidden><br><br>
        Initial Status:<br>
       <input type="radio" id="available" name="status" value="available" checked>
       <label for="available">available</label>
       <input type="radio" id="deactivate" name="status" value="deactivate">
       <label for="deactivate">deactivate</label><br><br>
       Quantity: <input name="quantity" id="quantity" type="range" value="1" min="0" max="50" oninput="this.nextElementSibling.value = this.value"/><output>1</output><p><br />
       <button onclick="myFunction()">Add</button>
       </div>
</div>
    </body>
</html>
