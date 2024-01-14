package com.ginDriver.main.service;

import com.ginDriver.main.constant.ClassifyConstant;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.ClassifyVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
@Service
@Slf4j
public class MediaClassifyService {
    @Resource
    private MediaService mediaService;

    public List<ClassifyVO> getClassifyFolderList(String type) {
        List<ClassifyVO> res = new ArrayList<>();
        MediaMapper mapper = (MediaMapper) mediaService.getMapper();
        switch (type) {
            case ClassifyConstant.MONTH:
                List<Map<String, Integer>> maps = mapper.selectGroupByMonth();
                for (Map<String, Integer> map : maps) {
                    Integer month = map.get("month");
                    Integer year = map.get("year");
                    res.add(new ClassifyVO()
                            .setName(year + "年" + month + "月")
                            .setId(year + "_" + month));
                }
                break;
            case ClassifyConstant.YEAR:
                List<String> years = mapper.selectGroupByYear();
                years.forEach(y -> res.add(new ClassifyVO(y, y + "年")));
                break;
            case ClassifyConstant.PLACE:
                log.info("地点分类");
                break;
            default:
                log.error("没有该分类");
        }

        return res;
    }

    public List<MediaVO> getDetailListByClassifyId(String type, String classifyId) {
        MediaMapper mapper = (MediaMapper) mediaService.getMapper();
        List<MediaVO> vos = new ArrayList<>();
        switch (type) {
            case ClassifyConstant.MONTH:
                String[] split = classifyId.split("_");
                String year = split[0];
                String month = split[1];
                LocalDate startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
                LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                List<Media> list = mapper.selectBetweenDate(startDate, endDate);
                for (Media media : list) {
                    MediaVO vo = new MediaVO();
                    BeanUtils.copyProperties(media, vo);
                    vos.add(vo);
                }
                break;
            case ClassifyConstant.YEAR:
                break;
            case ClassifyConstant.PLACE:
                log.info("地点分类");
                break;
            default:
                log.error("没有该分类");
        }
        return vos;
    }
}
