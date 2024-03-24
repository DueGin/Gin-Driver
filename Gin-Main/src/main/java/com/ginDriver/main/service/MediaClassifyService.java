package com.ginDriver.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.constant.ClassifyConstant;
import com.ginDriver.main.domain.vo.ClassifyVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaExifMapper;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    private MediaExifService mediaExifService;

    public List<ClassifyVO> getClassifyFolderList(String type) {
        List<ClassifyVO> res = new ArrayList<>();
        Long userId = SecurityUtils.getUserId();
        MediaExifMapper mediaExifMapper = mediaExifService.getBaseMapper();

        switch (type) {
            case ClassifyConstant.MONTH:
                List<Map<String, Integer>> maps = mediaExifMapper.selectGroupByMonth(userId);
                for (Map<String, Integer> map : maps) {
                    String month = String.valueOf(map.get("month"));
                    String year = String.valueOf(map.get("year"));
                    res.add(new ClassifyVO()
                            .setName(year + "年" + month + "月")
                            .setId(year + "_" + month)
                    );
                }
                break;
            case ClassifyConstant.YEAR:
                List<String> years = mediaExifMapper.selectGroupByYear(userId);
                years.forEach(y -> res.add(new ClassifyVO(y, y + "年")));
                break;
            case ClassifyConstant.PROVINCE:
                log.info("省份分类");
                List<Map<String, Object>> provinceAdCodeMap = mediaExifMapper.selectGroupByProvinceAdCode(userId);
                for (Map<String, Object> map : provinceAdCodeMap) {
                    String provinceName = (String) map.get("provinceName");
                    String adcode = String.valueOf(map.get("provinceAdcode"));
                    res.add(new ClassifyVO(adcode, provinceName));
                }
                break;
            case ClassifyConstant.CITY:
                log.info("城市分类");
                List<Map<String, Object>> cityAdCodeMap = mediaExifMapper.selectGroupByCityAdCode(userId);
                for (Map<String, Object> map : cityAdCodeMap) {
                    String cityName = (String) map.get("cityName");
                    String adcode = String.valueOf(map.get("cityAdcode"));
                    res.add(new ClassifyVO(adcode, cityName));
                }
                break;
            default:
                log.error("没有该分类");
        }

        return res;
    }

    public Page<MediaVO> getDetailPageByClassifyId(Page<MediaVO> page, String type, String classifyId) {
        MediaMapper mediaMapper = mediaService.getBaseMapper();
        Long userId = SecurityUtils.getUserId();

        switch (type) {
            case ClassifyConstant.MONTH:
                String[] split = classifyId.split("_");
                String year = split[0];
                String month = split[1];
                LocalDate startDate1 = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
                LocalDate endDate1 = startDate1.plusMonths(1).minusDays(1);

                mediaMapper.selectPageBetweenDate(page, startDate1, endDate1, userId);
                break;
            case ClassifyConstant.YEAR:
                LocalDate startDate2 = LocalDate.of(Integer.parseInt(classifyId), 1, 1);
                LocalDate endDate2 = startDate2.plusYears(1).minusDays(1);
                mediaMapper.selectPageBetweenDate(page, startDate2, endDate2, userId);
                break;
            case ClassifyConstant.PROVINCE:
                log.info("省份分类");
                mediaMapper.selectByProvinceAdCode(page, Integer.valueOf(classifyId), userId);
                break;
            case ClassifyConstant.CITY:
                log.info("市分类");
                mediaMapper.selectByCityAdCode(page, Integer.valueOf(classifyId), userId);
                break;
            default:
                log.error("没有该分类");
        }

        // 设置minio url
        if (!page.getRecords().isEmpty()) {
            mediaService.setObjectDbUrl(page.getRecords());
        }

        return page;
    }
}
