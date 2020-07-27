package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import filePretreatment.FileGetPath;
import mainAlgorithm.HistogramIntersecting;
import match.DataBaseAndFileOperating;
import match.HistogramIntersectingMathch;
import match.HSICenterMomentMatch;
import match.ShapeEdgeMathch;
import match.ShapeMatch;
import match.TextureMatch;

/**
 * ������
 */
public class MainFrame {

	private Map<String, Double> lhmResultImage;// ͨ�����ø��ַ������صļ������
	private JFrame jf;// ������
	private JTree treeFileList;// ���οؼ�,��ʾ�ļ�·��
	private DefaultTreeModel model;// ����JTree�����Ӧ��model
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("ͼ��");// ����Tree��ʼ�ڵ�
	private JLabel checkImage = new JLabel();// ��Ҫ����ͼƬ��ʾ
	private JPanel result = new JPanel();// ��ѯ���ͼƬ��ʾ
	private JTextField jTpath = new JTextField(60);// һ��60�еĵ����ı�����ʾͼƬ·��
	private JLabel jlMethod = new JLabel("��������");
	private List<JLabel> listjl = new ArrayList<JLabel>();// ��ʾͼƬ���
	private List<JTextField> listjtf = new ArrayList<JTextField>();// ��ʾ�����
	private boolean isDeleteFile = false;// �жϵ�����οؼ��Ƿ���Ҫɾ���ļ�

	private int count;// ͼƬ������Ŀ
	boolean isCount = false;// �Ƿ����ͳ�ƹ�

	InfiniteProgressPanel glasspane = new InfiniteProgressPanel();

