package vn.gpay.jitin.core.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.api.stock.Stock_menu_request;
import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;

@Service
public class StockrowServiceImpl extends AbstractService<Stockrow> implements IStockrowService{

	@Autowired
	StockrowRepository repositoty;
	@Autowired IOrgService orgService;
	@Autowired IWarehouseService warehouseService;
	
	@Override
	protected JpaRepository<Stockrow, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Stockrow> findStockrowByOrgID(long id) {
		// TODO Auto-generated method stub
		return repositoty.findStockrowByOrgID(id);
	}
	
	@Override
	public List<StockTree> createTree(List<Stockrow> stockrow_list, List<Long> listorgId, Stock_menu_request entity) {
		Map<String, StockTree> mapTmp = new HashMap<>();
		List<StockTree> listTmp = new ArrayList<>();
		
		// type: 0: dha, 1: phan xuong, 2: kho, 3: day, 4: hang, 5: tang
		
		// IdString: (dùng làm key của map)
		// dha = type + id dha // vd: "0;1"
		// phan xuong = type + id phanxuong // vd: "1;8"
		// kho = type + id kho
		// day = type + id day
		// hang = type + rowid_link + spacename
		// tang = type + spaceepc
		// phân cách bằng dấu ";" 
		
		Org dhaOrg = orgService.findOne((long) 1);
		StockTree dha = new StockTree();
		dha.setType(0); // dha
		dha.setIdString(0 + ";" + 1);
		dha.setParentIdString(null);
		dha.setId(dhaOrg.getId());
		dha.setName(dhaOrg.getName());
		dha.setExpanded(true);
		mapTmp.put(dha.getIdString(), dha);
		
		List<Org> phanxuong_list = orgService.findOrgByParentAndType(13, (long)1);
		for(Org phanxuong : phanxuong_list) {
			StockTree stockTree = new StockTree();
			stockTree.setType(1); // phanxuong
			stockTree.setIdString(1 + ";" + phanxuong.getId());
			stockTree.setParentIdString(0 + ";" + 1);
			stockTree.setId(phanxuong.getId());
			stockTree.setName(phanxuong.getName());
			stockTree.setExpanded(true);
			mapTmp.put(stockTree.getIdString(), stockTree);
			
			// Tìm kho phân xưởng
			List<Org> khonguyenlieu_list = orgService.findOrgByParentAndType(3, phanxuong.getId());
//			List<Org> khothanhpham_list = orgService.findOrgByParentAndType(8, phanxuong.getId());
//			List<Org> khophulieu_list = orgService.findOrgByParentAndType(19, phanxuong.getId());
			
			for(Org khonguyenlieu : khonguyenlieu_list) {
				StockTree stockTreeKho = new StockTree();
				stockTreeKho.setType(2); // kho
				stockTreeKho.setIdString(2 + ";" + khonguyenlieu.getId());
				stockTreeKho.setParentIdString(1 + ";" + phanxuong.getId());
				stockTreeKho.setId(khonguyenlieu.getId());
				stockTreeKho.setName(khonguyenlieu.getName());
				stockTreeKho.setExpanded(true);
				mapTmp.put(stockTreeKho.getIdString(), stockTreeKho);
				
				// Tạo khoang không xác định cho kho (ngang hàng với dãy)
				// IdString: 3 + idKho + KXD
				StockTree stockTreeKhoangKXD = new StockTree();
				stockTreeKhoangKXD.setType(3);
				stockTreeKhoangKXD.setIdString(3 + ";" + khonguyenlieu.getId() + ";KXD");
				stockTreeKhoangKXD.setParentIdString(2 + ";" + khonguyenlieu.getId());
				stockTreeKhoangKXD.setName("KXD");
				stockTreeKhoangKXD.setOrgid_link(khonguyenlieu.getId());
				stockTreeKhoangKXD.setKhoangKhongXacDinh(true);
				stockTreeKhoangKXD.setExpanded(false);
				mapTmp.put(stockTreeKhoangKXD.getIdString(), stockTreeKhoangKXD);
			}
//			for(Org khothanhpham : khothanhpham_list) {
//				StockTree stockTreeKho = new StockTree();
//				stockTreeKho.setType(2); // kho
//				stockTreeKho.setIdString(2 + ";" + khothanhpham.getId());
//				stockTreeKho.setParentIdString(1 + ";" + phanxuong.getId());
//				stockTreeKho.setId(khothanhpham.getId());
//				stockTreeKho.setName(khothanhpham.getName());
//				stockTreeKho.setExpanded(true);
//				mapTmp.put(stockTreeKho.getIdString(), stockTreeKho);
//			}
//			for(Org khophulieu : khophulieu_list) {
//				StockTree stockTreeKho = new StockTree();
//				stockTreeKho.setType(2); // kho
//				stockTreeKho.setIdString(2 + ";" + khophulieu.getId());
//				stockTreeKho.setParentIdString(1 + ";" + phanxuong.getId());
//				stockTreeKho.setId(khophulieu.getId());
//				stockTreeKho.setName(khophulieu.getName());
//				stockTreeKho.setExpanded(true);
//				mapTmp.put(stockTreeKho.getIdString(), stockTreeKho);
//			}
		}
		
		// Tìm danh sách các tầng (spaceepc_link warehouse) có chứa mã/đơn hàng/mã sp request
		String donHang = entity.donHang;
		String maSP = entity.maSP;
		Long maHangId = entity.maHangId;
		if(donHang == null) {
			donHang = "";
		}
		if(maSP == null) {
			maSP = "";
		}
//		if(maHang == null) {
//			maHang = "";
//		}
//		maHang = "%"+maHang+"%";
//		donHang = "%"+donHang+"%";
//		List<String> listSpaceEpc = warehouseService.getSpaceEpcBySkucodeAndPcontract(maHangId, donHang);
		List<String> listSpaceEpc = warehouseService.getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(maHangId, donHang, maSP);
//		System.out.println(maHangId);
//		System.out.println(donHang);
//		System.out.println(maSP);
//		System.out.println(listSpaceEpc.size());
		// day
		for(Stockrow stockrow : stockrow_list) {
			StockTree stockTree = new StockTree();
			stockTree.setType(3); // day row
			stockTree.setIdString(3 + ";" + stockrow.getId());
			stockTree.setParentIdString(2 + ";" + stockrow.getOrgid_link());
			stockTree.setId(stockrow.getId());
			stockTree.setName(stockrow.getCode());
			stockTree.setOrgid_link(stockrow.getOrgid_link());
			stockTree.setKhoangKhongXacDinh(false);
			stockTree.setExpanded(true);
			mapTmp.put(stockTree.getIdString(), stockTree);
			
			// hang, tang
			List<Stockspace> stockspace_list = stockrow.getStockspaces();
			for(Stockspace stockspace : stockspace_list) { 
				// check listSpaceEpc
//				System.out.println(stockspace.getSpaceepc());
				if(
						(donHang != null && !donHang.equals("")) || 
						(maSP != null && !maSP.equals("")) || 
						maHangId != null
						) {
					boolean isAdd = Arrays.stream(listSpaceEpc.toArray()).anyMatch(i -> i.equals(stockspace.getSpaceepc()));
//					System.out.println(isAdd);
					if(!isAdd) {
						continue;
					}
				}
				
				// hang
				StockTree stockTreeHang = new StockTree();
				stockTreeHang.setType(4); // hang space
				stockTreeHang.setIdString(4 + ";" + stockspace.getRowid_link() + ";" + stockspace.getSpacename());
				stockTreeHang.setParentIdString(3 + ";" + stockrow.getId());
				stockTreeHang.setId(null);
				stockTreeHang.setName(stockspace.getSpacename());
				stockTreeHang.setOrgid_link(stockspace.getOrgid_link());
				stockTreeHang.setSpacename(stockspace.getSpacename());
				stockTreeHang.setRowid_link(stockspace.getRowid_link());
				stockTreeHang.setExpanded(true);
				mapTmp.put(stockTreeHang.getIdString(), stockTreeHang);
				
				// tang
				StockTree stockTreeTang = new StockTree();
				stockTreeTang.setType(5); // tang floor
				stockTreeTang.setIdString(5 + ";" + stockspace.getSpaceepc());
				stockTreeTang.setParentIdString(4 + ";" + stockspace.getRowid_link() + ";" + stockspace.getSpacename());
				stockTreeTang.setId(null);
				stockTreeTang.setName("" + stockspace.getFloorid());
				stockTreeTang.setOrgid_link(stockspace.getOrgid_link());
				stockTreeTang.setFloorid(stockspace.getFloorid());
				stockTreeTang.setRowid_link(stockspace.getRowid_link());
				stockTreeTang.setSpaceepc(stockspace.getSpaceepc());
				stockTreeTang.setSpacename(stockspace.getSpacename());
				stockTreeTang.setExpanded(true);
				mapTmp.put(stockTreeTang.getIdString(), stockTreeTang);
				
//				System.out.println("---");
			}
		}
		
		//loop and assign parent/child relationships
		listTmp = new ArrayList<StockTree>(mapTmp.values());
		
		// cái này quan trọng, ko sort thì thiếu dữ liệu khi hien thi
		Comparator<StockTree> compareByType = (StockTree a1, StockTree a2) -> a1.getType().compareTo( a2.getType());
		Collections.sort(listTmp, compareByType);
		
		for(StockTree stockTree : listTmp) {
			String idString = stockTree.getIdString();
			// ko phai la dha
			if(!idString.equals("0;1")) { 
				StockTree parent = mapTmp.get(stockTree.getParentIdString());
				if(parent != null) {
					StockTree current_n = new StockTree();
					current_n.setIdString(stockTree.getIdString());
					current_n.setParentIdString(stockTree.getParentIdString());
					current_n.setId(stockTree.getId());
					current_n.setOrgid_link(stockTree.getOrgid_link());
					current_n.setName(stockTree.getName());
					current_n.setSpaceepc(stockTree.getSpaceepc());
					current_n.setSpacename(stockTree.getSpacename());
					current_n.setRowid_link(stockTree.getRowid_link());
					current_n.setFloorid(stockTree.getFloorid());
					current_n.setKhoangKhongXacDinh(stockTree.isKhoangKhongXacDinh());
					current_n.setType(stockTree.getType());
					parent.addChild(current_n);
					if(parent.getType() == 3 || parent.getType() == 4 || parent.getType() == 5) {
						parent.setExpanded(false);
					}else {
						parent.setExpanded(true);
					}
					mapTmp.put(parent.getIdString(), parent);
					mapTmp.put(current_n.getIdString(), current_n);
				}
			}
		}
		
		// bỏ các node ko có child khi tìm kiếm theo đơn/mã hàng
		if(
				maHangId != null || 
				(donHang != null && !donHang.equals("")) ||
				(maSP != null && !maSP.equals(""))
				) {
			listTmp = new ArrayList<StockTree>(mapTmp.values());
			compareByType = (StockTree a1, StockTree a2) -> a2.getType().compareTo( a1.getType());
			Collections.sort(listTmp, compareByType);
			
			for(StockTree stockTree : listTmp) {
//				String idString = stockTree.getIdString();
				String parentIdString = stockTree.getParentIdString();
				List<StockTree> children = stockTree.getChildren();
				StockTree parent = mapTmp.get(parentIdString);
				
				if(parent != null) {
					if(stockTree.getType() != 5) { // Tang
						if(stockTree.getType() == 3 && stockTree.isKhoangKhongXacDinh()) { // khoang kxd
							Long khoId = stockTree.getOrgid_link();
							List<Warehouse> listWarehouse = warehouseService.getWarehouseBySpaceEpcNull(maHangId, donHang, maSP, khoId);
							if(listWarehouse.size() == 0) {
								parent.getChildren().remove(stockTree);
								mapTmp.put(parent.getIdString(), parent);
							}
						}else { // con lai
							if(children.size() == 0) {
								parent.getChildren().remove(stockTree);
								mapTmp.put(parent.getIdString(), parent);
							}
						}
					}
				}
			}
		}
		
		// get the root
		List<StockTree> root = new ArrayList<StockTree>();
		
		for(StockTree node : mapTmp.values()) {
//			if(node.getIdString().equals("0;1")) {
//				root.add(node);
//				node.setExpanded(true);
//			}
			if(node.getType().equals(0)) { // dha
				List<StockTree> children = node.getChildren(); // px
				for(StockTree child : children) {
					Long childId = child.getId();
					boolean isAdd = true;
					
					// check px co trong danh sach listorgId
					// listorgId = null -> lay het
					if(listorgId != null && listorgId.size() > 0) {
						// mac dinh ko lay, check co moi lay
						isAdd = false;
						isAdd = Arrays.stream(listorgId.toArray()).anyMatch(i -> i.equals(childId));
					}
					
					if(!isAdd) continue; 
					
					child.setParentIdString(null);
					child.setExpanded(true);
					root.add(child);
				}
			}
		}
		
		return root;
	}

