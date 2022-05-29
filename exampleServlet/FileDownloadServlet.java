package com.example.medicinesystem.control;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hongxiaobin
 */
public class FileDownloadServlet extends HttpServlet {
    private static final long serialVersionUID=1L;

    public FileDownloadServlet(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得请求文件名
        String filename=request.getParameter("filename");
        //设置文件MIME类型
        response.setContentType(getServletContext().getMimeType(filename));
        //设置Content-Disposition
        response.setHeader("Content-Disposition","attachment;filename="+filename);
        //读取目标文件，通过response将目标文件写到客户端
        //获取目标文件的绝对路径
        String fullFileName=getServletContext().getRealPath("/img/"+filename);
        //读取文件
        InputStream inputStream= Files.newInputStream(Paths.get(fullFileName));
        OutputStream outputStream=response.getOutputStream();
        //写文件
        int b;
        while((b=inputStream.read())!=-1){
            outputStream.write(b);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
