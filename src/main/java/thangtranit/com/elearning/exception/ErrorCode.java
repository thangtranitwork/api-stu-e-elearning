package thangtranit.com.elearning.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Các mã lỗi liên quan đến người dùng (100-199)
    USER_ALREADY_EXISTS(100, "Người dùng đã tồn tại", HttpStatus.CONFLICT),
    USER_NOT_EXISTS(101, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_EMAIL(102, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(103, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(104, "Đăng nhập thất bại, vui lòng kiểm tra thông tin đăng nhập", HttpStatus.UNAUTHORIZED),
    OTP_NOT_FOUND(105, "Chưa xác minh OTP hoặc OTP trước đó đã hết hạn", HttpStatus.NOT_FOUND),
    OTP_HAS_ALREADY_EXPIRED(106, "OTP đã hết hạn", HttpStatus.GONE),
    NO_EMAIL_USER(107, "Không có người dùng nào sử dụng email này", HttpStatus.NOT_FOUND),
    EMAIL_HAS_BEEN_USED(108, "Email đã được sử dụng", HttpStatus.CONFLICT),
    INVALID_NEW_PASSWORD(109, "Mật khẩu mới phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_NEW_EMAIL(110, "Email mới không hợp lệ", HttpStatus.BAD_REQUEST),
    OTP_NOT_VERIFIED(111, "OTP chưa được xác minh", HttpStatus.FORBIDDEN),
    OAUTH2_LOGIN_HAS_NO_EMAIL(112, "Đăng nhập bằng OAuth2 không có email", HttpStatus.BAD_REQUEST),
    USER_HAS_NOT_VERIFIED_EMAIL(115, "Người dùng này chưa xác minh email", HttpStatus.FORBIDDEN),
    OLD_EMAIL_AND_NEW_EMAIL_ARE_THE_SAME(116, "Email cũ và email mới giống nhau", HttpStatus.BAD_REQUEST),
    OAUTH2_USER_CAN_NOT_CHANGE_LOGIN_INFO(118, "Người dùng OAuth2 không thể thay đổi thông tin đăng nhập", HttpStatus.FORBIDDEN),
    VERIFY_CODE_DOES_NOT_EXIST(120, "Không tồn tại mã xác thực này", HttpStatus.NOT_FOUND),
    VERIFY_CODE_TIMEOUT(121, "Mã xác thực đã hết hạn", HttpStatus.GONE),
    VERIFY_CODE_INVALID(122, "Mã xác thực không hợp lệ", HttpStatus.BAD_REQUEST),
    OTP_HAS_EXCEED_THE_NUMBER_OF_TRIES(123, "OTP đã vượt quá số lần thử", HttpStatus.FORBIDDEN),
    OTP_INVALID(123, "OTP không hợp lệ", HttpStatus.BAD_REQUEST),
    THIS_USER_HAS_BEEN_LOCKED(124, "Người dùng này đã bị khóa do nhập sai mật khẩu nhiều lần", HttpStatus.LOCKED),
    HAVE_ALREADY_BEEN_FRIEND(125, "Đã là bạn với người dùng này", HttpStatus.CONFLICT),
    IS_NOT_FRIEND(126, "Không phải bạn của người dùng này", HttpStatus.CONFLICT),
    CAN_NOT_SEND_ADD_FRIEND_INVITATION_TO_YOURSELF(127, "Không thể tự gửi kết bạn với chính mình", HttpStatus.CONFLICT),
    ADD_FRIEND_INVITATION_NOT_FOUND(128, "Không tìm thấy lời mời kết bạn này", HttpStatus.NOT_FOUND),
    ADD_FRIEND_INVITATION_ALREADY_SENT(129, "Đã gửi lời mời kết bạn đến người dùng này", HttpStatus.CONFLICT),
    CAN_NOT_UNFRIEND_YOURSELF(130, "Không thể hủy kết bạn với chính mình", HttpStatus.CONFLICT),
    CAN_NOT_REJECT_YOURSELF(131, "Không thể từ chối chính mình", HttpStatus.CONFLICT),
    CAN_NOT_REJECT_OWN_INVITATION(132, "Không thể là bạn với chính mình", HttpStatus.CONFLICT),
    CAN_NOT_BE_FRIEND_WITH_YOURSELF(133, "Không thể tự gửi kết bạn với chính mình", HttpStatus.CONFLICT),
    MESSAGE_CANNOT_BE_EMPTY(134, "Tin nhắn không được để trống", HttpStatus.BAD_REQUEST),
    // Các mã lỗi liên quan đến token (300-399)
    TOKEN_IS_EXPIRED_OR_INVALID(300, "Token đã hết hạn hoặc không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_HAS_BEEN_LOGGED_OUT(301, "Token đã bị đăng xuất", HttpStatus.UNAUTHORIZED),
    // Các mã lỗi liên quan đến truy cập (400-499)
    ACCESS_DENIED(403, "Không có quyền xem hoặc chỉnh sửa tài nguyên này", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(401, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    NO_RESOURCE_FOUND(404, "Không tồn tại", HttpStatus.NOT_FOUND),
    FORBIDDEN_WORD(405, "Từ khóa không hợp lệ!", HttpStatus.FORBIDDEN),
    FORBIDDEN_WORD_NOT_FOUND(406, "Từ khóa không tồn tại", HttpStatus.NOT_FOUND),
    FORBIDDEN_WORD_HAS_ALREADY_EXISTS(407, "Từ khóa đã tồn tại", HttpStatus.CONFLICT),

    // Các mã lỗi liên quan đến bài kiểm tra (500-599)
    QUIZ_DOES_NOT_EXIST(500, "Bài kiểm tra không tồn tại", HttpStatus.NOT_FOUND),
    ANSWER_QUIZ_NOT_FOUND_OR_TIMEOUT(501, "Không tìm thấy câu trả lời bài kiểm tra hoặc đã hết giờ", HttpStatus.NOT_FOUND),
    ANSWER_DOES_NOT_EXIST(502, "Câu trả lời không tồn tại", HttpStatus.NOT_FOUND),
    QUESTION_DOES_NOT_EXISTS(503, "Câu hỏi không tồn tại", HttpStatus.NOT_FOUND),
    NUMBER_OF_ANSWERS_DOES_NOT_MATCH(504, "Số lượng câu trả lời không khớp", HttpStatus.BAD_REQUEST),
    THE_ANSWER_DOES_NOT_BELONG_TO_THE_QUESTION(505, "Câu trả lời không thuộc về câu hỏi", HttpStatus.BAD_REQUEST),
    QUESTION_MUST_HAVE_ONE_CORRECT_ANSWER(506, "Câu hỏi phải có một câu trả lời đúng", HttpStatus.BAD_REQUEST),
    ANSWER_QUIZ_DOES_NOT_CONTAIN_ALL_QUESTIONS(507, "Bài kiểm tra không chứa tất cả các câu hỏi", HttpStatus.BAD_REQUEST),
    ANSWER_CAN_NOT_BE_EMPTY(508, "Câu trả lời không được để trống", HttpStatus.BAD_REQUEST),
    QUESTION_CAN_NOT_BE_EMPTY(509, "Câu hỏi không được để trống", HttpStatus.BAD_REQUEST),
    ANSWER_LIST_SIZE_IS_INVALID(510, "Mỗi câu hỏi phải có ít nhất 2 câu trả lời", HttpStatus.BAD_REQUEST),
    QUIZ_NAME_LENGTH_IS_INVALID(511, "Tên bài trắc nghiệm không hợp lệ", HttpStatus.BAD_REQUEST),
    QUIZ_DESCRIPTION_LENGTH_IS_INVALID(512, "Mô tả bài trắc nghiệm không hợp lệ", HttpStatus.BAD_REQUEST),
    QUIZ_DURATION__IS_INVALID(513, "Thời gian làm bài trắc nghiệm không hợp lệ", HttpStatus.BAD_REQUEST),
    QUESTION_LIST_SIZE_IS_INVALID(514, "Mỗi bài trắc nghiệm phải có ít nhất 2 câu hỏi", HttpStatus.BAD_REQUEST),

    // Các mã lỗi liên quan đến khóa học (600-699)
    COURSE_NOT_EXISTS(600, "Khóa học không tồn tại", HttpStatus.NOT_FOUND),
    COURSE_PRICE_IS_INVALID(601, "Giá khóa học phải là số dương", HttpStatus.BAD_REQUEST),
    LESSON_NOT_EXISTS(602, "Bài học không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_STAR(603, "Đánh giá sao phải từ 0 đến 5", HttpStatus.BAD_REQUEST),
    THIS_USER_HAS_ALREADY_REVIEWED_THIS_COURSE(604, "Người dùng này đã đánh giá khóa học này", HttpStatus.CONFLICT),
    THE_LESSON_DOES_NOT_BELONG_TO_THE_COURSE(605, "Bài học không thuộc về khóa học", HttpStatus.BAD_REQUEST),
    THE_USER_HAS_NOT_ENROLLED_IN_THE_COURSE(606, "Người dùng chưa đăng ký khóa học", HttpStatus.FORBIDDEN),
    THE_USER_HAS_ALREADY_ENROLLED_IN_THE_COURSE(607, "Người dùng đã đăng ký khóa học", HttpStatus.CONFLICT),
    THE_USER_HAS_NOT_STARTED_THIS_LESSON(608, "Người dùng này chưa bắt đầu bài học này", HttpStatus.FORBIDDEN),
    THE_COURSE_REQUIRES_PAYMENT(609, "Khóa học này cần thanh toán", HttpStatus.BAD_REQUEST),
    THE_COURSE_DOES_NOT_REQUIRE_PAYMENT(610, "Khóa học này không cần thanh toán", HttpStatus.CONFLICT),
    PAY_FAILED(611, "Thanh toán thất bại", HttpStatus.BAD_REQUEST),
    COURSE_NAME_LENGTH_IS_INVALID(612, "Tên khóa học không hợp lệ", HttpStatus.BAD_REQUEST),
    COURSE_DESCRIPTION_LENGTH_IS_INVALID(613, "Mô tả khóa học không hợp lệ", HttpStatus.BAD_REQUEST),
    COURSE_INTRODUCTION_LENGTH_IS_INVALID(614, "Giới thiệu khóa học không hợp lệ", HttpStatus.BAD_REQUEST),
    LESSON_NAME_LENGTH_IS_INVALID(615, "Tên bài học không hợp lệ", HttpStatus.BAD_REQUEST),
    LESSON_THEORY_LENGTH_IS_INVALID(616, "Nội dung bài học không hợp lệ", HttpStatus.BAD_REQUEST),
    REVIEW_LENGTH_IS_INVALID(617, "Nội dung đánh giá không hợp lệ", HttpStatus.BAD_REQUEST),

    POST_IS_NOT_EXISTS(700, "Bài viết không tồn tại", HttpStatus.NOT_FOUND),
    TITLE_LENGTH_IS_INVALID(701, "Tiêu đề phải có độ dài từ 5-255 ký tự", HttpStatus.BAD_REQUEST),
    POST_CONTENT_MUST_HAVE_AT_LEAST_50_CHARACTER(702, "Bài viết phải có ít nhất 50 ký tự", HttpStatus.BAD_REQUEST),
    POST_MUST_HAVE_AT_LEAST_ONE_TAG(703, "Bài viết phải có ít nhất 1 thẻ", HttpStatus.BAD_REQUEST),
    COMMENT_IS_NOT_EXISTS(704, "Bình luận không tồn tại", HttpStatus.NOT_FOUND),
    THE_COMMENT_DOES_NOT_BELONG_TO_THE_POST(605, "Bình luận không thuộc về bài viết", HttpStatus.BAD_REQUEST),

    ROLE_IS_INVALID(901, "Phân quyền không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_HAS_ALREADY_HAVE_THE_ROLE(902, "Người dùng này đã có quyền này", HttpStatus.CONFLICT),
    USER_DOES_NOT_HAVE_THE_ROLE(903, "Người dùng nay không có quyền này", HttpStatus.CONFLICT),
    CANT_NOT_SEFT_REVOKE_YOUR_ROLES(904, "Không thể tự thu hồi quyền của chính mình", HttpStatus.BAD_REQUEST),
    // Lỗi không phân loại
    UNCATEGORIZED_ERROR(1000, "Lỗi không phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    NEED_CHANGE(0, "Sửa mã lỗi dùm cái", HttpStatus.BAD_REQUEST),

    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
