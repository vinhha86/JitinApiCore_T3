package vn.gpay.jitin.core.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;



public interface IStockOutPklistService extends Operations<StockOutPklist>{

	public List<StockOutPklist> inv_getbyid(long stockoutid_link);
	
	List<StockOutPklist> getBystockOutDId(Long stockoutdid_link);
	
	List<StockOutPklist> getBystockOutDId_rip(Long stockoutdid_link);
	
	List<StockOutPklist> getByLotnumberAndPackageId(Long stockoutdid_link, String lotnumber, Integer packageid);
	
	List<StockOutPklist> getByEpc(Long stockoutdid_link, String epc);
	
	List<StockOutPklist> getBy_Lotnumber_StockoutId(String lotnumber, Long stockoutid_link);
	
	List<StockOutPklist> getByEpc_in_Stockout(String epc, List<Integer> statuses, Long stockoutid);

	void deleteByStockOutD(long stockoutdid_link);
}
