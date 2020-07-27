package mainAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

/**
 * ͼ��������������ȡ
 */
public class GLCM {

	private int GLCM_DISTEN = 3; // ƫ�����
	private int GLCM_CLASS = 8; // �ҶȽ���
	
	private int weigth, height;// ͼ���ȣ��߶�

	/**
	 * ��ͼ��4������ĻҶȹ������󣬲�����4������ֵ��������featherVector[]
	 * 
	 * @param gray
	 *            �Ҷ�ֵ
	 * @param angleDirection
	 *            ����
	 * @param featureVector
	 *            ��������ֵ
	 */
	public void callGLCM(int[] gray, int angleDirection, double[] featureVector) {

		int[] glcm = new int[GLCM_CLASS * GLCM_CLASS];// �Ҷȹ����� 8 * 8 һά����
		int[] histImage = new int[weigth * height];// �������ͼ��

		// ����ͼ��Ҷȵȼ�����ͼ��256���Ҷȼ���˳���Ϊ8������
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < weigth; j++) {
				histImage[i * weigth + j] = (int) (gray[weigth * i + j]
						* GLCM_CLASS / 256);// ��32ȡ�� =������ĻҶ�
				// System.out.print(histImage[i * width + j]+" ");
			}
		}
		// ��ʼ����������
		for (int i = 0; i < GLCM_CLASS * GLCM_CLASS; i++) {
			glcm[i] = 0;// 8 x 8 ����

		}
		// ����Ҷȹ�������
		int k, l;
		// 0��
		if (angleDirection == 0) {
			// �����������ͼ��
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// ȡͼ��㣨i,j������ֵ
					if (j + GLCM_DISTEN < weigth) {
						k = histImage[i * weigth + j + GLCM_DISTEN];// ȡͼ���(i,j+d)����ֵ
						glcm[l * GLCM_CLASS + k]++;// ���Ҷȹ������У�value(i,j),value(i,j+d)��ֵ��1
					}
				}
			}
		}
		// 90��
		else if (angleDirection == 90) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// ȡͼ��㣨i,j������ֵ
					if (i + GLCM_DISTEN < height) {
						k = histImage[(i + GLCM_DISTEN) * weigth + j];// ȡͼ��㣨i+d,j������ֵ
						glcm[l * GLCM_CLASS + k]++;// ���Ҷȹ������У�value(i,j),value(i+d,j)��ֵ��1
					}
				}
			}
		}
		// 45��
		else if (angleDirection == 45) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// ȡͼ��㣨i,j������ֵ
					if (j + GLCM_DISTEN < weigth && i + GLCM_DISTEN < height) {
						k = histImage[(i + GLCM_DISTEN) * weigth + j
								+ GLCM_DISTEN];// ȡͼ��㣨i+d,j+d������ֵ
						glcm[l * GLCM_CLASS + k]++;// ���Ҷȹ������У�value(i,j),value(i+d,j+d)��ֵ��1
					}
				}
			}
		}
		// 135��
		else if (angleDirection == 135) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < weigth; j++) {
					l = histImage[i * weigth + j];// ȡͼ��㣨i,j������ֵ

					if (j - GLCM_DISTEN >= 0 && i - GLCM_DISTEN >= 0) {
						k = histImage[(i - GLCM_DISTEN) * weigth + j
								- GLCM_DISTEN];// ȡͼ��㣨i+d,j-d������ֵ
						glcm[l * GLCM_CLASS + k]++;// ���Ҷȹ������У�value(i,j),value(i+d,j-d)��ֵ��1
					}
				}
			}
		}
		// ��������ֵ
		double entropy = 0, energy = 0, contrast = 0, homogenity = 0;

		for (int i = 0; i < GLCM_CLASS; i++) {
			for (int j = 0; j < GLCM_CLASS; j++) {
				// ������
				if (glcm[i * GLCM_CLASS + j] > 0)
					entropy -= glcm[i * GLCM_CLASS + j]
							* Math.log((glcm[i * GLCM_CLASS + j]))
							/ Math.log(10);
				// ���������
				energy += glcm[i * GLCM_CLASS + j] * glcm[i * GLCM_CLASS + j];
				// ����Աȶ�
				contrast += (i - j) * (i - j) * glcm[i * GLCM_CLASS + j];
				//����һ����
				homogenity += 1.0 / (1 + (i - j) * (i - j))
						* glcm[i * GLCM_CLASS + j];
			}
		}
		// ��������ֵ
		featureVector[0] = entropy;
		featureVector[1] = energy;
		featureVector[2] = contrast;
		featureVector[3] = homogenity;
	}

	/**
	 * �����������Ҷȹ�����
	 * @param srcPathԭͼƬ·��
	 * @return 8 ������ֵ
	 */
	public double[] getEigenValue(String srcPath) {
		double[] expStadv = new double[8];// ���� 8 ������ֵ
		try {
			BufferedImage reader = ImageIO.read(new File(srcPath));
			if(reader==null){
				return expStadv;
			}
			weigth = reader.getWidth();
			height = reader.getHeight();

			int[] gray = new int[weigth * height];
			for (int i = 0; i < weigth; i++) {// �����������أ�ת��ͼ���ź�,ʹ��ҶȻ�
				for (int j = 0; j < height; j++) {
					int rgb = reader.getRGB(i, j);
					gray[j * weigth + i] = (int) ((0.3 * ((rgb & 0xff0000) >> 16) + 0.59 * ((rgb & 0xff00) >> 8)) + 0.11 * ((rgb & 0xff)));
				}
			}

			// ��ֱ 0��
			double[] GLCM_0 = new double[4];
			callGLCM(gray, 0, GLCM_0);
			double Average0 = 0.25 * (GLCM_0[0] + GLCM_0[1] + GLCM_0[2] + GLCM_0[3]);
			//System.out.println(" 0���ֵ��" + Average0);
			double temp0 = 1;
			for (int i = 0; i < 4; i++) {
				temp0 += (GLCM_0[i] - Average0) * (GLCM_0[i] - Average0);
			}
			double StaDev0 = Math.sqrt(0.25 * temp0);
			//System.out.println(" 0���׼�" + StaDev0);
			// ˮƽ 90��
			double[] GLCM_90 = new double[4];
			callGLCM(gray, 90, GLCM_90);
			double Average90 = 0.25 * (GLCM_90[0] + GLCM_90[1] + GLCM_90[2] + GLCM_90[3]);
			//System.out.println(" 90���ֵ��" + Average0);
			double temp90 = 1;
			for (int i = 0; i < 4; i++) {
				temp90 += (GLCM_90[i] - Average90) * (GLCM_90[i] - Average90);
			}
			double StaDev90 = Math.sqrt(0.25 * temp90);
			//System.out.println(" 90���׼�" + StaDev90);
			// 45��
			double[] GLCM_45 = new double[4];
			callGLCM(gray, 45, GLCM_45);
			double Average45 = 0.25 * (GLCM_45[0] + GLCM_45[1] + GLCM_45[2] + GLCM_45[3]);
			//System.out.println(" 45���ֵ��" + Average0);
			double temp45 = 1;
			for (int i = 0; i < 4; i++) {
				temp45 += (GLCM_45[i] - Average45) * (GLCM_45[i] - Average45);
			}
			double StaDev45 = Math.sqrt(0.25 * temp45);
			//System.out.println(" 45���׼�" + StaDev45);
			// 135��
			double[] GLCM_135 = new double[4];
			callGLCM(gray, 135, GLCM_135);
			double Average135 = 0.25 * (GLCM_135[0] + GLCM_135[1] + GLCM_135[2] + GLCM_135[3]);
			//System.out.println(" 135���ֵ��" + Average135);
			double temp135 = 1;
			for (int i = 0; i < 4; i++) {
				temp135 += (GLCM_135[i] - Average135)
						* (GLCM_135[i] - Average135);
			}
			double StaDev135 = Math.sqrt(0.25 * temp135);
			//System.out.println(" 135���׼�" + StaDev135);
			DecimalFormat df = new DecimalFormat("0.0000000000");
			expStadv[0] = Double.parseDouble(df.format(Average0));
			expStadv[1] = Double.parseDouble(df.format(Average90));
			expStadv[2] = Double.parseDouble(df.format(Average45));
			expStadv[3] = Double.parseDouble(df.format(Average135));
			expStadv[4] = Double.parseDouble(df.format(StaDev0));
			expStadv[5] = Double.parseDouble(df.format(StaDev90));
			expStadv[6] = Double.parseDouble(df.format(StaDev45));
			expStadv[7] = Double.parseDouble(df.format(StaDev135));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expStadv;

	}

//	public static void main(String[] args) {
//		GLCM glcm = new GLCM();
//		glcm.getEigenValue("C:\\Users\\����\\Desktop\\ͼ��\\accordion\\02.jpg");
//
//	}

}
