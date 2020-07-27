package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

/**
 * ֱ��ͼ�ཻ��(������ɫ�ļ���)
 * 
 */
public class HistogramIntersecting {

	/**
	 * ֱ��ͼ�ཻ���õ�H(k)
	 * @param srcPathԭͼƬ·��
	 * @param printFlag �Ƿ�������ļ�
	 * @returnֱ��ͼ�ཻ������ֵ
	 */
	public static double[][] getHistogram1(String srcPath,boolean printFlag) {

		String str= srcPath.substring(0,srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1,str.length());// �ļ�������֣����������ݿ�������, ͨ��srcPath���·����������Ӧ������ļ�
		String  name= srcPath.substring(srcPath.lastIndexOf('\\')+1, srcPath.lastIndexOf('.'));
		//System.out.println(pathName + " "+fileName);
		BufferedImage img;
		double[][] histgram = new double[3][256];
		try {
			img = ImageIO.read(new File(srcPath));
			if(img==null){
				return histgram;
			}
			int w = img.getWidth();// ͼƬ���
			int h = img.getHeight();// ͼƬ�߶�
			int[] pix = new int[w * h];// ��������
			int r, g, b;// ��¼R��G��B��ֵ
			pix = img.getRGB(0, 0, w, h, pix, 0, w);// ��ͼƬ������ֵ�浽������
			for (int i = 0; i < pix.length; i++) {
				// ��ȡR,G,B
				r = pix[i] >> 16 & 0xff;
				g = pix[i] >> 8 & 0xff;
				b = pix[i] & 0xff;
				histgram[0][r]++;
				histgram[1][g]++;
				histgram[2][b]++;
			}
			double red = 0, green = 0, blue = 0;
			for (int j = 0; j < 256; j++) {
				red += histgram[0][j];
				green += histgram[1][j];
				blue += histgram[2][j];
			}
			for (int j = 0; j < 256; j++)// ��ֱ��ͼÿ������ֵ���ܸ�����������
			{
				histgram[0][j] /= red;
				histgram[1][j] /= green;
				histgram[2][j] /= blue;
			}

			if(printFlag){
				/*
				 * ��ֱ��ͼ�ཻ��������ֵ���浽�ļ�
				 * �Ƚ�����һ����Ϊ�ļ����ļ��У�����Ӧ���ݷ�������
				 */
				String save = "C:\\Users\\����\\Desktop\\�ļ�\\"+fileName;
				File file = new File(save);
				// ����ļ��в������򴴽�
				if (!file.exists() && !file.isDirectory()) {
					file.mkdir();
				}
				String destPath = save + "\\" + name + ".txt";
				PrintWriter pw = new PrintWriter(new FileWriter(destPath));// ������ļ�
				for (int i = 0; i < histgram.length; i++) {
					for (int j = 0; j < histgram[0].length; j++) {
						pw.print(histgram[i][j] + " ");
					}
					pw.println();
				}
				pw.close();
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		return histgram;
	}
	/*
	 * ͳ��RGB,��һ�����󽻵õ�����ͼƬ�����ƶ�
	 */
	public static double getSimilarity1(double[][] Qhistgram,
			double[][] Dhistgram) {
		double similar = 0.0;// ���ƶ�
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < Qhistgram[i].length; j++) {
				similar += Math.min(Qhistgram[i][j], Dhistgram[i][j]);
			}
		}
		similar = similar / 3;//Qhistgram[i][j]֮��Ϊ3
		return similar;
	}

	// ŷʽ������ͼƬ�����ƶ�
	public static double getSimilarity2(double[][] Qhistgram,
			double[][] Dhistgram) {
		double similar = (double) 0.0;// ���ƶ�
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < Qhistgram[i].length; j++) {
				similar += (Qhistgram[i][j] - Dhistgram[i][j])
						* (Qhistgram[i][j] - Dhistgram[i][j]);
			}
		}
		//similar = similar / 6;
		similar = Math.sqrt(similar);
		return similar;
	}

//	public static void main(String[] args) throws IOException {
//		double similarity = (double) 0.0;
//
//		double[][] Qhistgram = getHistogram1("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\accordion1.jpg",false);
//		double[][] Dhistgram = getHistogram1("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\accordion1.jpg",false);
//		similarity = getSimilarity1(Qhistgram, Dhistgram);
//		System.out.print("��ͳ��RGBֱ��ͼ�ཻ����õ�ͼ�����ƶ�:");
//		System.out.println(similarity);
//
//		System.out.print("��ͳ��ŷʽ������õ�ͼ�����ƶ�:");
//		similarity = 1 - getSimilarity2(Qhistgram, Dhistgram);
//		System.out.println(similarity);
//
//	}

}
