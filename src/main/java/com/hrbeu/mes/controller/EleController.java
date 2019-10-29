package com.hrbeu.mes.controller;


import com.hrbeu.mes.pojo.Electrolyze;
import com.hrbeu.mes.pojo.Template;
import com.hrbeu.mes.service.EleService;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tb_electrolyze")
public class EleController {

    @Resource
    private EleService eleService;

    @ResponseBody
    @RequestMapping(value = "/queryData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<Electrolyze> queryData(@RequestBody Map<String, String> map) {
        List<Electrolyze> list = eleService.queryData();
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/updateData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void updateData(@RequestBody Map<String, String> map) {
        eleService.updateData(map);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void saveTp(@RequestBody Map<String, String> map) {
        eleService.saveTp(map);
    }

    @ResponseBody
    @RequestMapping(value = "/updateTp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void updateTp(@RequestBody Map<String, String> map) {
        eleService.updateTp(map);
    }

    @ResponseBody
    @RequestMapping(value = "/getTpByID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Template getTpByID(@RequestBody Map<String, String> map) {
        return eleService.getTpByID(map);
    }

    @ResponseBody
    @RequestMapping(value = "/getTpList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<Template> getTpList(@RequestBody Map<String, String> map) {
        return eleService.getTpList(map);
    }

    @ResponseBody
    @RequestMapping(value = "/saveDataMerge", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void saveDataMerge(@RequestBody Map<String, String> map) {
        eleService.saveDataMerge(map);
    }

    @RequestMapping("/export")
    public void export(@RequestBody Map<String, String> map, HttpServletResponse response) {
        List<Electrolyze> list = eleService.queryData();
        Template tp = eleService.getTpByID(map);
        XSSFWorkbook book = new XSSFWorkbook();
        try {
            XSSFSheet sheet = book.createSheet("电解运行概况");
            //表头
            JSONArray jsonArray = JSONArray.fromObject(tp.getHeader_content());
            for (int i = 0; i < jsonArray.size(); i++) {
                XSSFRow row = sheet.createRow(i);
                Object o = jsonArray.get(i);
                JSONArray ar = JSONArray.fromObject(o);
                for (int j = 0; j < ar.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    String value;
                    if (!ar.get(j).equals(JSONNull.getInstance())) {
                        value = (String) ar.get(j);
                    } else {
                        value = "";
                    }
                    cell.setCellValue(value);
                }
            }
            //数据
            JSONArray arIndex = JSONArray.fromObject(tp.getHeader_index());
            for (int i = 0; i < list.size(); i++) {
                Electrolyze data = list.get(i);
                XSSFRow row = sheet.createRow(jsonArray.size() + i);
                for (int j = 0; j < arIndex.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    String p = (String) arIndex.get(j);//prop_5
                    Class<?> clas = data.getClass();
                    Method method = clas.getMethod("get" + p.substring(0, 1).toUpperCase() + p.substring(1));//getProp_5
                    String value = (String) method.invoke(data);
                    if (value != null && value.startsWith("=")) {
                        cell.setCellFormula(value.substring(1));//公式  ep:B1+C1
                    } else {
                        cell.setCellValue(value);
                    }
                }
            }
            //设置列宽(自动列宽)
            for (int i = 0; i < arIndex.size(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }
            //设置行高
            for (int i = 0; i < jsonArray.size() + list.size(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (i < jsonArray.size()) {
                    row.setHeightInPoints(25);//表头
                } else {
                    row.setHeightInPoints(50);//数据
                }
            }
            //设置字体、水平垂直居中、自动换行
            CellStyle cStyle = book.createCellStyle();
            XSSFFont font = book.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);//字体大小
            cStyle.setFont(font);
            cStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            cStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            cStyle.setWrapText(true);//自动换行
            for (int i = 0; i < jsonArray.size() + list.size(); i++) {
                XSSFRow row = sheet.getRow(i);
                for (int j = 0; j < arIndex.size(); j++) {
                    XSSFCell cell = row.getCell(j);
                    cell.setCellStyle(cStyle);
                }
            }
            //合并单元格
            JSONArray mergejsonAr = JSONArray.fromObject(tp.getData_merge());
            for (int i = 0; i < mergejsonAr.size(); i++) {
                Object o = mergejsonAr.get(i);
                JSONObject jo = JSONObject.fromObject(o);
                int row = jo.getInt("row");
                int col = jo.getInt("col");
                int rowspan = jo.getInt("rowspan");
                int colspan = jo.getInt("colspan");
                sheet.addMergedRegion(new CellRangeAddress(row, row + rowspan - 1, col, col + colspan - 1));//行起始，行终止，列起始，列终止
            }
            //固定列
            //4个参数分别为：固定列，固定行，右起始列，下起始行，通常后两个参数分别和前两个一致即可，ep:A B A B
            sheet.createFreezePane(tp.getFix_column_left(), 0, tp.getFix_column_left(), 0);


            book.write(generateResponseExcel("电解运行概况", response));
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServletOutputStream generateResponseExcel(String excelName, HttpServletResponse response) throws IOException {
        excelName = excelName == null || "".equals(excelName) ? "excel" : URLEncoder.encode(excelName, "UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + excelName + ".xlsx");
        return response.getOutputStream();
    }
}
