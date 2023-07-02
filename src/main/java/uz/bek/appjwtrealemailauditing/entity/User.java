package uz.bek.appjwtrealemailauditing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id; // Unique number for Users Id

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToMany
    private Set<Role> roles;


    private boolean accountNonExpired = true; // Userning amal qilish muddati o'tmagan

    private boolean accountNonLocked = true; // bu user bloklanmagan

    private boolean credentialsNonExpired = true; //Accountning ishonchliligini muddati tugaganligi qaytaradi

    private boolean enabled; //Accountning Aktivligini qaytaradi

    private String emailCode;

    //-----------Methods from UserDetails-------//

    //Userning huquqlari ro'yhati
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    //Userning usernameni qaytaruvchi metod
    @Override
    public String getUsername() {
        return this.email;
    }

    //Accountning amal qilish muddati
    @Override
    public boolean isAccountNonExpired() { //
        return this.accountNonExpired;
    }

    //Account bloklanganlik holati
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    //Accountning ishonchliligini muddati tugaganligi qaytaradi
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    //Accountning Aktivligini qaytaradi
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
