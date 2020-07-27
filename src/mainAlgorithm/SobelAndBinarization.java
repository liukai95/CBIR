package mainAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SobelAndBinarization {

	private BufferedImage img;// ͼ��
	private int weigth, height;// ͼ���ȣ��߶�
	private int[] pixel;// ����sobel�񻯴��������ص�
	private int max, min;// ���������������Сֵ

	public BufferedImage getImg() {
		return img;
	}
	public SobelAndBinarization() {
		super();
	}

	public void sobel(String srcPath) {
		Gray g = new Gray();
		g.grayImage(srcPath);
		// �õ�ͼ���ȣ��߶�
		weigth = g.getWeigth();
		height = g.getHeight();
		MedianFilter mf = new MedianFilter();
		mf.medianFiltering(g.getImg());
		// �õ����ҶȻ�����ֵ�˲��������
		int[] pix = mf.getNewPixel();

		pixel = new int[weigth * height];
		for (int j = 1; j < height - 1; j++) {
			for (int i = 1; i < weigth - 1; i++) {
				int s1 = pix[i - 1 + (j + 1) * weigth] + 2
						* pix[i + (j + 1) * weigth]
						+ pix[i + 1 + (j + 1) * weigth]
						- pix[i - 1 + (j - 1) * weigth] - 2
						* pix[i + (j - 1) * weigth]
						- pix[i + 1 + (j - 1) * weigth];
				int s2 = pix[i + 1 + (j - 1) * weigth] + 2
						* pix[i + 1 + (j) * weigth]
						+ pix[i + 1 + (j + 1) * weigth]
						- pix[i - 1 + (j - 1) * weigth] - 2
						* pix[i - 1 + (j) * weigth]
						- pix[i - 1 + (j + 1) * weigth];
				int s = Math.abs(s1) + Math.abs(s2);
				if (s < 0)
					s = 0;
				if (s > 255)
					s = 255;
				pixel[j * weigth + i] = s;
				// �õ����������������Сֵ
				if (s > max) {
					max = s;
				}
				if (s < min) {
					min = s;
				}
			}
		}

	}

	/**
	 * �õ�����ֵ�����ж�ֵ��
	 * 
	 * @return
	 */
	public int[] getBinaryImg() {
		if(weigth * height==0){
			return null;
		}
		int[] newPixel = new int[weigth * height];

		// ��ȡ�Ҷ�ֱ��ͼ
		int i, j, t, count1 = 0, count2 = 0, sum1 = 0, sum2 = 0;
		int bp, fp;
		int[] histogram = new int[256];
		for (t = min; t <= max; t++) {
			for (int index = 0; index < weigth * height; index++) {
				if (pixel[index] == t)
					histogram[t]++;
			}
		}

		/*
		 * �����������ѷָ���ֵ
		 */
		int T = 0;
		int newT = (max + min) / 2;// ��ʼ��ֵ
		while (T != newT) {// ���������ǰ����ƽ���Ҷ�ֵbp��fp
			for (i = 0; i < T; i++) {
				count1 += histogram[i];// �������ص���ܸ���
				sum1 += histogram[i] * i;// �������ص�ĻҶ���ֵ
			}
			bp = (count1 == 0) ? 0 : (sum1 / count1);// �������ص��ƽ���Ҷ�ֵ

			for (j = i; j < histogram.length; j++) {
				count2 += histogram[j];// ǰ�����ص���ܸ���
				sum2 += histogram[j] * j;// ǰ�����ص�ĻҶ���ֵ
			}
			fp = (count2 == 0) ? 0 : (sum2 / count2);// ǰ�����ص��ƽ���Ҷ�ֵ
			T = newT;
			newT = (bp + fp) / 2;
		}
		//System.out.println(max + " " + min + " " + newT);
		BufferedImage bi = new BufferedImage(weigth, height,
				BufferedImage.TYPE_BYTE_BINARY);
		// ��ֵ��
		for (int index = 0; index < weigth * height; index++) {
			if (pixel[index] <= newT) {
				newPixel[index] = Color.WHITE.getRGB();
			} else {
				newPixel[index] = Color.BLACK.getRGB();
			}
			bi.setRGB(index % weigth, index / weigth, newPixel[index]);
		}

//		try {
//			ImageIO.write(bi, "jpg", new File("C:\\Users\\����\\Desktop\\02.jpg"));// ���ͼ��
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		img = bi;
		return newPixel;
	}

	public static void main(String[] args) {
		SobelAndBinarization s = new SobelAndBinarization();
		s.sobel("C:\\Users\\����\\Desktop\\20170321151053365.jpg");
		s.getBinaryImg();
	}

}
