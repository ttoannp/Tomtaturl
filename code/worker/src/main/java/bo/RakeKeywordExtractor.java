package bo;


import java.util.*;
import java.util.stream.Collectors;

// Lớp này chưa hoàn thiện, chỉ là bản demo đơn giản
// Để có kết quả tốt hơn, cần triển khai đầy đủ thuật toán RAKE
public class RakeKeywordExtractor {
    public List<String> extract(String text, int numKeywords) {
        // Đơn giản hóa: loại bỏ các từ phổ biến, lấy các từ có tần suất cao nhất
    	// chuyển thành chữ thường loại bỏ ký tự đặc biệt 
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Zà-ỹÀ-Ỹ ]", "").split("\\s+");
        
        // Danh sách các từ dừng đơn giản
        Set<String> stopWords = new HashSet<>(Arrays.asList("và", "là", "của", "có", "một", "trong", "được", "cho", "tại", "với", "các", "đã", "thì", "mà", "khi", "để"));
        
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String word : words) {
            if (!stopWords.contains(word) && word.length() > 2) {
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        // Sắp xếp các từ theo tần suất giảm dần và lấy top N
        return wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(numKeywords)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}