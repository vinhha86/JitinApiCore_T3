package vn.gpay.jitin.core.stockout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.utils.DateFormat;
import vn.gpay.jitin.core.utils.StockoutStatus;

@Service
public class StockOutServiceImpl extends AbstractService<StockOut> implements IStockOutService{

	@Autowired
	StockOutRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<StockOut, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockOut> findByStockinCode(Long orgid_link,String stockoutcode) {
		// TODO Auto-generated method stub
		return repository.findByStockinCode(orgid_link,stockoutcode);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Results> test(String stockoutcode) {
		//Query query = entityManager.createNativeQuery("call NGB.getStateByName(?1)");
		//query.setParameter(1, title);
		
		Query query = entityManager.createNativeQuery("SELECT c.stockincode,c.invoicenumber from StockOut c where c.stockoutcode =:stockoutcode ");
		query.setParameter("stockoutcode", stockoutcode); 
        List<Object[]> objectList = query.getResultList();
        List<Results> result = new ArrayList<>();
        for (Object[] row : objectList) {
            result.add(new Results(row));
        }
        return result;
	}
	@Override
	public List<StockOut> stockout_getone(Long orgid_link,String stockoutcode, String stockcode) {
		// TODO Auto-generated method stub
		return repository.stockout_getone(orgid_link,stockoutcode, stockcode);
	}
	@Override
	public List<StockOut> stockout_list(Long orgid_link,Integer stockouttypeid_link,String stockoutcode, Long orgid_to_link, Date stockoutdate_from,
			Date stockoutdate_to) {
		// TODO Auto-generated method stub
		//return repository.stockout_list(stockouttypeid_link,stockoutcode, orgid_to_link,DateFormat.DateToString(stockoutdate_from),DateFormat.DateToString(stockoutdate_to));
		return repository.stockout_list(orgid_link,stockouttypeid_link,stockoutcode, orgid_to_link, stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<StockOut> stockout_listByOrgTo(Integer stockouttypeid_link, long orgid_to_link, Integer status) {
		// TODO Auto-generated method stub
		return repository.stockout_listByOrgTo(stockouttypeid_link, orgid_to_link, status);
	}
	
	@Override
	public void updateStatusById(long id) {
		// TODO Auto-generated method stub
		repository.updateStatusById(id);
	}	
	@Override
	public Page<StockOut> stockout_list_page(Long orgrootid_link,Integer stockouttypeid_link, Long stockouttypefrom, Long stockouttypeto, String stockoutcode,Long orgid_from_link, Long orgid_to_link, Date stockoutdate_from,
			Date stockoutdate_to,int page, int limit, List<Integer> statuses, List<Long> listStockoutId) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(stockouttypeid_link != null, "stockouttypeid_link", stockouttypeid_link)
	            .ge(stockouttypefrom != null, "stockouttypeid_link", stockouttypefrom)
	            .le(stockouttypeto != null, "stockouttypeid_link", stockouttypeto)
	            .like(Objects.nonNull(stockoutcode), "stockoutcode", "%"+stockoutcode+"%")
	            .eq(orgid_from_link != null, "orgid_from_link", orgid_from_link)
	            .eq(orgid_to_link != null, "orgid_to_link", orgid_to_link)
	            .ge(this.check1(stockoutdate_from,stockoutdate_to),"stockoutdate",DateFormat.atStartOfDay(stockoutdate_from))
                .le(this.check2(stockoutdate_from,stockoutdate_to),"stockoutdate",DateFormat.atEndOfDay(stockoutdate_to))
                .between(this.check3(stockoutdate_from,stockoutdate_to),"stockoutdate", DateFormat.atStartOfDay(stockoutdate_from), DateFormat.atEndOfDay(stockoutdate_to))
                .in(Objects.nonNull(statuses),"status", null!=statuses?statuses.toArray():null) // status
                .in(Objects.nonNull(listStockoutId),"id", null!=listStockoutId?listStockoutId.toArray():null) // idStockout
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
	    return repository.findAll(specification, PageRequest.of(page - 1, limit, sort));
	}
	public boolean check1(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from!=null && stockoutdate_to==null) return true;
		return false;
	}
	public boolean check2(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from==null && stockoutdate_to!=null) return true;
		return false;
	}
	public boolean check3(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from!=null && stockoutdate_to!=null) return true;
		return false;
	}
	
	@Override
	public List<StockOut> stockout_material_list(Long orgrootid_link,Integer stockouttypeid_link,List<Long> orgid_from_linkArray, Long orgid_to_link, 
			Date stockoutdate_from,
			Date stockoutdate_to,List<Integer> statuses, List<Long> listStockoutId) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ge("stockouttypeid_link", 1)
	            .le("stockouttypeid_link", 10)
	            .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
//	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .in(orgid_from_linkArray.size() > 0,"orgid_from_link", orgid_from_linkArray.toArray())
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockoutdate_from!=null && stockoutdate_to==null),"stockoutdate",DateFormat.atStartOfDay(stockoutdate_from))
                .le((stockoutdate_from==null && stockoutdate_to!=null),"stockoutdate",DateFormat.atEndOfDay(stockoutdate_to))
                .between((stockoutdate_from!=null && stockoutdate_to!=null),"stockoutdate", DateFormat.atStartOfDay(stockoutdate_from), DateFormat.atEndOfDay(stockoutdate_to))
