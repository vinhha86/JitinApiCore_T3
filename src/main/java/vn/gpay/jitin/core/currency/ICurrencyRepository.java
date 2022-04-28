package vn.gpay.jitin.core.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ICurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {

}
