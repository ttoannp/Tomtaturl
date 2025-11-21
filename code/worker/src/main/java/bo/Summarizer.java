package bo;


import java.util.*;
import java.util.stream.Collectors;

// Lớp này chưa hoàn thiện, chỉ là bản demo đơn giản
public class Summarizer {
    public String summarize(String text, int numSentences) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        // Tách văn bản thành các câu
        String[] sentences = text.split("[.!?]");
        if (sentences.length <= numSentences) {
            return text;
        }

        // Đơn giản hóa: trả về N câu đầu tiên
        // Một phiên bản tốt hơn sẽ tính điểm cho từng câu dựa trên tần suất từ
        return Arrays.stream(sentences)
                .limit(numSentences)
                .map(String::trim)
                .collect(Collectors.joining(". ")) + ".";
    }
}
