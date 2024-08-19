package com.psm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.Subtitles.SubtitlesVO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.mapper.SubtitlesMapper;
import com.psm.service.SubtitlesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubtitlesServiceImpl extends ServiceImpl<SubtitlesMapper, SubtitlesDAO> implements SubtitlesService {
    @Autowired
    private SubtitlesMapper showcaseMapper;

    public ResponseDTO getSubtitlesById(Long id) {
        try {
            SubtitlesDAO showcaseDAO = showcaseMapper.selectById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("result",showcaseDAO);
            return new ResponseDTO(HttpStatus.OK, "Query successful", map);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO getSubtitlesListByPage(Integer currentPage, Integer pageSize) {
        try {
            Page<SubtitlesDAO> page = new Page<>(currentPage,pageSize);//当前第1页，每页3条数据
            Page<SubtitlesDAO> resultPage = showcaseMapper.selectPage(page,null);
            List<SubtitlesDAO> records = resultPage.getRecords();

            Map<String, Object> map = new HashMap<>();
            map.put("result",records);
            return new ResponseDTO(HttpStatus.OK, "Query successful",map);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO addSubtitles(SubtitlesDAO subtitlesDAO) {
        try{

            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            BeanUtils.copyProperties(subtitlesDAO,showcaseDAO);
            save(showcaseDAO);

            SubtitlesVO SubtitlesVO = new SubtitlesVO();
            BeanUtils.copyProperties(showcaseDAO,SubtitlesVO);

            // 返回结果
            Map<String, Object> map = new HashMap<>();
            map.put("result",SubtitlesVO);
            return new ResponseDTO(HttpStatus.OK,"Upload successful",map);
        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"Upload failed, the showcase already exists");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO updateSubtitles(SubtitlesDAO subtitlesDAO) {
        try{
            SubtitlesDAO showcaseDAO = new SubtitlesDAO();
            BeanUtils.copyProperties(subtitlesDAO,showcaseDAO);
            updateById(showcaseDAO);
            return new ResponseDTO(HttpStatus.OK,"Update successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
   }

   public ResponseDTO deleteSubtitles(Long id) {
        try{
            removeById(id);
            return new ResponseDTO(HttpStatus.OK,"Delete successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
   }
}
