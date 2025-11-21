package controller;


import dao.ResultDAO;
import model.Result;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/download-pdf")
public class DownloadPdfServlet extends HttpServlet {
    private final ResultDAO resultDAO = new ResultDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long taskId = Long.parseLong(req.getParameter("taskId"));
            Result result = resultDAO.getResultByTaskId(taskId);
            
            if (result != null && result.getOutputPath() != null && !result.getOutputPath().isEmpty()) {
                File pdfFile = new File(result.getOutputPath());
                
                if (pdfFile.exists()) {
                    // Thiết lập response headers cho việc download
                    resp.setContentType("application/pdf");
                    resp.setHeader("Content-Disposition", "attachment; filename=\"" + pdfFile.getName() + "\"");
                    resp.setContentLength((int) pdfFile.length());
                    
                    // Đọc file và ghi vào response output stream
                    try (FileInputStream inStream = new FileInputStream(pdfFile);
                         OutputStream outStream = resp.getOutputStream()) {
                        
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inStream.read(buffer)) != -1) {
                            outStream.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File PDF không tồn tại trên server.");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy thông tin kết quả hoặc đường dẫn file.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task ID không hợp lệ.");
        }
    }
}