package ict.servlet;
import ict.bean.CheckOutRecord;
import ict.bean.EquipmentList;
import ict.bean.User;
import ict.db.CheckOutRecordDB;
import ict.db.EquipmentDB;
import ict.db.EquipmentListDB;
import ict.db.StudentListDB;
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

@WebServlet(name = "HandleStudent", urlPatterns = {"/handleS"})
public class HandleStudent extends HttpServlet {
    private StudentListDB sldb;
    private EquipmentDB edb;
    private CheckOutRecordDB crdb;
    private EquipmentListDB eldb;
    
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        sldb = new StudentListDB(dbUrl, dbUser, dbPassword);
        edb = new EquipmentDB(dbUrl, dbUser, dbPassword);
        crdb = new CheckOutRecordDB(dbUrl, dbUser, dbPassword);
        eldb = new EquipmentListDB(dbUrl, dbUser, dbPassword);
        sldb.createStudentListTable();
        edb.createEquipmentTable();
        crdb.createCheckOutRecordTable();
        eldb.createEquipmentListTable();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        doPost(request, response);
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
        String action = request.getParameter("action");
        String sId = "";
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        if (session != null) {
            User myBean = (User) request.getSession().getAttribute("user");
            sId = myBean.getUserId();
        }
        if ("Student".equals(action)) {      
                String message = crdb.queryStatusById(sId); 
                ArrayList<CheckOutRecord> cor = crdb.queryOverdueReservation(sId);
                ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
                if(cor.size()>0){
                    CheckOutRecord c = cor.get(0);
                    String rId= c.getRId();
                    el = eldb.queryEquipmentListById(rId);
                }
                request.setAttribute("el", el);
                request.setAttribute("cor", cor);
                User u = new User();
                u.setMessage(message);
                request.setAttribute("message", u);
                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/StudentHome.jsp");
                rd.forward(request, response);
        }else if ("addToList".equals(action)) {        
                String type = request.getParameter("type");
                int number = Integer.parseInt(request.getParameter("number"));
                if(sldb.addItem(sId,type,number)){
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('add "+ type +" successful');");
                    out.println("location='handleS?action=searchEquipment';");
                    out.println("</script>");
                }
                else{
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('each item can not more than 5 or in student list already!');");
                    out.println("location='handleS?action=searchEquipment';");
                    out.println("</script>");
                }
        }else if ("studentList".equals(action)) {
            if(!crdb.queryValidId(sId)){
            ArrayList list = sldb.queryStudentList(sId);
            request.setAttribute("list", list);
            RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/StudentList.jsp");
            rd.forward(request, response);
            }else{
                out.println("<script type=\"text/javascript\">");
                out.println("alert('you have reservation already! please finish your reservation first');");
                out.println("location='handleS?action=Student';");
                out.println("</script>");
            }
        }
        else if ("removeFromSlist".equals(action)) {
            String type = request.getParameter("type");
            int count = Integer.parseInt(request.getParameter("count"));
            sldb.removeFromList(sId,type,count);
            response.sendRedirect("handleS?action=studentList");
        }
        else if ("confirm".equals(action)) {
           ArrayList list = sldb.queryStudentList(sId);
           String rId = crdb.addCheckOutRecord(sId);
           eldb.addEquipmentList(list, rId);
           sldb.removeAllFromList(sId);
           response.sendRedirect("handleS?action=Student");
        }else if ("reservationList".equalsIgnoreCase(action)) {    
            String rId = request.getParameter("rId");
            ArrayList<CheckOutRecord> cor = crdb.queryStudentReservation(sId);
            request.setAttribute("cor", cor);
            ArrayList<EquipmentList> el = new ArrayList<EquipmentList>();
            if(rId!=null){
                 el = eldb.queryEquipmentListById(rId);
            }
            request.setAttribute("el", el);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/StudentReservationList.jsp");
            rd.forward(request, response);
        }else if ("searchEquipment".equals(action)) {
            if(!crdb.queryValidId(sId)){
            ArrayList types = edb.queryEquipmentByType();
            request.setAttribute("types", types);
            RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/EquipmentReservation.jsp");
            rd.forward(request, response);
            }
            else{
                out.println("<script type=\"text/javascript\">");
                out.println("alert('you have reservation already! please finish your reservation first');");
                out.println("location='handleS?action=Student';");
                out.println("</script>");
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
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