	public MainFrame() {

		jf = new JFrame("�������ݵ�ͼ�����1.0");
		// �˵���
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("�ļ�(F)");
		JMenu color = new JMenu("������ɫ����(CS)");
		JMenu shape = new JMenu("������״����(SS)");
		JMenu texture = new JMenu("�����������(VS)");
		JMenu face = new JMenu("����ʶ��(FF)");
		JMenu database = new JMenu("���ݿ����(DB)");
		JMenu help = new JMenu("����(H)");
		// �������Ƿ�ΪF������ALT + F ���Դ����ò˵�
		file.setMnemonic('F');
		// �������Ƿ�ΪF������ALT + H ���Դ����ò˵�
		help.setMnemonic('H');
		// �������򿪡��˵����Ϊָ֮��ͼ��
		JMenuItem open = new JMenuItem("��ͼƬ", new ImageIcon("icon/open.png"));
		// �������˳����˵����Ϊָ֮��ͼ��
		JMenuItem quit = new JMenuItem("�˳�", new ImageIcon("icon/exit.png"));
		file.add(open);
		// ���ò˵��ָ���
		file.addSeparator();
		file.add(quit);
		// ������ʹ��˵�����˵����Ϊָ֮��ͼ��
		JMenuItem use = new JMenuItem("ʹ��˵��", new ImageIcon("icon/use.png"));
		// ���������ڡ��˵����Ϊָ֮��ͼ��
		JMenuItem about = new JMenuItem("����", new ImageIcon("icon/about.png"));
		help.add(use);
		// ���ò˵��ָ���
		help.addSeparator();
		help.add(about);
		// ���������������ݿ⡱�˵���,��Ϊָ֮��ͼ��
		JMenuItem mdb = new JMenuItem("���������ݿ�", new ImageIcon("icon/db.png"));
		// ����������ͼ�񡱲˵���,��Ϊָ֮��ͼ��
		JMenuItem madd = new JMenuItem("����ͼ��", new ImageIcon("icon/add.png"));
		// ������ɾ��ͼ�񡱲˵���,��Ϊָ֮��ͼ��
		JMenuItem mdetele = new JMenuItem("ɾ��ͼ��", new ImageIcon(
				"icon/delete.png"));
		// ������ͼ��ͳ�ơ��˵���,��Ϊָ֮��ͼ��
		JMenuItem mcount = new JMenuItem("ͼ��ͳ��",
				new ImageIcon("icon/count.png"));
		// ������ͼ����ʾ���˵���,��Ϊָ֮��ͼ��
		JMenuItem mdisplay = new JMenuItem("ͼ����ʾ", new ImageIcon(
				"icon/show.png"));
		database.add(mdb);
		database.addSeparator();
		database.add(madd);
		database.addSeparator();
		database.add(mdetele);
		database.addSeparator();
		database.add(mcount);
		database.addSeparator();
		database.add(mdisplay);
		// ������������״����ط����˵���
		JMenuItem mHU = new JMenuItem("��״����ط�");
		// ��������Ե����ֱ��ͼ�����˵���
		JMenuItem mEdge = new JMenuItem("��Ե����ֱ��ͼ��");
		shape.add(mHU);
		shape.addSeparator();
		shape.add(mEdge);
		
		// ������HSI���ľط����˵���
		JMenuItem mHSI = new JMenuItem("HSI���ľط�");
		// ������ֱ��ͼ�ཻ�����˵���
		JMenuItem mHI = new JMenuItem("ֱ��ͼ�ཻ��");
		// �������Ҷȹ������󷨡��˵���
		JMenuItem mGLCM = new JMenuItem("�Ҷȹ�������");
		color.add(mHSI);
		color.addSeparator();
		color.add(mHI);
		texture.add(mGLCM);

		menuBar.add(file);
		menuBar.add(color);
		menuBar.add(shape);
		menuBar.add(texture);
		menuBar.add(face);
		menuBar.add(database);
		menuBar.add(help);

		// �������������(Haar-like����)���˵����Ϊָ֮��ͼ��
		JMenuItem mcamera = new JMenuItem("�������(Haar-like����)", new ImageIcon(
				"icon/camera.png"));
		face.add(mcamera);
		// ���ò˵�����ʹ�����ַ�ʽ���ò˵������Բ�ռ�ò��ֿռ�
		jf.setJMenuBar(menuBar);

		treeList();// �õ������ļ�·�������νṹ

		treeFileList = new JTree(root);
		// ��ȡJTree��Ӧ��TreeModel����
		model = (DefaultTreeModel) treeFileList.getModel();
		// ����JTree�ɱ༭
		treeFileList.setEditable(true);
		// ����ֻ��ѡ��һ��
		treeFileList.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// �����¼�����������õ����·��
		treeFileList.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode note = (DefaultMutableTreeNode) treeFileList
						.getLastSelectedPathComponent();

				if (isDeleteFile) {// ɾ��

					if (note != null && note.getParent() != null) {
						String name = note.toString();// ��������������
						int n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ��"
								+ name, "��ʾ�Ի���", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							// ����������ݿ���ɾ������
							boolean flag = DataBaseAndFileOperating
									.deleteData(name);
							if (flag) {
								model.removeNodeFromParent(note);// ɾ��ָ���ڵ�
								JOptionPane.showMessageDialog(jf, "ɾ���ɹ�! ",
										"��ʾ ", JOptionPane.INFORMATION_MESSAGE);
								// ɾ��Path.txt������
								delete("C:\\Users\\����\\Desktop\\Path.txt", name);
							}

						}

					}
					isDeleteFile = false;// һ��ɾ��һ��
				} else {// ��������ʾ
					if (note != null && note.getParent() != null) {
						String name = note.toString();// ��������������
						jTpath.setText(name);
						// ��ʾͼƬ
						checkImage.setIcon(new ImageIcon(name));
					}

				}

			}

		});
		// ����һ��ȷ����ť,��Ϊָ֮��ͼ��
		Icon okIcon = new ImageIcon("icon/ok.png");
		JButton ok = new JButton("ȷ��", okIcon);

		// ����¼����������õ�ʹ�õķ�����������ص��࣬������
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// �ж�·���Ƿ��Ѿ����һ���ļ�
				if ("".equals(jTpath.getText()) || jTpath.getText() == null) {
					JOptionPane.showMessageDialog(jf, "δѡ���ļ� ", "���� ",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// ��һ��תȦ�Ľ�����
					final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
					Dimension dimension = Toolkit.getDefaultToolkit()
							.getScreenSize();
					glasspane.setBounds(900, 900, (dimension.width) / 2,
							(dimension.height) / 2);
					jf.setGlassPane(glasspane);
					glasspane.start();// ��ʼ��������Ч��

					final String path = jTpath.getText();
					final String methodStr = jlMethod.getText();
					// ����һ���߳�����������
					new Thread() {
						@Override
						public void run() {
							switch (methodStr) {
							case "ֱ��ͼ�ཻ��":
								lhmResultImage = (LinkedHashMap<String, Double>) HistogramIntersectingMathch
										.hiMatch(path);
								break;
							case "�Ҷȹ�������":
								lhmResultImage = (LinkedHashMap<String, Double>) TextureMatch
										.textMatch(path);

								break;
							case "HSI���ľط�":
								lhmResultImage = (LinkedHashMap<String, Double>) HSICenterMomentMatch
										.hsiCenterMatch(path);

								break;
							case "��״����ط�":
								lhmResultImage = (LinkedHashMap<String, Double>) ShapeMatch
										.shapeMatch(path);

								break;
							case "��Ե����ֱ��ͼ��":
								lhmResultImage = (LinkedHashMap<String, Double>) ShapeEdgeMathch.seMatch(path);

								break;
							default:
								JOptionPane.showMessageDialog(jf, "�����޷�ʹ�� ",
										"���� ", JOptionPane.ERROR_MESSAGE);
								break;

							}
							int index = 0;
							if (lhmResultImage != null) {// ��Ϊ��
								for (Map.Entry<String, Double> entry : lhmResultImage
										.entrySet()) {
									// ����ͼƬ�Ͳ��
									listjl.get(index).setIcon(
											new ImageIcon(entry.getKey()));
									listjtf.get(index).setText(
											"���Ϊ:" + entry.getValue());
									index++;
								}
							}
							glasspane.stop();
						}
					}.start();
				}

			}
		});
		String[] method = new String[] { "һ��ŷ�Ͼ��뺯��", "���ڽ���ѯ�㷨", "�����ѯ�㷨",
				"��Ȩ���뺯��", "�ཻ����������", "���ľط�", "���Ҽн�" };
		// ����һ������ѡ���
		JComboBox<String> methodChooser = new JComboBox<>(method);

		jlMethod.setFont(new Font("��Բ", 1, 20));
		jlMethod.setForeground(Color.red);
		// ����һ��JPanel����������
		JPanel bottom = new JPanel();
		bottom.add(jlMethod);
		bottom.add(new JLabel("   ѡ������������������ :"));
		bottom.add(methodChooser);
		bottom.add(new JLabel("    ͼ���ļ�·��:"));
		bottom.add(jTpath);
		bottom.add(ok);

		// Ϊ�������������Ѵ�С
		treeFileList.setPreferredSize(new Dimension(150, 10000));// ���߶����úܴ�Ϊ���ܹ������Ĳ鿴
		checkImage.setPreferredSize(new Dimension(300, 200));
		result.setPreferredSize(new Dimension(800, 150));
		result.setLayout(new GridLayout(3, 4, 10, 10));
		saveListener sl = new saveListener();
		JLabel jli1 = new JLabel();
		listjl.add(jli1);
		jli1.addMouseListener(sl);
		JLabel jli2 = new JLabel();
		listjl.add(jli2);
		jli2.addMouseListener(sl);
		JLabel jli3 = new JLabel();
		listjl.add(jli3);
		jli3.addMouseListener(sl);
		JLabel jli4 = new JLabel();
		listjl.add(jli4);
		jli4.addMouseListener(sl);
		JLabel jli5 = new JLabel();
		listjl.add(jli5);
		jli5.addMouseListener(sl);
		JLabel jli6 = new JLabel();
		listjl.add(jli6);
		jli6.addMouseListener(sl);
		JLabel jli7 = new JLabel();
		listjl.add(jli7);
		jli7.addMouseListener(sl);
		JLabel jli8 = new JLabel();
		listjl.add(jli8);
		jli8.addMouseListener(sl);
		JLabel jli9 = new JLabel();
		listjl.add(jli9);
		jli9.addMouseListener(sl);
		JLabel jli10 = new JLabel();
		listjl.add(jli10);
		jli10.addMouseListener(sl);
		JLabel jli11 = new JLabel();
		listjl.add(jli11);
		jli11.addMouseListener(sl);
		JLabel jli12 = new JLabel();
		listjl.add(jli12);
		jli12.addMouseListener(sl);

		JTextField jtf1 = new JTextField("���Ϊ:");
		listjtf.add(jtf1);
		JTextField jtf2 = new JTextField("���Ϊ:");
		listjtf.add(jtf2);
		JTextField jtf3 = new JTextField("���Ϊ:");
		listjtf.add(jtf3);
		JTextField jtf4 = new JTextField("���Ϊ:");
		listjtf.add(jtf4);
		JTextField jtf5 = new JTextField("���Ϊ:");
		listjtf.add(jtf5);
		JTextField jtf6 = new JTextField("���Ϊ:");
		listjtf.add(jtf6);
		JTextField jtf7 = new JTextField("���Ϊ:");
		listjtf.add(jtf7);
		JTextField jtf8 = new JTextField("���Ϊ:");
		listjtf.add(jtf8);
		JTextField jtf9 = new JTextField("���Ϊ:");
		listjtf.add(jtf9);
		JTextField jtf10 = new JTextField("���Ϊ:");
		listjtf.add(jtf10);
		JTextField jtf11 = new JTextField("���Ϊ:");
		listjtf.add(jtf11);
		JTextField jtf12 = new JTextField("���Ϊ:");
		listjtf.add(jtf12);
		// ����Label��TextField
		for (int i = 0; i < 12; i++) {

			JPanel jpr = new JPanel();
			jpr.setLayout(new BorderLayout());
			jpr.add(listjtf.get(i), BorderLayout.NORTH);
			jpr.add(listjl.get(i), BorderLayout.SOUTH);
			result.add(new JScrollPane(jpr));
		}

		// ����һ����ֱ�ķָ���壬
		// ��tree�������棬��checkImage��������, ֧����������
		JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				new JScrollPane(treeFileList), new JScrollPane(checkImage));
		// �򿪡�һ����չ��������
		left.setOneTouchExpandable(true);
		left.setDividerSize(10);// ���÷ָ����Ĵ�С
		// ���ø÷ָ��������������������Ѵ�С����������
		left.resetToPreferredSizes();
		// ����һ��ˮƽ�ķָ����,��left���������ߣ���resultImage��������ұ�
		JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left,
				result);
		jf.add(new JScrollPane(content));
		jf.add(bottom, BorderLayout.SOUTH);
		jf.pack();
		// jf.setSize(900, 600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setIconImage(new ImageIcon("icon/icon.png").getImage());// Ϊ����ָ��һ��ͼƬ
		jf.setVisible(true);

		final JFileChooser dOpen = new JFileChooser();// �����ļ�ѡ����
		dOpen.setCurrentDirectory(new File("."));// ���õ�ǰĿ¼
		dOpen.setDialogTitle("ѡ����Ҫ����ͼƬ");

		// ��ʾ�ļ�Դ�ļ�
		dOpen.addChoosableFileFilter(new MyFileFilter("jpg"));// �����ѡ����ļ��ĺ�׺������
		dOpen.addChoosableFileFilter(new MyFileFilter("png"));
		dOpen.addChoosableFileFilter(new MyFileFilter("gif"));
		dOpen.addChoosableFileFilter(new MyFileFilter("bmp"));
		// Ϊ�򿪲˵�����¼�
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �õ��û�ѡ���ͼƬ·�����ļ���
				dOpen.showDialog(new JLabel(), "ѡ��");
				File f = dOpen.getSelectedFile();
				if (f != null) {
					jTpath.setText(f.getPath());
					// ��ʾͼƬ
					checkImage.setIcon(new ImageIcon(f.getPath()));
				}

			}

		});
		// Ϊ���������ݿ�˵�����¼�
		mdb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						"ԭ�����ݿ�ᱻɾ�������ҽ����µ�ͼ���������ݿ�ʱ��ϳ�,ȷ��Ҫ���������ݿ���?", "ѡ��Ի���",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {

					final JFileChooser jfc = new JFileChooser();// �����ļ�ѡ����
					jfc.setDialogTitle("ѡ�������ݿ�ͼƬ��Ŀ¼");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						// ��һ��תȦ�Ľ�����
						final InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
						Dimension dimension = Toolkit.getDefaultToolkit()
								.getScreenSize();
						glasspane.setBounds(900, 900, (dimension.width) / 2,
								(dimension.height) / 2);
						jf.setGlassPane(glasspane);
						glasspane.start();// ��ʼ��������Ч��
						// System.out.println(jfc.getSelectedFile().getAbsolutePath());
						// ����һ���߳�����������
						new Thread() {
							@Override
							public void run() {
								// �õ�Path.txt�ļ�
								String srcPath = "C:\\Users\\����\\Desktop\\Path.txt";// ����һ��·��
								FileGetPath fgp = new FileGetPath(srcPath);
								File file = new File(jfc.getSelectedFile()
										.getAbsolutePath() + File.separator);// ʹ��ϵͳ�йص�Ĭ�����Ʒָ���
								fgp.getList(file);
								fgp.close();// �ر���
								// ��ɾ�����ļ����롰�ļ�2���ļ�����ԭ�е��ļ�
								String s = "C:\\Users\\����\\Desktop\\�ļ�";
								deleteAllFilesOfDir(new File(s));
								// Ȼ�󴴽�һ�����ļ���
								File f = new File(s);
								// ����ļ��в������򴴽�
								if (!f.exists() && !f.isDirectory()) {
									f.mkdir();
								}
								s = "C:\\Users\\����\\Desktop\\�ļ�2";
								deleteAllFilesOfDir(new File(s));
								f = new File(s);
								if (!f.exists() && !f.isDirectory()) {
									f.mkdir();
								}
								
								// ��ʼ��
								boolean flag = DataBaseAndFileOperating
										.init(srcPath);
								if (flag) {
									//�Ƴ����οؼ���root���ʹ�ýڵ�
									root.removeAllChildren();
									model.reload();
									// �رս�����
									glasspane.stop();
									JOptionPane.showMessageDialog(jf, "�����ɹ�! ",
											"��ʾ ",
											JOptionPane.INFORMATION_MESSAGE);
									//���½������νṹ
									 treeList();// �õ������ļ�·�������νṹ
									 //ʵ����ʾ�½ڵ㣨�Զ�չ�����ڵ㣩
									 TreeNode[] nodes =
									 model.getPathToRoot(root);
									 TreePath path = new TreePath(nodes);
									 treeFileList.scrollPathToVisible(path);
									 treeFileList.updateUI();
								}
							}
						}.start();

					}

				}
			}

		});
		// Ϊ����ͼ��˵�����¼�
		madd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser();// �����ļ�ѡ����
				jfc.setDialogTitle("ѡ�������ݿ�ͼƬ");
				// �õ��û�ѡ���ͼƬ·�����ļ���
				jfc.showDialog(new JLabel(), "ѡ��");
				File f = jfc.getSelectedFile();
				if (f != null) {
					String name = JOptionPane
							.showInputDialog("������ͼƬ��ʶ(����ʲô���):");
					// System.out.println(f.getPath());
					// System.out.println(name);
					// ��������
					boolean flag = DataBaseAndFileOperating.addData(name,
							f.getPath());
					if (flag) {
						// �����������
						DefaultMutableTreeNode dmu = new DefaultMutableTreeNode(
								name);
						dmu.add(new DefaultMutableTreeNode(f.getPath()));
						root.add(dmu);
						// ʵ����ʾ�½ڵ㣨�Զ�չ�����ڵ㣩
						TreeNode[] nodes = model.getPathToRoot(dmu);
						TreePath path = new TreePath(nodes);
						treeFileList.scrollPathToVisible(path);
						treeFileList.updateUI();

						JOptionPane.showMessageDialog(jf, "��ӳɹ� ", "��ʾ ",
								JOptionPane.INFORMATION_MESSAGE);
						// ����Ĭ��·����Path.txt���������
						add("C:\\Users\\����\\Desktop\\Path.txt", f.getPath());
						// ֱ��ͼ�ཻ��������ļ�����Ӧ��file�ļ���֮�£�������ͬ��Ϊ.txt�ļ�
						HistogramIntersecting.getHistogram1(f.getPath(), true);
					}
				}

			}
		});
		// Ϊɾ��ͼ��˵�����¼�
		mdetele.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						"��������ʾ�ṹ�е������ɾ��,ȷ��Ҫɾ����?", "��ʾ�Ի���",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					isDeleteFile = true;
				}

			}
		});

		// Ϊͼ��ͳ�Ʋ˵�����¼�
		mcount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ���÷���ͳ����Ŀ
				count = DataBaseAndFileOperating.count();
				JOptionPane.showMessageDialog(jf, "ͼ������ĿΪ" + count, "��ʾ ",
						JOptionPane.INFORMATION_MESSAGE);
				isCount = true;

			}
		});
		// Ϊͼ����ʾ�˵�����¼�
		mdisplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isCount) {
					// û��ͳ�ƹ�,���÷���ͳ����Ŀ
					count = DataBaseAndFileOperating.count();
				}

				new FrameShow(count);

			}
		});
		// Ϊ�˳��˵�����¼�
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
			}
		});
		// Ϊ˵���˵�����¼�
		use.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UseFrame();// ��˵���ĵ�
			}
		});
		// Ϊ���ڲ˵�����¼�
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jf, "�������ݵ�ͼ�����1.0�汾", "˵�� ",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		MenuListener ml = new MenuListener();
		// Ϊ"ֱ��ͼ�ཻ��"�˵�����¼�
		mHI.addActionListener(ml);
		// Ϊ"HSI���ľط�"�˵�����¼�
		mHSI.addActionListener(ml);
		// Ϊ"�Ҷȹ�������"�˵�����¼�
		mGLCM.addActionListener(ml);
		// Ϊ"��״����ط�"�˵�����¼�
		mHU.addActionListener(ml);
		// Ϊ"��Ե����ֱ��ͼ��"�˵�����¼�
		mEdge.addActionListener(ml);
		// Ϊ"�������"�˵�����¼�
		mcamera.addActionListener(ml);
	}

	class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			System.out.println(str);
			switch (str) {
			case "ֱ��ͼ�ཻ��":
				jlMethod.setText("ֱ��ͼ�ཻ��");
				break;
			case "�Ҷȹ�������":
				jlMethod.setText("�Ҷȹ�������");

				break;
			case "HSI���ľط�":
				jlMethod.setText("HSI���ľط�");

				break;
			case "��״����ط�":
				jlMethod.setText("��״����ط�");
				break;
			case "��Ե����ֱ��ͼ��":
				jlMethod.setText("��Ե����ֱ��ͼ��");

				break;
			case "�������(Haar-like����)":
				jlMethod.setText("����ʶ��");

				break;
			default:
				break;

			}

		}

	}

	/*
	 * �ļ�ѡ���������
	 */
	class MyFileFilter extends FileFilter {
		private String ext;

		public MyFileFilter(String ext) {
			this.ext = ext;
		}

		// ��һ��Ŀ¼�������ļ�ʱ,����trueֵ,����Ŀ¼��ʾ����
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			String fileName = file.getName();
			int index = fileName.lastIndexOf('.');
			if (index > 0 && index < fileName.length() - 1) {
				String extension = fileName.substring(index + 1).toLowerCase();
				// �ļ���չ��ΪҪ��ʾ����չ��(������extֵ),�򷵻�true
				if (extension.equals(ext))
					return true;
			}
			return false;
		}

		// ʵ��getDescription()����,���������ļ���˵���ַ���
		public String getDescription() {
			if (ext.equals("png")) {
				return "ͼƬ(*.png)";
			} else if (ext.equals("jpg")) {
				return "ͼƬ(*.jpg)";
			} else if (ext.equals("gif")) {
				return "ͼƬ(*.gif)";
			} else if (ext.equals("bmp")) {
				return "ͼƬ(*.bmp)";
			}
			return "";
		}
	}

	/*
	 * �ļ������������
	 */
	class MyFileFilter2 extends FileFilter {

		private String ends; // �ļ���׺
		private String description; // �ļ���������

		public MyFileFilter2(String ends, String description) { // ���캯��
			this.ends = ends; // �����ļ���׺
			this.description = description; // �����ļ���������
		}

		@Override
		// ֻ��ʾ������չ�����ļ���Ŀ¼ȫ����ʾ
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String fileName = file.getName();
			if (fileName.toUpperCase().endsWith(this.ends.toUpperCase()))
				return true;
			return false;
		}

		@Override
		// ���������չ��������������
		public String getDescription() {
			return this.description;
		}

		// ���������չ������������չ��
		public String getEnds() {
			return this.ends;
		}
	}

	/*
	 * �����ļ�������
	 */
	class saveListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// jli1.getIcon();

			int n = JOptionPane.showConfirmDialog(null, "ȷ�ϱ�����?", "����Ի���",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				// FileDialog dSave = new FileDialog(jf,
				// "ѡ�񱣴�ͼƬ��·��,Ĭ��.jpg�ļ�",
				// FileDialog.SAVE);// ���ͼƬ����
				// dSave.setVisible(true);

				JFileChooser dSave = new JFileChooser();// �����ļ�ѡ����
				dSave.setCurrentDirectory(new File("."));// ���õ�ǰĿ¼
				dSave.setDialogTitle("ѡ����Ҫ����ͼƬ��·��");
				// ��������������
				MyFileFilter2 mf1 = new MyFileFilter2(".jpg", "ͼƬ(*.jpg)");
				MyFileFilter2 mf2 = new MyFileFilter2(".png", "ͼƬ(*.png)");
				MyFileFilter2 mf3 = new MyFileFilter2(".gif", "ͼƬ(*.gif)");
				MyFileFilter2 mf4 = new MyFileFilter2(".bmp", "ͼƬ(*.bmp)");
				// ��ʾ�ļ�Դ�ļ�
				dSave.addChoosableFileFilter(mf1);// �����ѡ����ļ��ĺ�׺������
				dSave.addChoosableFileFilter(mf2);
				dSave.addChoosableFileFilter(mf3);
				dSave.addChoosableFileFilter(mf4);
				dSave.showDialog(new JLabel(), "����");
				File file = dSave.getSelectedFile(); // ����ļ���
				// System.out.println(file.getAbsolutePath());
				// ��ñ�ѡ�еĹ�����
				MyFileFilter2 filter = (MyFileFilter2) dSave.getFileFilter();
				// ��ù���������չ��
				String ends = filter.getEnds();
				File newFile = null;
				if (file.getAbsolutePath().toUpperCase()
						.endsWith(ends.toUpperCase())) {
					// ����ļ�����ѡ����չ�������ģ���ʹ��ԭ��
					newFile = file;
				} else {
					// �������ѡ������չ��
					newFile = new File(file.getAbsolutePath() + ends);
					System.out.println(ends);
				}
				try {
					// ʹ��e.getSource()�õ��������ǿתΪJLabel
					Image image = ((ImageIcon) ((JLabel) (e.getSource()))
							.getIcon()).getImage();
					BufferedImage bi = new BufferedImage(
							image.getHeight((JLabel) (e.getSource())),
							image.getHeight((JLabel) (e.getSource())),
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d = bi.createGraphics();
					g2d.drawImage(image, 0, 0, null);
					// ���ͼƬ
					ImageIO.write(bi, ends.substring(1,ends.length()), newFile);
					
					JOptionPane.showMessageDialog(jf, "����ɹ� ", "��ʾ ",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}// ���ͼ��
			}
		}
	}

	/*
	 * ��ʾ���οؼ�
	 */
	public void treeList() {
		// �������οؼ�����ʾ����ͼƬ��·��
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"C:\\Users\\����\\Desktop\\Path.txt"));// �õ������ļ���·��
			String read = "";
			String name = "";
			if ((read = br.readLine()) != null) {
				String str = read.substring(0, read.lastIndexOf('\\'));
				name = str.substring(str.lastIndexOf('\\') + 1, str.length());// �ļ�������֣����������ݿ�������
				// System.out.println(name);
			}
			DefaultMutableTreeNode dmu = new DefaultMutableTreeNode(name);
			dmu.add(new DefaultMutableTreeNode(read));// �����һ���ڵ�
			while ((read = br.readLine()) != null) {
				String str = read.substring(0, read.lastIndexOf('\\'));
				String fileName = str.substring(str.lastIndexOf('\\') + 1,
						str.length());// �ļ�������֣����������ݿ�������
				if (name.equals(fileName)) {// ����δ�ı�
					dmu.add(new DefaultMutableTreeNode(read));// ��ײ�
				} else {
					root.add(dmu);
					name = fileName;
					dmu = new DefaultMutableTreeNode(name);
					dmu.add(new DefaultMutableTreeNode(read));// �����һ���ڵ�
				}
			}
			root.add(dmu);// �������һ��
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ��Path.txt��һ������
	 * 
	 * @param srcPath
	 *            Path·��
	 * @param text
	 *            ɾ��������
	 */
	public void delete(String srcPath, String text) {
		File temp = null;
		File file = new File(srcPath);
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			temp = File.createTempFile("temp", "temp");
			pw = new PrintWriter(temp);
			br = new BufferedReader(new FileReader(file));

			while (br.ready()) {
				String line = br.readLine();
				if (line.equals(text)) {
					continue;
				}
				pw.println(line);
			}
			pw.flush();
			br.close();
			pw.close();
			if (temp != null) {
				file.delete();
				temp.renameTo(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��Path.txt�����һ������
	 * 
	 * @param srcPath
	 *            Path·��
	 * @param text
	 *            ɾ��������
	 */
	public void add(String srcPath, String text) {
		File temp = null;
		File file = new File(srcPath);
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			temp = File.createTempFile("temp", "temp");
			pw = new PrintWriter(temp);
			br = new BufferedReader(new FileReader(file));

			while (br.ready()) {
				String line = br.readLine();
				pw.println(line);
			}
			pw.println(text);
			pw.flush();
			br.close();
			pw.close();
			if (temp != null) {
				file.delete();
				temp.renameTo(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ��һ���ļ��µ������ļ�
	 * 
	 * @param f
	 *            �ļ�
	 */
	public void deleteAllFilesOfDir(File f) {
		if (!f.exists())
			return;
		if (f.isFile()) {
			f.delete();
			return;
		}
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i]);
		}
		f.delete();
	}

	public static void main(String[] args) {
		// ����ΪWindows���
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new MainFrame();
	}

}
