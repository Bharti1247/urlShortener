package com.learn.linkShortener.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlStatusRequest {
	
	@NotNull
	private Boolean enabled;
	
	// Why Boolean and not boolean?
	//  Allows validation (null check)
	//  Supports partial updates semantics of PATCH
}
