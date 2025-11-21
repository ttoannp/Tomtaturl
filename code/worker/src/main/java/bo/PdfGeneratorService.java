package bo;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class PdfGeneratorService {

    public void createPdf(JSONObject resultJson, String outputPath) throws IOException {

        File file = new File(outputPath);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Font Unicode cho tiếng Việt
        
        URL fontUrl = getClass().getClassLoader().getResource("fonts/NotoSans-Regular.ttf");
        if (fontUrl == null) {
            throw new IOException("Không tìm thấy font NotoSans-Regular.ttf trong classpath");
        }

        String fontPath;
        try {
            fontPath = Paths.get(fontUrl.toURI()).toString();  // GIẢI MÃ %20 → khoảng trắng
        } catch (URISyntaxException e) {
            throw new IOException("Lỗi khi convert URL font sang đường dẫn", e);
        }

        PdfFont unicodeFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        document.setFont(unicodeFont);


        // Tiêu đề
        document.add(new Paragraph(resultJson.optString("title", "Không có tiêu đề"))
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));

        // URL
        document.add(new Paragraph("URL gốc: " + resultJson.optString("url", "")));

        // Mô tả
        document.add(new Paragraph("Mô tả: " + resultJson.optString("description", "")));

        // Số từ
        document.add(new Paragraph("Số từ: " + resultJson.optInt("wordCount", 0)));

        document.add(new Paragraph("\n"));

        // Tóm tắt
        document.add(new Paragraph("Tóm tắt nội dung").setBold());
        document.add(new Paragraph(resultJson.optString("summary", "")));

        document.add(new Paragraph("\n"));

        // Từ khóa
        document.add(new Paragraph("Các từ khóa chính").setBold());
        JSONArray keywords = resultJson.optJSONArray("keywords");
        if (keywords != null) {
            for (int i = 0; i < keywords.length(); i++) {
                document.add(new Paragraph("- " + keywords.getString(i)));
            }
        }

        document.close();
        System.out.println("Đã tạo PDF tại: " + outputPath);
    }
}
