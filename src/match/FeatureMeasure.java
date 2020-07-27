package match;

/**
 * ����������������
 * 
 */
public class FeatureMeasure {
	/**
	 * ŷ����þ������
	 * 
	 * @param vector1
	 *            ����1
	 * @param vector2
	 *            ����2
	 * @return
	 */
	public static double euclideanDistance(double[] vector1, double[] vector2) {
		double distance = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				double temp = (vector1[i] - vector2[i])
						* (vector1[i] - vector2[i]);
				distance += temp;
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * ŷ����þ������
	 * 
	 * @param vector1
	 *            ����1
	 * @param vector2
	 *            ����2
	 * @return
	 */
	public static double euclideanDistance2(double[] vector1, double[] vector2) {
		double distance = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length - 1; i++) {// �Ƚ�������ȥ���۲�
				double temp = (vector1[i] - vector2[i])
						* (vector1[i] - vector2[i]);
				distance += temp;

			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * ŷ����þ������
	 * 
	 * @param vector1
	 *            ����1
	 * @param vector2
	 *            ����2
	 * @return
	 */
	public static double euclideanDistance(double[][] vector1,
			double[][] vector2) {
		double distance = 0;
		if ((vector1.length == vector2.length)
				& (vector1[0].length == vector2[0].length)) {
			for (int i = 0; i < vector1.length; i++) {
				for (int j = 0; j < vector1[0].length; j++) {
					double temp = (vector1[i][j] - vector2[i][j])
							* (vector1[i][j] - vector2[i][j]);
					distance += temp;
				}
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * �ཻ������
	 * 
	 * @param vector1
	 *            ����1
	 * @param vector2
	 *            ����2
	 * @return
	 */
	public static double intersectDistance(double[] vector1, double[] vector2) {
		double distance = 0, sum = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				distance += Math.min(vector1[i], vector2[i]);
				sum += vector1[i];
			}
			distance = distance / sum;
			// if(distance == 1.0){
			// distance = 0;
			// }
		}
		return 1 - distance;
	}

	/**
	 * ���ľ෨����
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static double centerMomentDistance(float[][] vector1,
			float[][] vector2) {
		// ����Ȩ�ؾ�Ϊ1/3
		double distance = 0;

		for (int i = 0; i < vector1.length; i++) {
			distance += 1.0 / 3 * Math.pow(vector1[0][i] - vector2[0][i], 2)
					+ 1.0 / 3 * Math.pow(vector1[1][i] - vector2[1][i], 2)
					+ 1.0 / 3 * Math.pow(vector1[2][i] - vector2[3][i], 2);
		}
		distance = Math.sqrt(distance);
		// System.out.println(distance);

		return distance;
	}

	/**
	 * �н����Һ��� ����ֵԽ�ӽ�1���ͱ����н�Խ�ӽ�0�ȣ�Ҳ������������Խ���ƣ��нǵ���0��������������ȣ���ͽ�"����������"��
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static double Cosine(double[] vector1, double[] vector2) {
		double distance = 0, one = 0, two = 0, three = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				one += vector1[i] * vector2[i];
				two += vector1[i] * vector1[i];
				three += vector2[i] * vector2[i];
			}
			distance = 1 - one / (Math.sqrt(two) * Math.sqrt(three));
			if (distance > -0.0000000001 && distance < 0.00000000001) {// �ж��ǲ���0
				distance = 0;
			}

		}
		return distance;
	}

}
