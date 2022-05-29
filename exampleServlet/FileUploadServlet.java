package com.example.medicinesystem.control;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * @author hongxiaobin
 */
@MultipartConfig(location = "D:\\", fileSizeThreshold = 1024)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //返回Web应用程序文档根目录
        String path = this.getServletContext().getRealPath("/");
        String name = (String) request.getSession().getAttribute("id");
        Part p = request.getPart("fileName");
        String message;
        if (p.getSize() > 1024 * 1024) {
            p.delete();
            message = "文件太大不能上传！";
        } else {
            path = path + "\\img";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String filename = name + ".jpg";
            message = "上传成功";
            p.write(path + "\\" + filename);
        }
        request.getRequestDispatcher("jsp/showMedicine.jsp").forward(request, response);
        request.setAttribute("message", message);
    }
}
