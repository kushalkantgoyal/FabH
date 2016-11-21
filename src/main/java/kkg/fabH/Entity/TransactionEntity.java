package kkg.fabH.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kkg.fabH.Util.TransactionType;

/**
 * @author Kushal
 *
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity extends CommonEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623265882453966660L;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private UserEntity user;
    
    @Enumerated(EnumType.ORDINAL)
    @Column (name = "transaction_type", nullable = false, unique = true)
    private TransactionType transactionType;

    @Column (name = "amount", nullable = false)
    private Double amount;
    
    @Column (name = "current_balance", nullable = false)
    private Double currentBalance;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
    
}
