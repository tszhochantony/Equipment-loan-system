<ul>
    <li>  <a href="handleST?action=SeniorTechnician" ><img src="img/logo.png" style="width:200px;padding: 0"/></a><li>
    <li>  <a id="addUser"><p>Add User</p></a></li>
    <li>  <a href="handleST?action=list" ><p>User Management</p></a></li>
    <li>  <a href="main?action=logout" ><p>log out</p></a></li>
</ul>
<script type="text/javascript">
      $(document).ready(function(){
        $("#form").hide();
        $("#addUser").click(function(){ 
            $("#form").show();
            document.getElementById('blur').style.filter = 'blur(10px)';
        });
        $("#close").click(function(){ 
            $("#form").hide();
            document.getElementById('blur').style.filter = 'none';
        });
    });
</script>
<script>
        function confirmCorrect(theForm) {
            if (confirm("Please confirm all information is correct")) {
                return true;
            } else {
                return false;
            }
        
        }
</script>
<fieldset id="form">
        <button id="close"></button>         
        <form  action="handleST" onsubmit="return confirmCorrect(this);">
            <input type="hidden" name="action" value="addUser"/>
            <h1 id="formId">Register New User</h1>
            <table id="formRes">
              <tr>
                <th class="formInput">User Type:</th>
                <td><input type="radio" name="type" value="Student" checked>
                    <label for="Student">Student</label>
                    <input type="radio" name="type" value="Technician">
                    <label for="Technician">Technician</label>
                    <input type="radio" name="type" value="SeniorTechnician">
                    <label for="SeniorTechnician">SeniorTechnician</label>
                </td>
              </tr>
              <tr>
                <th class="formInput">Name:</th>
                <td><input type="text" name="name" required="required"></td>
              </tr>
              <tr>
                <th class="formInput">Email:</th>
                <td><input type="email" name="email"required="required"></td>
              </tr>
              <tr>
                <th class="formInput">password:</th>
                <td><input type="password" name="password" required="required"></td>
              </tr>
          </table>
          <button type="submit" id="signup">Sign Up</button>
          <button type="reset" class="reset">Reset</button>
        </form>
    </fieldset>