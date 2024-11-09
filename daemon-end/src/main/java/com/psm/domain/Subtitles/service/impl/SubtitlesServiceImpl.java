package com.psm.domain.Subtitles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.infrastructure.DB.SubtitlesMapper;
import com.psm.domain.Subtitles.service.SubtitlesService;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import com.psm.infrastructure.OSS.UploadOSS;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Setter
@Service
@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
public class SubtitlesServiceImpl extends BaseDBRepositoryImpl<SubtitlesMapper, SubtitlesDAO> implements SubtitlesService {
    @Autowired
    private SubtitlesMapper showcaseMapper;

    @Autowired
    UploadOSS uploadOSS;

    String imageFolderPath;

    String videoFolderPath;

    @Override
    public SubtitlesDAO getSubtitlesById(Long id) {
        try {
            return showcaseMapper.selectById(id);
        }
        catch (Exception e){
            throw new RuntimeException("Server error when getSubtitlesById: "+e.getMessage());
        }
    }

    @Override
    public List<SubtitlesDAO> getSubtitlesByUserId(Long userId) {
        try {
            LambdaQueryWrapper<SubtitlesDAO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SubtitlesDAO::getUserId,userId);
            return showcaseMapper.selectList(wrapper);
        }
        catch (Exception e){
            throw new RuntimeException("Server error when getSubtitlesByUserId: "+e.getMessage());
        }
    }

    @Override
    public List<SubtitlesDAO> getSubtitlesListByPage(Integer currentPage, Integer pageSize) {
        try {
            Page<SubtitlesDAO> page = new Page<>(currentPage,pageSize);//当前第1页，每页3条数据
            Page<SubtitlesDAO> resultPage = showcaseMapper.selectPage(page,null);
            List<SubtitlesDAO> records = resultPage.getRecords();

            return records;
        }
        catch (Exception e){
            throw new RuntimeException("Server error when getSubtitlesListByPage: "+e.getMessage());
        }
    }

    @Override
    public void addSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type) throws DuplicateKeyException{
        try{
            String coverUrl;
            String videoUrl;

            // 上传封面
            try{
                coverUrl = uploadOSS.multipartUpload(cover, imageFolderPath);
            }
            catch (Exception e){
                throw new RuntimeException("Cover upload failed");
            }

            // 上传视频
            try{
                videoUrl = uploadOSS.multipartUpload(video, videoFolderPath);
            }
            catch (Exception e){
                String coverOSSPath = new URL(coverUrl).getPath().substring(1);
                uploadOSS.deleteFile(coverOSSPath);

                throw new RuntimeException("Video upload failed");
            }

            // 将信息保存到数据库
            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            showcaseDAO.setTitle(title);
            showcaseDAO.setContent(content);
            showcaseDAO.setCover(coverUrl);
            showcaseDAO.setVideo(videoUrl);
            showcaseDAO.setStyle(style);
            showcaseDAO.setType(type);
            save(showcaseDAO);
        }
        catch (DuplicateKeyException e){
            throw new RuntimeException("Upload failed, the showcase already exists");
        }
        catch (Exception e){
            throw new RuntimeException("Server error when addSubtitles: "+e.getMessage());
        }
    }

    @Override
    public void updateSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type) {
        try{
            // TODO 需要完善修改,缺修改oss的视频和封面
            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            showcaseDAO.setTitle(title);
            showcaseDAO.setContent(content);
            showcaseDAO.setStyle(style);
            showcaseDAO.setType(type);
            updateById(showcaseDAO);
        }
        catch (Exception e){
            throw new RuntimeException("Server error when updateSubtitles: "+e.getMessage());
        }
   }

    @Override
   public void deleteSubtitles(Long id) {
        try{
            removeById(id);
        }
        catch (Exception e){
            throw new RuntimeException("Server error when deleteSubtitles: "+e.getMessage());
        }
   }
}
