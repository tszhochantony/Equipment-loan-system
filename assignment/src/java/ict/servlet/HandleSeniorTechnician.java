/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;
import ict.bean.CheckOutRecord;
import ict.bean.EquipmentList;
import ict.bean.Report;
import ict.bean.User;
import ict.db.CheckOutRecordDB;
import ict.db.EquipmentListDB;
import ict.db.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "HandleSeniorTechnician", urlPatterns = {"/handleST"})
public class HandleSeniorTechnician extends HttpServlet {
    private UserDB udb;
    private CheckOutRecordDB crdb;
    private EquipmentListDB eldb;
    
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        crdb = new CheckOutRecordDB(dbUrl, dbUser, dbPassword);
        udb = new UserDB(dbUrl, dbUser, dbPassword);
        eldb = new EquipmentListDB(dbUrl, dbUser, dbPassword);
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String stId = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            User myBean = (User) request.getSession().getAttribute("user");
            stId = myBean.getUserId();
        }
        if ("SeniorTechnician".equals(action)) {
            String year = request.getParameter("year");
            String[] month = request.getParameterValues("chooseMonth");
            if(month==null){
                month = new String[0];
            }
            
            String[] SelectedEquipment = request.getParameterValues("chooseEquipment");
            ArrayList<String> years = crdb.queryRecordYear();
            if(year==null){
                year=years.get(0);
            }
            ArrayList<String> equipments = crdb.queryEquipment(year);
            ArrayList<Report> ar = new ArrayList<Report>();
            for(int i=0;i<equipments.size();i++){
                if(SelectedEquipment!=null){
                    for(int z=0;z<SelectedEquipment.length;z++){
                        String equip1 = equipments.get(i);
                        String equip2 = SelectedEquipment[z];
                        if(equip1.equals(equip2)){
                            Report r = crdb.queryEquipmentCountEachMonth(equipments.get(i),year);
                            ar.add(r);
                            break;
                        }
                    }
                }else{
                   Report r = crdb.queryEquipmentCountEachMonth(equipments.get(i),year);
                   ar.add(r); 
                }
                
            }
            request.setAttribute("ar", ar);
            request.setAttribute("y", years);
            request.setAttribute("ty", year );
            request.setAttribute("e", equipments);
            request.setAttribute("chooseM", month);
            RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/SeniorTechnicianHome.jsp");
            rd.forward(request, response);
        }else if ("addUser".equalsIgnoreCase(action)) {
            String type = request.getParameter("type");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            udb.addUser(type,email,name,password);
            response.sendRedirect("handleST?action=SeniorTechnician");
        }else if ("list".equalsIgnoreCase(action)) {    
            ArrayList<User> users = udb.queryUser(stId);
            request.setAttribute("users", users);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/ListUser.jsp");
            rd.forward(request, response);
        }else if ("editUser".equalsIgnoreCase(action)) {    
            String uId = request.getParameter("id");
            User user = udb.queryTargetUser(uId);
            request.setAttribute("user", user);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/EditUser.jsp");
            rd.forward(request, response);
        }else if ("updateUser".equalsIgnoreCase(action)) {    
            String uId = request.getParameter("id");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            udb.updateUser(uId, name, email, password);
            response.sendRedirect("handleST?action=list");
        }else if ("userDetail".equalsIgnoreCase(action)) {    
            String uId = request.getParameter("id");
            String rId = request.getParameter("rId");
            User user = udb.queryTargetUser(uId);     
            ArrayList<CheckOutRecord> cor = crdb.queryStudentReservation(uId);
            ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
            if(rId!=null){
                 el = eldb.queryEquipmentListById(rId);
            }
            request.setAttribute("user", user);
            request.setAttribute("cor", cor);
            request.setAttribute("el", el);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/UserDetail.jsp");
            rd.forward(request, response);
        }else if ("deactivate".equalsIgnoreCase(action)) {    
            String id = request.getParameter("id");
            String status = request.getParameter("status");
            if(udb.deactivateUser(id,status)){
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('update Successful!');");
                out.println("location='handleST?action=list';");
                out.println("</script>");
            }           
        }else {
            PrintWriter out = response.getWriter();
            out.println("No such action!!!");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
