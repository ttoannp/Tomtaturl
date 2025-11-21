package bo;


import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapeService {

    private final RakeKeywordExtractor keywordExtractor = new RakeKeywordExtractor();
    private final Summarizer summarizer = new Summarizer();

    /**
     * Tải, trích xuất và phân tích nội dung từ một URL.
     * @param url URL của trang web cần phân tích.
     * @return Một đối tượng JSONObject chứa toàn bộ kết quả phân tích.
     * @throws IOException Nếu không thể kết nối hoặc tải trang.
     */
    public JSONObject fetchAndAnalyze(String url) throws IOException {
        // 1. Tải HTML bằng JSoup
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                .timeout(15000) // 15 giây timeout
                .get();

        // 2. Trích xuất nội dung
        String title = doc.title();
        String description = doc.select("meta[name=description]").attr("content");
        if (description.isEmpty()) {
            description = doc.select("meta[property=og:description]").attr("content");
        }
        String imageUrl = doc.select("meta[property=og:image]").attr("content");
        
        // Lấy toàn bộ text, loại bỏ các thẻ không cần thiết
        doc.select("script, style, nav, footer, header").remove();
        String mainText = doc.body().text();
        
        // Trích xuất các liên kết
        Elements linkElements = doc.select("a[href]");
        List<String> links = linkElements.stream()
                .map(element -> element.absUrl("href")) // Lấy URL tuyệt đối
                .filter(link -> !link.isEmpty() && link.startsWith("http"))
                .distinct()
                .limit(20) // Giới hạn 20 link đầu tiên
                .collect(Collectors.toList());

        // 3. Phân tích
        int wordCount = mainText.split("\\s+").length;
        List<String> keywords = keywordExtractor.extract(mainText, 10); // Lấy top 10 keywords
        String summary = summarizer.summarize(mainText, 3); // Tóm tắt trong 3 câu

        // 4. Đóng gói kết quả vào JSON
        JSONObject metaJson = new JSONObject();
        metaJson.put("url", url);
        metaJson.put("title", title);
        metaJson.put("description", description);
        metaJson.put("imageUrl", imageUrl);
        metaJson.put("wordCount", wordCount);
        metaJson.put("keywords", keywords);
        metaJson.put("summary", summary);
        metaJson.put("links", links);

        return metaJson;
    }
}