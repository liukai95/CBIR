package ui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ʹ��˵���ĵ�
 * 
 */
public class UseFrame {
	private JFrame frame = new JFrame("ʹ��˵��");// ������
	private JTextArea jtf = new JTextArea(25, 60);

	public UseFrame() {

		jtf.setFont(new Font("��Բ", 1, 20));
		jtf.setText("1.���û�����ݿ���Ҫ�ȵ�����ݿ����˵������½���ѡ���ļ��е�" +
				"�룬�ļ��϶�\nʱ����ʱ��ϳ��������ĵȴ���\n" +
				    "2.������οؼ���ʾ��������ͼ�������ļ�������ɽ��д򿪣��رգ�" +
				    "�ؼ������·\n��ѡ���ļ���Ҳ��ͨ���򿪲˵�ѡ����Ҫ���м�����ͼƬ��\n" +
				"3.ѡ�������������ȷ�����м��������ڴ����Ҳ���ʾ����������ʾ12��ͼƬ��\n" +
				"�������ƶȴ�С���У�ͼ���Ϸ�Ϊ��Ӧ�Ĳ�࣬�������һ��ͼƬ�ɽ��б��棬\nѡ��·���Լ�ͼƬ��ʽ�󱣴�ͼƬ��\n"
				+ "4.���ݿ�����пɽ��в����µ�ͼƬ��ѡ��ͼƬ�ļ�������ͼƬ��ʶ���������Ӳ�����\n" +
				"���ɾ�����ݺ󣬿������οؼ���ѡ����Ҫɾ�������ݣ�������ͼƬ�����ݿ���ɾ����\n" +
				"ͼƬ����û�б�ɾ�������ͳ�Ʋ˵�����ʾͼƬ����Ŀ�����ͼƬ��ʾ�˵���ʹ��\n��һ��������ʾ����ͼƬ��һ�����š�");
		jtf.setWrapStyleWord(false);
		frame.add(new JScrollPane(jtf));
		jtf.setEditable(false);// ���ɱ༭
		frame.pack();
		frame.setVisible(true);
		//����¼�������
		jtf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
				jtf.setCursor(new Cursor(Cursor.TEXT_CURSOR)); // ������Text�����Ϊ�ı�����ָ��
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent) {
				jtf.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ����뿪Text����ָ�Ĭ����̬
			}
		});
		jtf.getCaret().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				jtf.getCaret().setVisible(true); // ʹText�����ı������ʾ
			}
		});
	}

	public static void main(String[] args) {
		new UseFrame();
	}
}
