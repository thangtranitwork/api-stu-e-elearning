package thangtranit.com.elearning.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thangtranit.com.elearning.entity.user.ForbiddenWord;
import thangtranit.com.elearning.repository.user.ForbiddenWordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ForbiddenWordsValidator implements ConstraintValidator<ForbiddenWords, String> {

    @Autowired
    private ForbiddenWordRepository forbiddenWordRepository;

    private List<String> forbiddenWords;

    @Override
    public void initialize(ForbiddenWords constraintAnnotation) {
        forbiddenWords = forbiddenWordRepository.findAll().stream()
                .map(ForbiddenWord::getWord)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Cho phép chuỗi rỗng hoặc null, không kiểm tra
        }

        // Tách chuỗi đầu vào thành các từ bằng cách sử dụng dấu cách làm phân cách
        String[] inputWords = value.split("\\s+");

        // Kiểm tra từng từ trong chuỗi đầu vào
        for (String inputWord : inputWords) {
            for (String forbiddenWord : forbiddenWords) {
                if (inputWord.equalsIgnoreCase(forbiddenWord)) {
                    return false; // Chuỗi không hợp lệ nếu bất kỳ từ nào khớp với từ bị cấm
                }
            }
        }

        return true; // Hợp lệ nếu không có từ nào bị cấm
    }
}
