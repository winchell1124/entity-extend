package similarity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimilarDegreebyCos {
	/* 
     * ���������ַ���(Ӣ���ַ�)�����ƶȣ��򵥵����Ҽ��㣬δ��Ȩ�� 
     */  
     public static double getSimilarDegree(String str1, String str2)  
     {  
        //���������ռ�ģ�ͣ�ʹ��mapʵ�֣�����Ϊ���ֵΪ����Ϊ2�����飬����Ŷ�Ӧ�������ַ����еĳ��ִ���  
         Map<String, int[]> vectorSpace = new HashMap<String, int[]>();  
         int[] itemCountArray = null;//Ϊ�˱���Ƶ�������ֲ����������Խ�itemCountArray�����ڴ�  
           
         //�Կո�Ϊ�ָ������ֽ��ַ���  
         String strArray[] = str1.split(" ");  
         for(int i=0; i<strArray.length; ++i)  
         {  
             if(vectorSpace.containsKey(strArray[i]))  
                 ++(vectorSpace.get(strArray[i])[0]);  
             else  
             {  
                 itemCountArray = new int[2];  
                 itemCountArray[0] = 1;  
                 itemCountArray[1] = 0;  
                 vectorSpace.put(strArray[i], itemCountArray);  
             }  
         }  
           
         strArray = str2.split(" ");  
         for(int i=0; i<strArray.length; ++i)  
         {  
             if(vectorSpace.containsKey(strArray[i]))  
                 ++(vectorSpace.get(strArray[i])[1]);  
             else  
             {  
                 itemCountArray = new int[2];  
                 itemCountArray[0] = 0;  
                 itemCountArray[1] = 1;  
                 vectorSpace.put(strArray[i], itemCountArray);  
             }  
         }  
           
         //�������ƶ�  
         double vector1Modulo = 0.00;//����1��ģ  
         double vector2Modulo = 0.00;//����2��ģ  
         double vectorProduct = 0.00; //������  
         Iterator iter = vectorSpace.entrySet().iterator();  
           
         while(iter.hasNext())  
         {  
             Map.Entry entry = (Map.Entry)iter.next();  
             itemCountArray = (int[])entry.getValue();  
               
             vector1Modulo += itemCountArray[0]*itemCountArray[0];  
             vector2Modulo += itemCountArray[1]*itemCountArray[1];  
               
             vectorProduct += itemCountArray[0]*itemCountArray[1];  
         }  
           
         vector1Modulo = Math.sqrt(vector1Modulo);  
         vector2Modulo = Math.sqrt(vector2Modulo);  
           
         //�������ƶ�  
        return (vectorProduct/(vector1Modulo*vector2Modulo));  
     }  
       
     /* 
      *  
      */  
     public static void main(String args[])  
     {  
    	 JaroWinklerDistance jwd=new JaroWinklerDistance();
         String str1 = "gold silver truck";  
         String str2 = "Shipment of gold damaged in a fire";  
         String str3 = "Delivery of silver arrived in a silver truck";  
         String str4 = "Shipment of gold arrived in a truck";  
         String str5 = "gold gold gold gold gold gold";          
         System.out.println(SimilarDegreebyCos.getSimilarDegree(str1, str2));  
         System.out.println(SimilarDegreebyCos.getSimilarDegree(str1, str3));  
         System.out.println(SimilarDegreebyCos.getSimilarDegree(str1, str4));  
         System.out.println(SimilarDegreebyCos.getSimilarDegree(str1, str5)); 
         String src = "hello world!";
 		String tar = "hellor worldasdsa!";
 		System.out.println(SimilarDegreebyCos.getSimilarDegree(src, tar));
 		System.out.println(jwd.getDistance(str1, str2));
     }  
}
