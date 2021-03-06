package br.com.http.monitoring;

import br.com.http.timer.Job;
import br.com.http.timer.JobExecution;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.*;

@Stateless
public class JobMonitoringService {

	@PersistenceContext(unitName = "primary")
	private EntityManager em;

	public JobMonitoringResponse findJobs(int expectedNumberOfActiveJobs, int historySize) {
		List<Job> actualJobs = findActualActiveJobs();
		List<JobInfo> jobInfos = buildJobInfos(actualJobs, historySize);
		JobMonitoringResponse jobMonitoringResponse = new JobMonitoringResponse(jobInfos, expectedNumberOfActiveJobs);
		return jobMonitoringResponse;
	}

	private List<JobInfo> buildJobInfos(List<Job> actualJobs, int historySize) {
		List<JobInfo> jobInfos = new ArrayList<>();
		for (Job job : actualJobs) {
			List<JobExecutionInfo> history = buildJobExecutionHistory(job, historySize);
			jobInfos.add(new JobInfo(job.getId(), history, job.getCronExpressionSafeForQuartzParser()));
		}
		return jobInfos;
	}

	private List<JobExecutionInfo> buildJobExecutionHistory(Job job, int historySize) {
		String hql = "FROM JobExecution exec " +
				" WHERE exec.job = :jobId " +
				" ORDER BY exec.finish DESC";
		TypedQuery<JobExecution> query = em.createQuery(hql, JobExecution.class);
		query.setParameter("jobId", job.getId());
		query.setMaxResults(historySize);
		return transformActualJobExecutionToInfo(query.getResultList());
	}

	@SuppressWarnings("unused")
    private Date toThePast(Date date, int lookBackFinishTimeInHours) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, lookBackFinishTimeInHours * -1);
		return cal.getTime();
	}

	private List<JobExecutionInfo> transformActualJobExecutionToInfo(List<JobExecution> resultList) {
		List<JobExecutionInfo> history = new ArrayList<>();
		for (JobExecution exec : resultList) {
			history.add(
					new JobExecutionInfo(exec.getId(), exec.getFinish(), exec.getStart(), exec.getHttpResponseStatus(),
							exec.getStatus().name(), exec.getClientError()));
		}
		return history;
	}

	private List<Job> findActualActiveJobs() {
		String hql = "FROM Job job WHERE job.active = TRUE ORDER BY job.id";
		TypedQuery<Job> query = em.createQuery(hql, Job.class);
		return query.getResultList();
	}
}
