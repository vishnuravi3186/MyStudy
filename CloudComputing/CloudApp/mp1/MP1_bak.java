import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.Integer;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

package MP1;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

	// Read text file line by line
	public List<List> readTxtFile(String filePath) {
		// To store the text file content
		List<List> txtList = new ArrayList<List>();
		
		try {
			String encoding = "UTF-8";
			File fh = new File(filePath);
			if( fh.isFile() && fh.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(fh), encoding);
				BufferedReader bufReader = new BufferedReader(read);
				String strLine = null;
				while((strLine = bufReader.readLine()) != null) {
					// Divide each sentence into a list of words using delimiters
					List<String> lineItems = new ArrayList<String>();
					StringTokenizer st = new StringTokenizer(strLine, delimiters);
					while (st.hasMoreTokens()) {
						// Make all the tokens lower-case and remove any tailing and leading spaces
						String item = st.nextToken().trim().toLowerCase();
						lineItems.add(item);
					}
					txtList.add(lineItems);
				}
				read.close();
			} else {
				System.out.println("File not exists!");
			}
		} catch(Exception e) {
			System.out.println("Failed to read text file!");
		}
		
		// Debug
		//System.out.println("Info - Read file finished, total lines = " + txtList.size());
		
		return txtList;
	}
	
	public Map<String, Integer> sortMapByValue(Map<String, Integer> oldMap) {
		Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
		if( oldMap != null && !oldMap.isEmpty()) {
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
			
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				@Override
	  			public int compare(Map.Entry<String, Integer> arg0, Map.Entry<String, Integer> arg1) {
					int value0 = 0, value1 = 0;
					try {
						value0 = arg0.getValue().intValue();
						value1 = arg1.getValue().intValue();
					} catch(NumberFormatException e) {
						value0 = 0;
						value1 = 0;
					}		
					return value1 - value0;
				}
			});

			Iterator<Map.Entry<String, Integer>> iter = list.iterator();
			Map.Entry<String, Integer> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				newMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}  			
		}
        		
        return newMap;
    }
	
    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
		// Preparation
		Map<String, Integer> wordCounter = new HashMap<String, Integer>();
		List<String> stopWordsList = Arrays.asList(stopWordsArray);
		
		// Get line indexes to process
		Integer[] lineIndexes = getIndexes();
		
		// Open & read from the input file
		List<List> txtFile = readTxtFile(this.inputFileName);

		// Debug
		//System.out.println("Info - start word counting, total lines=" + lineIndexes.length);
		
		// Scan the line indexes list
		Integer itemCount = 0;
		int nLineIndex = 0;
		for( Integer index: lineIndexes ) {
			if( index >= txtFile.size() ) {
				System.out.println("Index exceeds the list range!");
				continue;
			}
			
			List<String> lineItems = (List<String>)txtFile.get(index);
			nLineIndex++;
			// Scan a line
			for( String item: lineItems ) {
				// Ignore all common words provided in the stopWordsArray variable
				if( !stopWordsList.contains(item) ) {
					itemCount = 0;
					// Keep track of word frequencies
					if( wordCounter.containsKey(item) ) {
						itemCount = (Integer)(wordCounter.get(item));
					}
					wordCounter.put(item, ++itemCount);
					
					// Debug
					//System.out.println("Info - line: " + index + " word: " + item + " count=" + itemCount + ", " + nLineIndex + " of total lines=" + lineIndexes.length);
				}
			}
		}

		// Debug
		//System.out.println("Info - finished word counting, total words=" + wordCounter.size());
		
		// Sort the wordCounter
		wordCounter = sortMapByValue(wordCounter);
		
		// Return the top 20 items from the sorted list as a String Array
		int nIndex = 0;
		Iterator it = wordCounter.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
			ret[nIndex] = (String)entry.getKey();
			if( ++nIndex >= ret.length )
				break;
        }
		
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}