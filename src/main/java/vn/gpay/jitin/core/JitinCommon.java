package vn.gpay.jitin.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.jitin.core.stocking_uniquecode.Stocking_UniqueCode;

@Service
public class JitinCommon {
	@Autowired IStocking_UniqueCode_Service stockingService;
	
	public String GetStockinCode() {
		String code = "";
		Stocking_UniqueCode stocking = stockingService.getby_type(1);
		String prefix = stocking.getStocking_prefix();
		Integer max = stocking.getStocking_max() + 1;
		String STT = max.toString();
		
		while(STT.toString().length() < 5) {
			STT = "0"+STT;
		}
		code = prefix + "_" + STT;
		return code;
	}
	
	public String GetStockoutCode() {
		String code = "";
		Stocking_UniqueCode stocking = stockingService.getby_type(2);
		String prefix = stocking.getStocking_prefix();
		Integer max = stocking.getStocking_max() + 1;
		String STT = max.toString();
		
		while(STT.toString().length() < 5) {
			STT = "0"+STT;
		}
		code = prefix + "_" + STT;
		return code;
	}
	
	public String GetSessionCode() {
		String code = "";
		Stocking_UniqueCode stocking = stockingService.getby_type(3);
		String prefix = stocking.getStocking_prefix();
		Integer max = stocking.getStocking_max() + 1;
		String STT = max.toString();
		
		while(STT.toString().length() < 5) {
			STT = "0"+STT;
		}
		code = prefix + "_" + STT;
		return code;
	}
}
