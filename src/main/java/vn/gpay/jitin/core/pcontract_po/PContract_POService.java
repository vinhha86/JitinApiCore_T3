package vn.gpay.jitin.core.pcontract_po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class PContract_POService extends AbstractService<PContract_PO> implements IPContract_POService {
	@Autowired
	IPContract_PORepository repo;

	@Override
	protected JpaRepository<PContract_PO, Long> getRepository() {
		return repo;
	}
}
