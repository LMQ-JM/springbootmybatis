package com.example.common.utils;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;



 


public class WxPoster {


 


 

/**

 * 把两张图片合并

 * 

 * @author WuHao

 * @version $Id: Pic.java, v 0.1 2019-10-26 下午3:21:23 1111 Exp $

 */



 
	//包含Font.PLAIN，Font.BOLD，Font.ITALIC三种，分别对应平体、加粗和斜体
	private Font font = new Font("微软雅黑", Font.BOLD, 20); // 添加字体的属性设置

	private static Graphics2D g = null;

	private int fontsize = 0;

	private int x = 0;

	private int y = 0;



	/**

	 * 导入本地图片到缓冲区

	 */

	public BufferedImage loadImageLocal(String imgName) {

		try {
			
			return  ImageIO.read(new File(imgName));

		} catch (IOException e) {
         e.printStackTrace();
			System.out.println(e.getMessage());

		}

		return null;

	}

	/**
	 * 导入网络图片到缓冲区
	 */

	public BufferedImage loadImageUrl(String imgName) {

		try {

			URL url = new URL(imgName);

			return ImageIO.read(url);

		} catch (IOException e) {

			System.out.println(e.getMessage());

		}

		return null;

	}

 

	/**

	 * 生成新图片到本地

	 */

	public String writeImageLocal(String newImage, BufferedImage img) {

		if (newImage != null && img != null) {

			try {

				File outputfile = new File(newImage);

				ImageIO.write(img, "jpg", outputfile);

			} catch (IOException e) {
				   e.printStackTrace();
				System.out.println(e.getMessage());

			}

		}
		return newImage;

	}

 

	/**

	 * 设定文字的字体等

	 */

	public void setFont(String fontStyle, int fontSize) {

		this.fontsize = fontSize;

		this.font = new Font(fontStyle, Font.PLAIN, fontSize);

	}

 

	/**

	 * 修改图片,返回修改后的图片缓冲区（只输出一行文本）

	 */

