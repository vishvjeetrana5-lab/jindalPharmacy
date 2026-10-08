package dataman.dmBase.dmSession.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class DmServlet extends HttpServlet {

    public abstract void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
