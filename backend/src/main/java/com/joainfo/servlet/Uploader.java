package com.joainfo.servlet;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;


//import com.oreilly.servlet.MultipartRequest;
//import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class Uploader extends HttpServlet {

    private static final long serialVersionUID = 4289756371725257219L;
    public static String rootPath = "";
    int maxSize = 5 * 1024 * 1024; //5메가는 ? 1024*1024는 메가니까..
    String encoding = "UTF-8";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try{
//			System.out.println("upload...");
//			String saveDir = request.getParameter("saveDir");
//			if ("".equals(saveDir)){
//			} else {
//				if ("".equals(rootPath)){
//					rootPath = request.getSession().getServletContext().getRealPath("/");
//				}
//				saveDir = rootPath + "gasmax_sign\\" + saveDir;
//				File dir = new File(saveDir);
//				if (!dir.exists()) { // 해당 경로가 존재하지 않을 때
//					dir.mkdir(); // 디렉토리 생성
//				}
//				MultipartRequest multi = new MultipartRequest(request, saveDir, maxSize, encoding, new DefaultFileRenamePolicy());
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
        try {
            String saveDir = request.getParameter("saveDir");
            String fileName = request.getParameter("fileName");
//			 System.out.println("upload..." + saveDir + ", " + fileName);
            int yourMaxMemorySize = 1024 * 1024 * 1000;            // threshold  값 설정
            long yourMaxRequestSize = 1024 * 1024 * 5;   //업로드 최대 사이즈 설정 (5M)
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(yourMaxMemorySize);

            //실제패스등록하기
            if ("".equals(rootPath)) {
                rootPath = request.getSession().getServletContext().getRealPath("/");
            }
            String realPath = rootPath + "gasmax_sign\\" + saveDir;
            File dir = new File(realPath);
            if (!dir.exists()) { // 해당 경로가 존재하지 않을 때
                dir.mkdir(); // 디렉토리 생성
            }
            factory.setRepository(new File(realPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(yourMaxRequestSize);
//			 List<FileItem> list = upload.parseRequest(request);

//			 List<FileItem> list = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            List<FileItem> list = upload.parseRequest(null);


            for (int i = 0, ii = list.size(); i < ii; i++) {
                FileItem fileItem = (FileItem) list.get(i);
                //첨부파일 체크
                if ("file".equals(fileItem.getFieldName())) {
                    File file = new File(realPath, fileName);
                    if (file.exists()) {
                        boolean deleteFile = file.delete();
                        if (deleteFile) {
//						 	System.out.println("삭제 완료.");
                        }
                    }
                    fileItem.write(file);
//					 System.out.println("파일생성 완료.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}