	@Override
	public List<StockTree> createStockProductTree(List<Stockrow> stockrow_list, List<Long> listorgId, Stock_menu_request entity) {
		Map<String, StockTree> mapTmp = new HashMap<>();
		List<StockTree> listTmp = new ArrayList<>();
		
		// type: 0: dha, 1: phan xuong, 2: kho, 3: day, 4: hang, 5: tang
		
		// IdString: (dùng làm key của map)
		// dha = type + id dha // vd: "0;1"
		// phan xuong = type + id phanxuong // vd: "1;8"
		// kho = type + id kho
		// day = type + id day
		// hang = type + rowid_link + spacename
		// tang = type + spaceepc
		// phân cách bằng dấu ";" 
		
		Org dhaOrg = orgService.findOne((long) 1);
		StockTree dha = new StockTree();
		dha.setType(0); // dha
		dha.setIdString(0 + ";" + 1);
		dha.setParentIdString(null);
		dha.setId(dhaOrg.getId());
		dha.setName(dhaOrg.getName());
		dha.setExpanded(true);
		mapTmp.put(dha.getIdString(), dha);
		
		List<Org> phanxuong_list = orgService.findOrgByParentAndType(13, (long)1);
		for(Org phanxuong : phanxuong_list) {
			StockTree stockTree = new StockTree();
			stockTree.setType(1); // phanxuong
			stockTree.setIdString(1 + ";" + phanxuong.getId());
			stockTree.setParentIdString(0 + ";" + 1);
			stockTree.setId(phanxuong.getId());
			stockTree.setName(phanxuong.getName());
			stockTree.setExpanded(true);
			mapTmp.put(stockTree.getIdString(), stockTree);
			
			// Tìm kho phân xưởng
			List<Org> khothanhpham_list = orgService.findOrgByParentAndType(8, phanxuong.getId());
			
			for(Org khothanhpham : khothanhpham_list) {
				StockTree stockTreeKho = new StockTree();
				stockTreeKho.setType(2); // kho
				stockTreeKho.setIdString(2 + ";" + khothanhpham.getId());
				stockTreeKho.setParentIdString(1 + ";" + phanxuong.getId());
				stockTreeKho.setId(khothanhpham.getId());
				stockTreeKho.setName(khothanhpham.getName());
				stockTreeKho.setExpanded(true);
				mapTmp.put(stockTreeKho.getIdString(), stockTreeKho);
				
				// Tạo khoang không xác định cho kho (ngang hàng với dãy)
				// IdString: 3 + idKho + KXD
				StockTree stockTreeKhoangKXD = new StockTree();
				stockTreeKhoangKXD.setType(3);
				stockTreeKhoangKXD.setIdString(3 + ";" + khothanhpham.getId() + ";KXD");
				stockTreeKhoangKXD.setParentIdString(2 + ";" + khothanhpham.getId());
				stockTreeKhoangKXD.setName("KXD");
				stockTreeKhoangKXD.setOrgid_link(khothanhpham.getId());
				stockTreeKhoangKXD.setKhoangKhongXacDinh(true);
				stockTreeKhoangKXD.setExpanded(false);
				mapTmp.put(stockTreeKhoangKXD.getIdString(), stockTreeKhoangKXD);
			}
			
		}
		
		// cua hang
		List<Org> shop_list = orgService.findOrgByParentAndType(4, (long)1);
		for(Org shop : shop_list) {
			StockTree stockTree = new StockTree();
			stockTree.setType(1); // phanxuong
			stockTree.setIdString(1 + ";" + shop.getId());
			stockTree.setParentIdString(0 + ";" + 1);
			stockTree.setId(shop.getId());
			stockTree.setName(shop.getName());
			stockTree.setExpanded(false);
			stockTree.setShop(true);
			mapTmp.put(stockTree.getIdString(), stockTree);
			
//			// Tạo khoang không xác định cho shop (ngang hàng với kho TP)
//			// IdString: 2 + idShop + KXD
//			StockTree stockTreeKhoangKXD = new StockTree();
//			stockTreeKhoangKXD.setType(2);
//			stockTreeKhoangKXD.setIdString(2 + ";" + stockTree.getId() + ";KXD");
//			stockTreeKhoangKXD.setParentIdString(2 + ";" + stockTree.getId());
//			stockTreeKhoangKXD.setName("KXD");
//			stockTreeKhoangKXD.setOrgid_link(stockTree.getId());
//			stockTreeKhoangKXD.setKhoangKhongXacDinh(true);
//			stockTreeKhoangKXD.setExpanded(false);
//			mapTmp.put(stockTreeKhoangKXD.getIdString(), stockTreeKhoangKXD);
		}
		
		// Tìm danh sách các tầng (spaceepc_link warehouse) có chứa mã/đơn hàng request
		String donHang = entity.donHang;
		Long maHangId = entity.maHangId;
		if(donHang == null) {
			donHang = "";
		}
		
		List<String> listSpaceEpc = warehouseService.getSpaceEpcBySkucode(maHangId);
		
		// day
		for(Stockrow stockrow : stockrow_list) {
			StockTree stockTree = new StockTree();
			stockTree.setType(3); // day row
			stockTree.setIdString(3 + ";" + stockrow.getId());
			stockTree.setParentIdString(2 + ";" + stockrow.getOrgid_link());
			stockTree.setId(stockrow.getId());
			stockTree.setName(stockrow.getCode());
			stockTree.setOrgid_link(stockrow.getOrgid_link());
			stockTree.setKhoangKhongXacDinh(false);
			stockTree.setExpanded(true);
			mapTmp.put(stockTree.getIdString(), stockTree);
			
			// hang, tang
			List<Stockspace> stockspace_list = stockrow.getStockspaces();
			for(Stockspace stockspace : stockspace_list) { 
				// check listSpaceEpc
//				System.out.println(stockspace.getSpaceepc());
				if((donHang != null && !donHang.equals("")) || maHangId != null) {
					boolean isAdd = Arrays.stream(listSpaceEpc.toArray()).anyMatch(i -> i.equals(stockspace.getSpaceepc()));
//					System.out.println(isAdd);
					if(!isAdd) {
						continue;
					}
				}
				
				// hang
				StockTree stockTreeHang = new StockTree();
				stockTreeHang.setType(4); // hang space
				stockTreeHang.setIdString(4 + ";" + stockspace.getRowid_link() + ";" + stockspace.getSpacename());
				stockTreeHang.setParentIdString(3 + ";" + stockrow.getId());
				stockTreeHang.setId(null);
				stockTreeHang.setName(stockspace.getSpacename());
				stockTreeHang.setOrgid_link(stockspace.getOrgid_link());
				stockTreeHang.setSpacename(stockspace.getSpacename());
				stockTreeHang.setRowid_link(stockspace.getRowid_link());
				stockTreeHang.setExpanded(true);
				mapTmp.put(stockTreeHang.getIdString(), stockTreeHang);
				
				// tang
				StockTree stockTreeTang = new StockTree();
				stockTreeTang.setType(5); // tang floor
				stockTreeTang.setIdString(5 + ";" + stockspace.getSpaceepc());
				stockTreeTang.setParentIdString(4 + ";" + stockspace.getRowid_link() + ";" + stockspace.getSpacename());
				stockTreeTang.setId(null);
				stockTreeTang.setName("" + stockspace.getFloorid());
				stockTreeTang.setOrgid_link(stockspace.getOrgid_link());
				stockTreeTang.setFloorid(stockspace.getFloorid());
				stockTreeTang.setRowid_link(stockspace.getRowid_link());
				stockTreeTang.setSpaceepc(stockspace.getSpaceepc());
				stockTreeTang.setSpacename(stockspace.getSpacename());
				stockTreeTang.setExpanded(true);
				mapTmp.put(stockTreeTang.getIdString(), stockTreeTang);
				
//				System.out.println("---");
			}
		}
		
		//loop and assign parent/child relationships
		listTmp = new ArrayList<StockTree>(mapTmp.values());
		
		// cái này quan trọng, ko sort thì thiếu dữ liệu khi hien thi
		Comparator<StockTree> compareByType = (StockTree a1, StockTree a2) -> a1.getType().compareTo( a2.getType());
		Collections.sort(listTmp, compareByType);
		
		for(StockTree stockTree : listTmp) {
			String idString = stockTree.getIdString();
			// ko phai la dha
			if(!idString.equals("0;1")) { 
				StockTree parent = mapTmp.get(stockTree.getParentIdString());
				if(parent != null) {
					StockTree current_n = new StockTree();
					current_n.setIdString(stockTree.getIdString());
					current_n.setParentIdString(stockTree.getParentIdString());
					current_n.setId(stockTree.getId());
					current_n.setOrgid_link(stockTree.getOrgid_link());
					current_n.setName(stockTree.getName());
					current_n.setSpaceepc(stockTree.getSpaceepc());
					current_n.setSpacename(stockTree.getSpacename());
					current_n.setRowid_link(stockTree.getRowid_link());
					current_n.setFloorid(stockTree.getFloorid());
					current_n.setKhoangKhongXacDinh(stockTree.isKhoangKhongXacDinh());
					current_n.setShop(stockTree.isShop());
					current_n.setType(stockTree.getType());
					parent.addChild(current_n);
					if(parent.getType() == 3 || parent.getType() == 4 || parent.getType() == 5) {
						parent.setExpanded(false);
					}else {
						parent.setExpanded(true);
					}
					mapTmp.put(parent.getIdString(), parent);
					mapTmp.put(current_n.getIdString(), current_n);
				}
			}
		}
		
		// bỏ các node ko có child khi tìm kiếm theo đơn/mã hàng
		if(maHangId != null || (donHang != null && !donHang.equals(""))) {
			listTmp = new ArrayList<StockTree>(mapTmp.values());
			compareByType = (StockTree a1, StockTree a2) -> a2.getType().compareTo( a1.getType());
			Collections.sort(listTmp, compareByType);
			
			for(StockTree stockTree : listTmp) {
//				String idString = stockTree.getIdString();
				String parentIdString = stockTree.getParentIdString();
				List<StockTree> children = stockTree.getChildren();
				StockTree parent = mapTmp.get(parentIdString);
				
				if(parent != null) {
					if(stockTree.getType() != 5) { // Tang
						if(stockTree.getType() == 3 && stockTree.isKhoangKhongXacDinh()) { // khoang kxd
							Long khoId = stockTree.getOrgid_link();
							List<Warehouse> listWarehouse = warehouseService.getWarehouseProductBySpaceEpcNull(maHangId, khoId);
							if(listWarehouse.size() == 0) {
								parent.getChildren().remove(stockTree);
								mapTmp.put(parent.getIdString(), parent);
							}
						}else if(stockTree.getType() == 1 && stockTree.isShop()){ // shop
							Long shopId = stockTree.getId();
							List<Warehouse> listWarehouse = warehouseService.getWarehouseProductBySpaceEpcNull(maHangId, shopId);
							if(listWarehouse.size() == 0) {
								parent.getChildren().remove(stockTree);
								mapTmp.put(parent.getIdString(), parent);
							}
						}else{ // con lai
							if(children.size() == 0) {
								parent.getChildren().remove(stockTree);
								mapTmp.put(parent.getIdString(), parent);
							}
						}
					}
				}
			}
		}
		
		// get the root
		List<StockTree> root = new ArrayList<StockTree>();
		
		for(StockTree node : mapTmp.values()) {
//			if(node.getIdString().equals("0;1")) {
//				root.add(node);
//				node.setExpanded(true);
//			}
			if(node.getType().equals(0)) { // dha
				List<StockTree> children = node.getChildren(); // px, cua hang
				for(StockTree child : children) {
					Long childId = child.getId();
					boolean isAdd = true;
					
					// check px co trong danh sach listorgId
					// listorgId = null -> lay het
					if(listorgId != null && listorgId.size() > 0) {
						// mac dinh ko lay, check co moi lay
						isAdd = false;
						isAdd = Arrays.stream(listorgId.toArray()).anyMatch(i -> i.equals(childId));
					}
					
					if(!isAdd) continue; 
					
					child.setParentIdString(null);
					child.setExpanded(true);
					root.add(child);
				}
			}
		}
		
		return root;
	}

	@Override
	public List<Stockrow> findStockrowByOrgIDAndCode(Long orgid_link, String code) {
		return repositoty.findStockrowByOrgIDAndCode(orgid_link, code);
	}

	@Override
	public List<Stockrow> findStockrowByOrgIDAndCodeAndNotId(Long orgid_link, String code, Long id) {
		return repositoty.findStockrowByOrgIDAndCodeAndNotId(orgid_link, code, id);
	}

	@Override
	public List<Stockrow> findStockRowByOrgtype(Integer orgtypeid_link) {
		return repositoty.findStockRowByOrgtype(orgtypeid_link);
	}
}
