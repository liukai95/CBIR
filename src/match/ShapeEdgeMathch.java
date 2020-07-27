package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mainAlgorithm.EdgeDetector;
import mainAlgorithm.EdgeHistogram;
import mainAlgorithm.HistogramIntersecting;

/**
 * ��Ե����ֱ��ͼ��
 * 
 */
public class ShapeEdgeMathch {

	/**
	 * ��ɫ�����������ļ�����
	 * 
	 * @param srcPath
	 * @return ����ͼƬ�Լ����
	 */
	public static Map<String, Double> seMatch(String srcPath) {

		String style = srcPath.substring(srcPath.lastIndexOf('.') + 1,
				srcPath.length());// �ļ�����
		String fileDis = srcPath.substring(0, srcPath.lastIndexOf('\\'));// ͼƬ�����ļ���
		String fileName = fileDis.substring(fileDis.lastIndexOf('\\') + 1,
				fileDis.length());// �ļ�������֣����������ݿ�������
		Map<String, Double> lhm = new LinkedHashMap<String, Double>();// ���ؽ��
		// �õ���Եֱ��ͼ
		EdgeDetector edgeDetector = new EdgeDetector();
		edgeDetector.setSourceImage(srcPath); // ���ñ�Ե����Ĳ���
		edgeDetector.setThreshold(128);
		edgeDetector.setWidGaussianKernel(5);
		edgeDetector.process();
		EdgeHistogram eh = new EdgeHistogram();
		//�õ���һ����ı�Ե����ֱ��ͼ
		double[] result = eh.edgeHistogram(edgeDetector.getFileName(),
				edgeDetector.getName(), edgeDetector.getEdgeImage(), true);

		String pathName = "C:\\Users\\����\\Desktop\\�ļ�2\\" + fileName;// ͨ��srcPath���·��

		File file = new File(pathName);
		BufferedReader br = null;// ������
		Map<String, Double> map = new HashMap<String, Double>();// ����ͼƬ�����ݿ��з���������ͼƬ�ľ���ֵ
		if (file.isDirectory()) {
			// ���һ��Ŀ¼�µ������ļ�
			File[] lists = file.listFiles();
			// System.out.println(lists.length);
			if (lists != null) {
				for (int l = 0; l < lists.length; l++) {
					try {
						br = new BufferedReader(new FileReader(lists[l]));
						String read = "";
						double[] result2 = new double[36];
						// ��ʼ��result2
						int i = 0;
						while ((read = br.readLine()) != null) {
							result2[i] = Double.parseDouble(read);
							i++;
						}
						br.close();
						// ����һά�����ŷʽ�������
						double similarity = FeatureMeasure.euclideanDistance(
								result, result2);// �õ�ŷʽ����
						map.put(lists[l].getAbsolutePath(), similarity);
					} catch (Exception e) {
					}
				}

				// ��map��ֵ����
				List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(
						map.entrySet());
				Collections.sort(entryList,
						new Comparator<Map.Entry<String, Double>>() {

							@Override
							public int compare(Entry<String, Double> o1,
									Entry<String, Double> o2) {
								return o1.getValue().compareTo(o2.getValue());

							}
						});
				Iterator<Map.Entry<String, Double>> iter = entryList.iterator();
				Map.Entry<String, Double> tmpEntry = null;

				// �������12��·��
				for (int i = 0; i < 12; i++) {
					if (iter.hasNext()) {
						tmpEntry = iter.next();
						String key = tmpEntry.getKey();
						String path = fileDis
								+ "\\"
								+ key.substring(key.lastIndexOf('\\') + 1,
										key.lastIndexOf('.')) + "." + style;
						lhm.put(path, tmpEntry.getValue());
						System.out.println(path+" " +tmpEntry.getValue());
					}
				}
				// ����map
				// for (Map.Entry<String, Double> entry : lhm.entrySet()) {
				// System.out.println("Key = " + entry.getKey() + ", Value = "
				// + entry.getValue());
				// }

			}
		}
		return lhm;
	}

	public static void main(String[] args) {
		seMatch("C:\\Users\\����\\Desktop\\\\ͼ��\\accordion\\accordion10.jpg");
	}

}
