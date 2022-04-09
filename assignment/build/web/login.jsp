<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>login</title>
    </head>
    <style>
body {
  font-family: Arial;
  color: black;
}

.split {
  height: 100%;
  position: fixed;
  top:0;
  bottom: 0;
}

.left {
  left: 0;
  width: 60%;
}

.right {
  right: 0;
  width: 40%;
}


#title {
  width:500px;
  margin: 10%;
}

form{
    margin: 10%;
}
p{
    font-size: 25px;
    margin-left: 30px;
}

.radio{
    margin-left: 30px;
}
</style>
</head>
<body>

<div class="split left">
    <img src="img/loginBG.jpg" style="width:100%;height:100%;"/>
</div>

<div class="split right">
      <img id="title" src="img/logo.png"/>
    <p>Welcome to IVPET borrow system,Please login</p>
    <form method="post" action="main">
            <input type="hidden" name="action" value="authenticate" />
            <input type="radio" name="userType" class="radio" value="Student"/>Student
            <input type="radio" name="userType" class="radio" value="Technician"/>Technician
            <input type="radio" name="userType" class="radio" value="SeniorTechnician"/>Senior Technician
            <table border="0">
                <tr>
                    <td><p align="right"><b>Email:</b></p></td>
                    <td><input type="text" name="email"  size="30" /></td>
                </tr>
                <tr>
                    <td><p align="right"><b>password</b></p></td>
                    <td><input type="password" name="password" size="30" /></td>
                </tr>
                <tr>
                    <td colspan="2"><p align="center"><input type="submit" value="Login" /></p></td>
                </tr>
            </table>
        </form>
</div>
    </body>
</html>
