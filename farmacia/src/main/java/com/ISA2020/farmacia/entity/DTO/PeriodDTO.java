package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;


public class PeriodDTO {
	@NotNull(message="Dates are mandatory")
 	@Future(message="Date has to be in the future")
	 	private LocalDateTime dateTime;
	@NotNull(message="Dates are mandatory")
 	@Future(message="Date has to be in the future")
	    private LocalDateTime endTime;
	    public PeriodDTO() {}
		public PeriodDTO(LocalDateTime dateTime, LocalDateTime endTime) {
			super();
			this.dateTime = dateTime;
			this.endTime = endTime;
		}
		 @AssertTrue public boolean isValidRange() {
			    return dateTime.isBefore(endTime);
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
