package Repository;
import Entidades.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
