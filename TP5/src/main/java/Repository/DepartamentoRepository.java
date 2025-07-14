package Repository;
import Entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Proyecto, Long> {
}
