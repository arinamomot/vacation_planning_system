package accountService.dao;
import accountService.model.VacationDay;
import org.springframework.stereotype.Repository;

@Repository
public class VacationDayDao extends BaseDao<VacationDay>{

    public VacationDayDao() {
        super(VacationDay.class);
    }
}
