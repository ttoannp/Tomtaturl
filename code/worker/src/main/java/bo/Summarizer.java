package bo;

import java.util.*;
import java.util.stream.Collectors;

public class Summarizer {

    private final RakeKeywordExtractor extractor = new RakeKeywordExtractor();

    /**
     * Tóm tắt văn bản bằng cách chấm điểm từng câu dựa vào keyword.
     * @param text toàn bộ văn bản
     * @param numSentences số câu muốn lấy
     * @return bản tóm tắt
     */
    public String summarize(String text, int numSentences) {

        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // 1) Tách câu: chính xác hơn split cũ
        String[] sentences = text.split("(?<=[.!?])\\s+");

        if (sentences.length <= numSentences) {
            return text;
        }

        // 2) Trích keyword bằng RAKE
        List<String> keywords = extractor.extract(text, 10);

        // 3) Chấm điểm từng câu
        Map<String, Integer> scoreMap = new HashMap<>();

        for (String sentence : sentences) {
            int score = 0;
            String lower = sentence.toLowerCase();

            for (String kw : keywords) {
                String kwLower = kw.toLowerCase();

                // keyword xuất hiện → +2
                if (lower.contains(kwLower)) {
                    score += 2;
                }

                // keyword xuất hiện nhiều lần → +3 mỗi lần
                score += countOccurrences(lower, kwLower) * 3;
            }

            scoreMap.put(sentence, score);
        }

        // 4) Lấy câu có điểm cao nhất
        return scoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(numSentences)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(" "));
    }

    // -----------------------
    // Hàm hỗ trợ: đếm số lần xuất hiện
    // -----------------------
    private int countOccurrences(String text, String keyword) {
        int count = 0, index = 0;
        while ((index = text.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }
}
