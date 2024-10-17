package thangtranit.com.elearning.service.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Enrollment;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.course.EnrollmentRepository;
import thangtranit.com.elearning.service.payment.PaymentInterface;
import thangtranit.com.elearning.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final PaymentInterface paymentInterface;

    public void enroll(String courseId) {
        User user = userService.getCurrentUser();
        Course course = courseService.getCourse(courseId);
        if(course.getPrice() > 0){
            throw new AppException(ErrorCode.THE_COURSE_REQUIRES_PAYMENT);
        }

        validateExistsEnrollment(courseId, user);

        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .student(user)
                .build();
        enrollmentRepository.save(enrollment);
    }

    private void validateExistsEnrollment(String courseId, User user) {
        enrollmentRepository.findByCourseIdAndStudentId(courseId, user.getId()).ifPresent(enrollment -> {
            throw new AppException(ErrorCode.THE_USER_HAS_ALREADY_ENROLLED_IN_THE_COURSE);
        });
    }

    public String buy(String courseId, HttpServletRequest request) throws JsonProcessingException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        User user = userService.getCurrentUser();
        Course course = courseService.getCourse(courseId);
        if(course.getPrice() <= 0) {
            throw new AppException(ErrorCode.THE_COURSE_DOES_NOT_REQUIRE_PAYMENT);
        }
        validateExistsEnrollment(courseId, user);
        String buyCourseInfo = generateBuyCourseInfo(course, user);
        return paymentInterface.createOrder(request, course.getPrice(), buyCourseInfo, baseUrl);
    }

    public String processPayment(HttpServletRequest request) throws JsonProcessingException {
        int paymentStatus = paymentInterface.orderReturn(request);
        ObjectMapper objectMapper = new ObjectMapper();
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        HashMap<String, Object> map = extractFromBuyCourseInfo(orderInfo);
        if (paymentStatus == 1) {
            map.put("paymentTime", paymentTime);
            map.put("transactionId", transactionId);
            String paymentInfo = objectMapper.writeValueAsString(map);
            Enrollment enrollment = Enrollment.builder()
                    .course(courseService.getCourse(String.valueOf(map.get("courseId"))))
                    .student(userService.getUser(String.valueOf(map.get("buyerId"))))
                    .paymentInfo(paymentInfo)
                    .build();
            enrollmentRepository.save(enrollment);
        }


        return String.valueOf(map.get("courseId"));
    }

    private String generateBuyCourseInfo(Course course, User buyer) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> info = Map.of("courseId", course.getId(), "courseName", course.getName(), "coursePrice", course.getPrice(), "buyerId" ,buyer.getId(), "buyerName", buyer.getLastname() + " " + buyer.getFirstname());

        return objectMapper.writeValueAsString(info);
    }

    private HashMap extractFromBuyCourseInfo(String info) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(info, HashMap.class);

    }
    
    public Enrollment getEnrollment(String courseId, String studentId) {
        return enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new AppException(ErrorCode.THE_USER_HAS_NOT_ENROLLED_IN_THE_COURSE));
    }


}
