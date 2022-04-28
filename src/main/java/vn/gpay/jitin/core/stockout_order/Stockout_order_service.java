package vn.gpay.jitin.core.stockout_order;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.utils.DateFormat;

@Service
public class Stockout_order_service extends AbstractService<Stockout_order> implements IStockout_order_service {
	@Autowired Stockout_order_repository repo;
	@Override
	protected JpaRepository<Stockout_order, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Stockout_order> getby_porder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}
	@Override
	public Page<Stockout_order> findBySearch(Date stockoutorderdate_from, Date stockoutorderdate_to, int page, int limit, Integer status, List<Long> listStockoutOrderId, Integer stockouttypeid_link) {
		Specification<Stockout_order> specification = Specifications.<Stockout_order>and()
	            .ge(this.check1(stockoutorderdate_from,stockoutorderdate_to),"timecreate",DateFormat.atStartOfDay(stockoutorderdate_from))
                .le(this.check2(stockoutorderdate_from,stockoutorderdate_to),"timecreate",DateFormat.atEndOfDay(stockoutorderdate_to))
                .between(this.check3(stockoutorderdate_from,stockoutorderdate_to),"timecreate", DateFormat.atStartOfDay(stockoutorderdate_from), DateFormat.atEndOfDay(stockoutorderdate_to))
                .eq(Objects.nonNull(status), "status", status)
                .in(Objects.nonNull(listStockoutOrderId),"id", null!=listStockoutOrderId?listStockoutOrderId.toArray():null)
                .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
                .build();
		Sort sort = Sorts.builder()
		        .desc("timecreate")
		        .build();
	    return repo.findAll(specification, PageRequest.of(page - 1, limit, sort));
	}

	public boolean check1(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from!=null && stockoutorderdate_to==null) return true;
		return false;
	}
	public boolean check2(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from==null && stockoutorderdate_to!=null) return true;
		return false;
	}
	public boolean check3(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from!=null && stockoutorderdate_to!=null) return true;
		return false;
	}
	
	@Override
	public Page<Stockout_order> findBySearch_KeHoachSanXuat(Date stockoutorderdate_from, Date stockoutorderdate_to, int page, int limit, Integer status, List<Long> listStockoutOrderId, Integer stockouttypeid_link, Long porder_grantid_link) {
		Specification<Stockout_order> specification = Specifications.<Stockout_order>and()
	            .ge(this.check1(stockoutorderdate_from,stockoutorderdate_to),"date_xuat_yc",DateFormat.atStartOfDay(stockoutorderdate_from))
                .le(this.check2(stockoutorderdate_from,stockoutorderdate_to),"date_xuat_yc",DateFormat.atEndOfDay(stockoutorderdate_to))
                .between(this.check3(stockoutorderdate_from,stockoutorderdate_to),"date_xuat_yc", DateFormat.atStartOfDay(stockoutorderdate_from), DateFormat.atEndOfDay(stockoutorderdate_to))
                .eq(Objects.nonNull(status), "status", status)
                .in(Objects.nonNull(listStockoutOrderId),"id", null!=listStockoutOrderId?listStockoutOrderId.toArray():null)
                .eq(Objects.nonNull(stockouttypeid_link), "stockouttypeid_link", stockouttypeid_link)
                .eq(Objects.nonNull(porder_grantid_link), "porder_grantid_link", porder_grantid_link)
                .build();
		Sort sort = Sorts.builder()
		        .desc("timecreate")
		        .build();
	    return repo.findAll(specification, PageRequest.of(page - 1, limit, sort));
	}
	
	@Override
	public List<Long> getStockoutOrderByOrgFromId(Long orgid_from_link) {
		return repo.getStockoutOrderByOrgFromId(orgid_from_link);
	}
}
