

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet(urlPatterns = { "/uploadservlet", "/imgupload" })
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100) //100MB

public class QuestionUpload extends HttpServlet {
	
	public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }
	
	private static final long serialVersionUID = 1L;
    public QuestionUpload() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("1");
		int imagecount = Integer.parseInt(request.getParameter("length"));
		/*@SuppressWarnings("rawtypes")
		Enumeration e = request.getParameterNames();
		while(e.hasMoreElements()){
	      Object obj = e.nextElement();
	      String fieldName = (String) obj;
	      System.out.println(fieldName + " : ");
	      if("type".equals(fieldName)){
	    	  String [] temp = request.getParameterValues("type");
	    	  System.out.println("in type ->");
	    	  String bb = "";
	    	  for(String gg : temp){
	    		  bb=bb+gg;
	    	  }
	    	  type=bb.split(",");
	    	  for(String mm : type){
	    		  System.out.print(mm+" ");
	    	  }
	      }else if("width".equals(fieldName)){
	    	  String [] temp = request.getParameterValues("width");
	    	  System.out.println("in width -> ");
	    	  String bb = "";
	    	  for(String gg : temp){
	    		  bb=bb+gg;
	    	  }
	    	  width = bb.split(",");
	    	  for(String mm : width){
	    		  System.out.print(mm+" ");
	    	  }
	      }else if("height".equals(fieldName)){
	    	  String [] temp = request.getParameterValues("height");
	    	  System.out.println("in height -> ");
	    	  String bb = "";
	    	  for(String gg : temp){
	    		  bb=bb+gg;
	    	  }
	    	  height=bb.split(",");
	    	  for(String mm : height){
	    		  System.out.print(mm+" ");
	    	  }
	      }
	    }*/
		for(int ijk = 1; ijk <= imagecount;ijk++){
			int width=Integer.parseInt(request.getParameter("width"+ijk));
			int height=Integer.parseInt(request.getParameter("height"+ijk));
			Object fileType = request.getParameter("type"+ijk);
			String mnbvc = "blob"+ijk;
			System.out.println("mnbvc "+mnbvc);
			Part part = request.getPart(mnbvc);
			System.out.println("part --> "+part);
			Image img = null;
			BufferedImage tempimage = null;
			//int width = Integer.parseInt(request.getParameter("width"));
			//int height = Integer.parseInt(request.getParameter("height"));
			System.out.println("part : "+part);
			System.out.println("2");
			//String outputImagePath = "D:" + File.separator + "jaja"+"."+fileType;
			System.out.println("3");
			if (part != null) {
				// writing blob
				System.out.println("4");
				/*
				 * image quality is not good*/
				//fileType = request.getParameter("type");
				System.out.println("5");
				double rand = Math.random() * (10 - 0) + 0;
				part.write("D:" + File.separator + "jaja"+rand+"."+fileType);
				File inputFile = new File("D:" + File.separator + "jaja"+rand+"."+fileType);
				img = ImageIO.read(inputFile);
				tempimage = resizeImage(img, width, height);
				ImageIO.write(tempimage, "png", inputFile);
		
			} else {
				System.out.println("6");
				// Writing image or file
				part = request.getPart("file");
				System.out.println("7");
				part.write("D:" + File.separator + "jaja"+"."+fileType);
			}
			System.out.println("8");
			response.getWriter().print("jaja" + " uploaded successfully");
		}
    }
}