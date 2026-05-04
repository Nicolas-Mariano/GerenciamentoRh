package controller;

import br.com.commandfactory.controller.ICommand;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller.do")
public class ControllerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            String paramAction = request.getParameter("acao");
            
            if (paramAction == null || paramAction.trim().isEmpty()) {
                throw new Exception("Ação não informada na requisição.");
            }

            
            String nomeDaClasse = "br.com.commandfactory.controller." + paramAction + "Action";
            
            Class<?> classAction = Class.forName(nomeDaClasse);                
            ICommand commandAction = (ICommand) classAction.getDeclaredConstructor().newInstance();
            
            String pageAction = commandAction.executar(request, response);
            request.getRequestDispatcher(pageAction).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("erro", "Erro interno: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}