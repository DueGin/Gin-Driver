package com.ginDriver.main.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.ginDriver.main.domain.dto.place.PlaceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DueGin
 */
public class PlaceListener implements ReadListener<PlaceDTO> {

    public static List<PlaceDTO> list = new ArrayList<>();

    @Override
    public void invoke(PlaceDTO data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
