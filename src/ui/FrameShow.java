package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * ͼƬ��ʾ
 * 
 */
public class FrameShow {
	private JFrame jf = new JFrame("ͼƬ��ʾ");// ������
	private JPanel result = new JPanel();// ��ѯ���ͼƬ��ʾ

	public FrameShow(final int count) {
		//��һ��תȦ�Ľ�����
		final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
		Dimension dimension = Toolkit.getDefaultToolkit()
				.getScreenSize();
		glasspane.setBounds(900, 900, (dimension.width) / 2,
				(dimension.height) / 2);
		jf.setGlassPane(glasspane);
		glasspane.start();// ��ʼ��������Ч��
		jf.setVisible(true);		
		jf.setSize(1500, 900);
		// ����һ���߳�����������
		new Thread() {
			@Override
			public void run() {
				int row = count / 4 + 1;
				result.setLayout(new GridLayout(row, 4, 5, 5));// ����4��
				BufferedReader br;
				try {

					br = new BufferedReader(new FileReader(new File(
							"C:\\Users\\����\\Desktop\\Path.txt")));
					String read = "";
					// һһ���뵽�����ϣ������ʾcount*4��
					int index = 0;
					while ((read = br.readLine()) != null) {
						if (index < count) {
							JLabel label = new JLabel();
							// System.out.println(read);
							label.setIcon(new ImageIcon(read));
							label.setSize(30, 30);
							result.add(new JScrollPane(label));
						}
						index++;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				JPanel jp = new JPanel();
				jp.setLayout(new BorderLayout());
				JLabel jl = new JLabel("����ͼƬ��ʾ����:");
				jl.setFont(new Font("��Բ", 1, 20));
				jl.setForeground(Color.red);
				jp.add(jl, BorderLayout.NORTH);
				jp.add(new JScrollPane(result));
				jf.add(jp);
				jf.pack();
				glasspane.stop();	
			}
		}.start();
		
	}

	public static void main(String[] args) {
		new FrameShow(40);
	}
}
