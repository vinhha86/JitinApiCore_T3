package vn.gpay.jitin.core.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface InvoiceDRepository extends JpaRepository<InvoiceD, Long>{
	@Query(value = "select a from InvoiceD a inner join Invoice b on a.invoiceid_link = b.id"
			+ " where b.pcontractid_link =:pcontractid_link and b.status > 0"
			+ " and a.skuid_link = :skuid_link")
	public List<InvoiceD> findBy_ContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("skuid_link")final Long skuid_link
			);

}