	public BufferedImage modifyImage(BufferedImage img, Object content, int x, int y, Font font, Color color) {

		try {

			g = img.createGraphics();

			g.setBackground(Color.WHITE);

			g.setColor(color);// 设置字体颜色

			if (font != null) {
				g.setFont(font);
			}

			// 验证输出位置的纵坐标和横坐标

			if (content != null) {

				g.drawString(content.toString(), 100, 220);

			}

			g.dispose();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

 

		return img;

	}

 

	/**

	 * 修改图片,返回修改后的图片缓冲区（输出多个文本段） xory：true表示将内容在一行中输出；false表示将内容多行输出

	 */

	public BufferedImage modifyImage(BufferedImage img, Object[] contentArr, int x, int y, boolean xory) {

		try {

			int w = img.getWidth();

			int h = img.getHeight();

			g = img.createGraphics();

			g.setBackground(Color.WHITE);

			g.setColor(Color.RED);

			if (this.font != null) {
				g.setFont(this.font);
			}

			// 验证输出位置的纵坐标和横坐标

			if (x >= h || y >= w) {

				this.x = h - this.fontsize + 2;

				this.y = w;

			} else {

				this.x = x;

				this.y = y;

			}

			if (contentArr != null) {

				int arrlen = contentArr.length;

				if (xory) {

					for (int i = 0; i < arrlen; i++) {

						g.drawString(contentArr[i].toString(), this.x, this.y);

						this.x += contentArr[i].toString().length() * this.fontsize / 2 + 5;// 重新计算文本输出位置

					}

				} else {

					for (int i = 0; i < arrlen; i++) {

						g.drawString(contentArr[i].toString(), this.x, this.y);

						this.y += this.fontsize + 2;// 重新计算文本输出位置

					}

				}

			}

			g.dispose();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

 

		return img;

	}

 

	/**
	 * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
	 * 时间:2007-10-8
	 * @param img
	 * @return
	 */
	public BufferedImage modifyImageYe(BufferedImage img,String userName,int x,int y,Font font) {

 

		try {

//			int w = img.getWidth();
//
//			int h = img.getHeight();
			//得到画笔对象
			g = img.createGraphics();

			//g.setBackground(Color.WHITE);
			
			g.setColor(Color.black);// 设置字体颜色
			if (this.font != null) {
				g.setFont(font);
			}
			g.drawString(userName, x, y);
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		return img;

	}

	/**
	 * 设置小程序码的大小
	 * @param b
	 * @param d
	 * @param X 整个布局的左右边距
	 * @param Y 整个布局的高度
	 * @return
	 */
	public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d, int X, int Y,int Width, int high) {

		try {

			int w =Width;

     		int h = high;
     		
			g = d.createGraphics();
			g.drawImage(b, X, Y, w, h, null);

			g.dispose();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

 

		return d;

	}
	

	
	

 

	/**

	 * 从url中读取图片

	 * 

	 * @param urlHttp

	 * @param path

	 */

	public static String getPicture(String urlHttp, String path) {

 

		int machineId = 1;// 最大支持1-9个集群机器部署

		int hashCodeV = UUID.randomUUID().toString().hashCode();

		if (hashCodeV < 0) {// 有可能是负数

			hashCodeV = -hashCodeV;

		}

 

		String file = path + machineId + String.format("%015d", hashCodeV) + ".jpg";

		try {

			URL url = new URL(urlHttp);

			BufferedImage img = ImageIO.read(url);

			ImageIO.write(img, "jpg", new File(file));

		} catch (Exception e) {

			e.printStackTrace();

		}

		return file;

	}

 

	public static void pichead(String url, String path) throws MalformedURLException, IOException {

 

		BufferedImage avatarImage = ImageIO.read(new URL(url));

		int width = 50;// 如果剪切出来的图片画质模糊，请将width 调大点.

		// 透明底的图片

		BufferedImage formatAvatarImage = new BufferedImage(width, width, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D graphics = formatAvatarImage.createGraphics();

		// 把图片切成一个圓

 

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖

		int border = 1;

		// 图片是一个圆型

		Ellipse2D.Double shape = new Ellipse2D.Double(border, border, width - border * 2, width - border * 2);

		// 需要保留的区域

		graphics.setClip(shape);

		graphics.drawImage(avatarImage, border, border, width - border * 2, width - border * 2, null);

		graphics.dispose();

 

		// 在圆图外面再画一个圆

 

		// 新创建一个graphics，这样画的圆不会有锯齿

		graphics = formatAvatarImage.createGraphics();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int border1 = 3;

		// 画笔是4.5个像素，BasicStroke的使用可以查看下面的参考文档

		// 使画笔时基本会像外延伸一定像素，具体可以自己使用的时候测试

		Stroke s = new BasicStroke(4.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		graphics.setStroke(s);

		graphics.setColor(Color.WHITE);

		graphics.drawOval(border1, border1, width - border1 * 2, width - border1 * 2);

		graphics.dispose();

 

		OutputStream os = new FileOutputStream(path);// 发布项目时，如：Tomcat 他会在服务器                

        //本地tomcat webapps文件下创建此文件名

		ImageIO.write(formatAvatarImage, "PNG", os);

		os.close();

	}

 

	/**

	 * 删除文件

	 * 


	 */

	public static void filedel(String filedel) {

		File file = new File(filedel);

		if (!file.exists()) {

			System.out.println("文件不存在");

		} else {

			System.out.println("存在文件");

			file.delete();// 删除文件

		}

	}

	
	
	/**
	 * @param leftUrl 背景图
	 * @param rightUrl 二维码
	 * @param loadUrl 合成图的地址  （自定义）
	 * @param headUrl 头像地址
	 * @param postImg 帖子第一张图片
	 * @param postContent 帖子内容
	 * @param userName 用户名
	 * @param title 标题
	 * @return
	 */
	public String getPosterUrlGreatMaster(String leftUrl, String rightUrl, String loadUrl, String headUrl, String postImg, String postContent,String userName,String title)  {
	/*	System.out.println("=="+leftUrl);
		System.out.println("=="+rightUrl);
		System.out.println("=="+loadUrl);
		System.out.println("=="+headUrl);
		System.out.println("=="+postImg);
		System.out.println("=="+postContent);
		System.out.println("=="+userName);
		System.out.println("=="+title);*/
		try {
			WxPoster tt = new WxPoster();
			
			//背景图
			BufferedImage j = tt.loadImageLocal(leftUrl);
			
			//二维码
			BufferedImage k = tt.loadImageLocal(rightUrl);

			tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(k, j,150, 560,160,160));
			
			//将头像图改为圆形
			BufferedImage ka = getRemoteBufferedImage(headUrl);
			if(ka==null){
				throw new ApplicationException(CodeType.SERVICE_ERROR);
			}
			//将图片设置为圆形
			BufferedImage convertCircular = getSque(ka);
			if(convertCircular==null){
				throw new ApplicationException(CodeType.SERVICE_ERROR,"错了");
			}
			tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(convertCircular, j,20, 20,80,80));

		    //帖子第一张图片的地址
		    //网络图片
		    BufferedImage remoteBufferedImage2 = getRemoteBufferedImage(postImg);
            tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(remoteBufferedImage2, j,0, 115,480,280));

			//设置用户名
			BufferedImage modifyImageYe = tt.modifyImageYe(j,userName,110,65,font);
			tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(null, j,0, 0,0,0));

//
			Font font1 = new Font("Monospaced", Font.BOLD, 20);
			g= j.createGraphics();
			//设置标题 文字换行
			drawStringWithFontStyleLineFeedTitle(g,title,300 , 15, 440,font1);
			tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(null, j,0, 0,0,0));

			//得到画图
			g= j.createGraphics();
			//设置内容  文字换行
			Font font2 = new Font("Monospaced", Font.PLAIN, 15);
			drawStringWithFontStyleLineFeed(g,postContent ,300 , 15, 490,font2);
			tt.writeImageLocal(loadUrl, tt.modifyImagetogeter(null, j,0, 0,0,0));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("错误异常++++++++++++++++++++++++++++++++++++++++++++"+e);
		}
		
		return loadUrl;
	}




