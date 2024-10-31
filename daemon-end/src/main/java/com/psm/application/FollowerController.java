package com.psm.application;

import com.psm.domain.User.follower.adaptor.FollowerAdaptor;
import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/followers")
public class FollowerController {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private FollowerAdaptor followerAdaptor;

    /**
     * 检查两个用户是否关注
     * @return ResponseVO
     */
    @GetMapping("/{tgtUserId}/{srcUserId}")
    public ResponseVO checkFollowing(@PathVariable Long tgtUserId, @PathVariable Long srcUserId) {
        try {
            return ResponseVO.ok(followerAdaptor.getByTgUserIdAndSrcUserId(tgtUserId, srcUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 检查当前用户是否关注了目标用户
     * @return ResponseVO
     */
    @GetMapping("/{tgtUserId}/self")
    public ResponseVO checkFollowing(@PathVariable Long tgtUserId) {
        // 获取当前用户id
        Long srcUserId = userAdaptor.getAuthorizedUserId();

        return checkFollowing(tgtUserId, srcUserId);
    }

    /**
     * 获取当前用户的关注对象
     * @return ResponseVO
     */
    @GetMapping
    public ResponseVO checkFollowing() {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            return ResponseVO.ok(followerAdaptor.getBySrcUserId(srcUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 获取谁关注了当前用户
     * @return ResponseVO
     */
    @GetMapping("/self")
    public ResponseVO checkFollower() {
        try {
            // 获取当前用户id
            Long tgtUserId = userAdaptor.getAuthorizedUserId();

            return ResponseVO.ok(followerAdaptor.getByTgtUserId(tgtUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 关注用户
     * @return ResponseVO
     */
    @PostMapping
    public ResponseVO followingUser(@RequestBody UserDTO userDTO) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            // 关注用户
            followerAdaptor.addFollower(userDTO.getId(), srcUserId);

            return ResponseVO.ok("Get users successful");
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 取消关注用户
     * @return ResponseVO
     */
    @DeleteMapping
    public ResponseVO unFollowingUser(@RequestBody UserDTO userDTO) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            followerAdaptor.removeByTgUserIdAndSrcUserId(userDTO.getId(), srcUserId);

            return ResponseVO.ok("Unfollow user successful");
        }
        catch (IllegalArgumentException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (Exception e) {
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}
