package sd.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sd.tweets.model.Tweet;
import sd.tweets.repository.TweetReposiotory;
import sd.users.repository.UserRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@WebServlet(name = "tweeterServlet", value = "/tweeter_servlet")
public class TweeterServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TweetReposiotory tweetReposiotory = new TweetReposiotory();

        if (req.getSession().getAttribute("loggedUser") == null || req.getSession().getAttribute("loggedUser") == "false") {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("tweets", tweetReposiotory.findAll());
            req.getRequestDispatcher("/tweeter.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            return;
        }

        UserRepository userRepository = new UserRepository();
        Integer userId = userRepository.getUserId(req.getSession().getAttribute("login").toString());

        TweetReposiotory tweetReposiotory = new TweetReposiotory();
        ServletFileUpload upload = getServletFileUpload();
        Tweet tweet = new Tweet();
        getParameters(req, upload)
                .forEach(param -> processParam(param, tweet, userId));


        req.setAttribute("tweet", tweet);
        req.setAttribute("tweets", tweetReposiotory.findAll());
        req.getRequestDispatcher("/tweeter.jsp").forward(req, resp);

    }


    private void processParam(FileItem item, Tweet tweet, Integer userId) {
        if (item.isFormField()) {
            try {
                processFormField(item, tweet);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            processUploadedFile(item, tweet);
            TweetReposiotory tweetReposiotory = new TweetReposiotory();
            tweetReposiotory.addTweet(tweet, userId);
        }

    }


    private List<FileItem> getParameters(HttpServletRequest req, ServletFileUpload upload) {
        List<FileItem> items;
        try {
            items = upload.parseRequest(req);
        } catch (FileUploadException e) {
            throw new RuntimeException("Parse request exception", e);
        }
        return items;
    }

    private ServletFileUpload getServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        return new ServletFileUpload(factory);
    }

    private void processFormField(FileItem item, Tweet tweet) throws UnsupportedEncodingException {
        String param = item.getFieldName();
        switch (param) {
            case "message":
                tweet.setMessage(item.getString("UTF-8"));
                break;
        }

    }

    private void processUploadedFile(FileItem item, Tweet tweet) {

        //todo save file in database or host folder on computer


        String realPath = getServletContext().getRealPath("") + "upload";
        File uploadDir = new File(realPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File file = new File(realPath + File.separator + item.getName());
        try {
            item.write(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tweet.setImage((getServletContext().getContextPath() + "/upload/" + item.getName()));
    }
}




