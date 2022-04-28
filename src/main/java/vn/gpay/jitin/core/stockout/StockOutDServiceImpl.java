package vn.gpay.jitin.core.stockout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.utils.StockoutStatus;
@Service
public class StockOutDServiceImpl extends AbstractService<StockOutD> implements IStockOutDService{

	@Autowired
	StockOutDRepository repository;
	@Autowired
	StockOutPklistRepository repository_pklist; 
	@Override
	protected JpaRepository<StockOutD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockOutD> getBystockOutId(Long stockoutid_link) {
		// TODO Auto-generated method stub
		return repository.getBystockOutId(stockoutid_link);
	}
	
	@Override
	public List<StockOutD> findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link, Long skuid_link){
		return repository.findBy_PContractAndSku(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link, skuid_link);
	}
	
	@Override
	//Xuất Nguyen lieu + Phu lieu cho sản xuất theo đơn hàng
	public List<StockOutD> to_production_m_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link, Long skuid_link){
		return repository.to_production_m_findBy_PContractAndSku(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link, skuid_link);
	}
	@Override
	//Xuất Nguyen lieu + Phu lieu cho sản xuất theo đơn hàng
	public List<StockOutD> to_production_m_findBy_PContract(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link){
		return repository.to_production_m_findBy_PContract(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link);
	}
	
	@Override
	//Xuất thành phẩm cho khách hàng theo đơn hàng
	public List<StockOutD> to_vendor_p_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link, Long skuid_link){
		return repository.to_vendor_p_findBy_PContractAndSku(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link, skuid_link);
	}
	
	@Override
	public List<StockOutD> to_vendor_p_findBy_PContract(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link){
		return repository.to_vendor_p_findBy_PContract(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link);
	}
	
//	@Override
//	public List<StockOutD> p_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link, Long stockid_link, Long skuid_link){
//		return repository.p_findBy_PContractAndSku(pcontractid_link, pcontract_poid_link, porderid_link, stockid_link, skuid_link);
//	}
	@Override
	public List<Long> getStockoutIdBySkuId(Long skuid_link) {
		// TODO Auto-generated method stub
		return repository.getStockoutIdBySkuId(skuid_link);
	}
	
	@Override
	public void recal_Totalcheck(Long stockoutdid_link){
		StockOutD theStockoutD = repository.getOne(stockoutdid_link);
		
		Specification<StockOutPklist> specification = Specifications.<StockOutPklist>and()
	            .ge( "status", StockoutStatus.STOCKOUT_EPC_STATUS_ERR_OUTOFSKULIST)
	            .eq( "stockoutdid_link", stockoutdid_link)
	            .build();
		List<StockOutPklist> ls_Pklist = repository_pklist.findAll(specification);
		
		//Tổng cây vải đã check
		theStockoutD.setTotalpackagecheck(ls_Pklist.size());
		
		//Tổng dài vải đã check, Tổng cân vải đã check
		Float totalydscheck = (float) 0;
		Float totalmet_check = (float) 0;
		for(StockOutPklist pklist:ls_Pklist){
			totalydscheck += null != pklist.getYdscheck()?pklist.getYdscheck():0;
			totalmet_check += null != pklist.getMet_check()?pklist.getMet_check():0;
		}
		theStockoutD.setTotalydscheck(totalydscheck);
		theStockoutD.setTotalmet_check(totalmet_check);
		
		//Set lại status
		if(theStockoutD.getTotalmet_check() >= theStockoutD.getTotalmet_origin()) {
			theStockoutD.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
		}else {
			theStockoutD.setStatus(StockoutStatus.STOCKOUT_D_STATUS_ERR);
		}
		
		repository.save(theStockoutD);
	}
	@Override
	public List<StockOutD> getBy_MaNpl_StockoutId(String maNpl, Long stockoutid_link) {
		return repository.getBy_MaNpl_StockoutId(maNpl, stockoutid_link);
	}
	@Override
	public List<StockOutD> getBy_StockoutOrder_and_SkuId(Long stockoutorderid_link, Long skuid_link,
			Integer status_from, Integer status_to) {
		return repository.getBy_StockoutOrder_and_SkuId(stockoutorderid_link, skuid_link, status_from, status_to);
	}
	
}
