package helpers;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;

public class EmailHelper {

    private String host;
    private String username;
    private String password;
    private String protocol = "imaps";

    public EmailHelper(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    /**
     * Lấy mã OTP từ email có tiêu đề chứa subjectKeyword.
     * @param subjectKeyword từ khóa trong tiêu đề (ví dụ: "Email OTP")
     * @return OTP code nếu tìm thấy, ngược lại trả về null.
     * @throws Exception
     */
    public String fetchOtpFromEmail(String subjectKeyword) throws Exception {
        // Thiết lập properties cho session
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", protocol);
        Session session = Session.getInstance(props, null);

        // Kết nối đến email server
        Store store = session.getStore(protocol);
        store.connect(host, username, password);

        // Mở folder INBOX để đọc email
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        String otpCode = null;
        // Duyệt ngược (lấy email mới nhất trước)
        for (int i = messages.length - 1; i >= 0; i--) {
            Message message = messages[i];
            String subject = message.getSubject();
            if (subject != null && subject.contains(subjectKeyword)) {
                // Lấy nội dung email dưới dạng text
                String content = getTextFromMessage(message);
                // Ví dụ mẫu: "Hi, your Agoda OTP is 868574. It's valid for 10 minutes."
                Pattern pattern = Pattern.compile("Agoda OTP is (\\d{6})");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    otpCode = matcher.group(1);
                    break;
                }
            }
        }

        inbox.close(false);
        store.close();
        return otpCode;
    }

    /**
     * Lấy nội dung text từ Message. Hỗ trợ cả trường hợp text/plain và text/html.
     */
    private String getTextFromMessage(Message message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) content);
        }
        return "";
    }

    /**
     * Trích xuất text từ MimeMultipart.
     */
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                // Nếu là HTML, có thể sử dụng thư viện jsoup để loại bỏ thẻ HTML nếu cần
                String html = (String) bodyPart.getContent();
                result.append(html); // hoặc xử lý với jsoup: Jsoup.parse(html).text()
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    public String getOPTCode(String subjectKeyword) {
        try {
            return fetchOtpFromEmail(subjectKeyword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Ví dụ cách sử dụng:
//    public static void main(String[] args) {
//        // Thay đổi các thông số dưới đây theo cấu hình của bạn.
//        String host = "imap.gmail.com";
//        String username = "thai07t3@gmail.com";
//        String password = "vkid txmb mlkx mcgc";
//        String subjectKeyword = "Email OTP";
//
//        EmailHelper emailHelper = new EmailHelper(host, username, password);
//        try {
//            String otp = emailHelper.fetchOtpFromEmail(subjectKeyword);
//            if (otp != null) {
//                System.out.println("OTP code lấy được: " + otp);
//            } else {
//                System.out.println("Không tìm thấy OTP trong email.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

