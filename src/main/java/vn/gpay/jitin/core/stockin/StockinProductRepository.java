package vn.gpay.jitin.core.stockin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockinProductRepository  extends JpaRepository<StockinProduct, Long>,JpaSpecificationExecutor<StockinProduct>{

}
