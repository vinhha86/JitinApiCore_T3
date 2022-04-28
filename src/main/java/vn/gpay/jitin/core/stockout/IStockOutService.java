package vn.gpay.jitin.core.stockout;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.jitin.core.base.Operations;



public interface IStockOutService extends Operations<StockOut>{

	public List<StockOut> findByStockinCode(Long orgid_link,String stockoutcode);
	
	public List<Results> test(String stockoutcode);
	
	public List<StockOut> stockout_getone(Long orgid_link,String stockoutcode,String stockcode);
	
	public List<StockOut> stockout_list(Long orgid_link,Integer stockouttypeid_link,String stockoutcode,Long orgid_to_link,Date stockoutdate_from,Date stockoutdate_to);
	
	public void updateStatusById(long id);
	
	public  Page<StockOut> stockout_list_page(Long orgrootid_link,Integer stockouttypeid_link, Long stockouttypefrom, Long stockouttypeto, String stockoutcode, Long orgid_from_link,Long orgid_to_link, Date stockoutdate_from,
				Date stockoutdate_to,int page, int limit, List<Integer> statuses, List<Long> listStockinId);

	List<StockOut> stockout_submaterial_list(Long orgrootid_link, Integer stockouttypeid_link, Long orgid_from_link,
			Long orgid_to_link, Date stockoutdate_from, Date stockoutdate_to);

	List<StockOut> stockout_shop_list(Long orgrootid_link, Integer stockouttypeid_link, Long orgid_from_link,
			Long orgid_to_link, Date stockoutdate_from, Date stockoutdate_to);

	List<StockOut> stockout_product_list(Long orgrootid_link, Integer stockouttypeid_link, List<Long> orgid_from_linkArray,
			Long orgid_to_link, Date stockoutdate_from, Date stockoutdate_to);

	List<StockOut> stockout_listByOrgTo(Integer stockouttypeid_link, long orgid_to_link, Integer status);

	List<StockOut> stockout_material_list(Long orgrootid_link, Integer stockouttypeid_link, List<Long> orgid_from_linkArray,
			Long orgid_to_link, Date stockoutdate_from, Date stockoutdate_to, List<Integer> statuses, List<Long> listStockoutId);
	
	List<Long> getStockoutIdByOrgTo(Long orgid_to_link);
	
	List<StockOut> findBy_StockoutOrder_approved(Long stockoutorderid_link);
	
	List<StockOut> findBy_StockoutOrder(Long stockoutorderid_link);
	List<StockOut> findBy_StockoutOrder_DaXuat(Long stockoutorderid_link);
}
