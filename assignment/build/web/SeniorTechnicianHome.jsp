<%@page import="ict.bean.Report"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE HTML >
<html>
    <head>
        <title>Home Page</title>
        <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
    <link type="text/css" rel="stylesheet" href="menucss.css">
    <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
    <script type="text/javascript">
    
      $(document).ready(function(){
        $(".year").click(function(){
          location.href = "handleST?action=SeniorTechnician&year=" + $(this).val();
        });
      });
  </script>
</head>
<body>
    
    <jsp:include page="SeniorTechnicianMenu.jsp" />
    <div class="blur" id="blur">    
        <div style="padding:20px;margin-top:30px;">
                <div id="chart" style="width: 100%"></div>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<jsp:useBean id="ar" scope="request" class="java.util.ArrayList<ict.bean.Report>" />
<jsp:useBean id="chooseM" scope="request" class="java.lang.String[]" />
<script type="text/javascript">
  google.charts.load('current', {'packages':['bar']});
  google.charts.setOnLoadCallback(drawChart);
  var titleList =[];
  var array = [];
  var jan = [];
  var feb = [];
  var mar = [];
  var apr = [];
  var may = [];
  var jun = [];
  var jul = [];
  var aug = [];
  var sep = [];
  var oct = [];
  var nov = [];
  var dec = [];
  var month = [jan,feb,mar,apr,may,jun,jul,aug,sep,oct, nov, dec];
  function init(){
    titleList =['Month'];
    array = [titleList];
    jan = ['Jananry'];
    feb = ['February'];
    mar = ['March'];
    apr = ['April'];
    may = ['May'];
    jun = ['June'];
    jul = ['July'];
    aug = ['August'];
    sep = ['September'];
    oct = ['October'];
    nov = ['November'];
    dec = ['December'];
  }
  borrowRecord();
  function drawChart() {
  var data = google.visualization.arrayToDataTable(array);
    
  var options = {
          chart: {
            title: 'Equipment borrowed report',
          },
          bars: 'horizontal', // Required for Material Bar Charts.
          hAxis: {format: 'decimal'},
          height:600,
          isStacked: true,
          seriesType: "bars",
          series: {
             0: {color: '#43459d' },
             1: {color: '#e7711b' },
             2: { color: '#f1ca3a' },
             3: { color: '#6f9654' },
             4: { color: '#1c91c0' },
             5: { color: '#e2431e' },
           }, 
           axes: {
            x: {
              0: { side: 'bottom', label: 'number of borrow'} // Top x-axis.
            },
            animation:{
              duration: 1000,
              easing: 'out',
            },
          },
        };

    var chart = new google.charts.Bar(document.getElementById('chart'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
  }
    function borrowRecord(){
      <%
        ArrayList<String> monthName = new ArrayList<String>();
        monthName.add("jan"); 
        monthName.add("feb");  
        monthName.add("mar");  
        monthName.add("apr");  
        monthName.add("may");  
        monthName.add("jun");  
        monthName.add("jul");  
        monthName.add("aug");  
        monthName.add("sep");  
        monthName.add("oct");  
        monthName.add("nov");
        monthName.add("dec");  
        boolean include=false;
        int index = 0;
        out.println("init();");
        for(int a=0;a<ar.size();a++){
            Report r = ar.get(a);
            ArrayList<Integer> months = r.getMonth();
            ArrayList<Integer> counts = r.getEquipmenCount();
            String equipment = r.getEquipmentType();
            out.println("titleList.push('"+equipment+"');");
            for(int y=0;y<12;y++){
                include=false;
                String thisMonth = monthName.get(y);
                for(int x=0;x<months.size();x++){
                    if(months.get(x)==y){
                        include=true;
                        index=x;
                        break;
                    }
                }
                if(a==0){
                    if(chooseM.length>0){
                        for(int n=0;n<chooseM.length;n++){
                            String abc = chooseM[n];
                            if(chooseM[n].equals(thisMonth)){
                                out.println("array.push("+thisMonth+");");
                                n=13;
                            }
                        }
                    }
                    else{
                        out.println("array.push("+thisMonth+");");
                    }            
                }
                if(include){
                    out.println(thisMonth+".push("+counts.get(index)+");");
                }
                else{
                    out.println(thisMonth+".push(0);");
                }
            }
        }
        out.println("drawChart();");
      %>
              
}
</script>
        
        <jsp:useBean id="y" scope="request" class="java.util.ArrayList<String>" />
        <%
            for(int z=0;z<y.size();z++){
                out.println("<button name='year' class='year' value='"+y.get(z)+"'>"+y.get(z)+"</button>");
            }
        %>
        <br>
        <jsp:useBean id="ty" scope="request" class="String" />
        <form method="post" action="handleST">
            <input type="hidden" name="action" value="SeniorTechnician" />
            <input type="hidden" name="year" value="<%=ty%>" />
            <input type="checkbox" name="chooseMonth" value="jan">Jananry</input>
            <input type="checkbox" name="chooseMonth" value="feb">February</input>
            <input type="checkbox" name="chooseMonth" value="mar">March</input>
            <input type="checkbox" name="chooseMonth" value="apr">April</input>
            <input type="checkbox" name="chooseMonth" value="may">May</input>
            <input type="checkbox" name="chooseMonth" value="jun">June</input>
            <input type="checkbox" name="chooseMonth" value="jul">July</input>
            <input type="checkbox" name="chooseMonth" value="aug">August</input>
            <input type="checkbox" name="chooseMonth" value="sep">September</input>
            <input type="checkbox" name="chooseMonth" value="oct">October</input>
            <input type="checkbox" name="chooseMonth" value="nov">November</input>
            <input type="checkbox" name="chooseMonth" value="dec">December</input>
            <br>
            <jsp:useBean id="e" scope="request" class="java.util.ArrayList<String>" />
            <%
            for(int x=0;x<e.size();x++){
                out.println("<input type='checkbox' name='chooseEquipment' value='"+e.get(x)+"'>"+e.get(x)+"</input>");
            }
            %>
            <br><input type="submit" class="greenButton" value="search"/></p></td>
        </form>
        
        </div>
    </div>
</body>
</html>
