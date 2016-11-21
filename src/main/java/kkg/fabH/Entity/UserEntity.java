package kkg.fabH.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Kushal
 *
 */
@Entity
@Table(name = "users")
public class UserEntity extends CommonEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8683857807002592289L;

	@Column (name = "first_name", nullable = false)
    private String firstName;
    
    @Column (name = "last_name")
    private String lastName;
    
    @Column (name = "email", nullable = false, unique = true)
    private String email;
    
    @Column (name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    public UserEntity() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
