package filePretreatment;

/*
 * ��ȡ�ļ�������
 */
import java.io.File;

public class FileNewName {
	private static String name;// �ļ��е�����
	private static int id = 1;// ���

	public static void getList(File file) {
		if (file.isDirectory())// �ж�file�Ƿ���Ŀ¼
		{
			name = file.getName();
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					getList(lists[i]);// ��Ŀ¼�͵ݹ����Ŀ¼���ٽ����ж�
				}
			}
			id = 1;// ����һ��Ŀ¼����Ŵ�1��ʼ
		}
		if (file.isFile()) {
			file.renameTo(new File(file.getParent() + File.separator + name
					+ id + ".jpg"));// ��������
			id++;
		}

	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\����\\Desktop\\ͼ��" + File.separator);// ʹ��ϵͳ�йص�Ĭ�����Ʒָ���
		getList(file);
	}

}
