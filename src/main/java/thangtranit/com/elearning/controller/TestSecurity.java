package thangtranit.com.elearning.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thangtranit.com.elearning.dto.response.ApiResponse;


@RestController
@RequestMapping("/api")
public class TestSecurity {
    @GetMapping("/hello")
    private String hello(){
        return "hello";
    }
    @GetMapping("/required/hello")
    private String requiredHello(){
        return "hello";
    }

    @GetMapping("/test")
    private ApiResponse<String> get(HttpServletRequest request){
        return ApiResponse.<String>builder()
                .body(request.getHeaderNames().toString())
                .build();
    }
}
