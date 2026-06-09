package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//security 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name="users") //mapping à la table nommée users
@Data
@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
//User doit implementer l'interface UserDetails -> les champs email et mot de passe vers Spring security
@Builder
public class User implements UserDetails {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	
	@Column(unique=true, nullable=false) // Email = unique + required 
	private String email; 
	
	private String motDePasse; 
	private String prenom;
	private String nom; 
	
	@Enumerated(EnumType.STRING) //mot ADMIN au lieu de 0...
	private UserRole role; 
	
	@Column(columnDefinition = "boolean default true")
	//IT DOES NTOHING WITHOUT THIS ANNOATION WTF ???
	private boolean actif=true; 
	
	private LocalDateTime dateCreation; 
	
	
	@PrePersist 
	protected void onCreate() {
		this.dateCreation=LocalDateTime.now();
	}
	
	//relation avec Addresses -> now can ask a User for all addresses
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> adresses = new ArrayList<>();
	//orphan removal again detaching an address from  a user -> deleted
	
	
	
	//staff! which branch does staffmember belong to 
	@ManyToOne
	@JoinColumn(name="branch_id")
	private Seller branch;
	//a staff user works in a branch 
	//regular user not staff = NULL
	
	
	//Security 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
	} //=> maps role (ADMIN..) to Spring security authorities
	// what is ? extends GrantedSecurity 
	//-> = promise -> all the objects in the collection i guarantee is either 
	// GrantedAuthory OR SOMETHING that inherits from it !! 
	//spring security 6 looks for the ROLE prefix ! 
	
	@Override 
	public String getPassword() {
		return motDePasse;
	}
	
	@Override 
	public String getUsername() {
		return email; // why email for token ? 
		//in postman emaild@example.com is WAY more readable than 59754 lol
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; //// ????
	}
	
	@Override 
	public boolean isAccountNonLocked() {
		return actif;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}
	
	@Override
	public boolean isEnabled() {
	    return actif; 
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
