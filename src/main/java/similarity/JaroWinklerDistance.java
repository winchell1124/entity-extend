package similarity;

import java.util.Arrays;

public class JaroWinklerDistance {
	 private static float threshold = 0.7f;

	  public static int[] matches(String s1, String s2) {
	    String max, min;
	    if (s1.length() > s2.length()) {
	      max = s1;
	      min = s2;
	    } else {
	      max = s2;
	      min = s1;
	    }
	    // �����ֱ�����s1��s2���ַ������಻���� floor(max(|s1|,|s2|) / 2) -1, ���Ǿ���Ϊ�������ַ�����ƥ���, ��ˣ�����ʱ��
	    // �����˾�����ֹͣ
	    int range = Math.max(max.length() / 2 - 1, 0);
	    // �̵��ַ���, �볤�ַ���ƥ�������λ
	    int[] matchIndexes = new int[min.length()];
	    Arrays.fill(matchIndexes, -1);
	    // ���ַ���ƥ��ı��
	    boolean[] matchFlags = new boolean[max.length()];
	    // ƥ�����Ŀ
	    int matches = 0;
	    // ���ѭ�����ַ�����̵Ŀ�ʼ
	    for (int mi = 0; mi < min.length(); mi++) {
	      char c1 = min.charAt(mi);
	      // ����ƥ��ľ��룬�����Ӹ���λ�ô�ǰ���ҺʹӺ����
	      for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max
	          .length()); xi < xn; xi++) {
	    	// �ų���ƥ������ַ������ҵ�ƥ����ַ�����ֹͣ
	        if (!matchFlags[xi] && c1 == max.charAt(xi)) {
	          matchIndexes[mi] = xi;
	          matchFlags[xi] = true;
	          matches++;
	          break;
	        }
	      }
	    }
	    
	    // ��¼min�ַ�����ƥ����ַ���������˳��
	    char[] ms1 = new char[matches];
	    // ��¼max�ַ�����ƥ����ַ���������˳��
	    char[] ms2 = new char[matches];
	    for (int i = 0, si = 0; i < min.length(); i++) {
	      if (matchIndexes[i] != -1) {
	        ms1[si] = min.charAt(i);
	        si++;
	      }
	    }
	    for (int i = 0, si = 0; i < max.length(); i++) {
	      if (matchFlags[i]) {
	        ms2[si] = max.charAt(i);
	        si++;
	      }
	    }
	    
	    // ���һ�λ����Ŀ
	    int transpositions = 0;
	    for (int mi = 0; mi < ms1.length; mi++) {
	      if (ms1[mi] != ms2[mi]) {
	        transpositions++;
	      }
	    }
	    
	    // ������ͬǰ׺����Ŀ
	    int prefix = 0;
	    for (int mi = 0; mi < min.length(); mi++) {
	      if (s1.charAt(mi) == s2.charAt(mi)) {
	        prefix++;
	      } else {
	        break;
	      }
	    }
	    
	    // ����ƥ����Ŀ��m������λ����Ŀ��t������ͬ��ǰ׺����Ŀ���ַ����
	    return new int[] { matches, transpositions / 2, prefix, max.length() };
	  }

	  public static float getDistance(String s1, String s2) {
	    int[] mtp = matches(s1, s2);
	    //  ����ƥ����Ŀ��m��
	    float m = (float) mtp[0];
	    if (m == 0) {
	      return 0f;
	    }
	    
	    // Jaro Distance
	    float j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m)) / 3;
	    
	    // ����Jaro-Winkler Distance�� �����������������=Math.min(0.1f, 1f / mtp[3])
	    float jw = j < getThreshold() ? j : j + Math.min(0.1f, 1f / mtp[3]) * mtp[2]
	        * (1 - j);
	    return jw;
	  }

	  /**
	   * Sets the threshold used to determine when Winkler bonus should be used.
	   * Set to a negative value to get the Jaro distance.
	   * @param threshold the new value of the threshold
	   */
	  public void setThreshold(float threshold) {
	    this.threshold = threshold;
	  }

	  /**
	   * Returns the current value of the threshold used for adding the Winkler bonus.
	   * The default value is 0.7.
	   * @return the current value of the threshold
	   */
	  public static float getThreshold() {
	    return threshold;
	  }
}
