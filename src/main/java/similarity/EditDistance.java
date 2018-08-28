package similarity;


import calculate.StringTransform;

public class EditDistance {
	public static int ld(String s, String t) {
		s=s.toLowerCase();
		//统一格式
		s=StringTransform.stringTransform(s);
		t=t.toLowerCase();
		t=StringTransform.stringTransform(t);
		int d[][];
		int sLen = s.length();
		int tLen = t.length();
		int si;	
		int ti;	
		char ch1;
		char ch2;
		int cost;
		if(sLen == 0) {
			return tLen;
		}
		if(tLen == 0) {
			return sLen;
		}
		d = new int[sLen+1][tLen+1];
		for(si=0; si<=sLen; si++) {
			d[si][0] = si;
		}
		for(ti=0; ti<=tLen; ti++) {
			d[0][ti] = ti;
		}
		for(si=1; si<=sLen; si++) {
			ch1 = s.charAt(si-1);
			for(ti=1; ti<=tLen; ti++) {
				ch2 = t.charAt(ti-1);
				if(ch1 == ch2) {
					cost = 0;
				} else {
					cost = 1;
				}
				d[si][ti] = Math.min(Math.min(d[si-1][ti]+1, d[si][ti-1]+1),d[si-1][ti-1]+cost);
			}
		}
		return d[sLen][tLen];
	}

	//编辑距离相似度
	public static float similarity(String src, String tar) {
		int ld = ld(src, tar);
		return 1 - (float) ld / Math.max(src.length(), tar.length()); 
	}
	
	
	public static void main(String[] args) {
		String src = "hello world!";
		String tar = "hellor worldasdsa!";
		System.out.println("sim="+EditDistance.similarity(src, tar));
	}

}
