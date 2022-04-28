package vn.gpay.jitin.core.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class CurrencyService extends AbstractService<Currency> implements ICurrencyService{
	@Autowired ICurrencyRepository repo;
	@Override
	protected JpaRepository<Currency, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
