package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/*
 * ��ֵ�˲�����ͼ����
 */
public class MedianFilter {
	
	private BufferedImage img;// ͼ��
	private int weigth,height;//ͼ���ȣ��߶�
	private int[] newPixel;//������ֵ�˲����������ص�
	
	public int getWeigth() {
		return weigth;
	}

	public int getHeight() {
		return height;
	}

	public int[] getNewPixel() {
		return newPixel;
	}

	/**
	 * ��ֵ�˲�
	 * @param srcPath ԭͼƬ·��
	 */
	public void medianFiltering(String srcPath) {
		try {
			medianFiltering(ImageIO.read(new File(srcPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * ��ֵ�˲������ؿ��Խ��лҶȻ�+��ֵ�˲�
	 * @param img ͼƬ
	 */
	public void medianFiltering(BufferedImage img) {
		try {
			if(img==null){
				return;
			}
			weigth = img.getWidth();
			height = img.getHeight();
			int[] pix = new int[weigth * height];
			img.getRGB(0, 0, weigth, height, pix, 0, weigth);
			
			newPixel=new int[weigth * height];
			//������ֵ�˲�
			int[] temp = new int[9];
			ColorModel cm = ColorModel.getRGBdefault();
			int r = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < weigth; x++) {
					if (x != 0 && x != weigth - 1 && y != 0 && y != height - 1) {
						temp[0] = cm.getRed(pix[x - 1 + (y - 1) * weigth]);
						temp[1] = cm.getRed(pix[x + (y - 1) * weigth]);
						temp[2] = cm.getRed(pix[x + 1 + (y - 1) * weigth]);
						temp[3] = cm.getRed(pix[x - 1 + (y) * weigth]);
						temp[4] = cm.getRed(pix[x + (y) * weigth]);
						temp[5] = cm.getRed(pix[x + 1 + (y) * weigth]);
						temp[6] = cm.getRed(pix[x - 1 + (y + 1) * weigth]);
						temp[7] = cm.getRed(pix[x + (y + 1) * weigth]);
						temp[8] = cm.getRed(pix[x + 1 + (y + 1) * weigth]);
						Arrays.sort(temp);
						r = temp[4];
						newPixel[y * weigth + x] = 255 << 24 | r << 16 | r << 8 | r;
					} else {
						newPixel[y * weigth + x] = pix[y * weigth + x];
					}
				}
			}		
			img.setRGB(0, 0, weigth, height, newPixel, 0, weigth);
			this.img=img;
			//ImageIO.write(img, "jpg", new File("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\oo33.jpg"));//�����ֵ�˲����ͼ��
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MedianFilter mf = new MedianFilter();
		mf.medianFiltering("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\02.jpg");
	}
}