	/**
	 * 剪裁成正方形
	 */
	public static BufferedImage getSque(BufferedImage bi) throws IOException {
		int init_width = bi.getWidth();
		int init_height = bi.getHeight();
		if (init_width != init_height){
			int width_height = 0;
			int x = 0;
			int y = 0;
			if (init_width > init_height) {
				//原图是宽大于高的长方形
				width_height = init_height;
				x = (init_width-init_height)/2;
				y = 0;
			} else if (init_width < init_height) {
				//原图是高大于宽的长方形
				width_height = init_width;
				y = (init_height-init_width)/2;
				x = 0;
			}
			bi = bi.getSubimage(x, y, init_width, width_height);
		}
		return convertCircular(bi);
	}

	/**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     * @return
     * @throws IOException
     */  
	public static BufferedImage convertCircular(BufferedImage bi1) throws IOException {
		 // 透明底的图片  
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);  
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());  
        Graphics2D g2 = bi2.createGraphics();  
        g2.setClip(shape);  
        // 使用 setRenderingHint 设置抗锯齿  
        g2.drawImage(bi1, -10, -10, null);  
        // 设置颜色  
        g2.setBackground(Color.green);  
        g2.dispose();
        
        return bi2;  
    }
 
	/**
     * 获取远程网络图片信息
     * @param imageURL
     * @return
     */
    public static BufferedImage getRemoteBufferedImage(String imageURL) {
        URL url = null;
        InputStream is = null;
        BufferedImage bufferedImage = null;
        
        try {
            url = new URL(imageURL);
            is = url.openStream();
            bufferedImage = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("imageURL: " + imageURL + ",无效!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("imageURL: " + imageURL + ",读取失败!");
            return null;
        } finally {
            try {
                if (is!=null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("imageURL: " + imageURL + ",流关闭异常!");
                return null;
            }
        }
        return bufferedImage;
    }

	/**
	 * 根据指定宽度自动换行
	 * @param g
	 * @param maxWdith
	 * @param strContent
	 * @param loc_X
	 * @param loc_Y
	 * @param font
	 */
	private  void  drawStringWithFontStyleLineFeed(Graphics g, String strContent,int maxWdith, int loc_X, int loc_Y, Font font){
		g.setFont(font);
		g.setColor(Color.darkGray);
		//获取字符串 字符的总宽度
		int strWidth =getStringLength(g,strContent);
		System.out.println(strWidth);
		if(strWidth>1866){
			String strsub=strContent.substring(0,50);//0到56的字符串
			strContent=strsub+"......";
		}

		//每一行字符串宽度
		int rowWidth=maxWdith;
		// System.out.println("每行字符宽度:"+rowWidth);
		//获取字符高度
		int strHeight=getStringHeight(g);
		//字符串总个数
		//  System.out.println("字符串总个数:"+strContent.length());
		if(strWidth>rowWidth){
			char[] strContentArr = strContent.toCharArray();
			int count = 0;
			int conut_value = 0;
			int line = 0;
			int charWidth = 0;
			for(int j=0;j< strContentArr.length;j++){

				if(conut_value>=rowWidth){
					conut_value = 0;
					g.drawString(strContent.substring(count,j),loc_X,loc_Y+strHeight*line);
					count = j;
					line++;

				}else{
					if(j==strContentArr.length - 1){
						g.drawString(strContent.substring(count,j),loc_X,loc_Y+strHeight*line);
					}else{
						charWidth = g.getFontMetrics().charWidth(strContentArr[j]);
						conut_value = charWidth + conut_value;
					}

				}

			}

		}else{
			//直接绘制
			g.drawString(strContent, loc_X, loc_Y);
		}

	}


	/**
	 * 根据指定宽度自动换行
	 * @param g
	 * @param maxWdith
	 * @param strContent
	 * @param loc_X
	 * @param loc_Y
	 * @param font
	 */
	private  void  drawStringWithFontStyleLineFeedTitle(Graphics g, String strContent,int maxWdith, int loc_X, int loc_Y, Font font){
		g.setFont(font);
		g.setColor(Color.darkGray);
		//获取字符串 字符的总宽度
		int strWidth =getStringLength(g,strContent);
		if(strWidth>=294){
			String strsub=strContent.substring(0,strContent.length()-1);//0到56的字符串
			strContent=strsub+"......";
		}

		//每一行字符串宽度
		int rowWidth=maxWdith;
		// System.out.println("每行字符宽度:"+rowWidth);
		//获取字符高度
		int strHeight=getStringHeight(g);
		//字符串总个数
		//  System.out.println("字符串总个数:"+strContent.length());
		if(strWidth>rowWidth){
			char[] strContentArr = strContent.toCharArray();
			int count = 0;
			int conut_value = 0;
			int line = 0;
			int charWidth = 0;
			for(int j=0;j< strContentArr.length;j++){

				if(conut_value>=rowWidth){
					conut_value = 0;
					g.drawString(strContent.substring(count,j),loc_X,loc_Y+strHeight*line);
					count = j;
					line++;

				}else{
					if(j==strContentArr.length - 1){
						g.drawString(strContent.substring(count,j),loc_X,loc_Y+strHeight*line);
					}else{
						charWidth = g.getFontMetrics().charWidth(strContentArr[j]);
						conut_value = charWidth + conut_value;
					}

				}

			}

		}else{
			//直接绘制
			g.drawString(strContent, loc_X, loc_Y);
		}

	}


	private int  getStringLength(Graphics g,String str) {
		char[]  strcha=str.toCharArray();
		int strWidth = g.getFontMetrics().charsWidth(strcha, 0, str.length());
		System.out.println("字符总宽度:"+strWidth);
		return strWidth;
	}

	private int getStringHeight(Graphics g) {
		int height = g.getFontMetrics().getHeight();
		//System.out.println("字符高度:"+height);
		return height;
	}






}
