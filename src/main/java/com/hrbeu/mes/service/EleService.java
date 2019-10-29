package com.hrbeu.mes.service;

import java.util.List;
import java.util.Map;

import com.hrbeu.mes.pojo.Electrolyze;
import com.hrbeu.mes.pojo.Template;

/**
 *
 */
public interface EleService {

    public List<Electrolyze> queryData();

    public void updateData(Map<String, String> map);

    public void saveTp(Map<String, String> map);

    public Template getTpByID(Map<String, String> map);

    public List<Template> getTpList(Map<String, String> map);

    public void saveDataMerge(Map<String, String> map);

    public void updateTp(Map<String, String> map);

}
