package vn.gpay.jitin.core.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface Warehouse_logs_Repository extends JpaRepository<Warehouse_logs, String>{

}
