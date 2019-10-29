package com.hrbeu.mes.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hrbeu.mes.dao.EleMapper;
import com.hrbeu.mes.pojo.Electrolyze;
import com.hrbeu.mes.pojo.Template;
import com.hrbeu.mes.service.EleService;


@Service
public class EleServiceImpl implements EleService {

    @Resource(name = "eleMapper")
    private EleMapper eleMapper;

    @Override
    public List<Electrolyze> queryData() {
        // TODO Auto-generated method stub

        return eleMapper.queryData();
    }

    @Override
    public void updateData(Map<String, String> map) {
        // TODO Auto-generated method stub
        eleMapper.updateData(map);
    }

    @Override
    public void saveTp(Map<String, String> map) {
        // TODO Auto-generated method stub
        eleMapper.saveTp(map);
    }

    @Override
    public Template getTpByID(Map<String, String> map) {
        // TODO Auto-generated method stub
        return eleMapper.getTpByID(map);
    }

    @Override
    public List<Template> getTpList(Map<String, String> map) {
        // TODO Auto-generated method stub
        return eleMapper.getTpList(map);
    }

    @Override
    public void saveDataMerge(Map<String, String> map) {
        // TODO Auto-generated method stub
        eleMapper.saveDataMerge(map);
    }

    @Override
    public void updateTp(Map<String, String> map) {
        // TODO Auto-generated method stub
        eleMapper.updateTp(map);
    }


}