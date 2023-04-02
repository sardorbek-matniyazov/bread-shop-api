package demobreadshop.repository;

import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.projection.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {

    List<WareHouse> findAllByType(ProductType type);

    boolean existsByName(String name);
    boolean existsByNameAndIdIsNot(String name, long id);

    @Query(
            value = "select sum(k.material_amount * k.wh_price) as realPrice, wh.wh_name as name, wh.wh_price as price, wh.id as id, wh.house_amount as amount, wh.kindergarten_price as kindergartenPrice \n" +
                    "from (ware_house wh join product_list pl on wh.id = pl.material_id) k join ware_house wh on k.warehouse_fk = wh.id \n" +
                    "group by wh.id " +
                    "order by id DESC " +
                    " limit 1000;",
            nativeQuery = true
    )
    List<ProductProjection> findAllProduct();

    @Query(
            value = "select sum(pl.material_amount * wh.wh_price) from product_list pl join ware_house wh on wh.id = pl.material_id " +
                    "             where pl.warehouse_fk = ?1",
            nativeQuery = true
    )
    Double findBenefitWithWarehouseId(Long id);
}
