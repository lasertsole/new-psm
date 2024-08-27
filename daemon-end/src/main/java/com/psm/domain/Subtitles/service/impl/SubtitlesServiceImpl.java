package com.psm.domain.Subtitles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.repository.SubtitlesMapper;
import com.psm.domain.Subtitles.service.SubtitlesService;
import com.psm.utils.DTO.ResponseDTO;
import com.psm.utils.OSS.UploadOSSUtil;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service
@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
public class SubtitlesServiceImpl extends ServiceImpl<SubtitlesMapper, SubtitlesDAO> implements SubtitlesService {
    @Autowired
    private SubtitlesMapper showcaseMapper;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

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
    public void addSubtitles(SubtitlesDTO subtitlesDTO) throws DuplicateKeyException{
        try{
            String coverUrl;
            String videoUrl;

            // 上传封面
            try{
                coverUrl = uploadOSSUtil.multipartUpload(subtitlesDTO.getCover(),imageFolderPath);
            }
            catch (Exception e){
                throw new RuntimeException("Cover upload failed");
            }

            // 上传视频
            try{
                videoUrl = uploadOSSUtil.multipartUpload(subtitlesDTO.getCover(),videoFolderPath);
            }
            catch (Exception e){
                String coverOSSPath = new URL(coverUrl).getPath().substring(1);
                uploadOSSUtil.deleteFile(coverOSSPath);

                throw new RuntimeException("Video upload failed");
            }

            // 将信息保存到数据库
            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            BeanUtils.copyProperties(subtitlesDTO, showcaseDAO);
            showcaseDAO.setCover(coverUrl);
            showcaseDAO.setVideo(videoUrl);
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
    public void updateSubtitles(SubtitlesDTO subtitlesDTO) {
        try{
            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            BeanUtils.copyProperties(subtitlesDTO,showcaseDAO);
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
