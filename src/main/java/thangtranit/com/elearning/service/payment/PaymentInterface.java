package thangtranit.com.elearning.service.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface PaymentInterface {
    String createOrder(HttpServletRequest request, int amount, String orderInfor, String urlReturn);

    int orderReturn(HttpServletRequest request);
}
