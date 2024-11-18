//package com.psm.domain.Subtitles.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.psm.domain.Subtitles.entity.SubtitlesDO;
//import com.psm.infrastructure.DB.SubtitlesMapper;
//import com.psm.domain.Subtitles.service.SubtitlesService;
//import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
//import com.psm.infrastructure.OSS.UploadOSS;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.net.URL;
//import java.util.List;
//
//@Setter
//@Service
//@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
//public class SubtitlesServiceImpl extends BaseDBRepositoryImpl<SubtitlesMapper, SubtitlesDO> implements SubtitlesService {
//    @Autowired
//    private SubtitlesMapper showcaseMapper;
//
//    @Autowired
//    UploadOSS uploadOSS;
//
//    String imageFolderPath;
//
//    String videoFolderPath;
//
//    @Override
//    public SubtitlesDO getSubtitlesById(Long id) {
//        try {
//            return showcaseMapper.selectById(id);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when getSubtitlesById: "+e.getMessage());
//        }
//    }
//
//    @Override
//    public List<SubtitlesDO> getSubtitlesByUserId(Long userId) {
//        try {
//            LambdaQueryWrapper<SubtitlesDO> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(SubtitlesDO::getUserId,userId);
//            return showcaseMapper.selectList(wrapper);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when getSubtitlesByUserId: "+e.getMessage());
//        }
//    }
//
//    @Override
//    public List<SubtitlesDO> getSubtitlesListByPage(Integer currentPage, Integer pageSize) {
//        try {
//            Page<SubtitlesDO> page = new Page<>(currentPage,pageSize);//当前第1页，每页3条数据
//            Page<SubtitlesDO> resultPage = showcaseMapper.selectPage(page,null);
//            List<SubtitlesDO> records = resultPage.getRecords();
//
//            return records;
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when getSubtitlesListByPage: "+e.getMessage());
//        }
//    }
//
//    @Override
//    public void addSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type) throws DuplicateKeyException{
//        try{
//            String coverUrl;
//            String videoUrl;
//
//            // 上传封面
//            try{
//                coverUrl = uploadOSS.multipartUpload(cover, imageFolderPath);
//            }
//            catch (Exception e){
//                throw new RuntimeException("Cover upload failed");
//            }
//
//            // 上传视频
//            try{
//                videoUrl = uploadOSS.multipartUpload(video, videoFolderPath);
//            }
//            catch (Exception e){
//                String coverOSSPath = new URL(coverUrl).getPath().substring(1);
//                uploadOSS.deleteFile(coverOSSPath);
//
//                throw new RuntimeException("Video upload failed");
//            }
//
//            // 将信息保存到数据库
//            SubtitlesDO showcaseDO = new SubtitlesDO();
//            showcaseDO.setTitle(title);
//            showcaseDO.setContent(content);
//            showcaseDO.setCover(coverUrl);
//            showcaseDO.setVideo(videoUrl);
//            showcaseDO.setStyle(style);
//            showcaseDO.setType(type);
//            save(showcaseDO);
//        }
//        catch (DuplicateKeyException e){
//            throw new RuntimeException("Upload failed, the showcase already exists");
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when addSubtitles: "+e.getMessage());
//        }
//    }
//
//    @Override
//    public void updateSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type) {
//        try{
//            // TODO 需要完善修改,缺修改oss的视频和封面
//            SubtitlesDO showcaseDO = new SubtitlesDO();
//            showcaseDO.setTitle(title);
//            showcaseDO.setContent(content);
//            showcaseDO.setStyle(style);
//            showcaseDO.setType(type);
//            updateById(showcaseDO);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when updateSubtitles: "+e.getMessage());
//        }
//   }
//
//    @Override
//   public void deleteSubtitles(Long id) {
//        try{
//            removeById(id);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Server error when deleteSubtitles: "+e.getMessage());
//        }
//   }
//}
