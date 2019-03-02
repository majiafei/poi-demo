package com.yyt.poi;

import com.yyt.poi.annotation.ExcelField;
import com.yyt.poi.model.Zone;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadExcel.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public static <T> List<T> readExcel(String fileName, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        // 获取工作簿
        Workbook workBook = getWorkBook(fileName);
        // 读取数据
        List<T> list = readDataFromExcel(workBook, clazz);
        return list;
    }

    /**
     * 获取workbook
     *
     * @param fileName
     */
    private static Workbook getWorkBook(String fileName) {
        Workbook workbook = null;
        try {
            InputStream inputStream = new FileInputStream(fileName);
            if (fileName.endsWith(xls)) {
                // 2003
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        return workbook;
    }

    /**
     * 从excel表格中读取数据
     *
     * @param workBook
     * @return
     */
    private static <T> List<T> readDataFromExcel(Workbook workBook, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<T>();
        String[] headers = null;
        if (workBook != null) {
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workBook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }

                // 获取头部
                headers = getHeaders(sheet);
                // 将头部信息添加到list中
                //list.add(headers);

                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    LOGGER.info("开始读取第" + rowNum + "行");
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];

                    // 创建对象
                    Object object = clazz.newInstance();

                    list.add(setValueOfFiled(row, clazz, headers));
                    LOGGER.info("第" + rowNum + "行的数据处理完毕");
                }
            }
            //workBook.close();
        }
        return list;
    }

    /**
     * 获取头信息
     *
     * @param sheet
     * @return
     */
    private static String[] getHeaders(Sheet sheet) {
        // 获取第一行的行号
        int firstRowNum = sheet.getFirstRowNum();
        // 获取第一行
        Row row = sheet.getRow(firstRowNum);

        if (row == null) {
            return new String[0];
        }
        // 定义存储头信息的数组
        String[] result = new String[row.getPhysicalNumberOfCells()];

        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            result[i] = getCellValue(row.getCell(i));
        }

        return result;
    }

    /**
     * 获取每一个单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /**
     * 根据头信息，给传入的class的相应的字段赋值
     * @param row
     * @param clazz
     * @param headers
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> T setValueOfFiled(Row row, Class<T> clazz, String[] headers) throws IllegalAccessException, InstantiationException {
        T object = clazz.newInstance();
        //循环当前行
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            String header = headers[cellNum];
            Cell cell = row.getCell(cellNum);
            // 获取所给类的所有字段
            Field[] declaredFields = clazz.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                // 获取该注解的name值
                String name = declaredFields[i].getAnnotation(ExcelField.class).name();
                if (header != null && !header.equals("") && header.equals(name)) {
                    declaredFields[i].setAccessible(true);
                    declaredFields[i].set(object, getCellValue(cell));
                }
            }
        }

        return object;
    }

}
