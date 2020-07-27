package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * ֱ��ͼ���⻯
 */
public class HistogramEqualization {

	private BufferedImage img;//ͼ��
	private int[] pixels;// ͼ���������ص�
	private int[] histogram = new int[256];
	private int weigth, height;//ͼ���ȣ��߶� 

	/**
	 * ���캯��������Gray����лҶȻ�
	 * @param srcPath ԭͼƬ·��
	 */
	public HistogramEqualization(String srcPath) {
		Gray g = new Gray();
		g.grayImage("srcPath");
		// �õ�ͼ���ȣ��߶�
		weigth = g.getWeigth();
		height = g.getHeight();
		// �õ����ҶȻ��������
		pixels = g.getNewPixels();
	}

	/**
	 *  ֱ��ͼ����
	 * @param destPath ����ļ�
	 */
	public void histogramEqualization(String destPath) {
		BufferedImage greyImage = new BufferedImage(weigth, height,
				BufferedImage.TYPE_BYTE_GRAY);// �޷��� byte,�Ҷȼ�ͼ��
		for (int i = 0; i < height - 1; i++) {// ����ÿһ���Ҷȼ���������
			for (int j = 0; j < weigth - 1; j++) {
				int grey = pixels[i * weigth + j] & 0xff;
				histogram[grey]++;
			}
		}

		double a = (double) 255 / (weigth * height);
		double[] c = new double[256];
		c[0] = (a * histogram[0]);
		for (int i = 1; i < 256; i++)
			c[i] = c[i - 1] + (int) (a * histogram[i]);// ֱ��ͼ���⻯(��ɢ���)
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < weigth; j++) {
				int grey = pixels[i * weigth + j] & 0x0000ff;
				int hist = (int) c[grey];
				pixels[i * weigth + j] = 255 << 24 | hist << 16 | hist << 8 | hist;
				greyImage.setRGB(j, i, pixels[i * weigth + j]);
			}
		}

		img = greyImage;

		try {
			ImageIO.write(greyImage, "jpg", new File(destPath));//���ͼ��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//
//		HistogramEqualization he = new HistogramEqualization("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\02.jpg");
//		he.histogramEqualization("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\oo4.jpg");// ���⻯�����
//	}

}
