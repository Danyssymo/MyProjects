package uno.dos.tres.controller.concrete.impl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uno.dos.tres.controller.concrete.Command;
import uno.dos.tres.service.NewsService;
import uno.dos.tres.service.ServiceException;
import uno.dos.tres.service.ServiceProvider;

import java.io.IOException;

public class UpdateNews implements Command {

    private final NewsService newsSerivice = ServiceProvider.getInstance().getNewsService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("news_id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String img = request.getParameter("image");
        String imgPath = "images/" + img;
        try {
            newsSerivice.updateNews(id, title, content, imgPath);
            response.sendRedirect("MyController?command=go_to_main_page&Info1=Successful update");
        } catch (ServiceException e) {
            response.sendRedirect("MyController?command=go_to_main_page&Info1=Unsuccessful update");
        }
    }
}
