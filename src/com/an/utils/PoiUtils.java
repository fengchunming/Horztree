package com.an.utils;

import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class PoiUtils<T> {

    public static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    public static String getString(Cell cell) throws Exception {
        if (cell == null)
            return "";
        CellReference cellRef = new CellReference(cell.getRowIndex(),
                cell.getColumnIndex());
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    return cell.getRichStringCellValue().getString();
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return Util.DateFmt.format(cell.getDateCellValue());
                    } else {
                        return new DecimalFormat("#.####").format(cell
                                .getNumericCellValue());
                    }
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                            .getCreationHelper().createFormulaEvaluator();
                    return String.valueOf(evaluator.evaluateFormulaCell(cell));
                case Cell.CELL_TYPE_BLANK:
                    return "";
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new Exception(cellRef.formatAsString() + "数据格式错误,期待格式：文本");
        }
    }

    public static BigDecimal getNumeric(Cell cell) throws Exception {
        CellReference cellRef = new CellReference(cell.getRowIndex(),
                cell.getColumnIndex());
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return new BigDecimal(cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    throw new Exception(cellRef.formatAsString() + "数据格式错误,期待格式：数字");
                } else {
                    return new BigDecimal((int) cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                        .getCreationHelper().createFormulaEvaluator();
                return new BigDecimal(evaluator.evaluateFormulaCell(cell));
            default:
                throw new Exception(cellRef.formatAsString() + "数据格式错误,期待格式：数字");
        }
    }

    public static Boolean getBoolean(Cell cell) throws Exception {
        CellReference cellRef = new CellReference(cell.getRowIndex(),
                cell.getColumnIndex());
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return "是".equals(cell.getRichStringCellValue().getString())
                        || "true".equalsIgnoreCase(cell.getRichStringCellValue()
                        .getString());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    throw new Exception(cellRef.formatAsString() + ":数据格式错误");
                } else {
                    return cell.getNumericCellValue() > 0;
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            default:
                throw new Exception(cellRef.formatAsString() + ":数据格式错误");
        }
    }

    public static Date getDate(Cell cell) throws Exception {
        CellReference cellRef = new CellReference(cell.getRowIndex(),
                cell.getColumnIndex());
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return Util.DateFmt
                        .parse(cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    throw new Exception(cellRef.formatAsString()
                            + "数据格式错误,期待格式：日期yyyy-mm-dd");
                }

            default:
                throw new Exception(cellRef.formatAsString()
                        + "数据格式错误,期待格式：日期yyyy-mm-dd");
        }
    }

    public void buildExcel(String title, JSONObject headers,
                           Collection<T> dataset, OutputStream out) {
        // 声明一个工作薄
        Workbook workbook = new XSSFWorkbook();
        // 生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 产生表格标题行
        Row row = sheet.createRow(0);
        Cell cellHeader;
        Iterator<?> head = headers.keys();
        int c = 0;
        while (head.hasNext()) {
            String key = (String) head.next();
            cellHeader = row.createCell(c);
            cellHeader.setCellValue(headers.getString(key));
            c++;
        }

        // 遍历集合数据，产生数据行
        int r = 0;
        String getMethodName;
        Cell cell;
        Class<?> tCls = null;
        Method getMethod;
        Object value;
        String textValue;
        for (T t : dataset) {
            r++;
            row = sheet.createRow(r);
            c = 0;
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            if (tCls == null)
                tCls = t.getClass();
            Iterator<?> iter = headers.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                cell = row.createCell(c);
                getMethodName = "get" + key.substring(0, 1).toUpperCase()
                        + key.substring(1);
                try {
                    getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    if (getMethod == null)
                        continue;
                    value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue(((Integer) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        textValue = (Boolean) value ? "是" : "否";
                    } else {
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    }
                    c++;
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
