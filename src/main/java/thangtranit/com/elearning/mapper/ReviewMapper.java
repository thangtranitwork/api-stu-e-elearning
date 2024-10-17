package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.request.course.ReviewRequest;
import thangtranit.com.elearning.dto.response.course.ReviewResponse;
import thangtranit.com.elearning.entity.course.Review;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ReviewMapper {

    public abstract Review toReview(ReviewRequest request);
    public abstract ReviewResponse toReviewResponse(Review review);
}