//                .ne("status", StockoutStatus.STOCKOUT_STATUS_DELETED)
                .in(Objects.nonNull(statuses),"status", null!=statuses?statuses.toArray():null)
                .in(Objects.nonNull(listStockoutId),"id", null!=listStockoutId?listStockoutId.toArray():null)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
		
		Pageable firstPageWith500Elements = PageRequest.of(0, 500, sort);
		Page<StockOut> page = repository.findAll(specification, firstPageWith500Elements);
		return page.getContent();
		
//	    return repository.findAll(specification,sort);
	}
	@Override
	public List<StockOut> stockout_submaterial_list(Long orgrootid_link,Integer stockouttypeid_link,Long orgid_from_link, Long orgid_to_link, 
			Date stockoutdate_from,
			Date stockoutdate_to) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ge("stockouttypeid_link", 11)
	            .le("stockouttypeid_link", 20)
	            .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockoutdate_from!=null && stockoutdate_to==null),"stockoutdate",DateFormat.atStartOfDay(stockoutdate_from))
                .le((stockoutdate_from==null && stockoutdate_to!=null),"stockoutdate",DateFormat.atEndOfDay(stockoutdate_to))
                .between((stockoutdate_from!=null && stockoutdate_to!=null),"stockoutdate", DateFormat.atStartOfDay(stockoutdate_from), DateFormat.atEndOfDay(stockoutdate_to))
                .ne("status", StockoutStatus.STOCKOUT_STATUS_DELETED)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
		
		Pageable firstPageWith500Elements = PageRequest.of(0, 500, sort);
		Page<StockOut> page = repository.findAll(specification, firstPageWith500Elements);
		return page.getContent();
		
//	    return repository.findAll(specification,sort);
	}
	@Override
	public List<StockOut> stockout_product_list(Long orgrootid_link,Integer stockouttypeid_link,List<Long> orgid_from_linkArray, Long orgid_to_link, 
			Date stockoutdate_from,
			Date stockoutdate_to) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ge("stockouttypeid_link", 21)
	            .le("stockouttypeid_link", 30)
	            .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
//	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .in(orgid_from_linkArray.size() > 0,"orgid_from_link", orgid_from_linkArray.toArray())
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockoutdate_from!=null && stockoutdate_to==null),"stockoutdate",DateFormat.atStartOfDay(stockoutdate_from))
                .le((stockoutdate_from==null && stockoutdate_to!=null),"stockoutdate",DateFormat.atEndOfDay(stockoutdate_to))
                .between((stockoutdate_from!=null && stockoutdate_to!=null),"stockoutdate", DateFormat.atStartOfDay(stockoutdate_from), DateFormat.atEndOfDay(stockoutdate_to))
                .ne("status", StockoutStatus.STOCKOUT_STATUS_DELETED)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
		
		Pageable firstPageWith500Elements = PageRequest.of(0, 500, sort);
		Page<StockOut> page = repository.findAll(specification, firstPageWith500Elements);
		return page.getContent();
		
//	    return repository.findAll(specification,sort);
	}
	@Override
	public List<StockOut> stockout_shop_list(Long orgrootid_link,Integer stockouttypeid_link,Long orgid_from_link, Long orgid_to_link, 
			Date stockoutdate_from,
			Date stockoutdate_to) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ge("stockouttypeid_link", 31)
	            .le("stockouttypeid_link", 40)
	            .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockoutdate_from!=null && stockoutdate_to==null),"stockoutdate",DateFormat.atStartOfDay(stockoutdate_from))
                .le((stockoutdate_from==null && stockoutdate_to!=null),"stockoutdate",DateFormat.atEndOfDay(stockoutdate_to))
                .between((stockoutdate_from!=null && stockoutdate_to!=null),"stockoutdate", DateFormat.atStartOfDay(stockoutdate_from), DateFormat.atEndOfDay(stockoutdate_to))
                .ne("status", StockoutStatus.STOCKOUT_STATUS_DELETED)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
	    return repository.findAll(specification,sort);
	}
	@Override
	public List<Long> getStockoutIdByOrgTo(Long orgid_to_link) {
		// TODO Auto-generated method stub
		return repository.getStockoutIdByOrgTo(orgid_to_link);
	}
	@Override
	public List<StockOut> findBy_StockoutOrder_approved(Long stockoutorderid_link) {
		return repository.findBy_StockoutOrder_approved(stockoutorderid_link);
	}
	@Override
	public List<StockOut> findBy_StockoutOrder(Long stockoutorderid_link) {
		return repository.findBy_StockoutOrder(stockoutorderid_link);
	}
	@Override
	public List<StockOut> findBy_StockoutOrder_DaXuat(Long stockoutorderid_link) {
		return repository.findBy_StockoutOrder_DaXuat(stockoutorderid_link);
	}
}
