package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.JDBCUtil;
import mainAlgorithm.ColorHSICenterMoment;
import mainAlgorithm.EdgeDetector;
import mainAlgorithm.EdgeHistogram;
import mainAlgorithm.GLCM;
import mainAlgorithm.HistogramIntersecting;
import mainAlgorithm.HuMoment;
import mainAlgorithm.SobelAndBinarization;

public class DataBaseAndFileOperating {
	/**
	 * ��ʼ�����ݿ�,�Լ����ɱ�Ҫ���ļ�
	 * @param srcPath ����ͼ���·���ļ�
	 * @return
	 */
	public static boolean init(String srcPath) {
		try {
			// �õ�Path.txt��Ĭ��Ŀ¼(�Ȳ�����ѡ��)
			BufferedReader br = new BufferedReader(new FileReader(new File(srcPath)));
			String read = "";

			// �������ݿ⣬�������
			Connection conn = (Connection) JDBCUtil.getMysqlConn();

			// ʹ��truncate���ԭ�б�Ϊ��ʹID�ܹ���1��ʼ
			conn.prepareStatement("truncate table texture").executeUpdate();
			conn.prepareStatement("truncate table color").executeUpdate();
			conn.prepareStatement("truncate table shape").executeUpdate();

			// �������ݵ�texture��
			String sql = "insert into texture (Name,Exp1,Exp2,Exp3,Exp4,Stadv1,Stadv2,Stadv3,Stadv4,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// �������ݵ�color��
			String sql2 = "insert into color (Name,H1,H2,H3,S1,S2,S3,I1,I2,I3,Path) values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt2 = conn.prepareStatement(sql2);

			// �������ݵ�shape��
			String sql3 = "insert into shape (Name,Shape0,Shape1,Shape2,Shape3,Shape4,Shape5,Shape6,Shape7,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);// ���ڲ���shape��
			while ((read = br.readLine()) != null) {
				String path = read;
				String str = read.substring(0, read.lastIndexOf('\\'));
				String name = str.substring(str.lastIndexOf('\\') + 1,
						str.length());
				// System.out.println(name);

				// ����������������
				preStmt.setString(1, name);
				GLCM glcm = new GLCM();
				double[] expStadv = glcm.getEigenValue(path);
				for (int i = 0; i < expStadv.length; i++) {
					// System.out.println(expStadv[i]);
					preStmt.setFloat((i + 2), (float) expStadv[i]);
				}
				preStmt.setString(10, path);
				preStmt.executeUpdate();

				// ������ɫ��������
				preStmt2.setString(1, name);

				float[][] mhsi = ColorHSICenterMoment.rgbTohsi(path);
				for (int i = 0; i < mhsi.length; i++) {
					for (int j = 0; j < mhsi[0].length; j++) {
						if (Float.isNaN(mhsi[i][j]))
							mhsi[i][j] = 0;
						preStmt2.setFloat((i * mhsi.length + j + 2), mhsi[i][j]);
					}
				}
				preStmt2.setString(11, path);
				preStmt2.executeUpdate();

				// ������״��������
				preStmt3.setString(1, name);
//				// �ҶȻ�����ֵ�˲���soble����ֵ��
//				SobelAndBinarization s = new SobelAndBinarization();
//				s.sobel(path);
//				s.getBinaryImg();
//				// �õ������
//				double[] hum = HuMoment.huMoment(s.getImg());

				EdgeDetector edgeDetector = new EdgeDetector();
				edgeDetector.setSourceImage(path); // ���ñ�Ե����Ĳ���
				edgeDetector.setThreshold(128);
				edgeDetector.setWidGaussianKernel(5);
				edgeDetector.process();
				 // ���б�Ե����
				double[] hum = HuMoment.huMoment(edgeDetector.getEdgeImage());
				
				for (int i = 0; i < hum.length; i++) {
					if (Double.isNaN(hum[i]))
						hum[i] = 0;
					// System.out.println(hum[i]);
					preStmt3.setFloat((i + 2), (float) hum[i]);
				}
				preStmt3.setString(10, path);
				preStmt3.executeUpdate();

				// ֱ��ͼ�ཻ��������ļ�����Ӧ���ļ��ļ���֮�£�������ͬ��.txt�ļ�
				HistogramIntersecting.getHistogram1(path, true);
				
				//��Ե����ֱ��ͼ��
				EdgeHistogram e=new EdgeHistogram();
				e.edgeHistogram(edgeDetector.getFileName(),edgeDetector.getName(),edgeDetector.getEdgeImage(), true);

			}
			br.close();
			// �ر���
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * �����ݿ��������
	 * 
	 * @param name
	 *            ����
	 * @param path
	 *            ·��
	 * @return �Ƿ����ӳɹ�
	 */
	public static boolean addData(String name, String path) {

		try {
			// �������ݿ⣬�������
			Connection conn = (Connection) JDBCUtil.getMysqlConn();

			// �������ݵ�texture��
			String sql = "insert into texture (Name,Exp1,Exp2,Exp3,Exp4,Stadv1,Stadv2,Stadv3,Stadv4,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// �������ݵ�color��
			String sql2 = "insert into color (Name,H1,H2,H3,S1,S2,S3,I1,I2,I3,Path) values(?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement preStmt2 = conn.prepareStatement(sql2);
			// �������ݵ�shape��
			String sql3 = "insert into shape (Name,Shape0,Shape1,Shape2,Shape3,Shape4,Shape5,Shape6,Shape7,Path) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);

			String str = path.substring(0, path.lastIndexOf('\\'));
			// ����������������
			preStmt.setString(1, name);
			GLCM glcm = new GLCM();
			double[] expStadv = glcm.getEigenValue(path);
			for (int i = 0; i < expStadv.length; i++) {

				preStmt.setFloat((i + 2), (float) expStadv[i]);
			}
			preStmt.setString(10, path);
			preStmt.executeUpdate();

			// ������ɫ��������
			preStmt2.setString(1, name);

			float[][] mhsi = ColorHSICenterMoment.rgbTohsi(path);
			for (int i = 0; i < mhsi.length; i++) {
				for (int j = 0; j < mhsi[0].length; j++) {
					if (Float.isNaN(mhsi[i][j]))
						mhsi[i][j] = 0;
					preStmt2.setFloat((i * mhsi.length + j + 2), mhsi[i][j]);
				}

			}
			preStmt2.setString(11, path);
			preStmt2.executeUpdate();

			// ������״��������
			preStmt3.setString(1, name);
			// �ҶȻ�����ֵ�˲���soble����ֵ��
			SobelAndBinarization s = new SobelAndBinarization();
			s.sobel(path);
			s.getBinaryImg();
			// �õ������
			double[] hum = HuMoment.huMoment(s.getImg());
			for (int i = 0; i < hum.length; i++) {
				// System.out.println(hum[i]);
				preStmt3.setFloat((i + 2), (float) hum[i]);
			}
			preStmt3.setString(10, path);
			preStmt3.executeUpdate();

			// ֱ��ͼ�ཻ��������ļ�����Ӧ��file�ļ���֮�£�������ͬ��.txt�ļ�
			HistogramIntersecting.getHistogram1(path, true);

			// �ر���
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);
		} catch (SQLException e) {
			return false;
		}

		return true;

	}

	/**
	 * ɾ�����ݿ���ĳ������
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteData(String path) {

		try {
			// �������ݿ⣬�������
			Connection conn = (Connection) JDBCUtil.getMysqlConn();
			// ��texture��ɾ������
			String sql = "delete from texture where Path=?";
			PreparedStatement preStmt = conn.prepareStatement(sql);

			// ��color��ɾ������
			String sql2 = "delete from color where Path=?";

			PreparedStatement preStmt2 = conn.prepareStatement(sql2);
			// ��shape��ɾ������
			String sql3 = "delete from shape where Path=?";
			PreparedStatement preStmt3 = conn.prepareStatement(sql3);

			preStmt.setString(1, path);
			preStmt.executeUpdate();
			preStmt2.setString(1, path);
			preStmt2.executeUpdate();
			preStmt3.setString(1, path);
			preStmt3.executeUpdate();

			// �ر���
			JDBCUtil.close(preStmt);
			JDBCUtil.close(preStmt2);
			JDBCUtil.close(preStmt3, conn);

		} catch (SQLException e) {
			return false;
		}

		return true;

	}

	/**
	 * ͳ��ͼ����Ŀ
	 * @return ��Ŀ
	 */
	public static int count() {
		int result=0;
		// �������ݿ⣬�������
		Connection conn = (Connection) JDBCUtil.getMysqlConn();
		Statement stmt;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select * from color");//����ѡ��һ�ű�ÿ�ű����������ͼ������
			rset.last();
			result = rset.getRow(); // ���ResultSet��������
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static void main(String[] args) {

		// init();
		// deleteData("C:\\Users\\����\\Desktop\\�ۺ�ʵѵ\\ͼ��\\flamingo_head\\flamingo_head2.jpg");
		//System.out.println(count());
		//init("C:\\Users\\����\\Desktop\\Path.txt");
	}

}
