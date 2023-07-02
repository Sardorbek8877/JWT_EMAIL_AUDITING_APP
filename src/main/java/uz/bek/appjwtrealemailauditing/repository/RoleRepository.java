package uz.bek.appjwtrealemailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.appjwtrealemailauditing.entity.Role;
import uz.bek.appjwtrealemailauditing.entity.User;
import uz.bek.appjwtrealemailauditing.entity.enums.RoleName;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
