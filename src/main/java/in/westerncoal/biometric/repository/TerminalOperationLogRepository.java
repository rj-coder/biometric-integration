package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.westerncoal.biometric.model.TerminalOperationLog;

public interface TerminalOperationLogRepository extends JpaRepository<TerminalOperationLog, String> {

	@Modifying
	@Query("UPDATE TerminalOperationLog tol " +
 		       "SET tol.lastOperationTime = :#{#terminalOperationLog.lastOperationTime}, " +
		       "tol.operationType = :#{#terminalOperationLog.operationType}, " +
		       "tol.pullId = :#{#terminalOperationLog.pullId}, " +
		       "tol.recordCount = :#{#terminalOperationLog.recordCount}, " +
		       "tol.recordFetchOperation = :#{#terminalOperationLog.recordFetchOperation}, " +
		       "tol.recordFetched = :#{#terminalOperationLog.recordFetched}, " +
		       "tol.sendId = :#{#terminalOperationLog.sendId}, " +		        
 		       "tol.terminalOperationStatus = :#{#terminalOperationLog.terminalOperationStatus}  " +
		       "WHERE tol.terminalLogId = :#{#terminalOperationLog.terminalLogId}")
	  void updateTerminalOperationLog(@Param("terminalOperationLog") TerminalOperationLog terminalOperationLog);
}
