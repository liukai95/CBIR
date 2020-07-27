package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;

public class CameraTest extends JFrame {

	public CameraTest() throws Exception {
		// ����������ͷ�����������ĳ�ʼ�����壬�ɱ�֤��ȷ��ʾ����ͷ
		initCapture();

		// ���ô����һЩ����
		this.setTitle("CaptureTest");
		this.setBounds(500, 100, 800, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * ��ʼ������ͷ
	 */
	private void initCapture() throws Exception {
		// ��ȡ������Ƶ����Ƶ�豸
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
		CaptureDeviceInfo deviceList = CaptureDeviceManager.getDevice(str2);
//cameraDevice = ;
		// ��ȡ��Ƶ�豸����Ƶ�豸��vfw��ͷ
		CaptureDeviceInfo cameraDevice =deviceList;
//		for (CaptureDeviceInfo cameraDeviceTmp : deviceList) {
//			if (cameraDeviceTmp.getName().startsWith("vfw")) {
//				
//				break;
//			}
//		}

		if (cameraDevice == null) {
			throw new Exception("�Ҳ�������ͷ�豸");
		}

		// ������Ƶ������
		MediaLocator ml = cameraDevice.getLocator();
		Player player = Manager.createRealizedPlayer(ml);

		if (player == null) {
			throw new Exception("��������ͷ������ʧ��");
		}

		// ������Ƶ
		player.start();

		// �����������봰��
		Component comp = null;
		if ((comp = player.getVisualComponent()) != null)
			add(comp, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		try {

			CameraTest mc = new CameraTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
