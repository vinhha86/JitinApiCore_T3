package vn.gpay.jitin.core.porder_grant;
//import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderGrant_SKURepository extends JpaRepository<POrderGrant_SKU, Long>, JpaSpecificationExecutor<POrderGrant_SKU>{
	
}
