package com.sen.test.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static <T> List<T> readExcel(Class<T> clz, String path,
			String sheetName) throws IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (null == path || "".equals(path)) {
			return null;
		}
		InputStream is = new FileInputStream(path);
		Workbook xssfWorkbook;
		if (path.endsWith(".xls")) {
			xssfWorkbook = new HSSFWorkbook(is);
		} else {
			xssfWorkbook = new XSSFWorkbook(is);
		}
		is.close();
		return transToObject(clz, xssfWorkbook, sheetName);
	}

	private static <T> List<T> transToObject(Class<T> clz,
			Workbook xssfWorkbook, String sheetName)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		List<T> list = new ArrayList<T>();
		Sheet xssfSheet = xssfWorkbook.getSheet(sheetName);
		Row firstRow = xssfSheet.getRow(0);
		List<String> heads = getRow(firstRow);
		Map<String, Method> headMethod = getSetMethod(clz, heads);
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			Row xssfRow = xssfSheet.getRow(rowNum);
			if (xssfRow == null) {
				continue;
			}
			T t = clz.newInstance();
			List<String> data = getRow(xssfRow);
			setValue(t, data, heads, headMethod);
			list.add(t);
		}
		return list;
	}

	private static Map<String, Method> getSetMethod(Class<?> clz,
			List<String> heads) {
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] methods = clz.getMethods();
		for (String head : heads) {
			boolean find = false;
			for (Method method : methods) {
				if (method.getName().toLowerCase()
						.equals("set" + head.toLowerCase())
						&& method.getParameterTypes().length == 1) {
					map.put(head, method);
					find = true;
					break;
				}
			}
			if (!find) {
				map.put(head, null);
			}
		}
		return map;
	}

	private static void setValue(Object obj, List<String> values,
			List<String> heads, Map<String, Method> methods)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		for (int index = 0; index < values.size(); index++) {
			String value = values.get(index);
			Method method = methods.get(heads.get(index));
			Class<?> param = method.getParameterTypes()[0];
			if (String.class.equals(param)) {
				method.invoke(obj, value);
			} else if (Integer.class.equals(param) || int.class.equals(obj)) {
				method.invoke(obj, Integer.valueOf(value));
			} else if (Long.class.equals(param) || long.class.equals(param)) {
				method.invoke(obj, Long.valueOf(value));
			} else if (Short.class.equals(param) || short.class.equals(param)) {
				method.invoke(obj, Short.valueOf(value));
			} else if (Boolean.class.equals(param)
					|| boolean.class.equals(param)) {
				method.invoke(obj, Boolean.valueOf(value));
			} else {
				// Date
				method.invoke(obj, value);
			}
		}
	}

	private static List<String> getRow(Row xssfRow) {
		List<String> cells = new ArrayList<String>();
		if (xssfRow != null) {
			for (short cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
				Cell xssfCell = xssfRow.getCell(cellNum);
				cells.add(getValue(xssfCell));
			}
		}
		return cells;
	}

	private static String getValue(Cell cell) {
		if (null == cell) {
			return "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(cell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(cell.getStringCellValue());
		}
	}
}