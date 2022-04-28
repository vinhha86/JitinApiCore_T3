package vn.gpay.jitin.core.invoice;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long>,JpaSpecificationExecutor<Invoice>{

	@Query(value = "select c from Invoice c where invoicenumber =:invoicenumber")
	public List<Invoice> findByInvoicenumber(@Param ("invoicenumber")final String invoicenumber);
	
	@Query(value = "select a from Invoice a where  orgid_link=:orgid_link and invoicenumber =:invoicenumber and p_skuid_link=:skucode")
	public List<Invoice> findInvoiceBySkuCode(@Param ("orgid_link")final Long orgid_link , @Param ("invoicenumber")final String invoicenumber,@Param ("skucode")final String skucode);
	
	@Query(value = "select a from InvoiceD a inner join Invoice b on a.invoiceid_link = b.id inner join SKU c on a.skuid_link = c.id where b.invoicenumber =:invoicenumber and c.code =:skucode")
	public List<InvoiceD> findInvoiceDBySkuCode( @Param ("invoicenumber")final String invoicenumber,@Param ("skucode")final String skucode);

	@Query(value = "select c from Invoice c where orgid_to_link=:orgid_link and status= 0" )
	public List<Invoice> findInvoiceActivate( @Param ("orgid_link")final Long orgid_link);
	
	
	@Modifying
	@Query(value = "update Invoice set status = 1 where invoicenumber=:invoicenumber")
	public void updateStatusByInvoicenumber(@Param ("invoicenumber")final String invoicenumber);
	
	@Query(value = "select c from Invoice c "
			+ "where (:custom_declaration = '' or lower(c.custom_declaration) like lower(concat('%',:custom_declaration,'%'))) "
			+ "and (:invoicenumber = '' or lower(c.invoicenumber) like lower(concat('%',:invoicenumber,'%'))) "
			+ "and (:org_prodviderid_link = 0L or c.org_prodviderid_link = :org_prodviderid_link) "
			+ "and (:status = 0 or c.status = :status) "
			+ "and (CAST(:invoicedate_from AS date) IS NULL or c.invoicedate >= :invoicedate_from) "
			+ "and (CAST(:invoicedate_to AS date) IS NULL or c.invoicedate <= :invoicedate_to) "
//			+ "where :custom_declaration = :custom_declaration "
//			+ "and :invoicenumber = :invoicenumber "
//			+ "and :org_prodviderid_link = :org_prodviderid_link "
//			+ "and :status = :status "
//			+ "and :invoicedate_from = :invoicedate_from "
//			+ "and :invoicedate_to = :invoicedate_to "
			)
	public List<Invoice> getInvoiceBySearch( 
			@Param ("custom_declaration")final String custom_declaration,
			@Param ("invoicedate_from")final Date invoicedate_from,
			@Param ("invoicedate_to")final Date invoicedate_to,
			@Param ("invoicenumber")final String invoicenumber,
			@Param ("org_prodviderid_link")final Long org_prodviderid_link,
			@Param ("status")final Integer status
			);

}
