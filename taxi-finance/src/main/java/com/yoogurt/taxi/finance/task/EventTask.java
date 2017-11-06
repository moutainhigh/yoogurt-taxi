package com.yoogurt.taxi.finance.task;

import com.yoogurt.taxi.dal.doc.finance.Event;
import com.yoogurt.taxi.dal.doc.finance.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTask {

	@Id
	private String taskId;

	private TaskInfo task;

	private Event event;

	private Payment payment;


	public EventTask(String taskId) {
		this.taskId = taskId;
	}
}
