package filePretreatment;

/*
 * ��ȡһ���ļ����µ��ļ��Լ�·��
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileGetPath {

	private static PrintWriter pw;// ������ļ�

	public FileGetPath(String decPath) {
		super();
		try {
			pw = new PrintWriter(new FileWriter(decPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getList(File file) {
		if (file.isDirectory())// �ж�file�Ƿ���Ŀ¼
		{
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					getList(lists[i]);// ��Ŀ¼�͵ݹ����Ŀ¼���ٽ����ж�
				}
			}
		}
		if (file.isFile()) {
			// System.out.println(file);// file���ļ������������·����
			pw.println(file);
		}

	}

	public void close() {
		pw.close();
	}

	public static void main(String[] args) {
		FileGetPath fgp = new FileGetPath("C:\\Users\\����\\Desktop\\Path.txt");
		File file = new File("C:\\Users\\����\\Desktop\\�ۺ�ʵѵ\\ͼ��"
				+ File.separator);// ʹ��ϵͳ�йص�Ĭ�����Ʒָ���
		fgp.getList(file);
		fgp.close();
	}

}
