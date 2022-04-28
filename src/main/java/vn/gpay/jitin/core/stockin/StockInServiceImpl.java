package vn.gpay.jitin.core.stockin;

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
import vn.gpay.jitin.core.utils.StockinStatus;

@Service
public class StockInServiceImpl extends AbstractService<StockIn> implements IStockInService{

	@Autowired
	StockInRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<StockIn, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockIn> findByStockinCode(Long orgid_link,String stockincode) {
		// TODO Auto-generated method stub
		return repository.findByStockinCode(orgid_link,stockincode);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Results> test(String stockincode) {
		//Query query = entityManager.createNativeQuery("call NGB.getStateByName(?1)");
		//query.setParameter(1, title);
		
		Query query = entityManager.createNativeQuery("SELECT c.stockincode,c.invoicenumber from StockIn c where c.stockincode =:stockincode ");
		query.setParameter("stockincode", stockincode); 
        List<Object[]> objectList = query.getResultList();
        List<Results> result = new ArrayList<>();
        for (Object[] row : objectList) {
            result.add(new Results(row));
        }
        return result;
	}
	@Override
	public List<StockIn> stockin_getone(Long orgid_link,String stockincode, String stockcode) {
		// TODO Auto-generated method stub
		return repository.stockin_getone(orgid_link,stockincode, stockcode);
	}
	@Override
	public List<StockIn> stockin_list(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link, Long orgid_to_link, Date stockindate_from,
			Date stockindate_to) {
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
                .ne("status", StockinStatus.STOCKIN_STATUS_DELETED)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,sort);
		//return repository.stockin_list(orgid_link,stockouttypeid_link,stockincode, orgid_from_link,orgid_to_link, stockindate_from, stockindate_to);
	}
	@Override
	public List<StockIn> stockin_product_list(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link, List<Long> orgid_to_linkArray, Date stockindate_from,
			Date stockindate_to, Long stockintypeid_link_from, Long stockintypeid_link_to, List<Integer> status) {
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ge(Objects.nonNull(stockintypeid_link_from), "stockintypeid_link", stockintypeid_link_from)
	            .le(Objects.nonNull(stockintypeid_link_to), "stockintypeid_link", stockintypeid_link_to)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
//	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .in(orgid_to_linkArray.size() > 0,"orgid_to_link", orgid_to_linkArray.toArray())
	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
                .in(Objects.nonNull(status),"status", status.toArray())
                .ne("status", StockinStatus.STOCKIN_STATUS_DELETED)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,sort);
		//return repository.stockin_list(orgid_link,stockouttypeid_link,stockincode, orgid_from_link,orgid_to_link, stockindate_from, stockindate_to);
	}
	@Override
	public List<StockIn> stockin_material_list(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link, List<Long> orgid_to_linkArray, Date stockindate_from,
			Date stockindate_to, Long pcontractid_link, Long stockintypeid_link_from, Long stockintypeid_link_to, 
			List<Integer> statuses, List<Long> listStockinId, List<Long> listPContractId
			) {
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
//	            .ge("stockintypeid_link", 1)
//	            .le("stockintypeid_link", 10)
	            .ge(Objects.nonNull(stockintypeid_link_from), "stockintypeid_link", stockintypeid_link_from)
	            .le(Objects.nonNull(stockintypeid_link_to), "stockintypeid_link", stockintypeid_link_to)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(pcontractid_link), "pcontractid_link", pcontractid_link)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
//	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .in(orgid_to_linkArray.size() > 0,"orgid_to_link", orgid_to_linkArray.toArray())
	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
