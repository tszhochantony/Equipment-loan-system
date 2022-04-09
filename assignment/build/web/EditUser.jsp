<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Edit User</title>
    <link type="text/css" rel="stylesheet" href="menucss.css">
    <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
    </head>
    <body>
        <jsp:include page="SeniorTechnicianMenu.jsp" />
        <div class="blur" id="blur">
                <div style="padding:20px;margin-top:30px;">    
                <jsp:useBean id="user" scope="request" class="ict.bean.User" />
                <form  action="handleST" onsubmit="return confirmCorrect(this);">
                    <input type="hidden" name="action" value="UpdateUser"/>
                    <input type="hidden" name="id" value="<%=user.getUserId()%>"/>
                    <h1 id="formId">Edit User</h1>
                    <table>
                      <tr>
                        <th class="formInput">User Name:</th>
                        <td><input type="text" name="name" value="<%=user.getUsername()%>" required="required"></td>
                      </tr>
                      <tr>
                        <th class="formInput">Email:</th>
                        <td><input type="text" name="email" value="<%=user.getUserEmail()%>" required="required"></td>
                      </tr>
                      <tr>
                        <th class="formInput">password:</th>
                        <td><input type="password" name="password" value="<%=user.getPassword()%>" required="required"></td>
                      </tr>
                  </table>
                  <button type="submit" id="signup">Update</button>
                  <button type="reset" class="reset">Reset</button>
                </form>
            </div>
        </div>
    </body>
</html>
