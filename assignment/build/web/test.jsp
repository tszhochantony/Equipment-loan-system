<%@page import="java.util.ArrayList"%>
<%@page import="ict.bean.Report"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>sale Report</title>
    <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
    <script type="text/javascript">
    
      $(document).ready(function(){
        $("#single").hide();
        $("#more").hide();
        $("#oneMore").click(function(){ 
          $("#single").hide();
          $("#more").show();
        });
        $("#oneOnly").click(function(){ 
          $("#single").show();
          $("#more").hide();
        });
        $("#year2020").click(function(){ 
          $("#selection").show();
        });
        $(".year").click(function(){ 
          $("#selection").hide();
        });
      });
  </script>
  <script>
      function search(){
    var multi = document.getElementsByName('multi');
    if(multi[0].checked==true){
      var agent = document.getElementsByName('agent');
    }
    if(multi[1].checked==true){
      var agent = document.getElementsByName('agents');
    }
    if( agent[0].checked==true){
      addKenji();
    }
    if(agent[1].checked==true){
      addFung();
    }
    if(agent[2].checked==true){
      addJohn();
    }
    drawChart();
  }
      
  </script>
</head>
<body>
    <h2 id="titleHead">Sale Report</h2>

    <div id="chart" style="width: 90%;"></div>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<jsp:useBean id="ar" scope="request" class="java.util.ArrayList<ict.bean.Report>" />
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
            title: 'Eqiupment borrowed report',
          },
          bars: 'horizontal', // Required for Material Bar Charts.
          hAxis: {format: 'decimal'},
          height:400,
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
              0: { side: 'bottom', label: 'Number of borrow'} // Top x-axis.
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
        checkMonth();
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
                    out.println("if(2>3){}");
                    //out.println("array.push("+thisMonth+");");
                    out.println("array.push("+thisMonth+");");
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
</br>
<div id="chartYear">
  Show total sales of year:
  
<button class="year" onclick="total2018()" value="2018">2018</button>
<button class="year" onclick="total2019()" value="2018">2019</button>
<button id="year2020" onclick="total2020()"> 2020</button>
</div>
</br>
</br>
<div id="selection">
  Select following option to search:</br></br>
<input type="checkbox" class="chooseMonth" value="1"checked="checked">Jananry</input>
<input type="checkbox" class="chooseMonth" value="2"checked="checked">February</input>
<input type="checkbox" class="chooseMonth" value="3"checked="checked">March</input>
<input type="checkbox" class="chooseMonth" value="4"checked="checked">April</input>
<input type="checkbox" class="chooseMonth" value="5"checked="checked">May</input>
<input type="checkbox" class="chooseMonth" value="6"checked="checked">June</input>
<input type="checkbox" class="chooseMonth" value="1"checked="checked">July</input>
<input type="checkbox" class="chooseMonth" value="2"checked="checked">August</input>
<input type="checkbox" class="chooseMonth" value="3"checked="checked">September</input>
<input type="checkbox" class="chooseMonth" value="4"checked="checked">October</input>
<input type="checkbox" class="chooseMonth" value="5"checked="checked">November</input>
</br>
</br>
<input type="radio" id="oneOnly" name="multi" value="0">single agent</input>
<input type="radio" id="oneMore" name="multi" value="1">multi agent</input>
</br>
</br>
<div id="single">
  <input type="radio" name="agent">Wu Chi Ho</input>
  <input type="radio" name="agent">Chan Ho Chi</input>
  <input type="radio" name="agent">Lo Dai Wai</input>
</div>
<div id="more">
  <input type="checkbox" name="agents">Wu Chi Ho</input>
  <input type="checkbox" name="agents">Chan Ho Chi</input>
  <input type="checkbox" name="agents">Lo Dai Wai</input>
</div>
</br>
<button onclick="search()" class="yesBtn">search</button>
</div>
</body>
</html>