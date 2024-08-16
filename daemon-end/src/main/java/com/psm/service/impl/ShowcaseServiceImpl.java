package com.psm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Showcase.ShowcaseDAO;
import com.psm.domain.Showcase.ShowcaseDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.mapper.ShowcaseMapper;
import com.psm.service.ShowcaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShowcaseServiceImpl extends ServiceImpl<ShowcaseMapper, ShowcaseDAO> implements ShowcaseService {
    @Autowired
    private ShowcaseMapper showcaseMapper;

    public ResponseDTO getShowcaseById(Long id) {
        try {
            ShowcaseDAO showcaseDAO = showcaseMapper.selectById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("result",showcaseDAO);
            return new ResponseDTO(HttpStatus.OK, "Query successful", map);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO getShowcaseListByPage(Integer currentPage, Integer pageSize) {
        try {
            Page<ShowcaseDAO> page = new Page<>(currentPage,pageSize);//当前第1页，每页3条数据
            Page<ShowcaseDAO> resultPage = showcaseMapper.selectPage(page,null);
            List<ShowcaseDAO> records = resultPage.getRecords();

            Map<String, Object> map = new HashMap<>();
            map.put("result",records);
            return new ResponseDTO(HttpStatus.OK, "Query successful",map);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO addShowcase(ShowcaseDTO showcaseDTO) {
        try{
            ShowcaseDAO showcaseDAO = new ShowcaseDAO();
            BeanUtils.copyProperties(showcaseDTO,showcaseDAO);
            save(showcaseDAO);
            return new ResponseDTO(HttpStatus.OK,"Upload successful");
        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"Upload failed, the showcase already exists");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    public ResponseDTO updateShowcase(ShowcaseDTO showcaseDTO) {
        try{
            ShowcaseDAO showcaseDAO = new ShowcaseDAO();
            BeanUtils.copyProperties(showcaseDTO,showcaseDAO);
            updateById(showcaseDAO);
            return new ResponseDTO(HttpStatus.OK,"Update successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
   }

   public ResponseDTO deleteShowcase(Long id) {
        try{
            removeById(id);
            return new ResponseDTO(HttpStatus.OK,"Delete successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
   }
}