//                .ne("status", StockinStatus.STOCKIN_STATUS_DELETED)
                .in(Objects.nonNull(statuses),"status", null!=statuses?statuses.toArray():null)
                .in(Objects.nonNull(listStockinId),"id", null!=listStockinId?listStockinId.toArray():null)
                .in(Objects.nonNull(listPContractId),"pcontractid_link", null!=listPContractId?listPContractId.toArray():null)
                
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
//		List<StockIn> a = repository.findAll(specification,sort);
//		return a;
		
		Pageable firstPageWith500Elements = PageRequest.of(0, 500, sort);
		Page<StockIn> page = repository.findAll(specification, firstPageWith500Elements);
		return page.getContent();
		
		//return repository.stockin_list(orgid_link,stockouttypeid_link,stockincode, orgid_from_link,orgid_to_link, stockindate_from, stockindate_to);
	}
	@Override
	public List<StockIn> stockin_material_list_pcontract(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link, List<Long> orgid_to_linkArray, Date stockindate_from,
			Date stockindate_to, Long pcontractid_link, Long stockintypeid_link_from, Long stockintypeid_link_to, List<Integer> statuses, List<Long> listStockinId,
			Long userId
			) {
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
//	            .ge("stockintypeid_link", 1)
//	            .le("stockintypeid_link", 10)
	            .ge(Objects.nonNull(stockintypeid_link_from), "stockintypeid_link", stockintypeid_link_from)
	            .le(Objects.nonNull(stockintypeid_link_to), "stockintypeid_link", stockintypeid_link_to)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(pcontractid_link), "pcontractid_link", pcontractid_link)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
//	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .in(orgid_to_linkArray.size() > 0,"orgid_to_link", orgid_to_linkArray.toArray())
	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
//                .ne("status", StockinStatus.STOCKIN_STATUS_DELETED)
                .in(Objects.nonNull(statuses),"status", null!=statuses?statuses.toArray():null)
                .in(Objects.nonNull(listStockinId),"id", null!=listStockinId?listStockinId.toArray():null)
                .eq(Objects.nonNull(userId), "usercreateid_link", userId)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
//		List<StockIn> a = repository.findAll(specification,sort);
//		return a;
		
		Pageable firstPageWith500Elements = PageRequest.of(0, 500, sort);
		Page<StockIn> page = repository.findAll(specification, firstPageWith500Elements);
		return page.getContent();
		
		//return repository.stockin_list(orgid_link,stockouttypeid_link,stockincode, orgid_from_link,orgid_to_link, stockindate_from, stockindate_to);
	}
	
	@Override
	public Page<StockIn> stockin_page(Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			Long orgid_to_link, Date stockindate_from, Date stockindate_to, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(stockintypeid_link != null, "stockintypeid_link", stockintypeid_link)
	            .eq(orgid_from_link != null, "orgid_from_link", orgid_from_link)
	            .eq(orgid_to_link != null, "orgid_to_link", orgid_to_link)
//	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
//                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
//                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,PageRequest.of(page - 1, limit, sort));
	}
	
	@Override
	public Page<StockIn> stockin_page(Integer userOrgType, Long userOrgId, Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			Long orgid_to_link, Date stockindate_from, Date stockindate_to, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(stockintypeid_link != null, "stockintypeid_link", stockintypeid_link)
	            .eq(orgid_from_link != null, "orgid_from_link", orgid_from_link)
	            .eq(orgid_to_link != null, "orgid_to_link", orgid_to_link)
//	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
//                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", DateFormat.atStartOfDay(stockindate_from), DateFormat.atEndOfDay(stockindate_to))
                .ne("status", StockinStatus.STOCKIN_STATUS_DELETED)
                .predicate(Specifications.or()
                        .eq(userOrgType == 13, "org_to.id", userOrgId)
                        .eq(userOrgType == 13, "org_to.parentid_link", userOrgId)
                        .build())
	            .build();
		
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,PageRequest.of(page - 1, limit, sort));
	}
	
	@Override
	public void stockin_update_status(Long stockinid_link, Integer status) {
		 repository.stockin_update_status(stockinid_link, status, new Date());
	}
	

	@Override
	public List<StockIn> findByPO_Type_Status(Long pcontract_poid_link, Integer stockintypeid_link, Integer status) {
		return repository.findByPO_Type_Status(pcontract_poid_link, stockintypeid_link, status);
	}
	@Override
	public List<StockinProduct> getStockinProductByStockinId(Long stockinid_link) {
		return repository.getStockinProductByStockinId(stockinid_link);
	}
	@Override
	public List<StockIn> findByStockoutId(Long stockoutid_link) {
		return repository.findByStockoutId(stockoutid_link);
	}
}
