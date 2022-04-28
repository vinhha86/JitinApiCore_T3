package vn.gpay.jitin.core.invoice;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.utils.DateFormat;

@Service
public class InvoiceServiceImpl extends AbstractService<Invoice> implements IInvoiceService{

	@Autowired
	InvoiceRepository repositoty;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	protected JpaRepository<Invoice, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Invoice> findByInvoicenumber(String invoicenumber) {
		// TODO Auto-generated method stub
		return repositoty.findByInvoicenumber(invoicenumber);
	}
	
	@Override
	public List<Invoice> findInvoiceBySkuCode(Long orgid_link,String invoicenumber, String skucode) {
		// TODO Auto-generated method stub
		return repositoty.findInvoiceBySkuCode(orgid_link,invoicenumber, skucode);
	}

	@Override
	public List<InvoiceD> findInvoiceDBySkuCode(String invoicenumber, String skucode) {
		// TODO Auto-generated method stub
		return repositoty.findInvoiceDBySkuCode(invoicenumber, skucode);
	}
	
	@Override
	public List<Invoice> InvoiceListComming(Long orgid_link,String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to,int status){
		
		Specification<Invoice> specification = Specifications.<Invoice>and()
	            .eq( "orgid_link", orgid_link)
	            .like(Objects.nonNull(invoicenumber), "invoicenumber", "%"+invoicenumber+"%")
	            .eq(Objects.nonNull(stockcode), "orgid_to_link", stockcode)
	            .ge((shipdateto_from!=null && shipdateto_to==null),"invoicedate",DateFormat.atStartOfDay(shipdateto_from))
                .le((shipdateto_from==null && shipdateto_to!=null),"invoicedate",DateFormat.atEndOfDay(shipdateto_to))
                .between((shipdateto_from!=null && shipdateto_to!=null),"invoicedate", DateFormat.atStartOfDay(shipdateto_from), DateFormat.atEndOfDay(shipdateto_to))
                .eq(status!=-1, "status", status)
	            .build();

	    return repositoty.findAll(specification);
	}
	
	@Override
	public List<Invoice> InvoiceList_ByPContractAndSKU(Long pcontractid_link,Long skuid_link){
		Specification<Invoice> specification = Specifications.<Invoice>and()
	            .eq(Objects.nonNull(pcontractid_link), "pcontractid_link", pcontractid_link)
	            .eq(Objects.nonNull(skuid_link), "invoice_d.skuid_link", skuid_link)
	            .build();

	    return repositoty.findAll(specification);
	}
	/*
	public List<InvoiceList> InvoiceListComming(String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to){
		
		List<InvoiceList> listdata =new ArrayList<>();
		try {
		Query query = entityManager.createNativeQuery("SELECT c.invoicenumber,c.invoicedate, '' as orgfrom_code,'' as orgfrom_name"
				+ " from gsmart_inv.invoice c  ");
        //query.setParameter("stockincode", "%"+stockcode+"%"); 
        List<Object[]> objectList = query.getResultList();
        for (Object[] row : objectList) {
        	listdata.add( new InvoiceList(row));
        }
		}catch(Exception ex) {
			ex.getStackTrace();
		}
        return listdata;
	}*/

	@Override
	public void updateStatusByInvoicenumber(String invoicenumber) {
		// TODO Auto-generated method stub
		repositoty.updateStatusByInvoicenumber(invoicenumber);
	}

	@Override
	public List<Invoice> findInvoiceActivate(Long orgid_link) {
		// TODO Auto-generated method stub
		return repositoty.findInvoiceActivate(orgid_link);
	}

	@Override
	public List<Invoice> getInvoiceBySearch(String custom_declaration, Date invoicedate_from, Date invoicedate_to,
			String invoicenumber, Long org_prodviderid_link, Integer status) {
		// TODO Auto-generated method stub
		return repositoty.getInvoiceBySearch(custom_declaration, invoicedate_from, invoicedate_to, invoicenumber, org_prodviderid_link, status);
	}

	

}
