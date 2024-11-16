package com.psm.trigger.http.User;

import com.psm.domain.User.relationships.adaptor.RelationshipsAdaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.utils.VO.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/followers")
public class RelationshipsController {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private RelationshipsAdaptor relationshipsAdaptor;

    /**
     * 检查src用户是否关注tgt用户
     * @return ResponseVO
     */
    @GetMapping("/{tgtUserId}/{srcUserId}")
    public ResponseVO checkFollowShip(@PathVariable Long tgtUserId, @PathVariable Long srcUserId) {
        try {
            return ResponseVO.ok(relationshipsAdaptor.checkFollowShip(tgtUserId, srcUserId));
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

        return checkFollowShip(tgtUserId, srcUserId);
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

            return ResponseVO.ok(relationshipsAdaptor.checkFollowing(srcUserId));
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
    public ResponseVO checkFollowers() {
        try {
            // 获取当前用户id
            Long tgtUserId = userAdaptor.getAuthorizedUserId();

            return ResponseVO.ok(relationshipsAdaptor.checkFollowers(tgtUserId));
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
    @PostMapping("/{tgtUserId}")
    public ResponseVO followUser(@PathVariable Long tgtUserId) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            // 关注用户
            relationshipsAdaptor.addFollowing(tgtUserId, srcUserId);

            return ResponseVO.ok("Get users successful");
        }
        catch (DuplicateKeyException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "DuplicateKey");
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "Invalid parameter");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 取消关注用户
     * @return ResponseVO
     */
    @DeleteMapping("/{tgtUserId}")
    public ResponseVO unFollowUser(@PathVariable Long tgtUserId) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            relationshipsAdaptor.removeFollowing(tgtUserId, srcUserId);

            return ResponseVO.ok("Unfollow user successful");
        }
        catch (IllegalArgumentException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST, "Invalid parameter");
        }
        catch (Exception e) {
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}