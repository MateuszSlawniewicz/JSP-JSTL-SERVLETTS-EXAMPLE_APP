package sd.servlets;

import sd.users.model.User;
import sd.users.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login_servlet")
public class LoginServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String login = req.getParameter("login");
      String password = req.getParameter("password");
      User user= new User();
      user.setLogin(login);
      user.setPassword(password);
      UserRepository userRepository = new UserRepository();
      if (userRepository.exists(user)){
          req.getSession().setAttribute("loggedUser","true");
          req.getSession().setAttribute("login",login);
       } else{
          req.getSession().setAttribute("loggedUser","false");
      }
       req.getRequestDispatcher("/index.jsp").forward(req, resp);

    }



}
