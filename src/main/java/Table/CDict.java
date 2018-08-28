package Table;

import config.PropertiesConfig;

import java.io.*;
import java.util.*;


//trie字典
public class CDict {
    CNode root = new CNode();
    public static final Integer DICT_STOP_WORD = 1;

    private class CNode {
        Integer mask = 0;
        Map<Character, CNode> map_char = new HashMap<Character, CNode>();
    }

    /**
     * 添加词语进入字典
     *
     * @param str_word（要添加的路径）
     * @param type             （路径的类型）
     */
    public void addWord(String str_word, Integer type) {
        CNode cur_node = root;
        for (int i = 0; i < str_word.length(); i++) {
            CNode next_node;
            if (!cur_node.map_char.containsKey(str_word.charAt(i))) {
                next_node = new CNode();
                cur_node.map_char.put(str_word.charAt(i), next_node);
            } else {
                next_node = cur_node.map_char.get(str_word.charAt(i));
            }
            cur_node = next_node;
        }
        cur_node.mask = type;
    }

    /**
     * 加载知识库
     *
     * @param str_file：知识库文件的路径名
     * @param type：知识库的类型
     */
    public void loadDict(String str_file, Integer type) {
        //System.out.println("loading "+str_file);
        try {
            String sline;
            int lcnt = 0;
            BufferedReader br = new BufferedReader(new FileReader(str_file));
            while ((sline = br.readLine()) != null) {
                addWord(sline, type);
                lcnt++;
            }
            br.close();

            //System.out.println(lcnt + " stopwords"); //词典中的停用词数目
        } catch (IOException e) {
            System.out.println("File Read ERROR : " + str_file);
        }
    }

    /**
     * 加载知识库2
     *
     * @param context=Table.get
     * @param stopwordpath="D:\\Workspace\\Test\\stopword.txt"
     * @return
     */

    public void loadDictSet(String context, String stopwordpath) {
        Set<String> stops = new HashSet<String>();
        try {
            Scanner sc = new Scanner(new File(stopwordpath)); //按行输入停用词文件
            while (sc.hasNextLine()) {
                stops.add(sc.nextLine());
            }
            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询词语是否在停用词典中
     *
     * @param str_text
     * @return
     */
    public int searchStopWord(String str_text) {
        Integer offset = 0;
        StringBuffer str_word = new StringBuffer();
        CNode cur_node = root;

        while (offset < str_text.length()) {
            CNode next_node = cur_node.map_char.get(str_text.charAt(offset));
            if (next_node == null)
                return 0;
            str_word.append(str_text.charAt(offset));
            offset++;
            cur_node = next_node;
        }

        //判断匹配到的类型
        if (1 == cur_node.mask) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 去除停用词函数
     *
     * @param context：表格的上下文文本字符串或其他需要去停用词的字符串
     * @return String：去停用词后的字符串，用于tf-idf提取关键词
     */
    public String removeStopWord(String context) {

        loadDict(PropertiesConfig.stopwordPath, DICT_STOP_WORD);

        String str = context.replaceAll("[\\pP\\p{Punct}]", ""); //去除标点符号
        //str=str.toLowerCase(); //大写转为小写
        String[] splitContext = str.split("\\s+"); //按空格分割字符串
        StringBuffer finalContext = new StringBuffer();

        for (int i = 0; i < splitContext.length; i++) {
            //System.out.println(splitContext[i]+"  "+i);
            if (searchStopWord(splitContext[i]) == 0) {
                finalContext = finalContext.append(splitContext[i]).append(" ");
            }
        }
        return finalContext.toString();
    }

    public static void main(String[] args) {
        CDict cd = new CDict();
        String line = "I am A the   students. And you are a good person! Thank you very much China!";
        System.out.println("final:" + cd.removeStopWord(line));
    }

}