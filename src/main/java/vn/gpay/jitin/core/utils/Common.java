package vn.gpay.jitin.core.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

@Service
public class Common  {
	
	public String getStringValue(Cell cell) {
		if(cell == null) return "";
		try {
			Double value = cell.getNumericCellValue();
			int i_value = value.intValue();
			if(value == i_value) {
				return (i_value+"").equals("0") ? "" : i_value+"";
			}
			return (value+"").equals("0") ? "" : value+"";
		}
		catch (Exception e) {
			
		}
		
		try {
			int value = (int)cell.getNumericCellValue();
			return (value+"").equals("0") ? "" : value+"";
		}
		catch (Exception e) {
			
		}
		
		try {
			return cell.getStringCellValue();
		}
		catch (Exception e) {
			return "";
		}
	}
}
