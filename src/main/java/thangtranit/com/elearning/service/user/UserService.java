package thangtranit.com.elearning.service.user;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import thangtranit.com.elearning.dto.request.user.ChangeEmailRequest;
import thangtranit.com.elearning.dto.request.user.ChangePasswordRequest;
import thangtranit.com.elearning.dto.request.user.OtpVerifyRequest;
import thangtranit.com.elearning.dto.request.user.UserProfileRequest;
import thangtranit.com.elearning.dto.response.user.*;
import thangtranit.com.elearning.entity.user.Friendship;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.Role;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.UserMapper;
import thangtranit.com.elearning.repository.user.FriendshipRepository;
import thangtranit.com.elearning.repository.user.UserRepository;
import thangtranit.com.elearning.service.util.CloudinaryService;
import thangtranit.com.elearning.service.util.JwtUtil;
import thangtranit.com.elearning.service.util.OtpService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final OtpService otpService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final RegisterService registerService;
    private final JwtUtil jwtUtil;
    @NonFinal
    private final int pageSize = 10;

    public UserProfileResponse getProfile(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        UserProfileResponse response = userMapper.toUserProfileResponse(user);
        String currentUserId = getCurrentUserId();
        if (!isAnonymous() && !Objects.equals(currentUserId, id)) {
            String a = (currentUserId.compareTo(id) > 0) ? id : currentUserId;
            String b = (currentUserId.compareTo(id) < 0) ? id : currentUserId;
            Optional<Friendship> friendship = friendshipRepository.findByA_IdAndB_Id(a, b);
            if (friendship.isPresent()) {
                if (friendship.get().isAccepted()) {
                    response.setFriend(true);
                } else if (Objects.equals(friendship.get().getSender().getId(), currentUserId)) {
                    response.setAddFriendRequestSent(1);
                } else {
                    response.setAddFriendRequestSent(-1);
                }
            }
        }
        return response;
    }

    public UserProfileResponse getProfile() {
        User user = userRepository.findById(getCurrentUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserProfileResponse(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<UserAdminViewResponse> getAllUser(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageable).map(userMapper::toUserAdminViewResponse);
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
    }


    private void doChangePassword(User user, String newPassword) {
        //check if user is not app user
        if (!user.getPlatform().equals(Platform.APP)) {
            throw new AppException(ErrorCode.OAUTH2_USER_CAN_NOT_CHANGE_LOGIN_INFO);
        }

        otpService.useOtp(user.getEmail());
        //change email and save
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean checkExists(String email, Platform platform) {
        return userRepository.existsByEmailAndPlatform(email, platform);
    }


    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        doChangePassword(user, request.getNewPassword());
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        doChangePassword(user, request.getNewPassword());
    }

    public String changeAvatar(MultipartFile avatar) {
        String url = cloudinaryService.upload(avatar, CloudinaryService.SQUARE_100);
        User user = getCurrentUser();
        String old = user.getAvatar();
        user.setAvatar(url);
        cloudinaryService.deleteImage(old);
        userRepository.save(user);
        return url;
    }

    public ChangeEmailResponse changeEmail(ChangeEmailRequest request) {
        User user = getCurrentUser();
        //check if user is not app user
        if (!user.getPlatform().equals(Platform.APP)) {
            throw new AppException(ErrorCode.OAUTH2_USER_CAN_NOT_CHANGE_LOGIN_INFO);
        }
        //check if new email has been used
        if (userRepository.existsByEmailAndPlatform(request.getEmail(), Platform.APP)) {
            throw new AppException(ErrorCode.EMAIL_HAS_BEEN_USED);
        }
        //check if old email and new email are equals
        if (user.getEmail().equals(request.getEmail())) {
            throw new AppException(ErrorCode.OLD_EMAIL_AND_NEW_EMAIL_ARE_THE_SAME);
        }

        otpService.useOtp(user.getEmail());

        //change email and save
        user.setEmail(request.getEmail());
        user.setVerified(false);
        userRepository.save(user);
        registerService.sendVerificationEmail(user.getEmail(), registerService.createAndSaveVerifyEmailCode(user));
        return ChangeEmailResponse.builder()
                .email(request.getEmail())
                .build();
    }

    public void prepare() {
        User user = getCurrentUser();
        if (!user.getPlatform().equals(Platform.APP)) {
            throw new AppException(ErrorCode.OAUTH2_USER_CAN_NOT_CHANGE_LOGIN_INFO);
        }
        otpService.sendOtp(user.getEmail());
    }

    public void prepare(String email) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        otpService.sendOtp(user.getEmail());
    }

    public OtpVerifyResponse verifyOtp(OtpVerifyRequest request) {
        User user = getCurrentUser();
        return otpService.verifyOtp(user.getEmail(), request.getOtp());
    }

    public OtpVerifyResponse verifyOtp(String email, OtpVerifyRequest request) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return otpService.verifyOtp(user.getEmail(), request.getOtp());
    }

    public Page<UserMinimumInfoResponse> findUsersByName(String name, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);

        return userRepository.findByNameContaining(name, pageable).map(userMapper::toUserMinimumInfoResponse);
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        User user = getCurrentUser();
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserProfileResponse(user);
    }

    public void delete() {
        userRepository.delete(getCurrentUser());
    }

    public String getCurrentUserId() {
        return jwtUtil.getCurrentUserId();
    }

    public User getCurrentUser() {
        return getUser(getCurrentUserId());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserAdminViewResponse grantAuthority(String id, String grant) {
        // Lấy thông tin user từ id
        User user = getUser(id);

        // Tách chuỗi role của user thành Set<Role>
        Set<Role> userRoles = parseRoles(user);

        // Thêm quyền mới cho user
        addRoleToUser(userRoles, grant);

        // Cập nhật lại role và lưu user
        updateUserRoles(user, userRoles);

        // Trả về thông tin user đã cập nhật
        return userMapper.toUserAdminViewResponse(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserAdminViewResponse revokeAuthority(String id, String revoke) {
        // Lấy thông tin user từ id
        User user = getUser(id);
        if(user.getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.CANT_NOT_SEFT_REVOKE_YOUR_ROLES);
        }
        // Tách chuỗi role của user thành Set<Role>
        Set<Role> userRoles = parseRoles(user);

        // Thu hồi quyền từ user
        removeRoleFromUser(userRoles, revoke);

        // Cập nhật lại role và lưu user
        updateUserRoles(user, userRoles);

        // Trả về thông tin user đã cập nhật
        return userMapper.toUserAdminViewResponse(user);
    }

    // Hàm bổ sung

    // Tách chuỗi role của user thành Set<Role>
    private Set<Role> parseRoles(User user) {
        return Arrays.stream(user.getRoles().split(" "))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    // Thêm quyền cho user
    private void addRoleToUser(Set<Role> userRoles, String grant) {
        try {
            Role role = Role.valueOf(grant);
            if (userRoles.contains(role)) {
                throw new AppException(ErrorCode.USER_HAS_ALREADY_HAVE_THE_ROLE);
            }
            userRoles.add(role);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.ROLE_IS_INVALID, Map.of("Quyền không hợp lệ", grant));
        }
    }

    // Thu hồi quyền từ user
    private void removeRoleFromUser(Set<Role> userRoles, String revoke) {
        try {
            Role role = Role.valueOf(revoke);
            if (!userRoles.contains(role)) {
                throw new AppException(ErrorCode.USER_DOES_NOT_HAVE_THE_ROLE);
            }
            userRoles.remove(role);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.ROLE_IS_INVALID, Map.of("Quyền không hợp lệ", revoke));
        }
    }

    // Cập nhật lại roles cho user và lưu
    private void updateUserRoles(User user, Set<Role> userRoles) {
        user.setRoles(userRoles.stream()
                .map(Role::name)
                .collect(Collectors.joining(" ")));
        userRepository.save(user);
    }

    public boolean isAnonymous() {
        return jwtUtil.isAnonymousUser();
    }
}

