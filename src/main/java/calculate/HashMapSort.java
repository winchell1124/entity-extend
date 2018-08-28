package calculate;


import model.CandidateTable;

import java.util.*;
import java.util.Map.Entry;

public class HashMapSort {
	public static void main(String[] args)
	{
//		Map<String,Double> map=new HashMap<String,Double>();
//		map.put("uspo", 2.0);
//		map.put("Luffy", 5.0);
//		map.put("zero", 3.0);
//		map.put("zhangsan", 3.0);
//		map.put("lisi", 5.0);
//		
//		map=sortMapByDouValue(map);
//		double t=0;
//		Set<String> keySet=map.keySet();
//		Iterator<String> it=keySet.iterator();
//		if(it.hasNext())
//		{
//			String s=it.next();
//			t=map.get(s);
//			System.out.println(s+" "+t);
//		}
//		while(it.hasNext())
//		{
//			String s=it.next();
//			double t1=map.get(s);
//			if(t1==t)
//			{
//				System.out.println(s+" "+t1);
//			}
//			
//		}
		System.out.println(7.804515242576599-8.621276557445526);
	}

	/**
	 * @param oriMap：待排序的Map集合
	 * @return 根据集合中列平均相似度的大小排序
	 */
	public static Map<String,Integer> sortMapByIntValue(Map<String,Integer> oriMap) {
		Map<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Entry<String,Integer>> entryList = new ArrayList<Entry<String,Integer>>(oriMap.entrySet());
			Collections.sort(entryList,
					new Comparator<Entry<String,Integer>>() {
						public int compare(Entry<String,Integer> entry1,
								Entry<String,Integer> entry2) {
							int value1 = entry1.getValue();
							int value2 = entry2.getValue();

							return value2 - value1;
						}
					});
//			Iterator<Map.Entry<String,Integer>> iter = entryList.iterator();
//			Map.Entry<String,Integer> tmpEntry = null;
//			while (iter.hasNext()) {
//				tmpEntry = iter.next();
//				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//			}
			for(Entry<String,Integer> mapping:entryList){
	             sortedMap.put(mapping.getKey(), mapping.getValue()) ;
	        }
		}
		return sortedMap;

	}
	public static Map<String,Double> sortMapByDouValue(Map<String,Double> oriMap) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Map<String,Double> sortedMap = new LinkedHashMap<String,Double>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Entry<String,Double>> entryList = new ArrayList<Entry<String,Double>>(oriMap.entrySet());
//			System.out.println(entryList);
			Collections.sort(entryList,
					new Comparator<Entry<String,Double>>() {
						public int compare(Entry<String,Double> entry1,
								Entry<String,Double> entry2) {
							double value1 = entry1.getValue();
							double value2 = entry2.getValue();
							double temp=value2 - value1;
							if(temp<0)
								return -1;
							else if(temp>0)
								return 1;
							else
								return 0;
						}
					});
//			Iterator<Map.Entry<String,Integer>> iter = entryList.iterator();
//			Map.Entry<String,Integer> tmpEntry = null;
//			while (iter.hasNext()) {
//				tmpEntry = iter.next();
//				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//			}
			for(Entry<String,Double> mapping:entryList){
	             sortedMap.put(mapping.getKey(), mapping.getValue()) ;
	        }
		}
		return sortedMap;

	}
//	public static Map<AnswerTables,Double> sortAnswerTablesDouValue(Map<AnswerTables,Double> oriMap) {
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//		Map<AnswerTables,Double> sortedMap = new LinkedHashMap<AnswerTables,Double>();
//		if (oriMap != null && !oriMap.isEmpty()) {
//			List<Map.Entry<AnswerTables,Double>> entryList = new ArrayList<Map.Entry<AnswerTables,Double>>(oriMap.entrySet());
////			System.out.println(entryList);
//			Collections.sort(entryList,
//					new Comparator<Map.Entry<AnswerTables,Double>>() {
//						public int compare(Entry<AnswerTables,Double> entry1,
//								Entry<AnswerTables,Double> entry2) {
//							double value1 = entry1.getValue();
//							double value2 = entry2.getValue();
//							double temp=value2 - value1;
//							if(temp<0)
//								return -1;
//							else if(temp>0)
//								return 1;
//							else
//								return 0;
//						}
//					});
////			Iterator<Map.Entry<String,Integer>> iter = entryList.iterator();
////			Map.Entry<String,Integer> tmpEntry = null;
////			while (iter.hasNext()) {
////				tmpEntry = iter.next();
////				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
////			}
//			for(Map.Entry<AnswerTables,Double> mapping:entryList){
//	             sortedMap.put(mapping.getKey(), mapping.getValue()) ;
//	        }
//		}
//		return sortedMap;
//
//	}
	public static Map<CandidateTable,Double> sortCandidateTableByDouValue(Map<CandidateTable,Double> oriMap) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Map<CandidateTable,Double> sortedMap = new LinkedHashMap<CandidateTable,Double>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Entry<CandidateTable,Double>> entryList = new ArrayList<Entry<CandidateTable,Double>>(oriMap.entrySet());
//			System.out.println(entryList);
			Collections.sort(entryList,
					new Comparator<Entry<CandidateTable,Double>>() {
						public int compare(Entry<CandidateTable,Double> entry1,
								Entry<CandidateTable,Double> entry2) {
							double value1 = entry1.getValue();
							double value2 = entry2.getValue();
							double temp=value2 - value1;
							if(temp<0)
								return -1;
							else if(temp>0)
								return 1;
							else
								return 0;
						}
					});
//			Iterator<Map.Entry<String,Integer>> iter = entryList.iterator();
//			Map.Entry<String,Integer> tmpEntry = null;
//			while (iter.hasNext()) {
//				tmpEntry = iter.next();
//				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//			}
			for(Entry<CandidateTable,Double> mapping:entryList){
	             sortedMap.put(mapping.getKey(), mapping.getValue()) ;
	        } 
		}
		return sortedMap;
		 
	}
}
