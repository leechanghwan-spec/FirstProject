package com.school.demo.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationRequest {
	
	private String chrstn_nm;
	private String chrgr_type;
	private String startTime;
	private String endTime;

}
