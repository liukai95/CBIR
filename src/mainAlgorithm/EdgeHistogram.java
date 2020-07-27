package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

/**
 * ��Ե�����ֱ��ͼ
 * 
 */
public class EdgeHistogram {

	/**
	 * �õ���Ե�����ֱ��ͼ
	 * 
	 * @param srcPathԭͼƬ·��
	 * @param printFlag
	 *            �Ƿ�������ļ�
	 */
	public double[] edgeHistogram(String srcPath, boolean printFlag) {
		String str = srcPath.substring(0, srcPath.lastIndexOf('\\'));
		String fileName = str.substring(str.lastIndexOf('\\') + 1, str.length());// �ļ�������֣����������ݿ�������,
																			// ͨ��srcPath���·����������Ӧ������ļ�
		String name = srcPath.substring(srcPath.lastIndexOf('\\') + 1,
				srcPath.lastIndexOf('.'));
		BufferedImage reader;
		try {
			reader = ImageIO.read(new File(srcPath));
			double[] result=edgeHistogram(fileName,name,reader, printFlag);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[] edgeHistogram(String fileName,String name,int weigth, int height, int[] gray,
			boolean printFlag) {
		//System.out.println(weigth + " " + height + " " + gray.length);
		// �����ݶ�ʸ��Gx,Gy
		// �����Ե���򣻷�Χ��[-pi,pi]������->�Ƕȣ� [0,360]
		double[][] dx = new double[weigth][height];
		double[][] dy = new double[weigth][height];
		double[][] theta = new double[weigth][height];// ��Ե����theta(x,y) =
														// atan(dy/dx)
		for (int i = 0; i < weigth - 1; i++) {// �����������أ�ת��ͼ���ź�,ʹ��ҶȻ�
			for (int j = 0; j < height - 1; j++) {
				dx[i][j] = gray[(j + 1) * weigth + i] - gray[j * weigth + i];
				dy[i][j] = gray[j * weigth + i + 1] - gray[j * weigth + i];
				theta[i][j] = Math.atan(dx[i][j] / dy[i][j]) * 180 / 3.1415926 + 180;
			}
		}
		// ��[0,360]ÿ10�ȷ�Ϊһ�飬��ô��������Ϊ36 bin
		int[] num = new int[36];// �洢������������Ŀ������
		int sum = 0;
		for (int i = 0; i < weigth; i++) {
			for (int j = 0; j < height; j++) {
				if (gray[j * weigth + i] == 0) {
					num[(int) theta[i][j] / 10]++;
					sum++;
				}
			}
		}
		double[] result = new double[36];// �洢������������Ŀ������
		// ���й�һ��
		for (int i = 0; i < num.length; i++) {
			result[i] = (double) num[i] / sum;

		}
		if (printFlag) {
			/*
			 * ��ֱ��ͼ�ཻ��������ֵ���浽�ļ� �Ƚ�����һ����Ϊ�ļ����ļ��У�����Ӧ���ݷ�������
			 */
			String save = "C:\\Users\\����\\Desktop\\�ļ�2\\" + fileName;
			File file = new File(save);
			// ����ļ��в������򴴽�
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			String destPath = save + "\\" + name + ".txt";
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileWriter(destPath));
				for (int i = 0; i < result.length; i++) {
					pw.println(result[i]);// ������ļ�
				}
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public double[] edgeHistogram(String fileName,String name,BufferedImage img, boolean printFlag) {
		int weigth = img.getWidth();
		int height = img.getHeight();

		int[] gray = new int[weigth * height];
		for (int i = 0; i < weigth; i++) {// �����������أ��õ��Ҷ�ֵ
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j);
				gray[j * weigth + i] = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
			}
		}
		double[] result=edgeHistogram(fileName,name,weigth, height, gray, printFlag);
		return result;
	}

	public static void main(String[] args) {

		//EdgeHistogram.edgeHistogram("C:\\Users\\����\\Desktop\\02.jpg", true);
		EdgeDetector edgeDetector = new EdgeDetector();
		edgeDetector.setSourceImage("C:\\Users\\����\\Desktop\\20170321151053365.jpg"); // ���ñ�Ե����Ĳ���
		edgeDetector.setThreshold(128);
		edgeDetector.setWidGaussianKernel(5);
		edgeDetector.process();
		EdgeHistogram e=new EdgeHistogram();
		e.edgeHistogram(edgeDetector.getFileName(),edgeDetector.getFileName(),edgeDetector.getEdgeImage(), true);
	}
}
