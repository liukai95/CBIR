package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * ����ͼƬ�ҶȻ�
 */
public class Gray {

	private BufferedImage img;// ͼ��
	private int[] newPixel;// ͼ���������ص�
	private int weigth, height;// ͼ���ȣ��߶�

	public int[] getNewPixels() {
		return newPixel;
	}

	public int getWeigth() {
		return weigth;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage getImg() {
		return img;
	}

	/**
	 * ͼ��ҶȻ�
	 * 
	 * @param srcPath
	 *            ԭͼƬ·��
	 */
	public void grayImage(String srcPath) {
		try {
			BufferedImage reader = ImageIO.read(new File(srcPath));
			if(reader==null)
				return;
			weigth = reader.getWidth();
			height = reader.getHeight();
			newPixel = new int[weigth * height];

			BufferedImage grayImage = new BufferedImage(weigth, height,
					BufferedImage.TYPE_BYTE_GRAY);// �޷��� byte,�Ҷȼ�ͼ��

			for (int i = 0; i < weigth; i++) {// �����������أ�ת��ͼ���ź�,ʹ��ҶȻ�
				for (int j = 0; j < height; j++) {
					int rgb = reader.getRGB(i, j);
					int grey = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
					rgb = 255 << 24 | grey << 16 | grey << 8 | grey;
					grayImage.setRGB(i, j, rgb);
				}
			}

			img = grayImage;//�õ��ҶȻ���ͼ��
			PixelGrabber pg = new PixelGrabber(img, 0, 0, weigth, height,
					newPixel, 0, weigth);
			pg.grabPixels();// ���ûҶȻ����ͼƬ�������ص����pixels����
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Gray g = new Gray();
		g.grayImage("C:\\Users\\����\\Desktop\\023.jpg");
	}

}
