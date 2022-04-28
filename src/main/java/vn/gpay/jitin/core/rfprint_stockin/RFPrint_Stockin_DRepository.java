package vn.gpay.jitin.core.rfprint_stockin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RFPrint_Stockin_DRepository extends JpaRepository<RFPrint_Stockin_D, Long>{

}
