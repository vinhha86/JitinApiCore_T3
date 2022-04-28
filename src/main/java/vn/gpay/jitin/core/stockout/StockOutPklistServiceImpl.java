package vn.gpay.jitin.core.stockout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockOutPklistServiceImpl extends AbstractService<StockOutPklist> implements IStockOutPklistService{

	@Autowired
	StockOutPklistRepository repository; 
	
	@Override
	protected JpaRepository<StockOutPklist, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockOutPklist> inv_getbyid(long stockoutid_link) {
		// TODO Auto-generated method stub
		return repository.inv_getbyid(stockoutid_link);
	}
	
	@Override
	public void deleteByStockOutD(long stockoutdid_link){
		repository.deleteByStockOutD(stockoutdid_link);
	}

	@Override
	public List<StockOutPklist> getBystockOutDId(Long stockoutdid_link) {
		return repository.getBystockOutDId(stockoutdid_link);
	}

	@Override
	public List<StockOutPklist> getBystockOutDId_rip(Long stockoutdid_link) {
		return repository.getBystockOutDId_rip(stockoutdid_link);
	}

	@Override
	public List<StockOutPklist> getByLotnumberAndPackageId(Long stockoutdid_link, String lotnumber, Integer packageid) {
		return repository.getByLotnumberAndPackageId(stockoutdid_link, lotnumber, packageid);
	}

	@Override
	public List<StockOutPklist> getByEpc(Long stockoutdid_link, String epc) {
		return repository.getByEpc(stockoutdid_link, epc);
	}

	@Override
	public List<StockOutPklist> getBy_Lotnumber_StockoutId(String lotnumber, Long stockoutid_link) {
		return repository.getBy_Lotnumber_StockoutId(lotnumber, stockoutid_link);
	}

	@Override
	public List<StockOutPklist> getByEpc_in_Stockout(String epc, List<Integer> statuses, Long stockoutid) {
		return repository.getByEpc_in_Stockout(epc, statuses, stockoutid);
	}
}
