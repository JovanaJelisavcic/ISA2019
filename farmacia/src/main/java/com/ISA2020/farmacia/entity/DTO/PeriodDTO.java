package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;

public class PeriodDTO {
	 	private LocalDateTime dateTime;
	    private LocalDateTime endTime;
	    public PeriodDTO() {}
		public PeriodDTO(LocalDateTime dateTime, LocalDateTime endTime) {
			super();
			this.dateTime = dateTime;
			this.endTime = endTime;
		}
		public LocalDateTime getDateTime() {
			return dateTime;
		}
		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}
		public LocalDateTime getEndTime() {
			return endTime;
		}
		public void setEndTime(LocalDateTime endTime) {
			this.endTime = endTime;
		}
	    
}
