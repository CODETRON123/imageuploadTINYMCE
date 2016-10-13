

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



@WebServlet(urlPatterns = { "/uploadservlet", "/imgupload","/viewim" })
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
    	String type = request.getParameter("type");
    	if("view".equals(type)){
    		List<String> localpaths = new ArrayList<String>();
    		Connection con = null;
    		java.sql.Statement statement;
    		try {
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(  
						"jdbc:mysql://localhost:3306/tinymcedemo","root","root");
				statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT id FROM pathtable");
				while(rs.next()){
					localpaths.add(rs.getString(1));
				}
				request.setAttribute("localpaths",localpaths);
				RequestDispatcher rd = null;
				rd = request.getRequestDispatcher("WEB-INF/jsp/viewimages.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else if("display".equals(type)){
    		int id = Integer.parseInt(request.getParameter("id"));
    		Connection con = null;
    		PreparedStatement ps = null;
    		try {
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(  
						"jdbc:mysql://localhost:3306/tinymcedemo","root","root");
				ps = con.prepareStatement("SELECT path from pathtable where id = ?");
				ps.setInt(1, id);
				String path = null;
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					path = rs.getString(1);
				}
				File inputFile = new File(path);
				FileInputStream fis = new FileInputStream(inputFile);
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        byte[] buf = new byte[1024];
		        try {
		            for (int readNum; (readNum = fis.read(buf)) != -1;) {
		                bos.write(buf, 0, readNum);
		                System.out.println("read " + readNum + " bytes,");
		            }
		        } catch (IOException ex) {
		        }
		        byte[] bytes = bos.toByteArray();
		        response.reset();
		        response.setContentType("image/jpg");
		        response.getOutputStream().write(bytes,0,bytes.length);
		        response.getOutputStream().flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/tinymcedemo","root","root");
			String sql = "insert into pathtable (path) values (?)";
			System.out.println("1");
			System.out.println("lenght _------> "+request.getParameter("length")); 
			int imagecount = Integer.parseInt(request.getParameter("length"));
			ps = con.prepareStatement(sql);
			for(int ijk = 1; ijk <= imagecount;ijk++){
				int width=Integer.parseInt(request.getParameter("width"+ijk));
				int height=Integer.parseInt(request.getParameter("height"+ijk));
				String fileType = request.getParameter("type"+ijk);
				String mnbvc = "blob"+ijk;
				System.out.println("mnbvc "+mnbvc);
				Part part = request.getPart(mnbvc);
				System.out.println("part --> "+part);
				Image img = null;
				BufferedImage tempimage = null;
				System.out.println("part : "+part);
				System.out.println("2");
				System.out.println("3");
				if (part != null) {
					System.out.println("4");
					System.out.println("5");
					Random randomGenerator = new Random();
					int rand = randomGenerator.nextInt(1000000)+randomGenerator.nextInt(10000);
					File dir = new File("D:" + File.separator +"images"+File.separator+"jaja"+rand);
					if(!dir.exists()){
						dir.mkdirs();
					}
					part.write("D:" + File.separator +"images"+File.separator+ "jaja"+rand+File.separator +"jaja"+rand+"."+fileType);
					File inputFile = new File("D:" + File.separator +"images"+File.separator+ "jaja"+rand+File.separator +"jaja"+rand+"."+fileType);
					img = ImageIO.read(inputFile);
					tempimage = resizeImage(img, width, height);
					ImageIO.write(tempimage, fileType, inputFile);
				    ps.setString(1,"D:" + File.separator +"images"+File.separator+ "jaja"+rand+File.separator +"jaja"+rand+"."+fileType);
				    ps.addBatch();
				}
				System.out.println("8");
				response.getWriter().print("jaja" + " uploaded successfully");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		finally{
			try {
				ps.executeBatch();
				ps.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}