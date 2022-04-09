/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;
import ict.bean.CheckOutRecord;
import ict.bean.Equipment;
import ict.bean.EquipmentList;
import ict.bean.User;
import ict.db.CheckOutRecordDB;
import ict.db.EquipmentDB;
import ict.db.EquipmentListDB;
import ict.db.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "HandleTechnician", urlPatterns = {"/handleT"})
public class HandleTechnician extends HttpServlet {
    private EquipmentDB edb;
    private CheckOutRecordDB crdb;
    private EquipmentListDB eldb;
    private UserDB udb;
    
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");       
        edb = new EquipmentDB(dbUrl, dbUser, dbPassword);
        crdb = new CheckOutRecordDB(dbUrl, dbUser, dbPassword);
        eldb = new EquipmentListDB(dbUrl, dbUser, dbPassword);
        udb = new UserDB(dbUrl, dbUser, dbPassword);
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
            throws ServletException, IOException, SQLException {
        String action = request.getParameter("action");
        String tId = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            User myBean = (User) request.getSession().getAttribute("user");
            tId = myBean.getUserId();
        }
        if ("Technician".equals(action)) {  
            String rId = request.getParameter("rId");
            ArrayList<CheckOutRecord> cor = crdb.queryPendingReservation();
            ArrayList<CheckOutRecord> cor2 = crdb.queryAllOverdueReservation();
            ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
            ArrayList<User> u = new ArrayList<User>();
                for(int count=0;count<cor2.size();count++){
                    CheckOutRecord c = cor2.get(count);
                    u.add(udb.queryTargetUser(c.getSId()));
                }
                if(rId!=null){
                    el = eldb.queryEquipmentListById(rId);
                }
                request.setAttribute("u", u);
                request.setAttribute("el", el);
                request.setAttribute("cor", cor2);
            String rMessage = "";
            if(cor.size()>0){rMessage = cor.size() + " Reservation request waiting,please handle";}
            User ui = new User();
            ui.setMessage(rMessage);
            request.setAttribute("rMessage", ui);
            RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/TechnicianHome.jsp");
            rd.forward(request, response);
        }else if ("goAdd".equalsIgnoreCase(action)) {
            Equipment e = new Equipment();
            request.setAttribute("equipment", e);
            ArrayList<Equipment> types = edb.queryEquipmentType();
            request.setAttribute("equipmentType", types);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/AddEquipment.jsp");
            rd.forward(request, response);
        }else if ("list".equalsIgnoreCase(action)) {    
            ArrayList<Equipment> equipments = edb.queryEquipment();
            request.setAttribute("equipments", equipments);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/ListEquipment.jsp");
            rd.forward(request, response);
        }else if ("changeStatus".equalsIgnoreCase(action)) {    
            String eId = request.getParameter("eId");
            String status = request.getParameter("status");
            edb.updateEquipment(eId, status);
            response.sendRedirect("handleT?action=list");
        }else if ("add".equalsIgnoreCase(action)) {    
            String status = request.getParameter("status");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String eType = request.getParameter("eType");
            if(eType.equalsIgnoreCase("other")){
                eType=request.getParameter("other");
            }
            for(int count=0;count<quantity;count++){
                edb.addEquipment(eType, status);
            }
            response.sendRedirect("handleT?action=goAdd");
        }else if ("returnEquipment".equalsIgnoreCase(action)) {    
            String sId = request.getParameter("sId");
            boolean isTrue = false;
            CheckOutRecord cor = new CheckOutRecord();
            String rId = crdb.isValidRid(sId);
            if(rId!=""){
                isTrue=true;
                cor.setRId(rId);
            }             
            cor.setSId(sId);
            request.setAttribute("cor", cor);
            if(isTrue||sId==null){
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/ReturnEquipment.jsp");
                rd.forward(request, response);
            }else{
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Wrong Student ID! input again!');");
                out.println("location='handleT?action=returnEquipment';");
                out.println("</script>");
            }
            
        }else if ("returnEquipmentID".equalsIgnoreCase(action)) {    
            String eId = request.getParameter("eId");
            String sId = request.getParameter("sId");
            edb.updateEquipment(eId,"available");
            response.sendRedirect("handleT?action=returnEquipment&sId="+sId);
        }else if ("reservationList".equalsIgnoreCase(action)) {    
            String rId = request.getParameter("rId");
            ArrayList<CheckOutRecord> cor = crdb.queryPendingReservation();
            request.setAttribute("cor", cor);
            ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
            if(rId!=null){
                 el = eldb.queryEquipmentListById(rId);
            }
            request.setAttribute("el", el);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/StudentReservation.jsp");
            rd.forward(request, response);
        }else if ("viewReservationList".equalsIgnoreCase(action)) {    
            String rId = request.getParameter("rId");
            ArrayList<CheckOutRecord> cor = crdb.queryReservation();
            request.setAttribute("cor", cor);
            ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
            if(rId!=null){
                 el = eldb.queryEquipmentListById(rId);
            }
            request.setAttribute("el", el);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/ReservationList.jsp");
            rd.forward(request, response);
        }else if ("approve".equalsIgnoreCase(action)) {    
            String rId = request.getParameter("id");
            String judgement = request.getParameter("judgement");
            crdb.UpdateRecord(rId, judgement,tId);
                ArrayList<EquipmentList> list= eldb.queryEquipmentByRId(rId);
                for(int i=0;i<list.size();i++){
                    EquipmentList el=list.get(i);
                    String eType = el.getEquipment();
                    int count = el.getCount();
                    edb.queryEquipmentByBorrowNum(eType,count,judgement);         
                }
            response.sendRedirect("handleT?action=reservationList");
        }else if ("finish".equalsIgnoreCase(action)) {    
            String rId = request.getParameter("rId");
            crdb.FinishRecord(rId);     
            response.sendRedirect("handleT?action=Technician");
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(HandleTechnician.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(HandleTechnician.class.getName()).log(Level.SEVERE, null, ex);
        }
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
