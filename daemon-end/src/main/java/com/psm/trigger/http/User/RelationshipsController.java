package com.psm.trigger.http.User;

import com.psm.domain.IndependentDomain.User.relationships.adaptor.RelationshipsAdaptor;
import com.psm.domain.IndependentDomain.User.user.adaptor.UserAdaptor;
import com.psm.types.common.DTO.ResponseDTO;
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
    public ResponseDTO checkFollowShip(@PathVariable Long tgtUserId, @PathVariable Long srcUserId) {
        try {

            return ResponseDTO.ok(relationshipsAdaptor.checkFollowShip(tgtUserId, srcUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 检查当前用户是否关注了目标用户
     * @return ResponseVO
     */
    @GetMapping("/{tgtUserId}/self")
    public ResponseDTO checkFollowing(@PathVariable Long tgtUserId) {
        // 获取当前用户id
        Long srcUserId = userAdaptor.getAuthorizedUserId();

        return checkFollowShip(tgtUserId, srcUserId);
    }

    /**
     * 获取当前用户的关注对象
     * @return ResponseVO
     */
    @GetMapping
    public ResponseDTO checkFollowing() {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            return ResponseDTO.ok(relationshipsAdaptor.checkFollowing(srcUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 获取谁关注了当前用户
     * @return ResponseVO
     */
    @GetMapping("/self")
    public ResponseDTO checkFollowers() {
        try {
            // 获取当前用户id
            Long tgtUserId = userAdaptor.getAuthorizedUserId();

            return ResponseDTO.ok(relationshipsAdaptor.checkFollowers(tgtUserId));
        }
        catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 关注用户
     * @return ResponseVO
     */
    @PostMapping("/{tgtUserId}")
    public ResponseDTO followUser(@PathVariable Long tgtUserId) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            // 关注用户
            relationshipsAdaptor.addFollowing(tgtUserId, srcUserId);

            return ResponseDTO.ok("Get users successful");
        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "DuplicateKey");
        }
        catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "Invalid parameter");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 取消关注用户
     * @return ResponseVO
     */
    @DeleteMapping("/{tgtUserId}")
    public ResponseDTO unFollowUser(@PathVariable Long tgtUserId) {
        try {
            // 获取当前用户id
            Long srcUserId = userAdaptor.getAuthorizedUserId();

            relationshipsAdaptor.removeFollowing(tgtUserId, srcUserId);

            return ResponseDTO.ok("Unfollow user successful");
        }
        catch (IllegalArgumentException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "Invalid parameter");
        }
        catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}