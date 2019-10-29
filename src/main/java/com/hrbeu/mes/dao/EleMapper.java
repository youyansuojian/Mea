package com.hrbeu.mes.dao;

import java.util.List;
import java.util.Map;

import com.hrbeu.mes.pojo.Electrolyze;
import com.hrbeu.mes.pojo.Template;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 */

@Mapper
public interface EleMapper {
    public List<Electrolyze> queryData();

    public void updateData(Map<String, String> map);

    public void saveTp(Map<String, String> map);

    public Template getTpByID(Map<String, String> map);

    public List<Template> getTpList(Map<String, String> map);

    public void saveDataMerge(Map<String, String> map);

    public void updateTp(Map<String, String> map);

}